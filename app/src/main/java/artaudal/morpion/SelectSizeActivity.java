package artaudal.morpion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import artaudal.morpion.models.MyApp;


public class SelectSizeActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);
        app = (MyApp) getApplicationContext();
        init();
    }
    private void init(){
        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(4);
        numberPicker.setMaxValue(10);
    }

    public void goToCustomGame(View view) {
        app.setSizeOfGame(numberPicker.getValue());
        Intent IngameCustomViewActivityIntent = new Intent(SelectSizeActivity.this, IngameCustomActivity.class);
        IngameCustomViewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(IngameCustomViewActivityIntent);
    }
}
