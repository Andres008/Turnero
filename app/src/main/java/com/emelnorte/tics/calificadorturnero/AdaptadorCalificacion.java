package com.emelnorte.tics.calificadorturnero;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCalificacion extends BaseAdapter {

    private List<CalificacionClass> calificacionClassList;
    private Context context;
    LayoutInflater inflater;
    private TextView lblCalificaionAdap;

    public AdaptadorCalificacion(Context pContext, ArrayList<CalificacionClass> pCalificacionClassList){
        context=pContext;
        calificacionClassList = pCalificacionClassList;

    }



    @Override
    public int getCount() {
        return calificacionClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return calificacionClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return new Long(calificacionClassList.get(position).getCodCalificacion());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.adaptador_calificacion,null);
        }
        //lblIdAdap =(TextView) convertView.findViewById(R.id.lblIdAdap);
        lblCalificaionAdap = (TextView) convertView.findViewById(R.id.lblCalificaionAdap);
        //lblIdAdap.setText(lstAgenciasClass.get(position).getId()+"");
        Log.w("ANTES DE ERROR","Mensaje");
        Log.w("llega:::::::::"+calificacionClassList.get(position).getDescripcionCalificacion(),"Mensaje");
        lblCalificaionAdap.setText(calificacionClassList.get(position).getDescripcionCalificacion());
        return  convertView;
    }
    public void setData(ArrayList<CalificacionClass> calificacionClassList){
        this.calificacionClassList=calificacionClassList;
        notifyDataSetChanged();
    }
}
