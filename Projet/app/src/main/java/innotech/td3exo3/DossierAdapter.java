package innotech.td3exo3;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mon pc on 25/05/2017.
 */

public class DossierAdapter extends ArrayAdapter {

    ArrayList<Dossier> content = new ArrayList<Dossier>();
    private Context context;
    private AdapterInterface buttonListner;

    public DossierAdapter(Context context, ArrayList<Dossier> resource,AdapterInterface listner) {
        super(context, 0, resource);
        buttonListner = listner;
        this.context = context;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Get the data item for this position
        final Dossier dossier = (Dossier) getItem(i);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_dossier, viewGroup, false);
        }
        // Lookup view for data population
        TextView titre = (TextView) view.findViewById(R.id.titre);
        TextView sousTitre = (TextView) view.findViewById(R.id.soustitre);
        TextView sousTitre2 = (TextView) view.findViewById(R.id.soustitre2);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    buttonListner.buttonPressed(dossier);
            }

        });

        // Populate the data into the template view using the data object
        titre.setText(dossier.getDate());
        sousTitre.setText(dossier.getLieu());
        sousTitre2.setText(dossier.getMontant()+" DA");
        // Return the completed view to render on screen
        return view;
    }

    public interface AdapterInterface{
        public void buttonPressed(Dossier dossier);
    }
}
