package com.tealive.jreyc.tealive;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by jreyc on 21/08/2017.
 *
 * @author Javier Rey Carbon
 * Clase Cuestionario tiene que ser serializable para poder enviarla por un Intent
 */

public class Cuestionario implements Serializable {
    private int i;
    private int finSnap;
    private ArrayList<Pregunta> cuestionario;
    private ArrayList<String> respuestaEA, respuestaSNAP;
    private ArrayList<String> textoPregunta; //¿Para qué?
    private Pregunta pregunta;
    private float valorEA,valorSNAP;

    private int numeroEncuesta;
    private String idNiño;
    private String fechaEncuesta;
    private String medicamento;
    private String dosis;
    private int peso;
    private int talla;
    private int fc;
    private int ta;
    private int ta2;

    //Getter and setters


    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getTalla() {
        return talla;
    }

    public void setTalla(int talla) {
        this.talla = talla;
    }

    public int getFc() {
        return fc;
    }

    public void setFc(int fc) {
        this.fc = fc;
    }

    public int getTa() {
        return ta;
    }

    public void setTa(int ta) {
        this.ta = ta;
    }

    public int getTa2() {
        return ta2;
    }

    public void setTa2(int ta2) {
        this.ta2 = ta2;
    }

    public float getValorEA() {
        return valorEA;
    }

    public void setValorEA(float valorEA) {
        this.valorEA = valorEA;
    }

    public float getValorSNAP() {
        return valorSNAP;
    }

    public void setValorSNAP(float valorSNAP) {
        this.valorSNAP = valorSNAP;
    }

    public int getNumeroEncuesta() {
        return numeroEncuesta;
    }

    public void setNumeroEncuesta(int numeroEncuesta) {
        this.numeroEncuesta = numeroEncuesta;
    }

    public String getIdNiño() {
        return idNiño;
    }

    public void setIdNiño(String idNiño) {
        this.idNiño = idNiño;
    }

    public String getFechaEncuesta() {
        return fechaEncuesta;
    }

    public void setFechaEncuesta(String fechaEncuesta) {
        this.fechaEncuesta = fechaEncuesta;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }




    //Constructores.
    public Cuestionario() { //Inicialización vacia FALTA PROBAR
        i = 0;
        textoPregunta = new ArrayList<String>();//?
        cuestionario = new ArrayList<Pregunta>();
        pregunta = new Pregunta("", 0);
        valorEA=(float) 0.0;
        valorSNAP=(float) 0.0;

    }

    /*
    Constructor de la clase Cuestionario.
    @param String[] snapIV : Las preguntas de snapIV
    @param String[] problemas : las preguntas de los efectos adversos
     */
    public Cuestionario(String[] snapIV, String[] problemas, String[] respSNAP, String[] respEA) {
        i = 0;
        textoPregunta = new ArrayList<String>();//?
        cuestionario = new ArrayList<Pregunta>();
        for (String preg : snapIV) {
            cuestionario.add(new Pregunta(preg, i));
            textoPregunta.add((String) preg);
            i++;
        }
        finSnap = i - 1;
        for (String preg : problemas) {
            textoPregunta.add((String) preg);
            cuestionario.add(new Pregunta(preg, i));
            i++;
        }

        //Añado los valores que tiene que mostrar las respuestas
        respuestaEA=new ArrayList<String>();
        respuestaSNAP=new ArrayList<String>();
        for (String resp : respSNAP) {
            respuestaSNAP.add(resp);
        }
        for (String resp : respEA) {
            respuestaEA.add(resp);
        }
        i = 0; //Posición actual
        pregunta = cuestionario.get(i);
        valorEA=(float)0.0;
        valorSNAP=(float)0.0;
    }

    public ArrayList<String> getTextoPregunta() {

        return textoPregunta;
    }

    /*
    Devuelve la pregunta actual del cuestionario
     */
    public String getPregunta() {

        return pregunta.getPregunta();
    }

    /*
    Añade el valor a la pregunta actual del cuestionario
    @param int valor : el valor de la pregunta actual del cuestionario
     */
    public void setPregunta(int valor) {

        if ((valor >= 0) && (valor < 4)) {
            pregunta.setValor(valor);//Añado el valor a la pregunta
            cuestionario.set(i, pregunta); //Modifico el valor de la pregunta
        }
    }

    /*
    Este metodo cambia la pregunta en la que estamos por la siguiente pregunta
     */
    public void siguientePregunta() {
        i++;
        if (i < cuestionario.size()) {
            pregunta = cuestionario.get(i);
        } else
            i--;
    }

    /*
    Esta función devuelve cierto si es la última pregunta
    @return boolean
     */

    public boolean ultima() {
        int j = i;
        boolean b;
        j++;
        if (j < cuestionario.size())
            b = false;
        else
            b = true;
        return b;
    }

    /*
    Este metodo cambia el número de la pregunta en la que estamos por la anterior.

    si es la primera se queda en la misma pregunta
     */
    public void anteriorPregunta() {
        if (i > 0) {
            i--;
            pregunta = cuestionario.get(i);
        }
    }

    /*
    Hace la media aritmetica de los valores del cuestionario
     */
    public float calcularEscala() {
        int suma = 0;
        int sumaSNAP=0;
        int sumaEA=0;
        int j = 0;
        int valor=0;
        for (Pregunta preg : cuestionario) {
            valor=preg.getValor();
            if (j<18)
                sumaSNAP=sumaSNAP+valor;
            else
                sumaEA=sumaEA+valor;
            suma = suma + valor;
            j++;
        }
        valorEA=(float) sumaEA /18;
        valorSNAP=(float) sumaSNAP /18;
        return (float) suma / j;

    }



    /**
     * Devuelve el valor que tiene la pregunta actual.
     *
     * Debe ser un valor entre [0,3]. Si tiene valor -1 significa que aún no se contesto
     * @return int
     */
    public int getValorPregunta(){
        return pregunta.getValor();
    }

    public int getValorPorPosicion(int posicion) {
        Pregunta aux = cuestionario.get(posicion);
        return aux.getValor();
    }

    /**
     * Este procedimiento devuelve un arrayList que contiene el texto de las 4 preguntas
     * El texto es el mismo para las preguntas de SNAP4
     * Las preguntas de EA tienen las mismas respuestas
     *
     * @return ArrayList Un ArrayList de 4 elementos que tiene el texto de las respuestas
     */
    public ArrayList<String> getTextoRespuestas() {
        if(i<18)
            return respuestaSNAP;
        else
            return  respuestaEA;
    }

    /**
     * Función sobreescrita para que devuelva las preguntas según un i dado
     * @param j
     * @return
     */
    public ArrayList<String> getTextoRespuesta(int j){
        if (j<18)
            return respuestaSNAP;
        else
            return respuestaEA;
    }

    public int getNumeroPregunta(){
        return i;
    }

    public void irAprimeraPregunta(){
        i=0;
        pregunta=cuestionario.get(i);
    }

    /**
     * Se mueve a la pregunta puesta por numeroPregunta.
     * @param numeroPregunta
     */
    public void irPregunta(int numeroPregunta){
        if (numeroPregunta<0)
                irAprimeraPregunta();
        else
            if(numeroPregunta>35)
                irPregunta(35);
            else
            {
                i=numeroPregunta;
                pregunta=cuestionario.get(i);
            }
    }

}
