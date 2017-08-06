package com.acad_example.nilea.axisdebug2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Math;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

public class dataAnalysis extends AppCompatActivity {

    ArrayList fileNames;
    Vector accData = new Vector();
    ArrayList target = new ArrayList();
    ArrayList tempStore = new ArrayList();

    TextView msgDisp1,msgDisp2,info_det;
    int tCount,dCount,dVal,n;
    String count,temp;
    public static final String TARGET_DATA = "com.acad_example.nilea.axisdebug2.dataAnalysis";
    double tempVal;
    FileInputStream fIS;
    File f;
    EditText dataSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        Intent intent = getIntent(); count = String.valueOf(tCount);
        fileNames = intent.getCharSequenceArrayListExtra(mainDisp.FILE_NAMES);
        tCount = fileNames.size();
        msgDisp1 = (TextView) findViewById(R.id.disp1);
        msgDisp2 = (TextView) findViewById(R.id.disp2);
        info_det = (TextView) findViewById(R.id.msg12);
        dataSelect = (EditText) findViewById(R.id.dataSet);
        dataSelect.setText("1");
       // msgDisp1.setText(fileNames.get(1).toString());
       // msgDisp2.setText(fileNames.get(2).toString());
        msgDisp1.setText("Intent receive success");
        msgDisp2.setText("Select record no.");

        for(int k = 0; k < tCount;k++ ){
            info_det.append(fileNames.get(k).toString()+ "\n");
        }
        //accData.add(new Vector());
        //accData.add(new Vector());
    }

    public void readData(View view){
        accData.removeAllElements();
        info_det.setText("");
        for(int i = 0; i<tCount;i++){
            try{
                f = new File(fileNames.get(i).toString());
                fIS = new FileInputStream(f);
            }
            catch(IOException ioe){
                ioe.printStackTrace();
                msgDisp2.setText("Cannot open: " + fileNames.get(i).toString());
            }
            if(fIS != null){
                info_det.append("Reading File: " + fileNames.get(i).toString() + "\n");
                accData.add(new Vector());
                int r = 0;
                try {
                    r = fIS.read();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
                while(r != -1){
                    try {
                        r = fIS.read();
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }

                    temp = "";
                    while((char)r != ' '){

                        temp+= (char)r;
                        try {
                            r = fIS.read();
                        } catch(IOException ioe){
                            ioe.printStackTrace();
                        }
                    }
                    tempVal = Double.parseDouble(temp);
                    ((Vector) accData.get(i)).add(tempVal);
                    try {
                        r = fIS.read();
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                    temp = "";
                    while((char)r != ' '){
                        temp+= (char)r;
                        try {
                            r = fIS.read();
                        } catch(IOException ioe){
                            ioe.printStackTrace();
                        }
                    }
                    tempVal = Double.parseDouble(temp);
                    ((Vector) accData.get(i)).add(tempVal);
                    try {
                        r = fIS.read();
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                    temp = "";
                    while((char)r != '\n'){
                        temp+= (char)r;
                        try {
                            r = fIS.read();
                        } catch(IOException ioe){
                            ioe.printStackTrace();
                        }
                    }
                    tempVal = Double.parseDouble(temp);
                    ((Vector) accData.get(i)).add(tempVal);
                    try {
                        r = fIS.read();
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }
            try {
                fIS.close();
                info_det.append("File read successful: " + fileNames.get(i).toString() + "\n");

            }catch (IOException ioe){
                ioe.printStackTrace();
                info_det.append("File read failed: " + fileNames.get(i).toString() + "\n");

            }

        }


    }

    public void meanFilt(View view){
        target.clear();
        String s = dataSelect.getText().toString();
        try {
            dCount = Integer.parseInt(s);
            msgDisp1.setText("Using the Data set number: " + s);
            dCount--;
            dVal = ((Vector) accData.get(dCount)).size();
            dVal /= 3;
            double i, j, k, m;
            i = j = k = 0;
            for (int l = 0; l < dVal; l++) {
                n = l * 3;
                i = (double) ((Vector) accData.get(dCount)).get(n);
                j = (double) ((Vector) accData.get(dCount)).get(n + 1);
                k = (double) ((Vector) accData.get(dCount)).get(n + 2);

                m = i * i + j * j + k * k;
                m = Math.sqrt(m);
                target.add(m);
            }
            m = 0;
            for (int l = 0; l < dVal; l++) {
                m += (double)target.get(l);
            }
            m/=dVal ;
            for (int l = 0; l < dVal; l++) {
             i = (double)target.get(l);
             target.add(l,(i-m));
            }
        }
        catch(IllegalArgumentException ie){
            ie.printStackTrace();
            msgDisp1.setText("Please enter valid number!!!");
        }
    }

    public void plotStuff(View view){
        Intent intent = new Intent(this, plotData.class);
        intent.putExtra(TARGET_DATA, target);
        startActivity(intent);
    }

    public ArrayList conv(ArrayList x, ArrayList h){
        int sizeX = x.size(); int sizeY = h.size();
        int sizeZ = sizeX + sizeY - 1;
        double k;
        ArrayList y = new ArrayList();
        y.clear();
        for(int i = 0; i < sizeZ;i++){
            k = 0;
            for(int j= 0; j<sizeX;j++){
                if((i-j)>=0 & (i-j)<sizeY){
                    k+= ((double)x.get(j) * (double)h.get(i-j)) ;
                }
            }
            y.add(k);
        }
        return y;
    }

    public void sGolayFilt(View V){
        tempStore.clear();
        //sGolay filter for 21 coeff and trying to fit data in 4 order curve
        double sGolayCoeff[] = {0.708187464709204, 0.364765669113495,0.134387351778656,-0.00639939770374554,
                        -0.0786749482401658,
                        -0.101148127235084,
                        -0.0901562205910032,
                        -0.0596649727084510,
                        -0.0212685864859778,
                        0.0158102766798419,
                        0.0447204968944100,
                        0.0609824957651045,
                        0.0624882364012799,
                        0.0495012234142669,
                        0.0246565029173726,
                        -0.00703933747412010,
                        -0.0382081686429512,
                        -0.0591003199698851,
                        -0.0575945793337098,
                        -0.0191981931112366,
                0.0729531338226989};
        /*
        //Fitting sGolay coeff with 31 coeff into 3rd order cubic polynomial
        double sGolayCoeff[] = {0.409069346213559,0.315163015352769,0.233655338968432,0.163791616353286,0.104817146800069,0.0559772296015181,
                                0.0165171640503709,-0.0143177505606348,-0.0372822149387614,-0.0531309297912713,-0.0626185958254269,-0.0664999137484906,
                                -0.0655295842677247,-0.0604623080903916,-0.0520527859237537,-0.0410557184750734,-0.0282258064516129,-0.0143177505606348,-8.62515094014414,
                                0.0137139899948249,0.0263282732447818,0.0370018975332068,0.0449801621528376,0.0495083663964119,0.0498318095566672,0.0451957909263412,0.0348456097981715,0.0180265654648957,-0.00601604278074864,-0.0380369156460237,-0.0787907538381921,
                          }

        */
        for(int i =0;i<sGolayCoeff.length;i++){
            tempStore.add(sGolayCoeff[i]);
        }
        msgDisp2.setText("Starting Convolution....");
        target = conv(target,tempStore);
        msgDisp2.setText("Convolution completed successfully");

    }

}
