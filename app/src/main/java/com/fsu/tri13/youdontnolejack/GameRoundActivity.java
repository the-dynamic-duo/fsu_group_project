package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GameRoundActivity extends AppCompatActivity {

    public static final String EXTRA_PLAYERS = "players";

    Button a,b,c,d;
    int numOfPlayers;
    Player player1, player2, player3, player4;
    int roundNum;
    //current or selected category
    String currentCategory;

    //stores the available categories
    ArrayList<String> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        a = (Button) findViewById(R.id.button_choice_a);
        b = (Button) findViewById(R.id.button_choice_b);
        c = (Button) findViewById(R.id.button_choice_c);
        d = (Button) findViewById(R.id.button_choice_d);

        categories = new ArrayList<>();
        roundNum = 1;

        //determines number of players set in title activity
        numOfPlayers = getIntent().getIntExtra(EXTRA_PLAYERS, 0);

        player1 = new Player();

        //for multi-player mode
        if (numOfPlayers > 1) {
            setPlayers();
        }

        //initializes category list
        setCategories();
        //sets listeners for answer buttons
        setListeners();
        //starts first round by selecting first category
        nextCategory();
        //nextRound(roundNum);
    }

    private void setPlayers() {
        switch (numOfPlayers) {
            case 2:
                player2 = new Player();
                break;
            case 3:
                player2 = new Player();
                player3 = new Player();
                break;
            case 4:
                player2 = new Player();
                player3 = new Player();
                player4 = new Player();
                break;
            default:
                break;
        }
    }

    private void setCategories() {
        categories.add(getString(R.string.category_academics));
        categories.add(getString(R.string.category_history));
        categories.add(getString(R.string.category_sports));
        categories.add(getString(R.string.category_student));
    }

    public void setListeners() {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setText(getText(R.string.developers));
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setText(getText(R.string.developers));
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setText(getText(R.string.developers));
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setText(getText(R.string.developers));
            }
        });

    }
    public void nextRound(int round) {


    }

    public void nextCategory() {
        Intent next_category = new Intent(this, CategoryActivity.class);

        next_category.putStringArrayListExtra(CategoryActivity.EXTRA_CATEGORY, categories);
        startActivity(next_category);


    }

}
