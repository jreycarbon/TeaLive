package com.tealive.jreyc.tealive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.tealive.jreyc.sql.OperacionesTeaLiveDB;

import java.util.Arrays;

public class ActivityGrafica extends AppCompatActivity {
    private XYPlot plot;
    private String idNiño;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        idNiño=getIntent().getExtras().getString("idNiño");

        plot=(XYPlot) findViewById(R.id.agGrafico);

        llenarValores(true);

    }
    private void llenarValores(boolean bBD){
        Number[] serieEA;
        Number[] serieSNAP;
        if (bBD)
        {
            //Hago operaciones con Base de datos
            OperacionesTeaLiveDB db=OperacionesTeaLiveDB.obtenerInstancia(this);
            serieEA=db.getSerieEA(idNiño);
            serieSNAP=db.getSerieSNAP(idNiño);
        }
        else {
            //Relleno valores a mano
            serieEA = new Number[]{0.2, 0.4, 0.4, 0.3, 0.2};
            serieSNAP =new Number[] {0.5, 0.6, 0.4, 0.4, 0.2};
        }

        XYSeries s1=new SimpleXYSeries(Arrays.asList(serieEA),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Efectos Adversos");
        XYSeries s2=new SimpleXYSeries(Arrays.asList(serieSNAP),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"SNAP IV");

        plot.addSeries(s1,new LineAndPointFormatter(Color.BLUE,Color.BLUE,null,null));
        plot.addSeries(s2,new LineAndPointFormatter(Color.GREEN,Color.GREEN,null,null));

        plot.setDomainLabel("GRAFICA EVOLUCION");

    }
}
