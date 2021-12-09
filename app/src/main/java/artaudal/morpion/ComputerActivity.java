package artaudal.morpion;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import artaudal.morpion.models.Computer;
import artaudal.morpion.models.ComputerLevel;
import artaudal.morpion.models.MyApp;
import artaudal.morpion.models.Partie;

public class ComputerActivity extends AppCompatActivity {

    private Computer bot;
    private Partie game;
    private MyApp app;
    private TextView playingPlayer_TextView;
    private String playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        init();
    }
    private void init(){
        game = new Partie(3,3, 1);
        bot = new Computer(game);
        app = (MyApp) getApplicationContext();
        bot.setLevel(app.getCompLevel());
        playerName = app.getPlayerNameOne();
        playingPlayer_TextView = findViewById(R.id.playingPlayerComp);
        computerPlay();
    }

    public void playerPlay(View view) {
        ImageButton image = (ImageButton) view;
        image.setImageBitmap(app.getPlayerOneBitmapImage());
        switch (view.getId()) {
            case R.id.button_01:
                makeMove(0,0);
                break;
            case R.id.button_02:
                makeMove(1,0);
                break;
            case R.id.button_03:
                makeMove(2,0);
                break;
            case R.id.button_04:
                makeMove(0,1);
                break;
            case R.id.button_05:
                makeMove(1,1);
                break;
            case R.id.button_06:
                makeMove(2,1);
                break;
            case R.id.button_07:
                makeMove(0,2);
                break;
            case R.id.button_08:
                makeMove(1,2);
                break;
            case R.id.button_09:
                makeMove(2,2);
                break;
        }
        view.setOnClickListener(null);
    }

    public void makeMove(int horizontal, int vertical){
        int result;
        result = game.playerPlayAgainstIA(horizontal, vertical);
        switch (result){
            case -1:
                setPlayerDisp(getNameActivePlayer(game.getPlayerID()));
                computerPlay();
                break;
            case 0:
                gameDraw();
                break;
            case 1:
                victory(1);
                break;
            case 2:
                victory(2);
                break;
        }
    }

    public void computerPlay() {
        int result = -1;
        if (bot.getLevel() == ComputerLevel.UNBEATABLE){
            result = bot.playComputerMove();
        }else{
            if (bot.getLevel() == ComputerLevel.RANDOM){
                result = bot.playRandomComputerMove();
            }
        }

        changePlayedButtonByBot(bot.getPreviousMove()[0], bot.getPreviousMove()[1]);
        switch (result){
            case -1:
                setPlayerDisp(getNameActivePlayer(game.getPlayerID()));
                break;
            case 0:
                gameDraw();
                break;
            case 1:
                victory(1);
                break;
            case 2:
                victory(2);
                break;
        }
    }

    private void victory(int playerID){
        game.reset();
        Intent intent = new Intent(ComputerActivity.this, ResultComputerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        MyApp app = (MyApp) getApplicationContext();
        app.setGameResult(1);

        if (playerID == 1){
            app.setPlayerNameWin("Ordinateur");
            app.setPlayerNameLoose(getNameActivePlayer(2));
        }else{
            app.setPlayerNameLoose("Ordinateur");
            app.setPlayerNameWin(getNameActivePlayer(playerID));
        }
        startActivity(intent);
    }

    private void gameDraw(){
        game.reset();
        Intent intent = new Intent(ComputerActivity.this, ResultComputerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApp app = (MyApp) getApplicationContext();
        app.setGameResult(0);
        app.setPlayerNameWin("Ordinateur");
        app.setPlayerNameLoose(getNameActivePlayer(2));
        startActivity(intent);
    }

    public void setPlayerDisp(String name){
        playingPlayer_TextView.setText("A " + name + " de jouer...");
    }

    public void changePlayedButtonByBot(int horizontal, int vertical){
        ImageButton imgToChange = null;
        if (horizontal == 0 && vertical == 0){ imgToChange = findViewById(R.id.button_01); }
        if (horizontal == 1 && vertical == 0){ imgToChange = findViewById(R.id.button_02); }
        if (horizontal == 2 && vertical == 0){ imgToChange = findViewById(R.id.button_03); }
        if (horizontal == 0 && vertical == 1){ imgToChange = findViewById(R.id.button_04); }
        if (horizontal == 1 && vertical == 1){ imgToChange = findViewById(R.id.button_05); }
        if (horizontal == 2 && vertical == 1){ imgToChange = findViewById(R.id.button_06); }
        if (horizontal == 0 && vertical == 2){ imgToChange = findViewById(R.id.button_07); }
        if (horizontal == 1 && vertical == 2){ imgToChange = findViewById(R.id.button_08); }
        if (horizontal == 2 && vertical == 2){ imgToChange = findViewById(R.id.button_09); }
        imgToChange.setBackgroundResource(R.drawable.bot);
        imgToChange.setOnClickListener(null);
    }

    public String getNameActivePlayer(int playerID){
        if (playerID == 1){
            return "l'ordinateur";
        }else
            return playerName;
    }



}
