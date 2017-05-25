package com.mobile.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mon pc on 23/04/2017.
 */

public class OffresAdapter extends BaseAdapter {
    private List<Offre> list;
    Context context;

    public OffresAdapter(Context context, List<Offre> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return  this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = parent.inflate(context, R.layout.fragment_offre_item,null);
        final Offre offre= (Offre) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_offre_item, parent, false);
        }

        TextView numero = (TextView) convertView.findViewById(R.id.numero);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView fournisseur = (TextView) convertView.findViewById(R.id.fournisseur);
        TextView montant = (TextView) convertView.findViewById(R.id.montant);
        TextView cles = (TextView) convertView.findViewById(R.id.motscle);

        numero.setText(offre.getNumero()+"");
        date.setText(offre.getDate());
        fournisseur.setText(offre.getFournisseur());
        montant.setText(offre.getMontant()+"");
        cles.setText(offre.getCles().toString());
        //checkBox

        return convertView;
    }
}
