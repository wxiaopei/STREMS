package wayne.edu.last.strems.sensor;

import wayne.edu.last.strems.models.HealthData;

/**
 * Created by Wuxiaopei on 8/26/2016.
 */
public class Equivital {

    private boolean   isConnected;
    private int       smaplingRate;


     public boolean blueToothPair(){

        return true;
    }

     public HealthData readData(){

         HealthData  hd=new HealthData() ;
       if(this.isConnected){
         //read data from sensors
       }else{

       }
        return hd;
    }



}
