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

public class DraftComputerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmapOne;
    private String[] images;
    private ImageView imagePlayerOne;
    private String[] items;
    private Spinner dropdown1;
    private EditText pseudo1;
    private Joueur firstPlayer;
    private MyApp app;
    private JoueurFirebaseDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_computer);
        dao = new JoueurFirebaseDAO();
        init();
    }

    private void init(){

        // Init context
        app = (MyApp) getApplicationContext();
        //Init Spinners with default values
        dropdown1 = findViewById(R.id.spinner1);
        pseudo1 = findViewById(R.id.pseudo1);
        imagePlayerOne = findViewById(R.id.imageView);
        getJoueurs();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        pseudo1.setText(items[i]);
        if (images[i] != null){
            imageBitmapOne = toBitMap(images[i]);
            imagePlayerOne.setImageBitmap(toBitMap(images[i]));
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown1.setAdapter(adapter);
        dropdown1.setOnItemSelectedListener(this);
    }

    private void getJoueurs() {
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

    private void checkJoueurOneExistence() {

        class CheckJoueurOneExistence extends AsyncTask<Void, Void, Joueur> {


            @Override
            protected Joueur doInBackground(Void... voids) {
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
                    createJoueurs(temp);
                }

            }
        }

        CheckJoueurOneExistence cje = new CheckJoueurOneExistence();
        cje.execute();
    }

    private void createJoueurs(Joueur joueur) {

        class CreateJoueurs extends AsyncTask<Void, Void, Joueur> {

            @Override
            protected Joueur doInBackground(Void... voids) {
                dao.add(joueur);
                return joueur;
            }

            @Override
            protected void onPostExecute(Joueur joueur) {
                super.onPostExecute(joueur);
                    setFirstPlayer(joueur);
            }
        }

        CreateJoueurs cj = new CreateJoueurs();
        cj.execute();
    }

    public void startGame(View view) {
        if (pseudo1.getText().toString().isEmpty()){
            Toast.makeText(DraftComputerActivity.this, "Vous devez renseigner un pseudo !", Toast.LENGTH_LONG).show();
        }else{
            if (pseudo1.getText().toString().equals("Ordinateur") || pseudo1.getText().toString().equals("ordinateur")){
                Toast.makeText(DraftComputerActivity.this, "Vous ne pouvez pas vous nommer ordinateur !", Toast.LENGTH_LONG).show();
            }else{
                if ((imageBitmapOne != null)){
                    checkJoueurOneExistence();
                    Intent intent = new Intent(DraftComputerActivity.this, ChooseDifficultyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    app.setPlayerNameOne(pseudo1.getText().toString());
                    app.setPlayerOneBitmapImage(imageBitmapOne);
                    startActivity(intent);
                }else{
                    Toast.makeText(DraftComputerActivity.this, "Aucune image sélectionné !", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void takeFirstUserPicture(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmapOne = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmapOne);
            setBase64Image((Bitmap) extras.get("data"));
        }
    }

    private void setBase64Image(Bitmap image){
        String encoded = toBase64(image);
        imageBitmapOne = toBitMap(encoded);
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
}
