package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalScreenActivity extends AppCompatActivity {
    public static final String FINAL_PLAYERS = "plyrs";
    int numOfPlayers;
    Button play_again, main_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        play_again = (Button) findViewById(R.id.button_player_again);
        main_menu = (Button) findViewById(R.id.button_main_menu);

        numOfPlayers = getIntent().getIntExtra(FINAL_PLAYERS, 1);

        setListeners();
    }

    public void setListeners() {

        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_game = new Intent(FinalScreenActivity.this, GameRoundActivity.class);

                new_game.putExtra(GameRoundActivity.EXTRA_PLAYERS, numOfPlayers);
                startActivity(new_game);
            }
        });

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(FinalScreenActivity.this, TitleScreenActivity.class);

                startActivity(menu);
            }
        });

    }
}
