package no.gardir.leavingearthoutcome.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import no.gardir.leavingearthoutcome.MainMenu;
import no.gardir.leavingearthoutcome.activities.NewResearchActivity;
import no.gardir.leavingearthoutcome.engine.Player;
import no.gardir.leavingearthoutcome.engine.Research;

/**
 * Created by gardir on 20.10.16.
 */
public class NewResearchAdapter extends BaseAdapter {
    private Context mContext;
    private int dpWidth = MainMenu.toDP(317),
            dpHeight = MainMenu.toDP(206);
    private LinkedList<Research.ResearchType> researches = Research.getAvailableResearches(Player.getPlayer());

    public NewResearchAdapter(Context c) {
        this.mContext = c;
    }

    @Override
    public int getCount() {
        if ( NewResearchActivity.dynamic ) {
            return NewResearchActivity.getCOUNT();
        } else {
            return 12;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if ( NewResearchActivity.dynamic ) {
            return createDynamicImageView( position, convertView, parent );
        } else {
            return createStaticImageView( position, convertView, parent );
        }
    }

    public View createStaticImageView( final int position, View convertView, ViewGroup parent ) {
        View view = null;
        boolean imageSet = false;
        for ( final Research.ResearchType r : researches ) {
            if (r.ordinal() == position) {
                imageSet = true;
                if (convertView == null || !(view instanceof ImageView)) {
                    view = new ImageView(mContext);
                } else {
                    view = convertView;
                }
                // Used to set width/height according to how many others there is
                int maxWidth = Math.round(dpWidth / (float) 4),
                        maxHeight = Math.round( dpHeight / (float) 3);
                /*((ImageView) view).setImageResource(
                        MainMenu.imageResIds.get(r)*/
                ((ImageView) view).setImageBitmap(
                        MainMenu.decodeSampledBitmapFromResource(
                                mContext.getResources(),
                                MainMenu.imageResIds.get(r),
                                maxWidth, maxHeight)
                );
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String clickedResearch = String.format("%d", position + 1);
                        Research.giveResearch(Player.getPlayer(), clickedResearch);
                        NewResearchActivity.calcCOUNT();
                        NewResearchAdapter.this.notifyDataSetChanged();
                        researches.remove(r);
                    }
                });
                ((ImageView)view).setAdjustViewBounds(true);
            }
        }
        if ( !imageSet ) {
            // No more researches
            view = new TextView(mContext);
            if ( NewResearchActivity.getCOUNT() == 0 ) {
                ((TextView) view).setText("No more researches");
            } else {
                ((TextView) view).setText("Researched");
            }
        }
        return view;
    }

    public View createDynamicImageView(final int position, View convertView, ViewGroup parent) {
        View view;
        int count  = NewResearchActivity.getCOUNT();
        if ( count > 0) {
            if ( convertView == null ) {
                view = new ImageView(mContext);
            } else {
                view = convertView;
            }
            // Used to set width/height according to how many others there is
            int maxWidth = count > 4 ? dpWidth/4 : dpWidth/count;
            int maxHeight = count % 4 == 0 ? dpHeight / (count/4) : dpHeight / ((count/4)+1);
            ((ImageView)view).setImageBitmap(
                    MainMenu.decodeSampledBitmapFromResource(
                            mContext.getResources(),
                            MainMenu.imageResIds.get(researches.get(position)),
                            maxWidth, maxHeight));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clickedResearch = String.format("%d", position + 1);
                    Research.giveResearch(Player.getPlayer(), clickedResearch);
                    researches.remove(position);
                    NewResearchActivity.calcCOUNT();
                    NewResearchAdapter.this.notifyDataSetChanged();
                }
            });
            ((ImageView)view).setAdjustViewBounds(true);
        } else {
            if ( convertView == null ) {
                view = new TextView(mContext);
            } else {
                view = convertView;
            }
            ((TextView)view).setText("No more researches");
        }
        return view;
    }

    private View createTextView( final int position, View convertView, View parent ) {
        TextView textView;
        if ( convertView == null ) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }
        if ( researches.size() > 0 ) {
            textView.setText( researches.get( position ).toString() );
        } else {
            textView.setText("No more researches");
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedResearch = String.format("%d", position+1);
                Research.giveResearch(Player.getPlayer(), clickedResearch);
                researches.remove(position);
                NewResearchAdapter.this.notifyDataSetChanged();
            }
        });
        return textView;
    }
}
