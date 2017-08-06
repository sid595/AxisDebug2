package com.acad_example.nilea.axisdebug2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Vector;

public class plotData extends AppCompatActivity {

    GraphView graph ;
    ArrayList target = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_data);
        Intent intent = getIntent();
        target = intent.getCharSequenceArrayListExtra(dataAnalysis.TARGET_DATA);
        graph = (GraphView) findViewById(R.id.graph);
        plot(target);
    }

    public void plot(ArrayList data){
        int tCount = data.size();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(int i = 0;i<tCount;i++){
            double dta = (double)data.get(i);
            series.appendData(new DataPoint((double)i,dta),true,tCount,false);
        }

        series.setTitle("Output");
        series.setColor(Color.BLACK);
        series.setThickness(5);

        graph.addSeries(series);


    }
}
