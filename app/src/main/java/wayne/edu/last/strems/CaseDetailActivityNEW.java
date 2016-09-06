package wayne.edu.last.strems;

/**
 * Created by Wuxiaopei on 9/2/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


import wayne.edu.last.strems.MyUtils.CommonUtils;
import wayne.edu.last.strems.adapter.HealthDataAdapter;
import wayne.edu.last.strems.models.Emergency;
import wayne.edu.last.strems.models.HealthData;

/**
 * Created by Wuxiaopei on 8/25/2016.
 */
public class CaseDetailActivityNEW extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CaseDetailActivityNEW";

    public static final String EXTRA_POST_KEY = "case_key";


    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;

    private ChildEventListener mDataListener;
    private ValueEventListener mEmergencytListener;
    private String mPostKey;
    public int index = 0;



    private HealthDataAdapter mHealthDataAdapter;
    private DatabaseReference mHealthDataReference;
    //private RecyclerView mHealthdataRecycler;

    public TextView ambulanceTypeView;
    public TextView emergencyTimeView;
    public TextView authorView;
    public TextView patient_First_View;
    public TextView patient_Last_View;


    public GraphView graph;//for tem
    public GraphView graph2;//for bloodpressue
    public LineGraphSeries<DataPoint> mSeries1;
    public LineGraphSeries<DataPoint> mSeries2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail_new);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }


        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("emergencies").child(mPostKey);


        mHealthDataReference = FirebaseDatabase.getInstance().getReference()
                .child("healthdata").child(mPostKey);


        authorView = (TextView) findViewById(R.id.case_author);
        emergencyTimeView = (TextView) findViewById(R.id.emergency_time);
        ambulanceTypeView = (TextView) findViewById(R.id.ambulancetype);
        patient_Last_View = (TextView) findViewById(R.id.patient_last_name);
        patient_First_View = (TextView) findViewById(R.id.patient_first_name);
        //  hospital_View  = (TextView) findViewById(R.id.case_hospital);


        graph = (GraphView) findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<DataPoint>();
        graph.addSeries(mSeries1);
       setGraphTitleandXY(mSeries1, graph, CommonUtils.BODAY_TEM, CommonUtils.BODAY_TEM_UNIT);

        graph2 = (GraphView) findViewById(R.id.graph2);
        mSeries2 = new LineGraphSeries<DataPoint>();
        graph2.addSeries(mSeries2);
       setGraphTitleandXY(mSeries2, graph2, CommonUtils.BLOOD_PRESSURE, CommonUtils.BLOOD_PRESSURE_UNIT);

        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(CaseDetailActivityNEW.this,
                           "Body Temprature="+ dataPoint.getY(),
                           Toast.LENGTH_SHORT).show();
            }
        });

        mSeries2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(CaseDetailActivityNEW.this,
                        "Blood Pressure="+ dataPoint.getY(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();

        ValueEventListener emergencyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // Post post = dataSnapshot.getValue(Post.class);
                Emergency em = dataSnapshot.getValue(Emergency.class);
                // [START_EXCLUDE]

                emergencyTimeView.setText(em.occur_time);
                authorView.setText(em.user);
                ambulanceTypeView.setText(em.ambulance_type);
                patient_First_View.setText(em.patient_First);
                patient_Last_View.setText(em.patient_Last);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(CaseDetailActivityNEW.this, "Failed to load the database data.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mPostReference.addValueEventListener(emergencyListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        // mPostListener = postListener;
        mPostListener = emergencyListener;

        ChildEventListener mHealthDataListener = new ChildEventListener() {


            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName){

                Log.d(TAG, "onChildAdded: in WXP Listener" + dataSnapshot.getKey());
                HealthData hd = dataSnapshot.getValue(HealthData.class);


               // DataPoint dp = new DataPoint(index, Double.valueOf((hd.temprature).trim()));
                DataPoint dp = new DataPoint(index, hd.temprature);
                //DataPoint dp = new DataPoint(hd.sample_time, hd.temprature);
                DataPoint dp2 = new DataPoint(index, hd.blood_pressure);
                System.out.println("&&&&&&&&&&&&&&&&&*****************Sample time =" + hd.sample_time);

                mSeries1.appendData(dp, true, 40);
                mSeries2.appendData(dp2, true, 40);
                index++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String previousChildName){
                Log.d(TAG, "onChildChanged: in WXP Listener" + dataSnapshot.getKey());

            }

            public void onChildMoved(DataSnapshot snapshot, String previousChildName){
                Log.d(TAG, "onChildMoved: in WXP Listener");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: in WXP Listener");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: in WXP Listener");


            }


        };
        mHealthDataReference.addChildEventListener(mHealthDataListener);
        mDataListener = mHealthDataListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
        if (mDataListener != null) {
            mHealthDataReference.removeEventListener(mDataListener);
        }
        // Clean up comments listener
       // mHealthDataAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        /*if (i == R.id.button_post_comment) {
            postComment();
        }*/
    }

    private void setGraphTitleandXY(LineGraphSeries series,
                                    GraphView graph, String title, String y) {

        graph.setTitle(title);
        graph.getGridLabelRenderer().setVerticalAxisTitle(y);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        // optional styles
       // graph.setTitleTextSize(40);
      //  graph.setTitleColor(Color.GREEN);
        //graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
       // graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
       // graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);


      //  series.setColor(Color.GREEN);
       // series.setDrawDataPoints(true);
      //  series.setDataPointsRadius(8);
      //  series.setThickness(6);


    }
}