package innotech.td3exo3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mon pc on 14/04/2017.
 */

public class PersonAdapter extends ArrayAdapter {
    private Context context;
    public PersonAdapter(Context context, ArrayList<Personne> depenses){

        super(context, 0, depenses);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Personne personne = (Personne) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item_info, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.nom);
        TextView prename = (TextView) convertView.findViewById(R.id.prenom);
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isTwopane()){
                    // printDetails(type);
                }else{
                    Intent intent = new Intent(context, InfoActivity.class);
                    context.startActivity(intent);
                }
            }

        });

        // Populate the data into the template view using the data object
        name.setText(personne.getName());
        prename.setText(personne.getPrename());
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.carcrash1);
        // Return the completed view to render on screen
        return convertView;
    }

    public boolean isTwopane(){
        View view = ((Activity) context).findViewById(R.id.item);
        return (view != null);

    }

    public void printDetails(Type Type){
    }
}
