package com.example.android.tabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.tabs.Utilidades.Utilidades;
import com.example.android.tabs.entidades.HeartRate;

import java.util.ArrayList;

public class historicos extends AppCompatActivity {
    String textTO;
    String textFROM;

    ListView listViewPersonas;
    ArrayList<String> listaInformacion;
    ArrayList<HeartRate> listaHeartRate;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos);

        conn=new ConexionSQLiteHelper(getApplicationContext(),"heartRate",null,1);

        listViewPersonas= (ListView) findViewById(R.id.listViewPersonas);


        consultarListaPersonas();

        ArrayAdapter adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaInformacion);
        adaptador.notifyDataSetChanged();
        listViewPersonas.setAdapter(adaptador);

        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion="id: "+listaHeartRate.get(pos).getId()+"\n";
                informacion+="Fecha: "+listaHeartRate.get(pos).getFecha()+"\n";
                informacion+="Trama: "+listaHeartRate.get(pos).getTrama()+"\n";

                Toast.makeText(getApplicationContext(),informacion,Toast.LENGTH_SHORT).show();

                HeartRate user=listaHeartRate.get(pos);


            }
        });

    }

    private void consultarListaPersonas() {

        textTO = getIntent().getExtras().getString("To");
        textFROM = getIntent().getExtras().getString("From");
        SQLiteDatabase db=conn.getReadableDatabase();

        HeartRate heartRate=null;
        listaHeartRate=new ArrayList<HeartRate>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_HEARTRATE+" WHERE "+Utilidades.CAMPO_FECHA+" >= Datetime('"+textFROM+"') AND "+Utilidades.CAMPO_FECHA+"<= Datetime('"+textTO+"')",null);


        while (cursor.moveToNext()){
            heartRate=new HeartRate();
            heartRate.setId(cursor.getInt(0));
            heartRate.setFecha(cursor.getString(1));
            heartRate.setTrama(cursor.getString(2));

            listaHeartRate.add(heartRate);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaHeartRate.size();i++){
            listaInformacion.add(listaHeartRate.get(i).getId()+" - "
                    +listaHeartRate.get(i).getFecha()+" - "
                    +listaHeartRate.get(i).getTrama());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
