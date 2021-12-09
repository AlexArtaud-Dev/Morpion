package artaudal.morpion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import artaudal.morpion.models.ComputerLevel;
import artaudal.morpion.models.MyApp;

public class ChooseDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
    }

    public void randomComputerGame(View view) {
        Intent intent = new Intent(ChooseDifficultyActivity.this, ComputerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApp app = (MyApp) getApplicationContext();
        app.setCompLevel(ComputerLevel.RANDOM);
        startActivity(intent);
    }

    public void unbeatableComputerGame(View view) {
        Intent intent = new Intent(ChooseDifficultyActivity.this, ComputerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApp app = (MyApp) getApplicationContext();
        app.setCompLevel(ComputerLevel.UNBEATABLE);
        startActivity(intent);
    }

}
