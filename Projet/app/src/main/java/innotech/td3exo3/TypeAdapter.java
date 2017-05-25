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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TypeAdapter extends ArrayAdapter {
    private Context context;
    public TypeAdapter(Context context, ArrayList<Type> depenses){

        super(context, 0, depenses);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Type type = (Type) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView typename = (TextView) convertView.findViewById(R.id.nom);
        TextView typedesc = (TextView) convertView.findViewById(R.id.description);
        ImageView icone = (ImageView) convertView.findViewById(R.id.icon);
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isTwopane()){
                   // printDetails(type);
                }else{
                    Intent intent = new Intent(context, InfoActivity.class);
                    intent.putExtra("type", type.getType());
                    context.startActivity(intent);
                }
            }

        });

        // Populate the data into the template view using the data object
        typename.setText(type.getType());
        typedesc.setText(type.getDescription());
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.carcrash1);
        // Return the completed view to render on screen
        return convertView;
    }

    public boolean isTwopane(){
        View view = ((Activity) context).findViewById(R.id.item);
        return (view != null);

    }

    public void printDetails(Type Type){
        //ImageView imageView = (ImageView)((Activity) context).findViewById(R.id.imageView2);
     /*   TextView textView = (TextView) ((Activity) context).findViewById(R.id.textView);
        TextView textView2 = (TextView) ((Activity) context).findViewById(R.id.textView2);
        textView.setText(Type.getNom());
        textView2.setText(Type.getFonction());
      */
        //imageView.setImageResource(Type.getImage());
    }
}
