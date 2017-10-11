package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityModificarPreguntas extends AppCompatActivity {
    private TextView textoPregunta,numeroPregunta;
    private Button btnCancelar,btnModificar;
    private RadioButton rb1,rb2,rb3,rb4,rb5;
    private int nPregunta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_preguntas);

        btnCancelar=(Button) findViewById(R.id.ampbtCancelar);
        btnModificar=(Button)findViewById(R.id.ampbtModificar);
        textoPregunta=(TextView)findViewById(R.id.ampTextoPregunta);
        numeroPregunta=(TextView)findViewById(R.id.ampNumeroPregunta);
        rb1=(RadioButton)findViewById(R.id.rbAmpCero);
        rb2=(RadioButton)findViewById(R.id.rbAmpUno);
        rb3=(RadioButton)findViewById(R.id.rbAmpDos);
        rb4=(RadioButton)findViewById(R.id.rbAmpTres);
        rb5=(RadioButton)findViewById(R.id.rbAmpCuatro);

        int i;
        i=getIntent().getExtras().getInt("numeroPregunta");
        nPregunta=i;
        numeroPregunta.setText(Integer.toString(i));
        int valor=getIntent().getExtras().getInt("respuesta");
        String pregunta=getIntent().getExtras().getString("textoPregunta");
        textoPregunta.setText(pregunta);

        //Cojo el cuestionario para saber el texto de las respuestas
        Cuestionario c=Comunicador.getObjeto();

        ArrayList<String> respuestas;
        respuestas=c.getTextoRespuesta(i);

        //Escribo el texto de las respuestas correctas
        rb1.setText(respuestas.get(0));
        rb2.setText(respuestas.get(1));
        rb3.setText(respuestas.get(2));
        rb4.setText(respuestas.get(3));

        //Limpiar radio button y poner la que estaba
        rb1.setChecked(false);
        rb2.setChecked(false);
        rb3.setChecked(false);
        rb4.setChecked(false);
        rb5.setChecked(false);
        switch (valor) {
            case 0:
                rb1.setChecked(true);
                break;
            case 1:
                rb2.setChecked(true);
                break;
            case 2:
                rb3.setChecked(true);
                break;
            case 3:
                rb4.setChecked(true);
                break;
            default:
                rb5.setChecked(true);
                break;
        }

    }

    public void modificar(View view){
        int valor=-1;
        if (rb1.isChecked())
            valor=0;
        if (rb2.isChecked())
            valor=1;
        if (rb3.isChecked())
            valor=2;
        if (rb4.isChecked())
            valor=3;
        if (rb5.isChecked())
            valor=-1;
        Intent i=getIntent();
        i.putExtra("numeroPregunta",nPregunta);//Hay que modificarlo esta mal
        i.putExtra("respuesta",valor);
        setResult(RESULT_OK,i);
        finish();
    }
    public void cancelar(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
