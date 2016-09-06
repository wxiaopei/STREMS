package wayne.edu.last.strems.MyUtils;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;

import java.util.Random;

/**
 * Created by Wuxiaopei on 8/30/2016.
 */
public class CommonUtils {

    //Often Use Constants

    public  static final String  BODAY_TEM = "Body Temprature";
    public  static final String  BODAY_TEM_UNIT = "F";
    public  static final String  BLOOD_PRESSURE = "Blood Pressure";
    public  static final String  BLOOD_PRESSURE_UNIT= "Times/Min";
    public  static final String  HEART_RATE= "Heart Rate";
    public  static final String  HEART_RATE_UNIT= "Times/Min";


    public CommonUtils (){

    }

    public static CommonUtils getInstance(){
        return new  CommonUtils ();
    }

    static public void SleepAwhile (int seconds)throws InterruptedException{
        Thread.sleep(seconds);
    }

    static public int GeneRandomNum(int start, int end){
        Random random = new Random();
        return geneRandomInteger(start, end, random);
    }


     public void setGraphTitleandXY( GraphView graph){

        graph.setTitle("Body Temprature");
        graph.getGridLabelRenderer().setVerticalAxisTitle("F");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        // optional styles
        //graph.setTitleTextSize(40);
        //graph.setTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);

    }


    private static int geneRandomInteger(int aStart, int aEnd, Random aRandom){
        int randomNumber =0;
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
         randomNumber =  (int)(fraction + aStart);

        return randomNumber;
    }




}
