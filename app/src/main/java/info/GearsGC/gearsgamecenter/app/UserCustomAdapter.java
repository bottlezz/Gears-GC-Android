package info.GearsGC.gearsgamecenter.app;

/**
 * Created by luna on 2014-08-17.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;

public class UserCustomAdapter extends ArrayAdapter<Game> {
    Context context;
    int layoutResourceId;
    ArrayList<Game> data = new ArrayList<Game>();

    public UserCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<Game> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.textView1);
            holder.textInfo = (TextView) row.findViewById(R.id.textView2);
            //holder.textLocation = (TextView) row.findViewById(R.id.textView3);
            holder.btnStart = (Switch) row.findViewById(R.id.button1);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        Game user = data.get(position);
        holder.textName.setText(user.getName());
        holder.textInfo.setText(user.getInfo());

        holder.btnStart.setChecked(false);
        holder.btnStart.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Log.i("Start checked", "**********");
                    Toast.makeText(context, "Start!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.i("Start unchecked", "**********");
                    Toast.makeText(context, "End!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textInfo;
        Switch btnStart;
    }
}
