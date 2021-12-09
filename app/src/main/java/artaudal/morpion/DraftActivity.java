package artaudal.morpion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import artaudal.morpion.database.Joueur;
import artaudal.morpion.database.JoueurFirebaseDAO;
import artaudal.morpion.models.MyApp;

public class DraftActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private MyApp app;
    private int userTakingPicture;
    private Bitmap imageBitmapOne;
    private Bitmap imageBitmapTwo;
    private String[] images;
    private ImageView imagePlayerOne;
    private ImageView imagePlayerTwo;
    private String[] items;
    private Spinner dropdown1;
    private Spinner dropdown2;
    private EditText pseudo1;
    private EditText pseudo2;
    private Joueur firstPlayer;
    private Joueur secondPlayer;
    JoueurFirebaseDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        dao = new JoueurFirebaseDAO();
        init();
    }

    private void init(){
        // Init app context
        app = (MyApp) getApplicationContext();
        //Init Spinners with default values
        dropdown1 = findViewById(R.id.spinner1);
        dropdown2 = findViewById(R.id.spinner2);
        pseudo1 = findViewById(R.id.pseudo1);
        pseudo2 = findViewById(R.id.pseudo2);
        imagePlayerOne = findViewById(R.id.imageView);
        imagePlayerTwo = findViewById(R.id.imageView2);
        getJoueurs();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == dropdown1.getId()){
            pseudo1.setText(items[i]);
            if (images[i] != null){
                imageBitmapOne = toBitMap(images[i]);
                imagePlayerOne.setImageBitmap(toBitMap(images[i]));
            }
        }else {
            pseudo2.setText(items[i]);
            if (images[i] != null){
                imageBitmapTwo = toBitMap(images[i]);
                imagePlayerTwo.setImageBitmap(toBitMap(images[i]));
            }
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown1.setAdapter(adapter);
        dropdown1.setOnItemSelectedListener(this);
        dropdown2.setAdapter(adapter);
        dropdown2.setOnItemSelectedListener(this);
    }

    private void getJoueurs() {
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetJoueurs extends AsyncTask<Void, Void, List<Joueur>> {

            @Override
            protected List<Joueur> doInBackground(Void... voids) {
                return dao.getAll();
            }

            @Override
            protected void onPostExecute(List<Joueur> joueurs) {
                super.onPostExecute(joueurs);
                String[] temp = new String[joueurs.size()];
                String[] tempImages = new String[joueurs.size()];
                for(int i=0;i<joueurs.size();i++){
                    temp[i] = joueurs.get(i).getPseudo();
                    tempImages[i] = joueurs.get(i).getImage();
                }
                setImages(tempImages);
                setItems(temp);
                setAdapter();
            }
        }

        GetJoueurs gj = new GetJoueurs();
        gj.execute();
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public void setFirstPlayer(Joueur firstPlayer) {
        this.firstPlayer = firstPlayer; }

    public void setSecondPlayer(Joueur secondPlayer) {
        this.secondPlayer = secondPlayer; }

    private void checkJoueurOneExistence() {
        class CheckJoueurOneExistence extends AsyncTask<Void, Void, Joueur> {


            @Override
            protected Joueur doInBackground(Void... voids) {
                System.out.println("0-1-1");
                return dao.getByPseudo(pseudo1.getText().toString());
            }

            @Override
            protected void onPostExecute(Joueur existingPlayer) {
                super.onPostExecute(existingPlayer);
                if (existingPlayer != null){
                    setFirstPlayer(existingPlayer);
                }else{
                    Joueur temp = new Joueur(pseudo1.getText().toString(), "", 0 ,0,0);
                    temp.setImage(toBase64(imageBitmapOne));
                    createJoueurs(temp,1);
                }

            }
        }


        CheckJoueurOneExistence cje = new CheckJoueurOneExistence();
        cje.execute();
    }

    private void checkJoueurTwoExistence() {
        class CheckJoueurTwoExistence extends AsyncTask<Void, Void, Joueur> {


            @Override
            protected Joueur doInBackground(Void... voids) {
                return dao.getByPseudo(pseudo2.getText().toString());
            }

            @Override
            protected void onPostExecute(Joueur existingPlayer) {
                super.onPostExecute(existingPlayer);
                if (existingPlayer != null){
                    setSecondPlayer(existingPlayer);
                }else{
                    Joueur temp = new Joueur(pseudo2.getText().toString(), "", 0 ,0,0);
                    temp.setImage(toBase64(imageBitmapTwo));
                    createJoueurs(temp, 2);
                }

            }
        }


        CheckJoueurTwoExistence cje = new CheckJoueurTwoExistence();
        cje.execute();
    }

    private void createJoueurs(Joueur joueur, int numberOfPlayer) {

        class CreateJoueurs extends AsyncTask<Void, Void, Joueur> {

            @Override
            protected Joueur doInBackground(Void... voids) {
                dao.add(joueur);
                return joueur;
            }

            @Override
            protected void onPostExecute(Joueur joueur) {
                super.onPostExecute(joueur);
                switch (numberOfPlayer){
                    case 1:
                        setFirstPlayer(joueur);
                        break;
                    case 2:
                        setSecondPlayer(joueur);
                        break;
                }
            }
        }

        CreateJoueurs cj = new CreateJoueurs();
        cj.execute();
    }

    public void startGame(View view) throws InterruptedException {
        if (pseudo1.getText().toString().isEmpty() || pseudo2.getText().toString().isEmpty()){
            Toast.makeText(DraftActivity.this, R.string.renseignePlayers, Toast.LENGTH_LONG).show();
        }else{
            if (pseudo1.getText().toString().equals(pseudo2.getText().toString())){
                Toast.makeText(DraftActivity.this, R.string.diffPlayers, Toast.LENGTH_LONG).show();
            }else{
                if ((imageBitmapOne != null && imageBitmapTwo != null)){
                    checkJoueurOneExistence();

                    // Obligatoire pour handle le verrouillage des données de chaque joueur -> aurait pu être fait en utilisant des thread et des verrous //
                    Thread.sleep(500);


                    checkJoueurTwoExistence();
                    Intent intent;
                    if (app.getTypeOfGame() == 0){
                        intent = new Intent(DraftActivity.this, IngameActivity.class);
                    }else{
                        intent = new Intent(DraftActivity.this, SelectSizeActivity.class);
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    app.setPlayerNameOne(pseudo1.getText().toString());
                    app.setPlayerNameTwo(pseudo2.getText().toString());
                    app.setPlayerOneBitmapImage(imageBitmapOne);
                    app.setPlayerTwoBitmapImage(imageBitmapTwo);
                    startActivity(intent);
                }else{
                    Toast.makeText(DraftActivity.this, R.string.haveTwoImages, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void takeFirstUserPicture(View view) {
        dispatchTakePictureIntent(1);
    }

    public void takeSecondUserPicture(View view) {
        dispatchTakePictureIntent(2);
    }

    private void dispatchTakePictureIntent(int user) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (user == 1){
                userTakingPicture = 1;
            }
            if (user == 2){
                userTakingPicture = 2;
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DraftActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (userTakingPicture == 1){
                imageBitmapOne = (Bitmap) extras.get("data");
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(imageBitmapOne);
                setBase64Image(1, (Bitmap) extras.get("data"));
            }else{
                imageBitmapTwo = (Bitmap) extras.get("data");
                ImageView imageView2 = findViewById(R.id.imageView2);
                imageView2.setImageBitmap(imageBitmapTwo);
                setBase64Image(2, (Bitmap) extras.get("data"));
            }
        }
    }

    private void setBase64Image(int user, Bitmap image){
        String encoded = toBase64(image);
        if (user == 1){
            imageBitmapOne = toBitMap(encoded);
        }
        if (user == 2){
            imageBitmapTwo = toBitMap(encoded);
        }
    }

    private String toBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }
    private Bitmap toBitMap(String base64){
        String encodedString = base64;
        String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void setImages(String[] tempImages){
        images = tempImages;
    }

    @Override
    public void onBackPressed() {
        Intent mainMenu = new Intent(DraftActivity.this, MainActivity.class);
        mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainMenu);
    }
}
