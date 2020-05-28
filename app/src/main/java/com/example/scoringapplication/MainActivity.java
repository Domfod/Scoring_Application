package com.example.scoringapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DialogHelper.DialogHelperListener {

    public static int player1, player2;
    int playerDiff;
    public static int gameScore1, gameScore2;
    int switchCount;
    int serverCount;
    int scoreCount;
    public static DatabaseHelper myDb;
    String lPlayerSavedName, rPlayerSavedName, Notes;

    public void rightScoreChange(int a) {
        TextView rightTextScore = findViewById(R.id.rightTextScore);
        rightTextScore.setText("" + a);
    }

    public void leftScoreChange(int a) {
        TextView leftTextScore = findViewById(R.id.leftTextScore);
        leftTextScore.setText("" + a);
    }

    public void scoreChange() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isTwoPointDifferenceEnabled = prefs.getBoolean("2_point_diff_y/n", true);
        TextView leftScore = findViewById(R.id.leftScore);
        TextView rightScore = findViewById(R.id.rightScore);
        if (player1 > 11 && playerDiff ==2) {
            gameScore1 = gameScore1 + 1;
            leftScore.setText("" + gameScore1);
        }
        else if (player1 > 11 && player1 > player2) {
            gameScore1 = gameScore1 + 1;
            leftScore.setText("" + gameScore1);
        }
        else if (player2 > player1 && playerDiff > 2 && player2 > 11) {
            gameScore2 = gameScore2 + 1;
            rightScore.setText("" + gameScore2);

        }
        else if (player2 > player1 && !isTwoPointDifferenceEnabled && player2 > 11) {
            gameScore2++;
            rightScore.setText("" + gameScore2);
        }
    }

    public void switchGameScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isServeSwitchEnabled = prefs.getBoolean("switch_serve", true);
        TextView leftGameScore = findViewById(R.id.leftScore);
        TextView rightGameScore = findViewById(R.id.rightScore);
        int temp;
        if (switchCount % 2 == 0 && isServeSwitchEnabled) {
            temp = gameScore1;
            gameScore1 = gameScore2;
            gameScore2 = temp;
        }
        else if(isServeSwitchEnabled) {
            temp = gameScore2;
            gameScore2 = gameScore1;
            gameScore1 = temp;
        }

        leftGameScore.setText("" + gameScore1);
        rightGameScore.setText("" + gameScore2);
    }

    public void switchGameScoreButton() {
        TextView leftGameScore = findViewById(R.id.leftScore);
        TextView rightGameScore = findViewById(R.id.rightScore);
        int temp;
        if (switchCount % 2 == 0) {
            temp = gameScore1;
            gameScore1 = gameScore2;
            gameScore2 = temp;
        }
        else {
            temp = gameScore2;
            gameScore2 = gameScore1;
            gameScore1 = temp;
        }

        leftGameScore.setText("" + gameScore1);
        rightGameScore.setText("" + gameScore2);
    }

    public void scoreReset() {
        TextView leftScore = findViewById(R.id.leftTextScore);
        TextView rightScore = findViewById(R.id.rightTextScore);
        player1 = 0;
        player2= 0;

        leftScore.setText("" + player1);
        rightScore.setText("" + player2);
    }

    public void hardReset() {
        TextView leftScore = findViewById(R.id.leftTextScore);
        TextView rightScore = findViewById(R.id.rightTextScore);
        TextView leftGameScore = findViewById(R.id.leftScore);
        TextView rightGameScore = findViewById(R.id.rightScore);
        player1 = 0;
        player2= 0;
        gameScore1 = 0;
        gameScore2= 0;

        leftScore.setText("" + player1);
        rightScore.setText("" + player2);
        leftGameScore.setText("" + gameScore1);
        rightGameScore.setText("" + gameScore2);
        startActivity(new Intent(MainActivity.this, server.class));
    }

    public void hideServes() {
        TextView leftServe = findViewById(R.id.leftServe);
        TextView rightScore = findViewById(R.id.rightServe);

        leftServe.setVisibility(View.GONE);
        rightScore.setVisibility(View.GONE);
    }
    public void twoPoints(int a, int b) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean  isTwoPointDiffEnabled = prefs.getBoolean("2_point_diff_y/n", true);
        if (isTwoPointDiffEnabled && a > b) {
            playerDiff = (a-b);
        }
        else if (isTwoPointDiffEnabled && b > a) {
            playerDiff = (b-a);
        }
    }

    public void serveDeclaration() {
        TextView leftServer = findViewById(R.id.leftServe);
        TextView rightServer = findViewById(R.id.rightServe);
        if (server.serve == "left") {
            leftServer.setVisibility(View.VISIBLE);
        }
        else if (server.serve == "right") {
            rightServer.setVisibility(View.VISIBLE);
        }
    }

    public void serveSwitch() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serveInterval = prefs.getString("point_interval", "2");
        TextView leftServe = findViewById(R.id.leftServe);
        TextView rightServe = findViewById(R.id.rightServe);

        int serveInt = Integer.parseInt(serveInterval);
        serverCount++;
            if (serverCount % serveInt == 0) {

                switch (rightServe.getVisibility()) {
                    case View.GONE:
                        rightServe.setVisibility(View.VISIBLE);
                        break;

                    case View.VISIBLE:
                        rightServe.setVisibility(View.GONE);
                        break;
                }

                switch (leftServe.getVisibility()) {
                    case View.GONE:
                        leftServe.setVisibility(View.VISIBLE);
                        break;

                    case View.VISIBLE:
                        leftServe.setVisibility(View.GONE);
                        break;
                }

            }

        }

        public void scoreChangeServerSide() {
            TextView leftServe = findViewById(R.id.leftServe);
            TextView rightServe = findViewById(R.id.rightServe);
            serverCount = 0;

            if (server.serve.equals("left")) {
                leftServe.setVisibility(View.VISIBLE);
                rightServe.setVisibility(View.GONE);
            }
            else if (server.serve.equals("right")) {
                rightServe.setVisibility(View.VISIBLE);
                leftServe.setVisibility(View.GONE);
            }
        }

        public void showPopup (View v) {
            PopupMenu popup = new PopupMenu(this, v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.popup_menu);
            popup.show();
        }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.reset:
                startActivity(new Intent(MainActivity.this, server.class));
                return true;
            case R.id.switch1:
               //switchCount = switchCount + 1;
               //switchGameScoreButton();
                Toast.makeText(MainActivity.this, "left player = "+ lPlayerSavedName  , Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "right player = "+ rPlayerSavedName  , Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "notes = "+ Notes  , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return false;
            case R.id.saveGame:
                openDialog();
            default:
                return false;

        }

    }

    public void openDialog() {
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.show(getSupportFragmentManager(), "Player Names");
    }

    public void winGameRightSide() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String gameLen = prefs.getString("game_len", "11");
        int gameLength = Integer.parseInt(gameLen);
        boolean isGameDiffEnabled = prefs.getBoolean("2_point_diff_y/n", true);
        if (player2 > gameLength && !isGameDiffEnabled){
            scoreChange();
            player2 = 0;
            rightScoreChange(player2);
            scoreReset();
            scoreChangeServerSide();
            switchCount = switchCount + 1;
            switchGameScore();
        }
        if (player2 > gameLength && playerDiff > 2) {
            scoreChange();
            player2 = 0;
            rightScoreChange(player2);
            scoreReset();
            scoreChangeServerSide();
            switchCount = switchCount + 1;
            switchGameScore();
        }
    }

    public void winGameLeftSide() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String gameLen = prefs.getString("game_len", "11");
        int gameLength = Integer.parseInt(gameLen);
        boolean isGameDiffEnabled = prefs.getBoolean("2_point_diff_y/n", true);
        if (player1 > gameLength && !isGameDiffEnabled) {
            scoreChange();
            player1 = 0;
            leftScoreChange(player1);
            scoreReset();
            scoreChangeServerSide();
            switchCount = switchCount + 1;
            switchGameScore();
        }
        if (player1 > gameLength && playerDiff > 2) {
            scoreChange();
            player1 = 0;
            leftScoreChange(player1);
            scoreReset();
            scoreChangeServerSide();
            switchCount = switchCount + 1;
            switchGameScore();
        }
    }

    public void leftPlayerTakeaway() {
        player1--;

        TextView leftServeText = findViewById(R.id.leftServe);
        TextView rightServeText = findViewById(R.id.rightServe);
        Boolean leftServeStatus = leftServeText.isShown();
        Boolean rightServeStatus = rightServeText.isShown();

        if (serverCount % 2 == 0 && leftServeStatus) {
            leftServeText.setVisibility(View.GONE);
            rightServeText.setVisibility(View.VISIBLE);

        }
        else if (serverCount % 2 == 0 && rightServeStatus) {
            rightServeText.setVisibility(View.GONE);
            leftServeText.setVisibility(View.VISIBLE);
        }
        serverCount--;
        TextView leftBut = findViewById(R.id.leftTextScore);
        leftBut.setText("" + player1);
    }

    public void rightPlayerTakeaway() {
        player2--;

        TextView leftServeText = findViewById(R.id.leftServe);
        TextView rightServeText = findViewById(R.id.rightServe);
        Boolean leftServeStatus = leftServeText.isShown();
        Boolean rightServeStatus = rightServeText.isShown();

        if (serverCount % 2 == 0 && leftServeStatus) {
            leftServeText.setVisibility(View.GONE);
            rightServeText.setVisibility(View.VISIBLE);

        }
        else if (serverCount % 2 == 0 && rightServeStatus) {
            rightServeText.setVisibility(View.GONE);
            leftServeText.setVisibility(View.VISIBLE);
        }
        serverCount--;
        TextView rightBut = findViewById(R.id.rightTextScore);
        rightBut.setText("" + player2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideServes();
        serveDeclaration();
        myDb = new DatabaseHelper(this);



        final Button leftBut = (Button) findViewById(R.id.leftBut);
        leftBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                player1 = player1 + 1;
                leftScoreChange(player1);
                twoPoints(player1, player2);
                scoreCount++;
                serveSwitch();

                winGameLeftSide();

            }
        });

        leftBut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                leftPlayerTakeaway();
                return true;
            }
        });

        final Button rightBut = (Button) findViewById(R.id.rightBut);
        rightBut.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                player2 = player2 + 1;
                rightScoreChange(player2);
                twoPoints(player1, player2);
                scoreCount++;
                serveSwitch();

                winGameRightSide();
            }
        });

        rightBut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                rightPlayerTakeaway();
                return true;
            }
        });



            }


    @Override
    public void getTexts(String lName, String rName, String notes) {
        lPlayerSavedName = lName;
        rPlayerSavedName = rName;
        Notes = notes;
    }
}
