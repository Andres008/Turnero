package com.emelnorte.tics.calificadorturnero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import  android.widget.VideoView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.net.Uri;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Calificacion extends AppCompatActivity {
    private ModulosAgencias adaptadorModulos;
    private TextView lblTituloCal;
    private String URL="http://alphaweb.emelnorte.com/Turnero/WsTurnos";
    private String metodo="listarAllCalificaciones";
    private String metodoCalificar="calificarServicio";
    private String name_espace="http://wsTurnos.dao.model.trn/";
    private String SOAP_ACTION= "";
    private SoapObject request=null;
    private SoapObject requestCal=null;
    private String resCalificacion;
    private AdaptadorCalificacion adaptador;
    private ListView lista;
    private ArrayList<CalificacionClass> calificacionClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.img);
        actionBar.setTitle("");
        Bundle b = getIntent().getExtras();
        if ( b!=null )
        {
            adaptadorModulos = new ModulosAgencias();
            adaptadorModulos.setIdAgencia(new BigDecimal(b.getString("idModuloAgen")));
            adaptadorModulos.setDescricionAgencia(b.getString("descripcionModAge"));
        }
        lblTituloCal = findViewById(R.id.lblTituloCal);
        lblTituloCal.setText(adaptadorModulos.getDescricionAgencia());
        lista= findViewById(R.id.lstCalificacion);



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.w("Calificacion: "+calificacionClassList.get(position).getCodCalificacion(),"Mensaje");
                Log.w("ModuloAgencia: "+adaptadorModulos.getIdAgencia(),"Mensaje");

                new Thread(){
                    public void run(){
                        requestCal= new SoapObject(name_espace,metodoCalificar);
                        requestCal.addProperty("idModuloAgencia",adaptadorModulos.getIdAgencia().toString());
                        requestCal.addProperty("codigoCalificacion",calificacionClassList.get(position).getCodCalificacion());
                        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                        envelope1.setOutputSoapObject(requestCal);
                        try{
                            HttpTransportSE httpsTransportSE= new HttpTransportSE(URL);
                            httpsTransportSE.call(SOAP_ACTION, envelope1);
                            final SoapPrimitive respuestaCal=(SoapPrimitive) envelope1.getResponse();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.w("Respuesta "+respuestaCal.toString(),"Mensaje");
                                    if(respuestaCal.toString().equals("true"))
                                    {
                                       // popUp("Calificación correcta.");
                                        Toast.makeText(getApplicationContext(),"Calificación Correcta", Toast.LENGTH_SHORT).show();

                                    }else
                                    {
                                       //popUp("Calificacion Incorrecta");
                                        Toast.makeText(getApplicationContext(),"Calificación Incorrecta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }catch (Exception e)
                        {
                            Log.w("Catch error "+e.getMessage(),"Mensaje");
                        }
                    }
                }.start();

            }
        });


        new Thread(){
            public void run(){
                request= new SoapObject(name_espace,metodo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.setOutputSoapObject(request);

                try{
                    HttpTransportSE httpsTransportSE= new HttpTransportSE(URL);
                    httpsTransportSE.call(SOAP_ACTION, envelope);
                    final SoapPrimitive respuesta=(SoapPrimitive) envelope.getResponse();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("Respuesta "+respuesta,"Mensaje");
                            if(! (respuesta.toString().isEmpty()))
                            {

                                resCalificacion = respuesta.toString();
                                consultarCalificaciones();
                            }else{
                                Toast.makeText(getApplicationContext(),"Sin respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch (Exception e)
                {

                }

            }
        }.start();
    }

    @Override
    public void onBackPressed() {

    }

    public void popUp(String mensaje)
    {
        // Toast.makeText(getApplicationContext(),"Calificación Incorrecta, vuelva a intentarlo", Toast.LENGTH_LONG).show();
        Builder builder = new Builder(this);
        builder.setMessage(mensaje)
                .setTitle("Fue un place atenderlo")
                .setCancelable(false)
                .setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void consultarCalificaciones()
    {
        calificacionClassList = new ArrayList<CalificacionClass>();
        String[] agen= resCalificacion.split(";");
        for(int i =0; i<agen.length;i++)
        {
            Log.w("Error: "+agen[i],"Mensaje");
            String[] objAgen = agen[i].split(",");
            if( objAgen.length==2)
            {
                CalificacionClass modulosAgencias = new CalificacionClass();
                modulosAgencias.setCodCalificacion(objAgen[0].toString());
                modulosAgencias.setDescripcionCalificacion(objAgen[1]);
                calificacionClassList.add(modulosAgencias);
            }
        }
        adaptador= new AdaptadorCalificacion(this,calificacionClassList);
        lista.setAdapter(adaptador);
    }
}
