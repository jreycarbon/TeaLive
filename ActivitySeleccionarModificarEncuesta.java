package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tealive.jreyc.model.AdapterMostrarPreguntaContestada;
import com.tealive.jreyc.model.Medicamento;
import com.tealive.jreyc.model.MostrarPreguntaContestada;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.util.ArrayList;
import java.util.List;

public class ActivitySeleccionarModificarEncuesta extends AppCompatActivity {

    private List<MostrarPreguntaContestada> listaPreguntas;
    private Cuestionario cuestionario;
    private Spinner spnPreguntas,spnMedicina,spnDosis;
    private EditText fc,talla,peso,ta1,ta2;
    private TextView numeroEncuesta,fechaEncuesta;
    int request_code=1;
    private boolean inicio=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_modificar_encuesta);

        fc=(EditText)findViewById(R.id.asmeFrecuenciaCardiaca);
        talla=(EditText)findViewById(R.id.asmeTalla);
        peso=(EditText)findViewById(R.id.asmePeso);
        ta1=(EditText)findViewById(R.id.asmeTASistolica);
        ta2=(EditText)findViewById(R.id.asmeTADiastolica);

        numeroEncuesta=(TextView)findViewById(R.id.asmeNumeroEncuesta);
        fechaEncuesta=(TextView)findViewById(R.id.asmeFechaEncuesta);

        spnMedicina=(Spinner)findViewById(R.id.asmeMedicina);
        spnDosis=(Spinner)findViewById(R.id.asmeDosis);
        spnPreguntas=(Spinner)findViewById(R.id.spMostrarPreguntaResultado);

        iniciarValores();

        spnPreguntas.setAdapter(new AdapterMostrarPreguntaContestada(this,listaPreguntas));
        spnPreguntas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Aquí es donde tengo que ir a otro sitio
                if (!inicio) {
                    modificarUnaPregunta(view, position);
                }
                else
                    inicio=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void iniciarPreguntas(){
        cuestionario=Comunicador.getObjeto();
        listaPreguntas=new ArrayList<MostrarPreguntaContestada>();
        MostrarPreguntaContestada pregContestada;
        cuestionario.irAprimeraPregunta();
        //Cargo en una lista todas las preguntas contestadas. Tienen dos campos La pregunta y el valor
        boolean continuar=true;
        while (continuar){
            String texto=cuestionario.getPregunta();
            int i=cuestionario.getValorPregunta();
            pregContestada=new MostrarPreguntaContestada(texto,i);
            listaPreguntas.add(pregContestada);
            if (cuestionario.ultima())
                continuar=false;
            else
                cuestionario.siguientePregunta();
        }
        //Tengo la lista inicializada con todos los valores
        inicio=true;
    }
    private void iniciarValores(){
        iniciarPreguntas();
        numeroEncuesta.setText(String.valueOf(cuestionario.getNumeroEncuesta()));
        fechaEncuesta.setText(cuestionario.getFechaEncuesta());
        fc.setText(String.valueOf(cuestionario.getFc()));
        talla.setText(String.valueOf(cuestionario.getTalla()));
        peso.setText(String.valueOf(cuestionario.getPeso()));
        ta1.setText(String.valueOf(cuestionario.getTa()));
        ta2.setText(String.valueOf(cuestionario.getTa2()));

        Resources res=getResources();
        String [] medicamentos=res.getStringArray(R.array.medicamentos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, medicamentos);
        spnMedicina.setAdapter(adapter);
        Medicamento usaMedicamento=new Medicamento(medicamentos);
        int linea=usaMedicamento.posicionMedicamento(cuestionario.getMedicamento());
        if (linea>=0)
        {
            spnMedicina.setSelection(linea);
            String [] valoresDosis=usaMedicamento.crearDosis(cuestionario.getMedicamento(),getResources());
            linea=usaMedicamento.posicionDosis(cuestionario.getDosis());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresDosis);
            spnDosis.setAdapter(adapter);
            if (linea>=0)
                spnDosis.setSelection(linea);
        }


    }

    public void modificar(View view){
        //Primero calcula los valores de snap y ea
        float escalaGlobal=(float) cuestionario.calcularEscala();
        OperacionesTeaLiveDB bd=OperacionesTeaLiveDB.obtenerInstancia(this);
        if (!bd.existeEncuesta(cuestionario.getIdNiño(),cuestionario.getNumeroEncuesta())) {
            bd.crearEncuesta(cuestionario);
            //Despues de guardar la encuesta en la base de datos veo la gráfica
            Intent intent=new Intent(this,ActivityGrafica.class);
            intent.putExtra("idNiño",cuestionario.getIdNiño());
            startActivity(intent);
            finish();
        }

    }
    /**
     * Llama a un activity para que le devuelva un valor, realmente la pregunta es la número i
     *
     * @param view
     * @param i
     */
    private void modificarUnaPregunta(View view,int i){
        //Realmente debo modificar una pregunta
        Intent intent=new Intent(this,ActivityModificarPreguntas.class);
        MostrarPreguntaContestada preguntaContestada=listaPreguntas.get(i);

        //Paso el texto de la pregunta
        intent.putExtra("textoPregunta",preguntaContestada.getPregunta());
        //Paso el valor de la resupuesta
        intent.putExtra("respuesta",preguntaContestada.getRespuesta());
        //Paso el número de la pregunta
        intent.putExtra("numeroPregunta",i);

        //Llamo al activty esperando una respuesta
        startActivityForResult(intent,request_code);
    }

    /**
     * Este metodo es el que recoge la respuesta de la activity modificar pregunta y puyede tener varios valores
     *
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if ((requestCode==request_code) && (resultCode==RESULT_OK)){
            // Se ha modificado algo o como mínimo a dado a guardar la modificación
            int j=data.getIntExtra("numeroPregunta",0);
            int i = data.getExtras().getInt("numeroPregunta");
            int valor=data.getExtras().getInt("respuesta");
            cuestionario.irPregunta(j);
            cuestionario.setPregunta(valor);
            iniciarPreguntas();
            spnPreguntas.setAdapter(new AdapterMostrarPreguntaContestada(this,listaPreguntas));
        }
    }
}
