package innotech.td3exo3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Type> data = new ArrayList<Type>();
        Type type1 = new Type("seul", "dérapage");
        data.add(type1);
        Type type2 = new Type("Avec autre véhicule", "face à face");
        data.add(type2);
        Type type3 = new Type("Stationnement", "Stationnement");
        data.add(type3);

        ListView listView = (ListView) findViewById(R.id.listV);
        listView.setAdapter(new TypeAdapter(this, data));

    }

}