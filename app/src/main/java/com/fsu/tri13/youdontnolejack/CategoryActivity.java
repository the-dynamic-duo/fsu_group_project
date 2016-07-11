package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "cats";

    String selection;
    RadioButton academic, history, sport, student, random, choice;
    Button begin;

    ArrayList<String> categoryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryList = getIntent().getStringArrayListExtra(EXTRA_CATEGORY);

        academic = (RadioButton) findViewById(R.id.rb_academics);
        history = (RadioButton) findViewById(R.id.rb_history);
        sport = (RadioButton) findViewById(R.id.rb_sports);
        student = (RadioButton) findViewById(R.id.rb_student);
        random = (RadioButton) findViewById(R.id.rb_random);

        begin = (Button) findViewById(R.id.button_begin);

        setAvailableCategories();
        setListener();
    }

    public void setAvailableCategories() {
        if (!categoryList.contains(getResources().getString(R.string.category_academics)));
            academic.setEnabled(false);
        if (!categoryList.contains(getResources().getString(R.string.category_history)));
            history.setEnabled(false);
        if (!categoryList.contains(getResources().getString(R.string.category_sports)));
            sport.setEnabled(false);
        if (!categoryList.contains(getResources().getString(R.string.category_student)));
            student.setEnabled(false);
        for (int index = 0; index < categoryList.size(); index++) {
            Log.d("category", categoryList.get(index));
        }
    }

    public void setListener() {
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
