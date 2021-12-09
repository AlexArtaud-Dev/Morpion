package artaudal.morpion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import artaudal.morpion.database.Joueur;
import artaudal.morpion.database.JoueurFirebaseDAO;
import artaudal.morpion.models.MyApp;

public class ResultActivity extends AppCompatActivity {

    private static final String GAME_RESULT = "0";
    private static final String PLAYER_WIN = "";
    private static final String PLAYER_LOOSE = "";
    private MyApp app;
    private ResultAdapter adapter;
    private ListView listBestPlayers;
    private JoueurFirebaseDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        dao = new JoueurFirebaseDAO();
        app = (MyApp) getApplicationContext();
        if (savedInstanceState != null) {
            app.setGameResult(savedInstanceState.getInt(GAME_RESULT));
            app.setPlayerNameWin(savedInstanceState.getString(PLAYER_WIN));
            app.setPlayerNameLoose(savedInstanceState.getString(PLAYER_LOOSE));
        }
        init();
    }

    private void init(){
        listBestPlayers = findViewById(R.id.listView);
        adapter = new ResultAdapter(this, new ArrayList<Joueur>());
        listBestPlayers.setAdapter(adapter);

        TextView displayResult = findViewById(R.id.displayResult);
        if (app.getGameResult() != 0){
            if (Locale.getDefault().getLanguage() == "en"){
                displayResult.setText("Victory of " + app.getPlayerNameWin() + " on " + app.getPlayerNameLoose());
            }else{
                displayResult.setText("Victoire de " + app.getPlayerNameWin() + " sur " + app.getPlayerNameLoose());
            }
            setResult(app.getPlayerNameWin(), 1);
            setResult(app.getPlayerNameLoose(), 2);
        }else{
            if (Locale.getDefault().getLanguage() == "en"){
                displayResult.setText("Draw !");
            }else{
                displayResult.setText("Match Nul !");
            }
            setResult(app.getPlayerNameWin(), 0);
            setResult(app.getPlayerNameLoose(), 0);
        }
    }

    private void setResult(String pseudo, int result) {
        System.out.println("SetResult : " + pseudo);
        class SetResult extends AsyncTask<Void, Void, Joueur> {


            @Override
            protected Joueur doInBackground(Void... voids) {
                return dao.getByPseudo(pseudo);
            }

            @Override
            protected void onPostExecute(Joueur player) {
                super.onPostExecute(player);
                switch (result){
                    case 0: // Draw
                        updatePlayerResult(player, 0);
                        break;
                    case 1: // Victory
                        updatePlayerResult(player, 1);
                        break;
                    case 2: //Defeat
                        updatePlayerResult(player, 2);
                        break;
                }
            }
        }


        SetResult sr = new SetResult();
        sr.execute();
    }

    private void updatePlayerResult(Joueur player, int result) {

        class UpdatePlayerResult extends AsyncTask<Void, Void, Joueur> {

            @Override
            protected Joueur doInBackground(Void... voids) {
                HashMap<String, Object> parameters = new HashMap<>();
                switch (result){
                    case 0: // Draw
                        parameters.put("pseudo", player.getPseudo());
                        parameters.put("image", player.getImage());
                        parameters.put("victory", player.getVictory());
                        parameters.put("defeat", player.getDefeat());
                        parameters.put("draw", player.getDraw()+1);
                        break;
                    case 1: // Victory
                        parameters.put("pseudo", player.getPseudo());
                        parameters.put("image", player.getImage());
                        parameters.put("victory", player.getVictory()+1);
                        parameters.put("defeat", player.getDefeat());
                        parameters.put("draw", player.getDraw());
                        break;
                    case 2: //Defeat
                        parameters.put("pseudo", player.getPseudo());
                        parameters.put("image", player.getImage());
                        parameters.put("victory", player.getVictory());
                        parameters.put("defeat", player.getDefeat()+1);
                        parameters.put("draw", player.getDraw());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + result);
                }

                dao.update(player.getPseudo(), parameters).addOnSuccessListener(suc -> {
                    Toast.makeText(ResultActivity.this, "Résultats mis à jour (firebase)", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er -> {
                    Toast.makeText(ResultActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                });
                return player;
            }

            @Override
            protected void onPostExecute(Joueur player) {
                super.onPostExecute(player);
                updateTopPlayers();
            }
        }


        UpdatePlayerResult upr = new UpdatePlayerResult();
        upr.execute();
    }

    private void updateTopPlayers(){
        class UpdateTopPlayers extends AsyncTask<Void, Void, List<Joueur>> {


            @Override
            protected List<Joueur> doInBackground(Void... voids) {
                return dao.getTopPlayers();
            }

            @Override
            protected void onPostExecute(List<Joueur> players) {
                super.onPostExecute(players);
                Collections.reverse(players);
                adapter.clear();
                adapter.addAll(players);
                adapter.notifyDataSetChanged();
            }
        }


        UpdateTopPlayers utp = new UpdateTopPlayers();
        utp.execute();
    }
    
    public void restart(View view) {
        Intent intent = new Intent(ResultActivity.this, DraftActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent mainMenu = new Intent(ResultActivity.this, MainActivity.class);
        mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainMenu);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save the user's current game state
        savedInstanceState.putInt(GAME_RESULT, app.getGameResult());
        savedInstanceState.putString(PLAYER_WIN, app.getPlayerNameWin());
        savedInstanceState.putString(PLAYER_LOOSE, app.getPlayerNameLoose());
        // Always call the superclass so it can save
        // the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
