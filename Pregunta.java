package com.tealive.jreyc.tealive;

/**
 * Created by jreyc on 21/08/2017.
 *
 * @author Javier Rey Carbón
 */

public class Pregunta {
    private String pregunta;
    private int valor;

    /**
     * Devuelve el número de pregunta actual
     *
     * @return int  Devuelve el número de la pregunta
     */
    public int getNumeroPregunta() {
        return numeroPregunta;
    }

    private int numeroPregunta;

    /*
        Función que devuelve el valor que tengo guardado en pregunta
        @return String pregunta : El texto de la pregunta guardada
     */
    public String getPregunta()
    {
        return pregunta;
    }

    /*
    Función que devuelve el valor que tiene la pregunta. entre [0,3]

    @return int valor
     */
    public int getValor(){
        return valor;
    }

    /*
        Función que añade un valor a la pregunta. Solo puede tener un valor y tiene que estar entre 0 y 4

        Si no es un valor valido guarda -1
        @param int n : valor entre [0,3]
     */
    public void setValor(int n){
        if ((n>=0) & (n<4))
            valor=n;
        else
            valor=-1;


    }

    /*
        Constructor de la clase
        Es necesario guardar el valor d ela pregunta y su valor
        @param String texto : texto de la pregunta a guardar. Es la que mostraremos
        @param int numero : numero de la pregunta que realizamos. La guardamos para saber la proxima u la anterior
     */
    public Pregunta(String texto, int numero){
        if (numero>=0) {
            pregunta = texto;
            valor = -1; //Significa que no tiene valor
            numeroPregunta = numero;
        }
    }
}
