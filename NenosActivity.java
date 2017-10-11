package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tealive.jreyc.model.MiniEncuesta;
import com.tealive.jreyc.model.RVModelEncuesta;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;
import com.tealive.jreyc.sql.TeaLiveContract.TEncuesta;

import java.util.ArrayList;
import java.util.List;

public class NenosActivity extends AppCompatActivity {
    boolean valoresBD;
    String idNiño;

    RecyclerView rv;
    List<MiniEncuesta> lista;
    RVModelEncuesta adaptadorEncuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nenos);
        valoresBD=getIntent().getExtras().getBoolean("valoresBD");
        idNiño=getIntent().getExtras().getString("idNiño");

        rv=(RecyclerView)findViewById(R.id.rvEncuestas);
       // rv.setHasFixedSize(true);//?

        LinearLayoutManager llm=new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empiezaEncuesta(view);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });

        if (valoresBD)
            cargarDatosNiñoBD();
        else
            cargarDatosNiño();
        adaptadorEncuesta=new RVModelEncuesta(lista);
        rv.setAdapter(adaptadorEncuesta);
    }

    public void cargarDatosNiño(){
        //Carga la lista con los datos de la BD
        lista=new ArrayList<MiniEncuesta>();
        MiniEncuesta mini=new MiniEncuesta();
        mini.setIdNiño("TDH-00");
        mini.setPeso("20");
        mini.setTalla("120");
        mini.setTa1("100");
        mini.setTa2("85");
        mini.setFc("95");
        mini.setValorEncuesta("1,9");
        lista.add(mini);
        mini=new MiniEncuesta("TDH-00","1","MEDIKINET","5","105","90","21","120","102","1,2");
        lista.add(mini);
        mini=new MiniEncuesta("TDH-00","2","MEDIKINET","10","100","90","20","120","110","1,7");
        lista.add(mini);
        mini=new MiniEncuesta("TDH-00","3","MEDIKINET","15","120","85","21","121","96","1,1");
        lista.add(mini);
        mini=new MiniEncuesta("TDH-00","4","MEDIKINET","20","120","85","25","121","102","1,7");
        lista.add(mini);
    }

    private void cargarDatosNiñoBD(){
        OperacionesTeaLiveDB bd=OperacionesTeaLiveDB.obtenerInstancia(this);
        int i=bd.numeroEncuestas(idNiño);
        Cursor item=bd.encuestasNiño(idNiño);
        lista = new ArrayList<MiniEncuesta>();
        MiniEncuesta mini=new MiniEncuesta();
        while (item.moveToNext()){
            String s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.ENCUESTA))) ;
            mini.setNumero(s);
            s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.FC)));
            mini.setFc(s);
            s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.PESO)));
            mini.setPeso(s);
            s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.TALLA)));
            mini.setTalla(s);
            s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.TA)));
            mini.setTa1(s);
            s=String.valueOf(item.getInt(item.getColumnIndex(TEncuesta.TAdiastolica)));
            mini.setTa2(s);
            s=item.getString(item.getColumnIndex(TEncuesta.MEDICAMENTO));
            mini.setNombre(s);
            s=item.getString(item.getColumnIndex(TEncuesta.DOSIS));
            mini.setDosis(s);
            float valor=(float)item.getFloat(item.getColumnIndex(TEncuesta.VALORESNAP));
            s=String.format("%.2f",valor);
            mini.setValorEncuesta(s);
            s=item.getString(item.getColumnIndex(TEncuesta.ID_NIÑO));
            mini.setIdNiño(s);
            valor=(float)item.getFloat(item.getColumnIndex(TEncuesta.VALOREA));
            s=String.format("%.2f",valor);
            mini.setValorEA(s);
            lista.add(mini);
            mini=new MiniEncuesta();
        }

    }

    public void empiezaEncuesta(View view){
        //Primero tengo que crear un objeto encuesta nuevo, pero sabiendo el número de encuesta que toca
        int numeroEncuesta=lista.size();

        Intent pantalla=new Intent(this,EmpiezaEncuesta.class);
        //Ahora paso el numero de encuesta a la próxima activity
        pantalla.putExtra("numeroProximaEncuesta",numeroEncuesta);
        pantalla.putExtra("idNeno",idNiño);
        if (numeroEncuesta>0) {
            Comunicador com = new Comunicador();
            com.setMiniEncuesta(lista.get(lista.size() - 1));
        }
        startActivity(pantalla);
    }
}
