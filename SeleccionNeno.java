package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tealive.jreyc.model.ItemClickListener;
import com.tealive.jreyc.model.Niño;
import com.tealive.jreyc.model.RVModelNiño;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.util.ArrayList;
import java.util.List;

public class SeleccionNeno extends AppCompatActivity implements ItemClickListener {
    @Override
    public void onItemClick(View view, int position) {
        //realmente debo ir a un niño en concreto
        irANiño(view);

    }
    boolean datosBD;
    List<Niño> listaNiños;
    RecyclerView rv;
    RVModelNiño adaptadorNiño;
    String idNiño;

    TextView numeroNiños;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_neno);
        datosBD=getIntent().getExtras().getBoolean("valoresBD");
        numeroNiños =(TextView)findViewById(R.id.seleccionNenoNumero);

        rv=(RecyclerView)findViewById(R.id.rvNeno);

        LinearLayoutManager llm=new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        if (datosBD)
        {
            //tengo que sacar la lista de la BD
            OperacionesTeaLiveDB bd=OperacionesTeaLiveDB.obtenerInstancia(this);
            int n=bd.numeroNiños();
            if (n>0)
            {
                //Existe algún niño
                cargaCursorEnLista(bd.todosNiños());
            }
        }
        else {
            cargarDatosNiño();


        }
        //adaptadorNiño = new RVModelNiño(listaNiños, this);

        adaptadorNiño=new RVModelNiño(listaNiños, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Niño rapaz=listaNiños.get(position);
                idNiño=rapaz.getId();
                irANiño(view);

               //System.out.println(rapaz.getId());
            }
        });

        rv.setAdapter(adaptadorNiño);

    }

    public void irANiño(View view){

        Intent pantalla=new Intent(this,NenosActivity.class);
        pantalla.putExtra("valoresBD",datosBD);
        pantalla.putExtra("idNiño",idNiño);
        startActivity(pantalla);
    }
    public void cargarDatosNiño(){
        //Carga la lista con los datos de la BD
        listaNiños=new ArrayList<Niño>();

        Niño rapaz=new Niño("TDH-001","Javier Gonzalez Sobrado","papa@gmail.com","10-10-2010");
        listaNiños.add(rapaz);

        rapaz=new Niño("TDH-002","Rocio Duran Perez","mama@hotmail.es","31-12-2012");
        listaNiños.add(rapaz);


    }

    private void cargaCursorEnLista(Cursor datosBD){
        listaNiños=new ArrayList<Niño>();
        Niño rapaz;
        if (datosBD!=null) {
            int max = datosBD.getCount();
            int i = 0;
            while (datosBD.moveToNext()) //La primera vez está antes de lo que tenemos que leer
            {
                rapaz = new Niño();
                String s=datosBD.getString(0);
                rapaz.setId(s);//ID
                rapaz.setNombre(datosBD.getString(1));
                rapaz.setFechaNacimiento(datosBD.getString(2));
                rapaz.setMail(datosBD.getString(3));
                listaNiños.add(rapaz);
                //datosBD.moveToNext();
                i++;
            }
        }
    }
}
