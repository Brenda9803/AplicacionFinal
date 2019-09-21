package com.example.bgom.aplicacionfinal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.Tienda;

//va a transformar esta clase en una vista
public class MyAdapterStore extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Tienda>lst;

    public MyAdapterStore(Activity activity, ArrayList<Tienda> lst) {
        this.activity = activity;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v=view;
        //llenar la interfaz grafica
        if(v==null){
            LayoutInflater layoutInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=layoutInflater.inflate(R.layout.item,null);
        }
        Tienda tienda=lst.get(position);
        TextView lblNombre,lblDescripcion;
        lblNombre=v.findViewById(R.id.lblNameItem);
        lblDescripcion=v.findViewById(R.id.lblDescripcionItem);
        lblNombre.setText(tienda.getNombre());
        lblDescripcion.setText(tienda.getDescripcion());

        return v;
    }
}
//referencias https://miguelangellv.wordpress.com/2012/05/05/listview-y-arrayadapter-en-android/