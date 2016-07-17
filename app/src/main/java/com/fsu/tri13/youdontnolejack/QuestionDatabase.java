package com.fsu.tri13.youdontnolejack;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class QuestionDatabase
{
    private static final String DB_PATH
            = "/data/data/com.fsu.tri13.youdontnolejack/databases/questions.db";

    private static final String DB_NAME = "questions.db";
    private static final String CSV_NAME = "questions.csv";

    private static final String TABLE_NAME   = "Questions";
    private static final String QUESTION_COL = "Question";
    private static final String ANSWER_COL   = "Answer";
    private static final String FAKE1_COL    = "Fake1";
    private static final String FAKE2_COL    = "Fake2";
    private static final String FAKE3_COL    = "Fake3";
    private static final String CATEGORY_COL = "Category";
    private static final String USED_COL     = "Used";

    private static final String STRING_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";

    private static final String VALUE_SEPERATOR = ", ";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            QUESTION_COL + STRING_TYPE + "PRIMARY KEY, " +
            ANSWER_COL   + STRING_TYPE + ", " +
            FAKE1_COL    + STRING_TYPE + ", " +
            FAKE2_COL    + STRING_TYPE + ", " +
            FAKE3_COL    + STRING_TYPE + ", " +
            CATEGORY_COL + STRING_TYPE + ", " +
            USED_COL     + INT_TYPE    + ");";

    private static SQLiteDatabase db;

    private static int CSVLineCount = 0;

    private final Context context;

    public QuestionDatabase(Context ctx)
    {
        context = ctx;

        db = context.openOrCreateDatabase(DB_PATH, ctx.MODE_PRIVATE, null);
        db.execSQL(CREATE_TABLE);

        if (CSVLineCount == 0)
        {
            try
            {
                setCSVLineCount();
            }
            catch (IOException e)
            {
                throw new AssertionError("Could not load .csv file when checking line count");
            }
        }

        if (CSVLineCount != DatabaseUtils.queryNumEntries(db, TABLE_NAME))
        {
            db.delete(TABLE_NAME, null, null);
            try
            {
                createDatabase();
            }
            catch (IOException e)
            {
                throw new AssertionError("Could not load .csv file when creating table");
            }
        }
    }

    private boolean dbExists()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException e)
        {
            // database DNE
        }

        return checkDB != null;
    }

    private void setCSVLineCount() throws IOException
    {
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(context.getAssets().open(CSV_NAME)));

        while (in.readLine() != null)
        {
            ++CSVLineCount;
        }
    }

    private void createDatabase() throws IOException
    {
        String question;
        String answer;
        String fake1;
        String fake2;
        String fake3;
        String category;
        String reader;

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(context.getAssets().open(CSV_NAME)));

        while ((reader = in.readLine()) != null)
        {
            String[] RowData = reader.split(",");
            question = RowData[0];
            answer   = RowData[1];
            fake1    = RowData[2];
            fake2    = RowData[3];
            fake3    = RowData[4];
            category = RowData[5];

            ContentValues values = new ContentValues();

            values.put(QUESTION_COL, question);
            values.put(ANSWER_COL,   answer);
            values.put(FAKE1_COL,    fake1);
            values.put(FAKE2_COL,    fake2);
            values.put(FAKE3_COL,    fake3);
            values.put(CATEGORY_COL, category);
            values.put(USED_COL,     "1");

            db.insert(TABLE_NAME, null, values);
        }
        in.close();
    }

    public String[] returnByCategory(String category)
    {
        String[] output = new String[5];

        // SELECT <columns> FROM <table> WHERE <category == category> AND <used = 1(false)>
        String selectStatement = "SELECT " + QUESTION_COL + ", " + ANSWER_COL + ", " + FAKE1_COL + ", "
                                           + FAKE2_COL    + ", " + FAKE3_COL +
                                 " FROM "  + TABLE_NAME +
                                 " WHERE (" + CATEGORY_COL + " LIKE \""  + category + "\")" +
                                              " AND ("   + USED_COL + " LIKE 1)";

        Cursor c = db.rawQuery(selectStatement, null);

        if(c.moveToFirst())
        {
            output[0] = c.getString(0); // Question
            output[1] = c.getString(1); // Answer
            output[2] = c.getString(2); // Fake1
            output[3] = c.getString(3); // Fake2
            output[4] = c.getString(4); // Fake3

            db.execSQL("UPDATE " + TABLE_NAME + " SET " + USED_COL + "=0 WHERE " + QUESTION_COL
                       + " ='" + output[0] + "'");
        }

        c.close();

        return output;
    }


}