package wayne.edu.last.strems.safetyPad;

import java.util.Date;

import wayne.edu.last.strems.models.Emergency;

/**
 * Created by Wuxiaopei on 8/25/2016.
 */
public class SafetyPadRecord {

    public static Emergency getCurrentRecord() {
        Emergency em = new Emergency();

        return em;
    }

    public static Emergency getCurrentRecord(Date emergency_time) {
        Emergency em = new Emergency();

        return em;
    }

    public static Emergency getCurrentRecord(String patient_first_name, String patient_last_name) {
        Emergency em = new Emergency();

        return em;
    }

    public static Emergency getCurrentRecord(String ambulance) {
        Emergency em = new Emergency();

        return em;
    }
}
