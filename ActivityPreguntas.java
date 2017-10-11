package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.util.ArrayList;

public class ActivityPreguntas extends AppCompatActivity {
    private TextView textoPregunta,tvNumeroPregunta;
    private RadioButton rb0,rb1,rb2,rb3,rb4;
    private Cuestionario cuestionario,cuestionarioAnterior;
    private int nPregunta;
    private String[] preguntaEA,preguntaSNAP,respuestaEA,respuestaSNAP;
    private Button btnSiguiente,btnAnterior;
    private Comunicador comunicador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        textoPregunta=(TextView)findViewById(R.id.textoPregunta);
        tvNumeroPregunta=(TextView)findViewById(R.id.apNumeroPregunta);

        rb0=(RadioButton)findViewById(R.id.rbCero);
        rb1=(RadioButton)findViewById(R.id.rbUno);
        rb2=(RadioButton)findViewById(R.id.rbDos);
        rb3=(RadioButton)findViewById(R.id.rbTres);
        rb4=(RadioButton)findViewById(R.id.rbCuatro);
        btnSiguiente=(Button)findViewById(R.id.aebtSiguiente);
        btnAnterior=(Button)findViewById(R.id.aebtAnterior);

        nPregunta=0;
        cuestionarioAnterior=null;
        cuestionario=Comunicador.getObjeto();
        int nEncuestaAnterior=cuestionario.getNumeroEncuesta();
        if (nEncuestaAnterior>0){
            nEncuestaAnterior--; //Este es realmente el número de encuesta anterior
            //Busco en la base de datos la encuesta anterior
            OperacionesTeaLiveDB teaLiveDB=OperacionesTeaLiveDB.obtenerInstancia(this);
            cuestionarioAnterior=teaLiveDB.muestraCuestionario(cuestionario.getIdNiño(),nEncuestaAnterior);

        }



        mostrarPregunta();
    }

    public void mostrarPregunta(){

        textoPregunta.setText(cuestionario.getPregunta());
        int i=cuestionario.getNumeroPregunta(); //Es el numero de pregunta en el que estoy
        tvNumeroPregunta.setText(String.valueOf(i));

        if (i==0 || i==18 || i==17)
        {
            ArrayList<String> respuestas;
            respuestas=cuestionario.getTextoRespuestas();

            rb0.setText(respuestas.get(0));
            rb1.setText(respuestas.get(1));
            rb2.setText(respuestas.get(2));
            rb3.setText(respuestas.get(3));
        }

        rb4.setChecked(true);
        rb0.setChecked(false);
        rb1.setChecked(false);
        rb2.setChecked(false);
        rb3.setChecked(false);
        rb4.setChecked(false);


        switch (cuestionario.getValorPorPosicion(i)){
            case 0:
                rb0.setChecked(true);
                break;
            case 1:
                rb1.setChecked(true);
                break;
            case 2:
                rb2.setChecked(true);
                break;
            case 3:
                rb3.setChecked(true);
                break;
            default:

                if (cuestionarioAnterior!=null)
                {
                    //Existen valores anteriores pero no existe valor actual
                    switch (cuestionarioAnterior.getValorPorPosicion(i)){
                        case 0:
                            rb0.setChecked(true);
                            break;
                        case 1:
                            rb1.setChecked(true);
                            break;
                        case 2:
                            rb2.setChecked(true);
                            break;
                        case 3:
                            rb3.setChecked(true);
                            break;
                        case 4:
                            rb4.setChecked(true);
                            break;
                    }
                }
                else {
                    rb4.setChecked(true);
                    break;
                }
        }

    }

    public void siguientePregunta(View view){
        boolean salir=false;
      /* if (cuestionario.ultima())
            salir=true;
        */
        if (cuestionario.getNumeroPregunta()==5)
            salir=true;
             //*/
        int valor=-1;
        if (rb0.isChecked())
            valor=0;
        if (rb1.isChecked())
            valor=1;
        if (rb2.isChecked())
            valor=2;
        if (rb3.isChecked())
            valor=3;

        if (valor>=0)
        {
            //Necesito guardar el valor
            cuestionario.setPregunta(valor);
            cuestionario.siguientePregunta();
            mostrarPregunta();
        }
        if (salir)
        {
            //Guardar en la base de datos y llamar
            comunicador.eliminarObjeto(0);
            comunicador.setObjeto(cuestionario);
            Intent intent=new Intent(this,ActivitySeleccionarModificarEncuesta.class);
            startActivity(intent);
        }

    }

    public void anteriorPregunta(View view){
        if (cuestionario.getNumeroPregunta()>0)
            cuestionario.anteriorPregunta();
        int valor=cuestionario.getValorPregunta();

        if (valor>=0)
        {
            //Necesito guardar el valor
            cuestionario.setPregunta(valor);

        }

        mostrarPregunta();
    }
}
