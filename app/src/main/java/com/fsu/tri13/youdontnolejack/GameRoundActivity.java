package com.fsu.tri13.youdontnolejack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    private String name = "";
    Stack<String> playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

       // TODO: These aren't executed in order when I test. wtf java?
            setPlayers();

            //initializes category list
            setCategories();
            //sets listeners for answer buttons
            setListeners();
            //starts first round by selecting first category
            nextCategory();

    }

    private void setPlayers()
    {
        playerNames = new Stack<String>();

        for (int i = 0; i < numOfPlayers; ++i)
        {
            name = "";
            promptForName(i);
            playerNames.push(name);
        }

        switch (numOfPlayers)
        {
            case 4:
                player4 = new Player(playerNames.pop());
                //Fall through
            case 3:
                player3 = new Player(playerNames.pop());
                //Fall through
            case 2:
                player2 = new Player(playerNames.pop());
                //Fall through
            case 1:
                player1 = new Player(playerNames.pop());
                break;
            default:
                break;
        }
    }

    private void promptForName(int currentPlayer)
    {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        ++currentPlayer; // account for zero-based integer
        String player = "Player " + currentPlayer;
        boolean reprompt = false;

        do {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(player);

            new AlertDialog.Builder(this)
                    .setTitle(!reprompt ? ("Please enter a name for " + player + ".") :
                            ("The name you entered is already taken.\nPlease choose another name."))
                    .setView(input)
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name = input.getText().toString();
                            if (playerNames.contains(name))
                            {
                                name = "";
                            }
                            handler.sendMessage(handler.obtainMessage());
                        }
                    })
                    .setNegativeButton("Return to Menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.sendMessage(handler.obtainMessage());
                            finish();
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();

            reprompt = true;

            try { Looper.loop(); }
            catch(RuntimeException e2) {}
        } while (name.equals(""));
    }

    private void setCategories() {
        categories.add(getResources().getString(R.string.category_academics));
        categories.add(getResources().getString(R.string.category_history));
        categories.add(getResources().getString(R.string.category_sports));
        categories.add(getResources().getString(R.string.category_student));
    }

    public void setListeners()
    {
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
                player1.remove();
                if (numOfPlayers > 1) {
                    switch (numOfPlayers) {
                        case 2:
                            player2.remove();
                            break;
                        case 3:
                            player2.remove();
                            player3.remove();
                            break;
                        case 4:
                            player2.remove();
                            player3.remove();
                            player4.remove();
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
