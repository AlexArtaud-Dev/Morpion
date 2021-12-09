package artaudal.morpion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import artaudal.morpion.database.Joueur;
import artaudal.morpion.models.MyApp;
import artaudal.morpion.models.Partie;

public class IngameCustomActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {


    private int horizontalDim;
    private int verticalDim;
    private Partie game;
    private MyApp app;
    MyRecyclerViewAdapter adapter;
    TextView playingPlayer_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_size_ingame);
        app = (MyApp) getApplicationContext();
        init();
    }

    private void init() {
        horizontalDim = app.getSizeOfGame();
        verticalDim = app.getSizeOfGame();
        Random rand = new Random();
        int random_player = rand.nextInt(3 - 1) + 1;
        game = new Partie(horizontalDim, verticalDim, random_player);
        String[] data = new String[game.getHorizontal() * game.getVertical()];
        // Creation de toutes les case de jeu
        for (int i = 0; i < game.getVertical() * game.getHorizontal(); i++) {
            data[i] = String.valueOf(i);
        }

        // Creation du recycler grace à l'adapter
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = game.getHorizontal();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        playingPlayer_TextView = findViewById(R.id.playingPlayerT);
        setPlayerDisp(getNameActivePlayer(game.getPlayerID()));
    }

    @Override
    public void onItemClick(View view, int position) {
        if (game.getPlayerID() == 1) {
            makeMove(position / game.getHorizontal(), position % game.getHorizontal());
            view.setBackgroundDrawable(makeDrawableBitmap(app.getPlayerOneBitmapImage()));
            view.setOnClickListener(null);
        } else {
            makeMove(position / game.getHorizontal(), position % game.getHorizontal());
            view.setBackgroundDrawable(makeDrawableBitmap(app.getPlayerTwoBitmapImage()));
            view.setOnClickListener(null);
        }

//        System.out.println("Test : " + "(" + position / game.getHorizontal() + "-" + position % game.getHorizontal() + ")");
    }

    public void makeMove(int horizontal, int vertical) {
        int result;
        result = game.play(horizontal, vertical);
        switch (result) {
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

    private void gameDraw() {
        System.out.println("Match nul");
        game.reset();
        Intent intent = new Intent(IngameCustomActivity.this, ResultActivity.class);
        app.setPlayerNameWin(app.getPlayerNameOne());
        app.setPlayerNameLoose(app.getPlayerNameTwo());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        app.setGameResult(0);
        startActivity(intent);
    }

    private void victory(int playerID) {
        game.reset();
        Intent intent = new Intent(IngameCustomActivity.this, ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (playerID == 1){
            app.setGameResult(1);
            app.setPlayerNameWin(app.getPlayerNameOne());
            app.setPlayerNameLoose(app.getPlayerNameTwo());
        }else{
            app.setGameResult(2);
            app.setPlayerNameWin(app.getPlayerNameTwo());
            app.setPlayerNameLoose(app.getPlayerNameOne());
        }
        startActivity(intent);
    }

    public void setPlayerDisp(String name) {
        if (Locale.getDefault().getLanguage() == "en"){
            playingPlayer_TextView.setText(name + " turn to play...");
        }else{
            playingPlayer_TextView.setText("A "+ name + " de jouer...");
        }
    }

    public String getNameActivePlayer(int playerID) {
        if (playerID == 1) {
            return app.getPlayerNameOne();
        } else
            return app.getPlayerNameTwo();
    }


    private BitmapDrawable makeDrawableBitmap(Bitmap bitmap) {
        BitmapDrawable background = new BitmapDrawable(bitmap);
        return background;
    }

}
