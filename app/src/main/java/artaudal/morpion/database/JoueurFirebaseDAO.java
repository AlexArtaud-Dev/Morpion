package artaudal.morpion.database;


import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JoueurFirebaseDAO {

    private Joueur playerToReturn;

    private DatabaseReference databaseReference;

    public JoueurFirebaseDAO(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Joueur.class.getSimpleName());
    }

    public Task<Void> add(Joueur j) {
        System.out.println("Adding an user" + j.getPseudo());
        return databaseReference.child(j.getPseudo()).setValue(j);
    }

    public Task<Void> update(String pseudo, HashMap<String, Object> hashMap){
        return databaseReference.child(pseudo).updateChildren(hashMap);
    }

    public Joueur getByPseudo(String pseudoToSearch){
        setPlayerToReturn(null);
        List<Joueur> joueur = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String pseudo = ds.child("pseudo").getValue().toString();
                        String image = ds.child("image").getValue().toString();
                        Integer victory = Integer.parseInt(ds.child("victory").getValue().toString());
                        Integer draw = Integer.parseInt(ds.child("draw").getValue().toString());
                        Integer defeat = Integer.parseInt(ds.child("defeat").getValue().toString());
                        Joueur temp = new Joueur(pseudo, image, victory, defeat, draw);
                        if (pseudo.equals(pseudoToSearch)){
                            setPlayerToReturn(temp);
                        }
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("onCanceled: " + error);
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Player to return : " + getPlayerToReturn());
        return getPlayerToReturn();
    }

    public List<Joueur> getTopPlayers(){
        List<Joueur> joueurs = new ArrayList<>();
        databaseReference.orderByChild("victory").limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String pseudo = ds.child("pseudo").getValue().toString();
                        String image = ds.child("image").getValue().toString();
                        Integer victory = Integer.parseInt(ds.child("victory").getValue().toString());
                        Integer draw = Integer.parseInt(ds.child("draw").getValue().toString());
                        Integer defeat = Integer.parseInt(ds.child("defeat").getValue().toString());
                        Joueur temp = new Joueur(pseudo, image, victory, defeat, draw);
                        if (!pseudo.equals("Ordinateur")){
                            joueurs.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        while(joueurs.isEmpty()){}
        return joueurs;
    }

    public List<Joueur> getPlayers(String pseudo1, String pseudo2){
        List<Joueur> joueurs = new ArrayList<>();
        databaseReference.orderByChild("victory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String pseudo = ds.child("pseudo").getValue().toString();
                        String image = ds.child("image").getValue().toString();
                        Integer victory = Integer.parseInt(ds.child("victory").getValue().toString());
                        Integer draw = Integer.parseInt(ds.child("draw").getValue().toString());
                        Integer defeat = Integer.parseInt(ds.child("defeat").getValue().toString());
                        Joueur temp = new Joueur(pseudo, image, victory, defeat, draw);
                        if (pseudo.equals(pseudo1) || pseudo.equals(pseudo2)){
                            joueurs.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        while(joueurs.isEmpty()){}
        return joueurs;
    }

    public List<Joueur> getAll(){
        List<Joueur> joueurs = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String pseudo = ds.child("pseudo").getValue().toString();
                        String image = ds.child("image").getValue().toString();
                        Integer victory = Integer.parseInt(ds.child("victory").getValue().toString());
                        Integer draw = Integer.parseInt(ds.child("draw").getValue().toString());
                        Integer defeat = Integer.parseInt(ds.child("defeat").getValue().toString());
                        Joueur temp = new Joueur(pseudo, image, victory, defeat, draw);
                        if (!pseudo.equals("Ordinateur")){
                            joueurs.add(temp);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        while(joueurs.isEmpty()){}
        return joueurs;
    }

    public Joueur getPlayerToReturn() {
        return playerToReturn;
    }

    public void setPlayerToReturn(Joueur playerToReturn) {
        this.playerToReturn = playerToReturn;
    }
}
