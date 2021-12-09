package artaudal.morpion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

import artaudal.morpion.models.MyApp;
import artaudal.morpion.models.Partie;
//-------------------------------------------------------------------------------------------------------------------------------------------------//
// ici nous aurions pu utiliser l'activité permettant de faire du NxN, mais ayant créer cette activité auparavant , je préfère la laisser ainsi,
// Le modèle 3x3 aurait donc pu être juste appliqué grâce à l'activité IngameCustomActivity, et donc enlever un activité non nécessaire
//-------------------------------------------------------------------------------------------------------------------------------------------------//
public class IngameActivity extends AppCompatActivity {
    private MyApp app;
    private Partie game;
    private TextView playingPlayer_TextView;
    private String playerOne_Name;
    private String playerTwo_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);
        init();
    }
    public void init(){
        // Init app context
        app = (MyApp) getApplicationContext();
        playerOne_Name = app.getPlayerNameOne();
        playerTwo_Name = app.getPlayerNameTwo();


        // Randomly choose the first player to play
        Random rand = new Random();
        int random_player = rand.nextInt(3-1) + 1;

        //Create the game
        game = new Partie(3, 3, random_player);

        //Set display of the player name
        playingPlayer_TextView = findViewById(R.id.playingPlayerT);
        setPlayerDisp(getNameActivePlayer(game.getPlayerID()));
    }



    public void playBox(View view) {
        if (game.getPlayerID() == 1){
            ImageButton image = (ImageButton) view;
            image.setImageBitmap(app.getPlayerOneBitmapImage());
        }else{
            ImageButton image = (ImageButton) view;
            image.setImageBitmap(app.getPlayerTwoBitmapImage());
        }

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
        result = game.play(horizontal, vertical);
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
    public void gameDraw(){
        game.reset();
        Intent intent = new Intent(IngameActivity.this, ResultActivity.class);
        app.setPlayerNameWin(getNameActivePlayer(1));
        app.setPlayerNameLoose(getNameActivePlayer(2));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        app.setGameResult(0);
        startActivity(intent);
    }
    public void victory(int playerID){
        game.reset();
        Intent intent = new Intent(IngameActivity.this, ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (playerID == 1){
            app.setGameResult(1);
            app.setPlayerNameWin(getNameActivePlayer(playerID));
            app.setPlayerNameLoose(getNameActivePlayer(2));
        }else{
            app.setGameResult(2);
            app.setPlayerNameWin(getNameActivePlayer(playerID));
            app.setPlayerNameLoose(getNameActivePlayer(1));
        }
        startActivity(intent);
    }
    public void setPlayerDisp(String name){
        if (Locale.getDefault().getLanguage() == "en"){
            playingPlayer_TextView.setText(name + " turn to play...");
        }else{
            playingPlayer_TextView.setText("A "+ name + " de jouer...");
        }

    }
    public String getNameActivePlayer(int playerID){
        if (playerID == 1){
            return playerOne_Name;
        }else
            return playerTwo_Name;
        }

}
