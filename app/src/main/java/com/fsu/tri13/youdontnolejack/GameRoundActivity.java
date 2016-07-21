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
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class GameRoundActivity extends AppCompatActivity {

    //to set the selected amount of players
    public static final String EXTRA_PLAYERS = "players";

    QuestionDatabase db;

    TextView category_text, question_number_text, player_number_text, question_text, score_text;
    Button a,b,c,d;
    int numOfPlayers, playerTurn;
    Player player1, player2, player3, player4, currentPlayer;
    int questionNum;
    //current or selected category
    String currentCategory;
    //final screen activity choice
    String newGameOrMenu;

    String answer;

    //stores the available categories
    ArrayList<String> categories;

    private String name = "";
    Stack<String> playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        db = new QuestionDatabase(this);
        db.reset();

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
        questionNum = 1;

        currentCategory = "default";
        newGameOrMenu = "menu";

        //determines number of players set in title activity
        numOfPlayers = getIntent().getIntExtra(EXTRA_PLAYERS, 1);
        playerTurn = 1;

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
        //set first player as current player at start of game
        currentPlayer = player1;
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
                    .setCancelable(false)
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

    //For safety!
    public void enableButtons() {
        a.setClickable(true);
        b.setClickable(true);
        c.setClickable(true);
        d.setClickable(true);
    }

    public void disableButtons() {
        a.setClickable(false);
        b.setClickable(false);
        c.setClickable(false);
        d.setClickable(false);
    }


    public void setListeners()
    {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.setSelection(a);
                ++playerTurn;
                checkAnswer();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.setSelection(b);
                ++playerTurn;
                checkAnswer();
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.setSelection(c);
                ++playerTurn;
                checkAnswer();
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.setSelection(d);
                ++playerTurn;
                checkAnswer();
            }
        });
    }

    public void nextRound() {
        enableButtons();
        playerTurn = 1;
        currentPlayer = player1;

        //generate question from the DATABASE!!!!!
        final String[] answers = db.returnByCategory(currentCategory);

        String question = answers[0];
        answer = answers[1];

        // Store answer options in a list and shuffle them
        List<String> randomizedAnswers = new ArrayList<>();
        randomizedAnswers.add(answers[1]);
        randomizedAnswers.add(answers[2]);
        randomizedAnswers.add(answers[3]);
        randomizedAnswers.add(answers[4]);
        Collections.shuffle(randomizedAnswers);

        question_text.setText(question);

        //display answer buttons
        a.setText(randomizedAnswers.get(0));
        b.setText(randomizedAnswers.get(1));
        c.setText(randomizedAnswers.get(2));
        d.setText(randomizedAnswers.get(3));

        //display headers
        category_text.setText("Category: " + currentCategory);
        question_number_text.setText("Question Number: " + Integer.toString(questionNum));
        player_number_text.setText(currentPlayer.getPlayerName() + "'s Turn");
        score_text.setText("Current Score: " + Integer.toString(currentPlayer.getCurrentScore()));

        Log.d("debug", "control flow should pause here to wait for player input");
    }

    public void checkAnswer() {
        if (currentPlayer.getSelection().getText() == answer)
            currentPlayer.incrementCurrentScore();
        nextPlayer();
    }

    public void nextPlayer() {
        Log.d("debug", Integer.toString(playerTurn));

        if (numOfPlayers > 1 && playerTurn < 5) {
            switch (playerTurn) {
                case 2:
                    currentPlayer = player2;
                    break;
                case 3:
                    currentPlayer = player3;
                    break;
                case 4:
                    currentPlayer = player4;
                    break;
                default:
                    break;
            }
            player_number_text.setText(currentPlayer.getPlayerName() + "'s Turn");
            score_text.setText(
                    "Current Score: " + Integer.toString(currentPlayer.getCurrentScore()));
        }
        else {

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message mesg) {
                    throw new RuntimeException();
                }
            };
            new AlertDialog.Builder(this)
                    .setTitle("Correct Answer")
                    .setMessage("The correct answer was \"" + answer + "\".")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.sendMessage(handler.obtainMessage());
                            dialog.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();

            try { Looper.loop(); }
            catch(RuntimeException e2) {}

            //check for end of game
            if (questionNum >= 20) {
                disableButtons();
                finalScore();
            }
            else if (questionNum < 20 && questionNum % 5 == 0) {
                disableButtons();
                questionNum++;
                nextCategory();
            }
            else {
                questionNum++;
                nextRound();
            }
        }

        Log.d("debug","once again control flow should pause here");
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

                categories.remove(categories.indexOf(currentCategory));
                nextRound();

            }
            //back pressed
            else {
                clearGame();
                db.reset();
                db.close();
                finish();
            }
        }

        else if (requestCode == 99) {

            newGameOrMenu = data.getStringExtra("finalScreenSelection");

            if ("new_game".equals(newGameOrMenu)) {
                clearGame();
                nextCategory();
            }
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
                db.close();
                finish();
            }

        }
    }

    public void finalScore() {
        Intent final_score = new Intent(GameRoundActivity.this, FinalScreenActivity.class);

        //put number of players
        final_score.putExtra(FinalScreenActivity.FINAL_PLAYERS, numOfPlayers);
        //put scores and names
        final_score.putExtra(FinalScreenActivity.P1_SCORE,
                Integer.toString(player1.getCurrentScore()));
        final_score.putExtra(FinalScreenActivity.P1_NAME, player1.getPlayerName());
        if (numOfPlayers > 1) {
            switch (numOfPlayers) {
                case 2:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            Integer.toString(player2.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P2_NAME, player2.getPlayerName());
                    break;
                case 3:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            Integer.toString(player2.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P2_NAME, player2.getPlayerName());
                    final_score.putExtra(FinalScreenActivity.P3_SCORE,
                            Integer.toString(player3.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P3_NAME, player3.getPlayerName());
                    break;
                case 4:
                    final_score.putExtra(FinalScreenActivity.P2_SCORE,
                            Integer.toString(player2.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P2_NAME, player2.getPlayerName());
                    final_score.putExtra(FinalScreenActivity.P3_SCORE,
                            Integer.toString(player3.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P3_NAME, player3.getPlayerName());
                    final_score.putExtra(FinalScreenActivity.P4_SCORE,
                            Integer.toString(player4.getCurrentScore()));
                    final_score.putExtra(FinalScreenActivity.P4_NAME, player4.getPlayerName());
                    break;
                default:
                    break;
            }
        }
        int final_code = 99;
        startActivityForResult(final_score, final_code);
    }

    public void clearGame() {
        db.reset();
        questionNum = 1;
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
