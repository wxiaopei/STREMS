package wayne.edu.last.strems.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuxiaopei on 8/24/2016.
 */
public class Emergency {

    public String uid;
    public String user;


    public String ambulance_type;
    public String    station_number;//1 to 24 for detroit area
    public String emergency_type;
    public String  occur_time;

    public String patient_First;
    public String patient_Last;
    public String   patient_Dob;
    public String patient_ID; //driver license


    public String   hospital_address;
    public String      residual_mins;
    public String  status; //false: one the way, true: arrive at hospital


    // public int starCount = 0;
    //public Map<String, Boolean> stars = new HashMap<>();

    public Emergency() {

    }

    public Emergency(String uid,
                     String user,
                     String ambulance_type,
                     String    station_number,
                     String emergency_type,
                     String  occur_time,
                     String patient_First,
                     String patient_Last,
                     String   patient_Dob,
                     String patient_ID,
                     String   hospital_address,
                     String residual_mins,
                     String  status) {
        this.uid = uid;
        this.user = user;
        this.ambulance_type = ambulance_type;
        this.station_number=station_number;
        this.emergency_type=emergency_type;
        this.occur_time=occur_time;

        this.patient_First=patient_First;
        this.patient_Last=patient_Last;

        this.patient_Dob=patient_Dob;
        this.patient_ID=patient_ID;

        this.hospital_address=hospital_address;
        this.residual_mins=residual_mins;
        this.status=status;
    }


    public Emergency(String uid,
                     String user,
                     String ambulance_type,
                     String patient_First,
                     String patient_Last,
                     String occur_time,
                     String residual_mins) {
        this.uid = uid;
        this.user = user;
        this.ambulance_type = ambulance_type;
        this.station_number="Branch 1";
        this.emergency_type="Car Crash";
        this.occur_time=occur_time;

        this.patient_First=patient_First;
        this.patient_Last=patient_Last;

        /*SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String dateInString = "7-Jun-1983";
        Date dob= new Date() ;
        try {
           dob = formatter.parse(dateInString);
        }catch (ParseException e) {
            e.printStackTrace();
        }*/
        this.patient_Dob= "7-Jun-1983";
        this.patient_ID= "Driver License:";
        this.hospital_address="hospital address:";
        this.residual_mins=residual_mins;
        this.status= "On the Way";//on the way to er
    }

    //public Map<String, Object> toMap() {
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("user", user);

        result.put("ambulance_type", ambulance_type);
        result.put("station_number", station_number);
        result.put("emergency_type", emergency_type);
        result.put("occur_time", occur_time);


        result.put("patient_First", patient_First);
        result.put("patient_Last", patient_Last);
        result.put("patient_Dob", patient_Dob);
        result.put("patient_ID", patient_ID);


        result.put("hospital_address", hospital_address);
        result.put("residual_mins", residual_mins);
        result.put("status",status );
        //result.put("occur_time",occur_time );
        //result.put("starCount", starCount);
        // result.put("stars", stars);
        return result;
    }





}
