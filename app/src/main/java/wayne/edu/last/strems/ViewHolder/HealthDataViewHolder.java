package wayne.edu.last.strems.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import wayne.edu.last.strems.R;
import wayne.edu.last.strems.models.Emergency;
import wayne.edu.last.strems.models.HealthData;

/**
 * Created by Wuxiaopei on 8/26/2016.
 */
public class HealthDataViewHolder  extends RecyclerView.ViewHolder{

    public TextView sampleTimeView;
    public TextView tempView;
    public TextView bpView;

    public HealthDataViewHolder(View itemView) {
        super(itemView);
        sampleTimeView = (TextView) itemView.findViewById(R.id.sampling_time);
        tempView = (TextView) itemView.findViewById(R.id.sensor_temp);
        bpView = (TextView) itemView.findViewById(R.id.sensor_bp);
    }


//    public void bindToHealthData(HealthData hd){
//        sampleTimeView.setText(hd.sample_time);
//        tempView.setText(hd.temprature);
//    }


}
