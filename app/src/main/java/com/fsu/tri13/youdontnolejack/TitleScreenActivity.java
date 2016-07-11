package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleScreenActivity extends AppCompatActivity {
    Button sp, mp, howTo;
    //number of players
    int players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        sp = (Button) findViewById(R.id.button_single_player);
        mp = (Button) findViewById(R.id.button_multi_player);
        howTo = (Button) findViewById(R.id.button_how_to_play);

        players = 1;

        setListeners();
    }

    public void setListeners() {
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 1;
                startRound();
            }
        });

        mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //dialog to set player number with a spinner???
                //players = number returned from dialog
                startRound();
            }
        });

        howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog will go here
            }
        });
    }

    public void startRound() {
        Intent round_one = new Intent(this, GameRoundActivity.class);

        round_one.putExtra(GameRoundActivity.EXTRA_PLAYERS, players);
        startActivity(round_one);
    }
}
