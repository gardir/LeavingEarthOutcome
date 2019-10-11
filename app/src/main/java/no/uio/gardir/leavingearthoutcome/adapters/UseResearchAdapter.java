package no.uio.gardir.leavingearthoutcome.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import no.uio.gardir.leavingearthoutcome.MainMenu;
import no.uio.gardir.leavingearthoutcome.activities.DisplayResearchActivity;
import no.uio.gardir.leavingearthoutcome.engine.Player;
import no.uio.gardir.leavingearthoutcome.engine.Research;

/**
 * Created by gardir on 20.10.16.
 */
public class UseResearchAdapter extends BaseAdapter {
    int dpWidth = MainMenu.toDP(317), dpHeight = MainMenu.toDP(206);
    private Context mContext;

    public UseResearchAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        int count = Player.getPlayer().getResearchCount();
        return count < 1 ? 1 : count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createImageView(position, convertView, parent);
    }

    public View createImageView( int position, View convertView, ViewGroup parent ) {
        Player player = Player.getPlayer();
        View view;
        int count  = player.getResearchCount();
        if ( count > 0) {
            if ( convertView == null ) {
                view = new ImageView(mContext);
            } else {
                view = convertView;
            }
            String s_id = String.format("%d", position + 1);
            Research r = player.getResearch(s_id);
            // Used to set width/height according to how many others there is
            int maxWidth = count > 4 ? dpWidth/4 : dpWidth/count;
            int maxHeight = count % 4 == 0 ? dpHeight / (count/4) : dpHeight / ((count/4)+1);
            ((ImageView)view).setImageBitmap(
                    MainMenu.decodeSampledBitmapFromResource(
                            mContext.getResources(),
                            MainMenu.imageResIds.get(r.research),
                            maxWidth, maxHeight));
            //view.setScaleY(scale);
            ((ImageView) view).setAdjustViewBounds(true);
        } else {
            if ( convertView == null ) {
                view = new TextView(mContext);
            } else {
                view = (TextView) convertView;
            }
            ((TextView)view).setText("No researches yet");
        }
        return view;
    }

    private View createTextView( int position, View convertView ) {
        Player player = Player.getPlayer();
        TextView textView;
        if ( convertView == null ) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }
        if ( player.getResearchCount() > 0 ) {
            String s_id = String.format( "%d", position+1 );
            Research r = player.getResearch( s_id );
            textView.setText(r.toString());
        } else {
            textView.setText("No researches yet");
        }
        return textView;
    }
}
