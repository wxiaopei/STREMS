package wayne.edu.last.strems;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import wayne.edu.last.strems.MyUtils.CommonUtils;
import wayne.edu.last.strems.adapter.HealthDataAdapter;
import wayne.edu.last.strems.models.Emergency;
import wayne.edu.last.strems.models.HealthData;

/**
 * Created by Wuxiaopei on 8/25/2016.
 */
public class CaseDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CaseDetailActivity";

    public static final String EXTRA_POST_KEY = "case_key";

    public DataPoint[] temp_datapoint_array;
    public DataPoint[] bp_datapoint_array;
    public List<DataPoint> datapoint_list = new ArrayList<DataPoint>();
    public List<DataPoint> datapoint_list2 = new ArrayList<DataPoint>();
    private ValueEventListener mHealthDataListener;


    private DatabaseReference mPostReference;

    private ValueEventListener mPostListener;
    //private ValueEventListener mDataListener;
    private ChildEventListener mDataListener;
    private ValueEventListener mEmergencytListener;
    private String mPostKey;

    private HealthDataAdapter mHealthDataAdapter;
    private DatabaseReference mHealthDataReference;
    private RecyclerView mHealthdataRecycler;

    public TextView ambulanceTypeView;
    public TextView emergencyTimeView;
    public TextView authorView;
    public TextView patient_First_View;
    public TextView patient_Last_View;


    public GraphView graph;
    public GraphView graph2;
    public LineGraphSeries<DataPoint> mSeries1;
    public LineGraphSeries<DataPoint> mSeries2;
    //   public TextView hospital_View;

    public TextView sampleTimeView;
    public TextView tempView;

    public int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

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


        //rececyle viewer
        sampleTimeView = (TextView) findViewById(R.id.sampling_time);
        tempView = (TextView) findViewById(R.id.sensor_temp);


        mHealthdataRecycler = (RecyclerView) findViewById(R.id.recycler_data);
        mHealthdataRecycler.setLayoutManager(new LinearLayoutManager(this));

        graph = (GraphView) findViewById(R.id.graph);
        // addStaticGraph(graph);
        mSeries1 = new LineGraphSeries<DataPoint>();
        graph.addSeries(mSeries1);

        graph2 = (GraphView) findViewById(R.id.graph2);
        // addStaticGraph(graph2);

        mSeries2 = new LineGraphSeries<DataPoint>();
        graph2.addSeries(mSeries2);

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
                Toast.makeText(CaseDetailActivity.this, "Failed to load the database data.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mPostReference.addValueEventListener(emergencyListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        // mPostListener = postListener;
        mPostListener = emergencyListener;

        // Listen for new data

        mHealthDataAdapter = new HealthDataAdapter(this, mHealthDataReference);
        mHealthdataRecycler.setAdapter(mHealthDataAdapter);


        /*ValueEventListener mHealthDataListener = new ValueEventListener() {


            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot child : snapshot.getChildren()) {

                    HealthData hd = child.getValue(HealthData.class);

                    DataPoint dp = new DataPoint(i,
                            Double.valueOf(
                                    (hd.temprature).trim())
                    );

                    DataPoint dp2 = new DataPoint(i,
                            Double.valueOf(
                                    (hd.blood_pressure).trim())
                    );
                    // temp_datapoint[i++] = dp;
                    datapoint_list.add(dp);
                    datapoint_list2.add(dp2);
                    //add more data to the datapoint array.
                    i++;
                }


                temp_datapoint_array = datapoint_list.toArray(
                        new DataPoint[datapoint_list.size()]
                );

                bp_datapoint_array = datapoint_list2.toArray(
                        new DataPoint[datapoint_list2.size()]
                );

                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(temp_datapoint_array);
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(bp_datapoint_array);

                graph.addSeries(series);
                setGraphTitleandXY(series, graph, "Body Temprature", "F");

                graph2.addSeries(series2);
                setGraphTitleandXY(series2, graph2, "Blood Pressure", "BPM");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadHealthdata:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(CaseDetailActivity.this, "Failed to load the database data.",
                        Toast.LENGTH_SHORT).show();

            }

        };*/

        ChildEventListener mHealthDataListener = new ChildEventListener() {


            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName){

                Log.d(TAG, "onChildAdded&&888777: in WXP Listener" + dataSnapshot.getKey());
                HealthData hd = dataSnapshot.getValue(HealthData.class);
                DataPoint dp = new DataPoint(index++,
                        Double.valueOf(
                                (hd.temprature))
                );

                mSeries1.appendData(dp, true, 40);
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

        //mHealthDataReference.addValueEventListener(mHealthDataListener);
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
        mHealthDataAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        /*if (i == R.id.button_post_comment) {
            postComment();
        }*/
    }

    private void addStaticGraph(GraphView graph) {
        //****************************add graph********************************************
        //****************************Begin of add a graph
        //******************************************************************************

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        graph.addSeries(series2);

        // legend
        series.setTitle("foo");
        series2.setTitle("bar");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        //**********************************************************
        //********************************************************

    }

    private void addDatabaseGraph_Ekg(GraphView graph, DatabaseReference dataref) {
    }

    private void addDatabaseGraph_BloodPressure(GraphView graph, DatabaseReference dataref) {
    }

    private void addDatabaseGraph_HeartBeat(GraphView graph, DatabaseReference dataref) {
    }

    private void setGraphTitleandXY(LineGraphSeries series,
                                    GraphView graph, String title, String y) {

        graph.setTitle(title);
        graph.getGridLabelRenderer().setVerticalAxisTitle(y);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        // optional styles
        graph.setTitleTextSize(40);
        graph.setTitleColor(Color.GREEN);
        //graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);


        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setThickness(6);


    }
}