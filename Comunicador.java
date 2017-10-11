package com.tealive.jreyc.tealive;

/**
 * Created by jreyc on 21/08/2017.
 */

import com.tealive.jreyc.model.MiniEncuesta;

import java.util.ArrayList;

public class Comunicador {

    private static ArrayList<Cuestionario> objetos;
    private static ArrayList<MiniEncuesta> objetoMiniEncuesta;


    /**
     * Constructor es privado para que solo haya una instancia
     */
    public Comunicador(){
        if (objetos==null) {
            objetos = new ArrayList<Cuestionario>();
            objetoMiniEncuesta = new ArrayList<MiniEncuesta>();
        }
    }

    /**
     * Solicitamos una instancia
     *
     * @return Comunicador
     */
    public Comunicador getComunicador(){
        if (objetos==null)
            objetos=new ArrayList<Cuestionario>();
        return this;
    }


    public MiniEncuesta getMiniEncuesta(){
        return objetoMiniEncuesta.get(0);
    }

    public void setMiniEncuesta(MiniEncuesta mini){
        objetoMiniEncuesta.add(mini);
    }
    /**
     * Guarda en el array el Cuestionario para poder recuperarlo en otro momento
     * @param newObjeto  Cuestionario
     */
    public static void setObjeto(Cuestionario newObjeto){
        //Añade un objeto a compartir en la colección
        objetos.add(newObjeto);

    }

    /**
     * Pide un Cuestionario. Si no hay nada devuelve null
     *
     *
     * @return
     */
    public static Cuestionario getObjeto(){
        if (objetos==null) {
            objetos = new ArrayList<Cuestionario>();
        }

        return objetos.get(0);
    }

    /**
     * Elimina un cuestionario de los cuestionarios guardados.
     * Debe ser llamado despues de recuperar correctamente el Cuestionario
     *
     * @param indice
     */
    public static void eliminarObjeto(int indice){
        objetos.remove(indice);
    }


}
