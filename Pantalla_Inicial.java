package com.tealive.jreyc.tealive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tealive.jreyc.model.Niño;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Pantalla_Inicial extends AppCompatActivity {
    Button boton,bDemo;
    TextView texto;
    boolean sinDatos;

    private OperacionesTeaLiveDB datos;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ; //?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__inicial);
        boton=(Button) findViewById(R.id.button);
        texto=(TextView)findViewById(R.id.textView);
        bDemo=(Button) findViewById(R.id.button2);
        sinDatos=false;
        //Necesito solicitar los permisos al principio de la aplicación sino no funciona
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        //Lo anterior no sirve para nada

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            }
            else
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }
    }

    public void ventana(View view){
        Intent mo=new Intent(this,SeleccionNeno.class);
        if (sinDatos)
            mo.putExtra("valoresBD",true);
        else
            mo.putExtra("valoresBD",false);
        startActivity(mo);
    }
    public void prueba(View view){
        sinDatos=true;
        File fichero=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());

        String cadena=fichero.getAbsolutePath()+"/enviar.xml";

        File ficheros=new File(cadena);
        if (ficheros.exists())
        {
            //Tengo que meter los datos en la BD
            //Primero descifro el xml

            //Ahora creo la base de datos
            datos=OperacionesTeaLiveDB.obtenerInstancia(this);
            //Inserto los datos del niño
            Niño usarNiño=new Niño();
            List <Niño> listaNiños=usarNiño.leerXMLNiño(ficheros);
            Iterator <Niño> it=listaNiños.iterator();
           while  (it.hasNext()){
               usarNiño=it.next();
                datos.insertarNiño(usarNiño);
            }
            //Finalmente borro el fichero
        }
        else
        {
            Resources res=this.getResources();
            texto.setText(res.getText(R.string.no_xml_entrada));
        }
        ventana(view);
    }
}
