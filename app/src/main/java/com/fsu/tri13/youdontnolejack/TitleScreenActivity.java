package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class TitleScreenActivity extends AppCompatActivity {
    //For the spinner adapter
    private static final String[] mp_choices = {"How Many?", "2 Players",
            "3 Players", "4 Players"};

    Button sp, mp, howTo;
    Spinner spin;
    ArrayAdapter<String> aa;
    //number of players
    int players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        sp = (Button) findViewById(R.id.button_single_player);
        mp = (Button) findViewById(R.id.button_multi_player);
        howTo = (Button) findViewById(R.id.button_how_to_play);

        spin = (Spinner) findViewById(R.id.spinner1);
        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                mp_choices);
        aa.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line);
        spin.setAdapter(aa);

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
                spin.setVisibility(View.VISIBLE);
                setSpinner();
                //startRound();
            }
        });

        howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog will go here
                DialogFragment newFragment = new HowToPlayFragment();
                newFragment.show(getSupportFragmentManager(), "howtoplay");
            }
        });
    }

    public void setSpinner() {
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        players = 2;
                        startRound();
                        break;
                    case 2:
                        players = 3;
                        startRound();
                        break;
                    case 3:
                        players = 4;
                        startRound();
                    default:
                        break;
                }
                Log.d("debug", Integer.toString(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void startRound() {
        Intent round_one = new Intent(this, GameRoundActivity.class);

        round_one.putExtra(GameRoundActivity.EXTRA_PLAYERS, players);
        startActivity(round_one);
    }
}
