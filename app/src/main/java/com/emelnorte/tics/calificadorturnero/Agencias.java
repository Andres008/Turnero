package com.emelnorte.tics.calificadorturnero;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Agencias extends AppCompatActivity {
    private TextView lblTituloAgen;
    private int codAgencia;
    private String URL="http://alphaweb.emelnorte.com/Turnero/WsTurnos";
    private String metodo="listarModulosAgencias";
    private String name_espace="http://wsTurnos.dao.model.trn/";
    private String SOAP_ACTION= "";
    private SoapObject request=null;
    private String resModulos;
    private AdaptadorModulos adaptador;
    private ListView lista;
    ArrayList<ModulosAgencias> lstModulosAgencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencias);
        lblTituloAgen = findViewById(R.id.lblTituloAgen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.img);
        actionBar.setTitle("");
        lista = findViewById(R.id.lstModulosAgen);
        Bundle b = getIntent().getExtras();
        if ( b!=null )
        {
            codAgencia=  b.getInt("idAgencia");
            lblTituloAgen.setText("Modulos "+b.get("descripcionAgencia"));
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Calificacion.class);
                intent.putExtra("idModuloAgen",lstModulosAgencia.get(position).getIdAgencia().toString());
                intent.putExtra("descripcionModAge",lstModulosAgencia.get(position).getDescricionAgencia());
                startActivity(intent);
            }
        });

        new Thread(){
            public void run(){
                request= new SoapObject(name_espace,metodo);
                Log.w("Codigo Agencia "+codAgencia,"Mensaje");
                request.addProperty("idAgencia",codAgencia);
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

                                resModulos = respuesta.toString();
                                consultarAgencias();
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

    public void consultarAgencias()
    {
        lstModulosAgencia = new ArrayList<ModulosAgencias>();
        String[] agen= resModulos.split(";");
        for(int i =0; i<agen.length;i++)
        {
            Log.w("Error: "+agen[i],"Mensaje");
            String[] objAgen = agen[i].split(",");
            if( objAgen.length==2)
            {
                ModulosAgencias modulosAgencias = new ModulosAgencias();
                modulosAgencias.setIdAgencia(new BigDecimal(objAgen[0].toString()));
                modulosAgencias.setDescricionAgencia(objAgen[1]);
                lstModulosAgencia.add(modulosAgencias);
            }
        }
        adaptador= new AdaptadorModulos(this, lstModulosAgencia);
        lista.setAdapter(adaptador);
    }
}
