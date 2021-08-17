package com.emelnorte.tics.calificadorturnero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    private ArrayList<AgenciasClass> lstAgenciasClass;
    private Context context;
    LayoutInflater inflater;
    //private TextView lblIdAdap;
    private TextView lblDescripAdap;

    public Adaptador(Context pContext, ArrayList<AgenciasClass> pAgenciasClass)
    {
        context= pContext;
        lstAgenciasClass= pAgenciasClass;
    }


    @Override
    public int getCount() {
        return lstAgenciasClass.size();
    }

    @Override
    public Object getItem(int position) {
        return lstAgenciasClass.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstAgenciasClass.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null)
        {
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.adaptador,null);
        }
        //lblIdAdap =(TextView) convertView.findViewById(R.id.lblIdAdap);
        lblDescripAdap =(TextView) convertView.findViewById(R.id.lblDescripAdap);
        //lblIdAdap.setText(lstAgenciasClass.get(position).getId()+"");
        lblDescripAdap.setText(lstAgenciasClass.get(position).getDescripcion());
        return  convertView;
    }

    public void setData(ArrayList<AgenciasClass> persona){
        this.lstAgenciasClass=persona;
        notifyDataSetChanged();
    }


}
