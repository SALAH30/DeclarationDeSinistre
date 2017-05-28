package innotech.td3exo3;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailInfoFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference mRootReference;
    private DatabaseReference mChildReference;
    DatabaseReference myRef;


    private static Dossier mDossier;

    EditText typeView;
    EditText dateView;
    EditText lieuView;
    EditText montantView;
    EditText listPersonneView;
    Button modifier;
    public DetailInfoFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(Dossier dossier){
        mDossier = dossier;
        return new DetailInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_composant1, container, false);
        typeView = (EditText) view.findViewById(R.id.typeView);
        dateView = (EditText) view.findViewById(R.id.dateview);
        lieuView = (EditText) view.findViewById(R.id.lieuview);
        montantView = (EditText) view.findViewById(R.id.montantView);
        listPersonneView = (EditText) view.findViewById(R.id.listePersonneView);

        typeView.setText(mDossier.getType());
        dateView.setText(mDossier.getDate());
        lieuView.setText(mDossier.getLieu());
        montantView.setText(mDossier.getMontant() + "");
        listPersonneView.setText(mDossier.getInformation());

        modifier = (Button) view.findViewById(R.id.modifier);

        System.out.println("+++++++++++++ ID : " + mDossier.getId());

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("dossiers");

        myRef.keepSynced(true);

        if (mDossier.getEtat().equals("ouvert")) {
            modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myRef.child(mDossier.getId()).child("type").setValue(typeView.getText().toString());
                    myRef.child(mDossier.getId()).child("lieu").setValue(lieuView.getText().toString());
                    myRef.child(mDossier.getId()).child("date").setValue(dateView.getText().toString());
                    myRef.child(mDossier.getId()).child("liste").setValue(listPersonneView.getText().toString());
                    myRef.child(mDossier.getId()).child("montant").setValue(montantView.getText().toString());
                }
            });
        }
        else {
            modifier.setEnabled(false);
        }


        return  view;
    }
}
