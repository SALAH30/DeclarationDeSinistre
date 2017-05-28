package innotech.td3exo3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class DossierActivity extends AppCompatActivity implements DossierAdapter.AdapterInterface{

    private FirebaseDatabase database;
    private DatabaseReference mRootReference;
    private DatabaseReference mChildReference;
    DatabaseReference myRef;
    public static StorageReference mStorageRef;
    long taille=0;
    ArrayList<Dossier> data;
    DossierAdapter dAdapter;
    private String type;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dossier);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        database = FirebaseDatabase.getInstance();
        mRootReference = database.getReference();


        mChildReference = mRootReference.child("dossiers");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        myRef = database.getReference("dossiers");
        myRef.keepSynced(true);


        data = new ArrayList<Dossier>();
        ListView listView = (ListView) findViewById(R.id.listdossier);
        dAdapter = new DossierAdapter(this, data,(DossierAdapter.AdapterInterface) DossierActivity.this);
        listView.setAdapter(dAdapter);

        add = (Button) findViewById(R.id.addDossier);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DossierActivity.this, InfoActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mChildReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dossier;
                taille = dataSnapshot.getChildrenCount();
                Intent intent = getIntent();
                type = intent.getStringExtra("type");

                SessionManager sessionManager = new SessionManager(getApplication());
                String idUser = sessionManager.getIdUser();
                System.out.println("ID dossier : " + idUser + " " + type);

                String nom = idUser;
                data.clear();
                for(int i=0;i<taille;i++){

                    nom = nom + i;
                    dossier = dataSnapshot.child(nom);
                    try {
                        //System.out.println("+++++++++++++ nom : " + nom);
                        //System.out.println("+++++++++++++ IDuser : " + idUser);


                        if(dossier.child("type").getValue(String.class).equals(type)){
                            String stest = dossier.child("id").getValue(String.class).substring(0, 28);
                            System.out.println("+++++++++++++ IDTEST : " + stest);

                    Dossier d1 = new Dossier(dossier.child("id").getValue(String.class)
                            ,dossier.child("type").getValue(String.class),
                            dossier.child("lieu").getValue(String.class),
                            dossier.child("date").getValue(String.class),
                            dossier.child("photo").getValue(String.class),
                            dossier.child("video").getValue(String.class),
                            dossier.child("liste").getValue(String.class),
                            dossier.child("etat").getValue(String.class),
                            Float.parseFloat(dossier.child("montant").getValue().toString()));
                    data.add(d1);
                    System.out.println("PHOTO  : " + d1.getPhoto());
                    }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    nom =idUser;
                }
                dAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DossierActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void buttonPressed(Dossier dossier) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("dossier",dossier);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
