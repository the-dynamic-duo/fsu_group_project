package com.fsu.tri13.youdontnolejack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameRoundActivity extends AppCompatActivity {

    //to set the selected amount of players
    public static final String EXTRA_PLAYERS = "players";
    //to set the selected category
    public static final String EXTRA_CURRENT_CATEGORY= "category";

    TextView category_text, question_number_text, player_number_text, question_text, score_text;
    Button a,b,c,d;
    int numOfPlayers;
    Player player1, player2, player3, player4;
    int questionNum;
    //current or selected category
    String currentCategory;
    //final screen activity choice
    String newGameOrMenu;

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

        //textviews
        category_text = (TextView) findViewById(R.id.text_category);
        question_number_text = (TextView) findViewById(R.id.text_question_number);
        player_number_text = (TextView) findViewById(R.id.text_player_number);
        question_text = (TextView) findViewById(R.id.text_question_text);
        score_text = (TextView) findViewById(R.id.text_player_score);

        categories = new ArrayList<>();
        questionNum = 0;

        currentCategory = "default";
        newGameOrMenu = "menu";
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
        //check for end of game
        if (questionNum >= 20)
            finalScore();
        //proceed with question

        questionNum++;
    }

    public void nextCategory() {
        Intent next_category = new Intent(this, CategoryActivity.class);

        next_category.putStringArrayListExtra(CategoryActivity.EXTRA_CATEGORY, categories);
        int requestCode = 1;
        startActivityForResult(next_category, requestCode);
    }

    //to obtain selected category & determine choice from final score screen
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (!"none - back pressed".equals(data.getStringExtra("categoryChoice"))) {
                currentCategory = data.getStringExtra("categoryChoice");

                Log.d("dbug", "Returning from category screen");
                Log.d("dbug", currentCategory);

                categories.remove(categories.indexOf(currentCategory));

                //TODO: database query would go here
                // we would fill our question vector here
                // and we would call nextround with a random question here
            } else //back pressed
                finish();
        }

        else if (requestCode == 99) {

            newGameOrMenu = data.getStringExtra("finalScreenSelection");

            if ("new_game".equals(newGameOrMenu)) {
                clearGame();
                nextCategory();
            }
            //TODO: This is a temporary solution because JAVA sucks
            //Update: and this still doesn't work????
            else if ("menu".equals(newGameOrMenu)) {
                clearGame();
                player1.close();
                if (numOfPlayers > 1) {
                    switch (numOfPlayers) {
                        case 2:
                            player2.close();
                            break;
                        case 3:
                            player2.close();
                            player3.close();
                            break;
                        case 4:
                            player2.close();
                            player3.close();
                            player4.close();
                            break;
                        default:
                            break;
                    }
                }
                finish();
            }

        }
    }

    public void finalScore() {
        Intent final_score = new Intent(GameRoundActivity.this, FinalScreenActivity.class);

        //put number of players
        final_score.putExtra(FinalScreenActivity.FINAL_PLAYERS, numOfPlayers);
        //put scores
        final_score.putExtra(FinalScreenActivity.P1_SCORE, player1.getCurrentScore());
        if (numOfPlayers > 1) {
            switch (numOfPlayers) {
                case 2:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            player2.getCurrentScore());
                    break;
                case 3:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            player2.getCurrentScore());
                    final_score.putExtra(FinalScreenActivity.P3_SCORE,
                            player3.getCurrentScore());
                    break;
                case 4:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            player2.getCurrentScore());
                    final_score.putExtra(FinalScreenActivity.P3_SCORE,
                            player3.getCurrentScore());
                    final_score.putExtra(FinalScreenActivity.P4_SCORE,
                            player4.getCurrentScore());
                    break;
                default:
                    break;
            }
        }
        int final_code = 99;
        startActivityForResult(final_score, final_code);
    }

    public void clearGame() {
        questionNum = 0;
        setCategories();
        player1.setCurrentScore(0);
        if (numOfPlayers > 1) {
            switch (numOfPlayers) {
                case 2:
                    player2.setCurrentScore(0);
                    break;
                case 3:
                    player2.setCurrentScore(0);
                    player3.setCurrentScore(0);
                    break;
                case 4:
                    player2.setCurrentScore(0);
                    player3.setCurrentScore(0);
                    player4.setCurrentScore(0);
                    break;
                default:
                    break;
            }
        }
    }
}
