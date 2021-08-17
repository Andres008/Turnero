package com.emelnorte.tics.calificadorturnero;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorModulos extends BaseAdapter {
    private List<ModulosAgencias> lstModulosAgencias;
    private Context context;
    LayoutInflater inflater;
    //private TextView lblIdAdap;
    private TextView lblDescripModulos;


    public AdaptadorModulos(Context pContext, ArrayList<ModulosAgencias> pModulosAgencia)
    {
        context= pContext;
        lstModulosAgencias= pModulosAgencia;
    }

    @Override
    public int getCount() {
        return lstModulosAgencias.size();
    }

    @Override
    public Object getItem(int position) {
        return lstModulosAgencias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstModulosAgencias.get(position).getIdAgencia().longValue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.adaptador_modulos,null);
        }
        //lblIdAdap =(TextView) convertView.findViewById(R.id.lblIdAdap);
        lblDescripModulos = (TextView) convertView.findViewById(R.id.lblAgenModuAdap);
        //lblIdAdap.setText(lstAgenciasClass.get(position).getId()+"");
        Log.w("ANTES DE ERROR","Mensaje");
        Log.w("llega:::::::::"+lstModulosAgencias.get(position).getDescricionAgencia(),"Mensaje");
        lblDescripModulos.setText(lstModulosAgencias.get(position).getDescricionAgencia());
        return  convertView;
    }

    public void setData(ArrayList<ModulosAgencias> modulosAgencias){
        this.lstModulosAgencias=modulosAgencias;
        notifyDataSetChanged();
    }
}
