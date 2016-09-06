package wayne.edu.last.strems.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import wayne.edu.last.strems.R;
import wayne.edu.last.strems.models.Emergency;

/**
 * Created by Wuxiaopei on 8/23/2016.
 */
public class EmergencyViewHolder extends RecyclerView.ViewHolder {


    public TextView ambulanceTypeView;
    public TextView emergencyTimeView;
    public TextView authorView;
    public TextView patient_First_View;
    public TextView patient_Last_View;


    public EmergencyViewHolder(View itemView) {

        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.case_author);
        emergencyTimeView = (TextView) itemView.findViewById(R.id.emergency_time);
        ambulanceTypeView = (TextView) itemView.findViewById(R.id.ambulancetype);
        patient_Last_View  = (TextView) itemView.findViewById(R.id.patient_last_name);
        patient_First_View  = (TextView) itemView.findViewById(R.id.patient_first_name);


    }

    public void bindToEmergency(Emergency em, View.OnClickListener starClickListener){
        ambulanceTypeView.setText(em.ambulance_type);
        emergencyTimeView.setText(em.occur_time);
        authorView.setText(em.user);
        patient_Last_View.setText(em.patient_Last);
        patient_First_View.setText(em.patient_First);
    }
}
