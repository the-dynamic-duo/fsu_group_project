package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;


public class FinalScreenActivity extends AppCompatActivity {
    public static final String FINAL_PLAYERS = "plyrs";
    public static final String P1_SCORE = "score1";
    public static final String P1_NAME = "player1";
    public static final String P2_SCORE = "score2";
    public static final String P2_NAME = "player2";
    public static final String P3_SCORE = "score3";
    public static final String P3_NAME = "player3";
    public static final String P4_SCORE = "score4";
    public static final String P4_NAME = "player4";

    MediaPlayer mediaPlayer;

    int numOfPlayers, player1_score, player2_score, player3_score, player4_score;

    int[] scores = {0, 0, 0, 0};
    String[] player_order = {P1_NAME, P2_NAME, P3_NAME, P4_NAME};

    String player1_name, player2_name, player3_name, player4_name;
    Button play_again, main_menu;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    TextView text1, score1, text2, score2, text3, score3, text4, score4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        mediaPlayer = MediaPlayer.create(this, R.raw.end_theme);
        mediaPlayer.start();

        play_again = (Button) findViewById(R.id.button_player_again);
        main_menu = (Button) findViewById(R.id.button_main_menu);

        //set layouts
        linearLayout1 = (LinearLayout) findViewById(R.id.fs_LL1);
        linearLayout2 = (LinearLayout) findViewById(R.id.fs_LL2);
        linearLayout3 = (LinearLayout) findViewById(R.id.fs_LL3);
        linearLayout4 = (LinearLayout) findViewById(R.id.fs_LL4);
        resetLayout();

        //set text views
        text1 = (TextView) findViewById(R.id.fs_player1);
        score1 = (TextView) findViewById(R.id.fs_score1);
        text2 = (TextView) findViewById(R.id.fs_player2);
        score2 = (TextView) findViewById(R.id.fs_score2);
        text3 = (TextView) findViewById(R.id.fs_player3);
        score3 = (TextView) findViewById(R.id.fs_score3);
        text4 = (TextView) findViewById(R.id.fs_player4);
        score4 = (TextView) findViewById(R.id.fs_score4);

        numOfPlayers = getIntent().getIntExtra(FINAL_PLAYERS, 1);

        //get player1 info
        player1_name = getIntent().getStringExtra(P1_NAME);
        player1_score = getIntent().getIntExtra(P1_SCORE, 0);

        scores[0] = player1_score;
        player_order[0] = player1_name;
        if (numOfPlayers > 1) {
            multiplayerView();
            orderScores();
        }
        else {
            text1.setText(player1_name);
            score1.setText(Integer.toString(player1_score));
        }
        setListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mediaPlayer != null) {
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            }
            catch (Throwable t) {
                return;
            }
            mediaPlayer.start();
        }

        else {
            mediaPlayer = MediaPlayer.create(this, R.raw.end_theme);
            mediaPlayer.start();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void multiplayerView() {

        switch (numOfPlayers) {
            case 2:
                linearLayout2.setVisibility(View.VISIBLE);
                player2_name = getIntent().getStringExtra(P2_NAME);
                player2_score = getIntent().getIntExtra(P2_SCORE, 0);
                scores[1] = player2_score;
                player_order[1] = player2_name;
                break;
            case 3:
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.VISIBLE);
                player2_name = getIntent().getStringExtra(P2_NAME);
                player2_score = getIntent().getIntExtra(P2_SCORE, 0);
                player3_name = getIntent().getStringExtra(P3_NAME);
                player3_score = getIntent().getIntExtra(P3_SCORE, 0);
                scores[1] = player2_score;
                scores[2] = player3_score;
                player_order[1] = player2_name;
                player_order[2] = player3_name;
                break;
            case 4:
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);
                player2_name = getIntent().getStringExtra(P2_NAME);
                player2_score = getIntent().getIntExtra(P2_SCORE, 0);
                player3_name = getIntent().getStringExtra(P3_NAME);
                player3_score = getIntent().getIntExtra(P3_SCORE, 0);
                player4_name = getIntent().getStringExtra(P4_NAME);
                player4_score = getIntent().getIntExtra(P4_SCORE, 0);
                scores[1] = player2_score;
                scores[2] = player3_score;
                scores[3] = player4_score;
                player_order[1] = player2_name;
                player_order[2] = player3_name;
                player_order[3] = player4_name;
                break;
            default:
                break;
        }
    }

    public void orderScores() {
        //Bubble sort
        boolean swapped = true;
        int j = 0;
        int tmp_score;
        String tmp_player;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i <scores.length - j; i++) {
                if (scores[i] < scores[i + 1]) {
                    //swap score
                    tmp_score = scores[i];
                    scores[i] = scores[i + 1];
                    scores[i + 1] = tmp_score;
                    //swap player
                    tmp_player = player_order[i];
                    player_order[i] = player_order[i + 1];
                    player_order[i+ 1] = tmp_player;

                    swapped = true;
                }
            }
        }

        setScores();
    }

    public void setScores() {
        switch (numOfPlayers) {
            case 2:
                text1.setText(player_order[0]);
                score1.setText(Integer.toString(scores[0]));
                text2.setText(player_order[1]);
                score2.setText(Integer.toString(scores[1]));
                break;
            case 3:
                text1.setText(player_order[0]);
                score1.setText(Integer.toString(scores[0]));
                text2.setText(player_order[1]);
                score2.setText(Integer.toString(scores[1]));
                text3.setText(player_order[2]);
                score3.setText(Integer.toString(scores[2]));
                break;
            case 4:
                text1.setText(player_order[0]);
                score1.setText(Integer.toString(scores[0]));
                text2.setText(player_order[1]);
                score2.setText(Integer.toString(scores[1]));
                text3.setText(player_order[2]);
                score3.setText(Integer.toString(scores[2]));
                text4.setText(player_order[3]);
                score4.setText(Integer.toString(scores[3]));
                break;
            default:
                break;
        }
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
                resetLayout();

                for (int i = 0; i < numOfPlayers; ++i)

                finish();
            }
        });

    }

    public void resetLayout() {
        linearLayout2.setVisibility(View.INVISIBLE);
        linearLayout3.setVisibility(View.INVISIBLE);
        linearLayout4.setVisibility(View.INVISIBLE);
    }
}
