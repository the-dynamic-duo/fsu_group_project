package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

//TODO: Still having an issue when returning to the main menu at end of game

public class FinalScreenActivity extends AppCompatActivity {
    public static final String FINAL_PLAYERS = "plyrs";
    public static final String P1_SCORE = "score1";
    public static final String P2_SCORE = "score2";
    public static final String P3_SCORE = "score3";
    public static final String P4_SCORE = "score4";

    int numOfPlayers;
    Button play_again, main_menu;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        play_again = (Button) findViewById(R.id.button_player_again);
        main_menu = (Button) findViewById(R.id.button_main_menu);

        linearLayout1 = (LinearLayout) findViewById(R.id.fs_LL1);
        linearLayout2 = (LinearLayout) findViewById(R.id.fs_LL2);
        linearLayout3 = (LinearLayout) findViewById(R.id.fs_LL3);
        linearLayout4 = (LinearLayout) findViewById(R.id.fs_LL4);

        numOfPlayers = getIntent().getIntExtra(FINAL_PLAYERS, 1);

        generateView();
        orderScores();

        setListeners();
    }

    public void generateView() {

        if (numOfPlayers > 1) {
            switch (numOfPlayers) {
                case 2:
                    linearLayout2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout4.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }

    public void orderScores() {


    }

    public void setListeners() {

        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_game = new Intent(FinalScreenActivity.this, GameRoundActivity.class);

                new_game.putExtra("finalScreenSelection", "new_game");
                setResult(RESULT_OK, new_game);
                finish();
            }
        });

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_game = new Intent(FinalScreenActivity.this, GameRoundActivity.class);

                new_game.putExtra("finalScreenSelection", "menu");
                setResult(RESULT_OK,new_game);

                for (int i = 0; i < numOfPlayers; ++i)

                finish();
            }
        });

    }
}
