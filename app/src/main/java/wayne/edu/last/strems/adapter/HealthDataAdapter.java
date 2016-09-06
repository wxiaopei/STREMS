package wayne.edu.last.strems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import wayne.edu.last.strems.R;
import wayne.edu.last.strems.ViewHolder.HealthDataViewHolder;
import wayne.edu.last.strems.models.HealthData;


/**
 * Created by Wuxiaopei on 8/26/2016.
 */
public class HealthDataAdapter extends RecyclerView.Adapter<HealthDataViewHolder> {

    private static final String TAG = "RecyclerView.Adapter";


    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mHealthDataIds = new ArrayList<>();
    private List<HealthData> mHealthData = new ArrayList<>();



    public HealthDataAdapter(final Context context, DatabaseReference ref) {


        mContext = context;
        mDatabaseReference = ref;

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list


                HealthData hd = dataSnapshot.getValue(HealthData.class);
              //  System.out.println("iterate data from database====temp"+hd.temprature);
                //Object hdo = dataSnapshot.getValue();
               // HealthData hd = (HealthData)hdo;

                // [START_EXCLUDE]
                // Update RecyclerView
                mHealthDataIds.add(dataSnapshot.getKey());
                mHealthData.add(hd);
                notifyItemInserted(mHealthData.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                HealthData newhd = dataSnapshot.getValue(HealthData.class);
                String hdKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mHealthDataIds.indexOf(hdKey);
                if (commentIndex > -1) {
                    // Replace with the new data
                    mHealthData.set(commentIndex, newhd);

                    // Update the RecyclerView
                    notifyItemChanged(commentIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + hdKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mHealthDataIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Remove data from the list
                    mHealthDataIds.remove(commentIndex);
                    mHealthData.remove(commentIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                HealthData movedComment = dataSnapshot.getValue(HealthData.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }



        };
        ref.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;


        //System.out.println("healdata data size="+mHealthData.size());

    }



    @Override
    public HealthDataViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_data, parent, false);
        return new HealthDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HealthDataViewHolder holder, int position) {
        HealthData hd = mHealthData.get(position);
        //holder.sampleTimeView.setText(hd.sample_time);
       // holder.tempView.setText(hd.temprature);
        //holder.bpView.setText(hd.blood_pressure);

    }

    @Override
    public int getItemCount() {
        return mHealthData.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
