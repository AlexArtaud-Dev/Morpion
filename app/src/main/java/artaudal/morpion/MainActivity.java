package artaudal.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

import artaudal.morpion.models.MyApp;

public class MainActivity extends AppCompatActivity {

    private MyApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApp) getApplicationContext();
    }

    public void SoloIngameMode(View view) {

        // Create intent for draft 3x3 activity (choose name (in database or create it, and take picture)
        Intent SoloInGameViewActivityIntent = new Intent(MainActivity.this, DraftComputerActivity.class);

        // Lancement de la demande de changement d'activité
        startActivity(SoloInGameViewActivityIntent);
    }

    public void DraftMode(View view) {
        // Create intent for draft 3x3 activity (choose name (in database or create it, and take picture)
        Intent DraftViewActivityIntent = new Intent(MainActivity.this, DraftActivity.class);
        app.setTypeOfGame(0);
        // Lancement de la demande de changement d'activité
        startActivity(DraftViewActivityIntent);
    }

    public void CustomGame(View view) {
        // Create intent for draft NxN activity (choose name (in database or create it, and take picture)
        Intent DraftViewActivityIntent = new Intent(MainActivity.this, DraftActivity.class);
        app.setTypeOfGame(1);
        // Lancement de la demande de changement d'activité
        startActivity(DraftViewActivityIntent);

    }
}