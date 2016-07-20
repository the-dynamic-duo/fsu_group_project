/********************************************************************
 Player.java
 Adam Taylor & Timothy Ingle
 COP-4656 - Group Project (The Dynamic Duo)
 07/09/2016

 The Player class creates an instance of a player for the game
 "You Don't Nole Jack"

 Player() stores the player's name, ID, current score, high score,
 and number of games played for each instance of the class.

 A count of the number of players and a map of each player ID to
 their corresponding name is shared across all instances of the class
 *******************************************************************/

package com.fsu.tri13.youdontnolejack;

import android.widget.Button;

import java.util.Map;
import java.util.LinkedHashMap;

public class Player
{
    private int currentScore, highScore, gamesPlayed, playerID;
    private String playerName;
    //TODO: Adam, Need this and new setSelection method to track score...
    private Button selection;

    private static int numPlayers = 0;
    private static Map<Integer,String> playerMap = new LinkedHashMap<Integer, String>();

    public Player(String inputName)
    {
        ++numPlayers;

        playerName = inputName;
        initializePlayer();
    }

    /* TODO: We need some sort of destructor for the class to decrement numPlayers and remove extra
             entries in playerMap. For now we can call close() if a certain player is not continuing
             with the game. It's too bad Java doesn't have an actual destructor... */
    public void remove()
    {
        --numPlayers;
        playerMap.remove(playerID);
    }

    private void initializePlayer()
    {
        currentScore = 0;
        highScore = 0;
        gamesPlayed = 0;
        playerID = numPlayers;
        playerMap.put(playerID, playerName);
    }

    public void resetAllTracking()
    {
        currentScore = 0;
        highScore = 0;
        gamesPlayed = 0;
    }

    public void setSelection(Button button) {
        selection = button;
    }
    public Button getSelection() {return selection;}

    public void incrementCurrentScore() {++currentScore;}

    public void   setCurrentScore(int score)    {currentScore = score;}
    public int    getCurrentScore()             {return currentScore;}
    public void   setHighScore(int score)       {highScore = score;}
    public int    getHighScore()                {return highScore;}
    public void   setGamesPlayed(int numPlayed) {gamesPlayed = numPlayed;}
    public int    getGamesPlayed()              {return gamesPlayed;}
    public void   setPlayerName(String name)    {playerName = name;}
    public String getPlayerName()               {return playerName;}

    // The following are internally managed and should have no set methods
    public int getPlayerID()                   {return playerID;}
    public int getNumPlayers()                 {return numPlayers;}

    // Search map for the given String value and return true if a name is taken.
    public boolean searchPlayerMapForName(String name) {return playerMap.containsValue(name);}
}
