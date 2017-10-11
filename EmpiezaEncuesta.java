package com.tealive.jreyc.tealive;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tealive.jreyc.model.Medicamento;
import com.tealive.jreyc.model.MiniEncuesta;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EmpiezaEncuesta extends AppCompatActivity {
    private EditText TA,TA2,frecuenciaCardiaca,talla,peso;
    private Spinner medicamento,dosis;
    private String[] preguntaEA,preguntaSNAP,respuestaEA,respuestaSNAP,medicamentos,arrayDosis;
    private Cuestionario cuestionario;
    private  Comunicador comunicador;
    private Button continuar,cancelar;
    private ArrayList<String> arrayMedicamentos;
    private TextView tvNumeroEncuesta,fechaEncuesta;
    private int proximaEncuesta;
    private Medicamento usaMedicamento;
    private String anteriorMedicamento,idNiño;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empieza_encuesta);
        Resources res=getResources();
        proximaEncuesta=getIntent().getExtras().getInt("numeroProximaEncuesta");
        idNiño=getIntent().getExtras().getString("idNeno");
        comunicador=new Comunicador();

        anteriorMedicamento=""; //Se usa para no cargar dosis en caso de que el anterior medicamento sea el mismo

        preguntaEA=res.getStringArray(R.array.efectosAdversos);
        preguntaSNAP=res.getStringArray(R.array.snap4);
        respuestaEA=res.getStringArray(R.array.respuestaEfectosAdversos);
        respuestaSNAP=res.getStringArray(R.array.respuestaSNAP);
        medicamentos=res.getStringArray(R.array.medicamentos);

        usaMedicamento=new Medicamento(medicamentos);
        //Base de datos llena las preguntas en la bd solo si no existen
        OperacionesTeaLiveDB bd=OperacionesTeaLiveDB.obtenerInstancia(this);
        if (!bd.existenPreguntas())
            bd.llenarPeguntas(preguntaSNAP,preguntaEA,respuestaSNAP,respuestaEA);

        //Inicializo cuestionario
        cuestionario=new Cuestionario(preguntaSNAP,preguntaEA,respuestaSNAP,respuestaEA);
        cuestionario.setNumeroEncuesta(proximaEncuesta); //Añade el número de la próxima encuesta
        cuestionario.setIdNiño(idNiño);
        //Calculo fecha actual para cuestionario
        Calendar c= Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        String fechaFormateada=sdf.format(c.getTime());
        cuestionario.setFechaEncuesta(fechaFormateada);


        List<String> listaDinamica=new ArrayList<String>();
        arrayMedicamentos=new ArrayList<String>();
        for (String med:medicamentos)
        {
            arrayMedicamentos.add(med);
        }
        listaDinamica=arrayMedicamentos;
        ArrayAdapter<String> prueba=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaDinamica);


        medicamento=(Spinner)findViewById(R.id.spMedicamento);

        medicamento.setAdapter(prueba);
        medicamento.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                      //  cargarDosis();
                        if (parent.getId()==R.id.spMedicamento){
                            cargarDosis();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        dosis=(Spinner)findViewById(R.id.spDosis);
        dosis.setEnabled(false);
        dosis.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId()==R.id.spDosis) {
                            String dato = dosis.getSelectedItem().toString();
                            cuestionario.setDosis(arrayDosis[position]);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        if (proximaEncuesta==0){
            //Quiere decir que no puede tener medicamento
            medicamento.setEnabled(false); //No permite modificar
            cuestionario.setDosis("0");
            cuestionario.setMedicamento(arrayMedicamentos.get(0));
        }
        continuar=(Button)findViewById(R.id.btContinuar);

        tvNumeroEncuesta=(TextView)findViewById(R.id.aeeNumeroEncuesta);

        tvNumeroEncuesta.setText(String.valueOf(proximaEncuesta));

        fechaEncuesta=(TextView)findViewById(R.id.aeeFechaEncuesta);
        fechaEncuesta.setText(cuestionario.getFechaEncuesta());
        iniciarEditText();
        if (proximaEncuesta>0)
            iniciarValores();
    }

    private void iniciarValores(){
        MiniEncuesta mini=comunicador.getMiniEncuesta();
        peso.setText(mini.getPeso());
        talla.setText(mini.getTalla());
        frecuenciaCardiaca.setText(mini.getFc());
        TA.setText(mini.getTa1());
        TA2.setText(mini.getTa2());
        fechaEncuesta.setText(cuestionario.getFechaEncuesta());
        if (Integer.parseInt(mini.getNumero())>0) {
            int linea = usaMedicamento.posicionMedicamento(mini.getNombre());
            if (linea > 0) {
                anteriorMedicamento = mini.getNombre();
                cuestionario.setMedicamento(anteriorMedicamento);
                medicamento.setSelection(linea);
                String[] valoresDosis = usaMedicamento.crearDosis(mini.getNombre(), getResources());
                linea = usaMedicamento.posicionDosis(mini.getDosis());
                if (linea > 0) {
                    cuestionario.setDosis(mini.getDosis());
                    dosis.setEnabled(true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresDosis);
                    dosis.setAdapter(adapter);
                    dosis.setSelection(linea);
                }
            }
        }

    }
    private void iniciarEditText(){

        TA=(EditText)findViewById(R.id.etTASistolica);

        TA2=(EditText)findViewById(R.id.etTAdiastolica);

        frecuenciaCardiaca=(EditText)findViewById(R.id.aeeFrecunciaCardiaca);

        talla=(EditText)findViewById(R.id.aeeTalla);

        peso=(EditText)findViewById(R.id.aeePeso);

    }

    public void preguntarSNPAyEA(View view){

        cuestionario.setPeso(Integer.parseInt(peso.getText().toString()));
        cuestionario.setFc(Integer.parseInt(frecuenciaCardiaca.getText().toString()));
        cuestionario.setTa(Integer.parseInt(TA.getText().toString()));
        cuestionario.setTalla(Integer.parseInt(talla.getText().toString()));
        cuestionario.setTa2(Integer.parseInt(TA2.getText().toString()));
        comunicador.setObjeto(cuestionario);

        Intent preguntasEncuesta=new Intent(this,ActivityPreguntas.class);

        startActivity(preguntasEncuesta);
    }

    public void cargarDosis(){
        int posicion=medicamento.getSelectedItemPosition();
        List<String> listaDosis=new ArrayList<String>();
        Resources res=getResources();
        String [] valoresDosis = new String[0];
        boolean correcto=true;
        cuestionario.setMedicamento(arrayMedicamentos.get(posicion));

            switch (arrayMedicamentos.get(posicion)) {
                case "RUBIFEN":
                    valoresDosis = res.getStringArray(R.array.dosis_rubifen);
                    break;
                case "MEDICEBRAN":
                    valoresDosis = res.getStringArray(R.array.dosis_rubifen);
                    break;
                case "CONCERTA":
                    valoresDosis = res.getStringArray(R.array.dosis_concerta);
                    break;
                case "MEDIKINET":
                    valoresDosis = res.getStringArray(R.array.dosis_medikinet);
                    break;
                case "EQUASYM":
                    valoresDosis = res.getStringArray(R.array.dosis_equasym);
                    break;
                case "METILFENIDATO":
                    valoresDosis = res.getStringArray(R.array.dosis_concerta);
                    break;
                default:
                    correcto = false;
            }
            if (correcto) {
                dosis.setEnabled(true);
                arrayDosis = valoresDosis;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresDosis);
                dosis.setAdapter(adapter);
            }

        if (anteriorMedicamento.equals(arrayMedicamentos.get(posicion))){
            if (usaMedicamento!=null) {
                String dosisBuscar=cuestionario.getDosis();
                dosis.setSelection(usaMedicamento.posicionDosis(dosisBuscar));
            }
        }

        else
            anteriorMedicamento=cuestionario.getMedicamento();

    }
}
