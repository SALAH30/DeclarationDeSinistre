package com.mobile.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ArrayList list = new ArrayList<Offre>();

        String[] cles = {"mot1","mot3"};
        list.add(new Offre(1,"15/02/2017","France",150000,cles));
        list.add(new Offre(2,"15/02/2017","France",150000,cles));
        list.add(new Offre(3,"15/02/2017","France",150000,cles));


        OffresAdapter offresAdapter = new OffresAdapter(this, list);
        ListView listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(offresAdapter) ;
    }
}
