package wayne.edu.last.strems.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuxiaopei on 8/25/2016.
 */
public class HealthData {

   // public String    sample_time;
    public Date    sample_time;

    //public String    temprature;
    public  double    temprature;
    public  double     blood_pressure;
    public  String     breath_rate;
    public  String     heart_rate;
    public String     ekg_lead_1;
    public String    ekg_lead_2;

    public HealthData (){

    }

    //public HealthData (String st, int tem, String bp){
    public HealthData (Date st, double tem, double bp){


        this.sample_time = st;
        this.temprature = tem;
        this.blood_pressure = bp;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("sample_time", this.sample_time);
        result.put("temprature", this.temprature);
        result.put("blood_pressure", this.blood_pressure);


        return result;
    }


}
