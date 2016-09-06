package wayne.edu.last.strems;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wayne.edu.last.strems.MyUtils.CommonUtils;
import wayne.edu.last.strems.models.Emergency;
import wayne.edu.last.strems.models.HealthData;
import wayne.edu.last.strems.models.User;
import wayne.edu.last.strems.safetyPad.SafetyPadRecord;


public class NewCaseActivity extends BaseActivity {

    private static final String TAG = "NewCaseActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    /*private EditText mTitleField;
    private EditText mBodyField;*/

   /* private EditText mAmbulancetype;
    private EditText mPatient;
    private EditText mTempreture;
    private EditText mEkg;*/

    private EditText   mAmbulance_type;
    private EditText   mStation_number;
    private EditText   mEmergency_type;
    private EditText   mOccur_time;
    private EditText    mPatient_First;
    private EditText    mPatient_Last;
    private  EditText   mPatient_Dob;
    private EditText    mPatient_ID;
    private EditText    mHospital_address;
    private EditText   mResidual_mins;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_case);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mAmbulance_type = (EditText) findViewById(R.id.ambulance_type);
        mPatient_First  = (EditText) findViewById(R.id.patient_First);
        mPatient_Last  = (EditText) findViewById(R.id.patient_Last);
        mResidual_mins  = (EditText) findViewById(R.id.residual_mins);

        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
       /* final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();*/

        //now get data from input
        final String   ambulance_type = mAmbulance_type.getText().toString();
        //final String   station_number =  mStation_number.getText().toString();
     //   final String   emergency_type = mEmergency_type.getText().toString();
        final String   patient_First = mPatient_First.getText().toString();
        final String   patient_Last = mPatient_Last.getText().toString();
      //  final String   patient_Dob =  mPatient_Dob.getText().toString();
      //  final String   patient_ID =  mPatient_ID.getText().toString();
      //  final String   hospital_address =  mHospital_address.getText().toString();
        final String   residual_mins =  mResidual_mins.getText().toString();


        //will get these data from safetyPad
        //Emergency newcase = SafetyPadRecord.getCurrentRecord();
        // Ambulance type is required
        if (TextUtils.isEmpty(ambulance_type)) {
            mAmbulance_type.setError(REQUIRED);
            return;
        }
        // Patient name is required
        if (TextUtils.isEmpty(patient_First)) {
            mPatient_First.setError(REQUIRED);
            return;
        }
        // Tmeprature is required
        if (TextUtils.isEmpty(patient_Last)) {
            mPatient_Last.setError(REQUIRED);
            return;
        }

        // [START single_value_read]
        final String userId = getUid();


        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewCaseActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            //writeNewPost(userId, user.username, title, body);
                            writeNewEmergency(userId, user.username, ambulance_type, patient_First,patient_Last,residual_mins.trim());
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        // [END single_value_read]
    }


    private void writeNewEmergency(String userId,
                                   String username,
                                   String ambulance_type,
                                   String patient_First,
                                   String patient_Last,
                                   String residual_mins) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        Date dNow = new Date( );
        //SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");


        String key = mDatabase.child("emergencies").push().getKey();


        int int_residual_mins= Integer.parseInt(residual_mins);
        Emergency em = new Emergency(userId,
                                    username,
                                    ambulance_type,
                                    patient_First,
                                    patient_Last,
                                    dNow.toString(),
                                     residual_mins);

        Map<String, Object> emergencyValues = em.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/emergencies/" + key, emergencyValues);
        childUpdates.put("/user-emergencies/" + userId + "/" + key, emergencyValues);
        mDatabase.updateChildren(childUpdates);




        DatabaseReference mHealthDataDatabase= FirebaseDatabase.getInstance().getReference()
                .child("healthdata").child(key);
        //get health data from sensor

        new Thread(
                new GenerateData(mHealthDataDatabase)
                 ).start();

       // handler.post(new GenerateData(mHealthDataDatabase));


        /*int i = 0;

        while(i<3) {
            HealthData hd=null;

            hd = new HealthData(new Date().toString(),
                                CommonUtils.GeneRandomNum(95,105),
                               CommonUtils.GeneRandomNum(100,125));

            if(hd!=null) {

                mHealthDataDatabase.push().setValue(hd);

                try{
                CommonUtils.SleepAwhile(4000);
                }catch(Exception e){
                    Log.e(TAG, "Sleep Thread Error!!!");
                }


            }else{
                System.out.println("Error*************hr is null ***********");
            }
            i++;
        }*/


        //let a thread to generate data every 30 s to simulate the sensor data


    }

private class GenerateData  implements Runnable{

    public DatabaseReference  drf;


    public GenerateData(DatabaseReference d){
        this.drf = d;
    }

    public GenerateData( ){

    }

    public void run(){

        for (int i=0; i<20; i++){

           HealthData hd=null;

            hd = new HealthData(new Date(),
                     CommonUtils.GeneRandomNum(95,105),
                     CommonUtils.GeneRandomNum(100,125));

            if(hd!=null) {

                this.drf.push().setValue(hd);
                System.out.println("Geneate a new data....="+ i);

                try{
                    //sleep 5 seconds
                    CommonUtils.SleepAwhile(3000);
                }catch(Exception e){
                    Log.e(TAG, "Sleep Thread Error!!!");
                }

            }else{
                System.out.println("Error*************hr is null ***********");
            }

        }

    }
}

}
