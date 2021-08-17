package com.emelnorte.tics.calificadorturnero;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {
    private String URL="http://alphaweb.emelnorte.com/Turnero/WsTurnos";
    private String metodo="listarAllAgencias";
    private String name_espace="http://wsTurnos.dao.model.trn/";
    private String SOAP_ACTION= "";
    private SoapObject request=null;
    private String resAgencias;
    private Adaptador adaptador;
    private ListView lista;
    ArrayList<AgenciasClass> lstAgenciaCl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.img);
        actionBar.setTitle("");
        lista = findViewById(R.id.lstAgenciasPr);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Agencias.class);
                intent.putExtra("idAgencia",lstAgenciaCl.get(position).getId());
                intent.putExtra("descripcionAgencia",lstAgenciaCl.get(position).getDescripcion());
                startActivity(intent);
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
                            if(! (respuesta.toString().isEmpty()))
                            {
                                resAgencias = respuesta.toString();
                                consultarAgencias();
                            }else{
                                Toast.makeText(getApplicationContext(),"Sin respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch (Exception e)
                {
                   // Toast.makeText(getApplicationContext(),"Servidor no disponible.", Toast.LENGTH_SHORT).show();
                }

            }
        }.start();
    }

    @Override
    public void onBackPressed() {

    }

    public void  consultarAgencias()
    {
        lstAgenciaCl = new ArrayList<AgenciasClass>();
        String[] agen= resAgencias.split(";");

        for(int i =0; i<agen.length;i++)
        {
            Log.w("Error: "+agen[i],"Mensaje");
            String[] objAgen = agen[i].split(",");
            if( objAgen.length==2)
            {
                AgenciasClass agenciasClass = new AgenciasClass();
                agenciasClass.setId(Integer.parseInt(objAgen[0].toString()));
                agenciasClass.setDescripcion(objAgen[1]);
                lstAgenciaCl.add(agenciasClass);
            }
        }
        adaptador= new Adaptador(this, lstAgenciaCl);
        lista.setAdapter(adaptador);
    }
}
