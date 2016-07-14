package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GameRoundActivity extends AppCompatActivity {

    //to set the selected amount of players
    public static final String EXTRA_PLAYERS = "players";
    //to set the selected category
    public static final String EXTRA_CURRENT_CATEGORY= "category";

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

        currentCategory = "default";
        //determines number of players set in title activity
        numOfPlayers = getIntent().getIntExtra(EXTRA_PLAYERS, 1);

        player1 = new Player("Player 1");

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
                player2 = new Player("Player 2");
                break;
            case 3:
                player2 = new Player("Player 2");
                player3 = new Player("Player 3");
                break;
            case 4:
                player2 = new Player("Player 2");
                player3 = new Player("Player 3");
                player4 = new Player("Player 4");
                break;
            default:
                break;
        }
    }

    private void setCategories() {
        categories.add(getResources().getString(R.string.category_academics));
        categories.add(getResources().getString(R.string.category_history));
        categories.add(getResources().getString(R.string.category_sports));
        categories.add(getResources().getString(R.string.category_student));
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
                nextCategory();
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalScore();
            }
        });

    }
    public void nextRound(int round) {


    }

    public void nextCategory() {
        Intent next_category = new Intent(this, CategoryActivity.class);

        next_category.putStringArrayListExtra(CategoryActivity.EXTRA_CATEGORY, categories);
        int requestCode = 1;
        startActivityForResult(next_category, requestCode);
    }

    //to obtain selected category
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentCategory = data.getStringExtra("categoryChoice");
        Log.d("dbug", "Returning from category screen");
        Log.d("dbug", currentCategory);

        categories.remove(categories.indexOf(currentCategory));
    }


    public void finalScore() {
        Intent final_score = new Intent(GameRoundActivity.this, FinalScreenActivity.class);

        final_score.putExtra(FinalScreenActivity.FINAL_PLAYERS, numOfPlayers);
        startActivity(final_score);

    }

}
