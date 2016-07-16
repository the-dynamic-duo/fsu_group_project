package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "cats";

    String selection;
    RadioButton academic, history, sport, student, random, choice;
    Button begin;

    ArrayList<String> categoryList;

    Random rand;

    //for return value
    Intent intent;

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

        intent = new Intent();

        rand = new Random();

        setAvailableCategories();
        setListener();
    }

    public void setAvailableCategories() {
        if (categoryList.contains(getResources().getString(R.string.category_academics)))
            academic.setEnabled(true);
        if (categoryList.contains(getResources().getString(R.string.category_history)))
            history.setEnabled(true);
        if (categoryList.contains(getResources().getString(R.string.category_sports)))
            sport.setEnabled(true);
        if (categoryList.contains(getResources().getString(R.string.category_student)))
            student.setEnabled(true);

       //debuggin
        for (int index = 0; index < categoryList.size(); index++) {
            Log.d("category", categoryList.get(index));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAvailableCategories();
    }

    public void setListener() {
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (academic.isChecked()) {
                    intent.putExtra("categoryChoice",
                            getResources().getString(R.string.category_academics));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (history.isChecked()) {
                    intent.putExtra("categoryChoice",
                            getResources().getString(R.string.category_history));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (sport.isChecked()) {
                    intent.putExtra("categoryChoice",
                            getResources().getString(R.string.category_sports));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (student.isChecked()) {
                    intent.putExtra("categoryChoice",
                            getResources().getString(R.string.category_student));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (random.isChecked()) {
                    int r = rand.nextInt(categoryList.size());

                    intent.putExtra("categoryChoice", categoryList.get(r));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else ;

            }
        });
    }

    //TODO: Still an issue with the Player Class and checking the name.
    @Override
    public void onBackPressed() {

        //if back pressed
        intent.putExtra("categoryChoice", "none - back pressed");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
