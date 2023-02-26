package no.gardir.leavingearthoutcome.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import no.gardir.leavingearthoutcome.adapters.NewResearchAdapter;
import no.gardir.leavingearthoutcome.R;
import no.gardir.leavingearthoutcome.engine.Player;
import no.gardir.leavingearthoutcome.engine.Research;
import no.gardir.leavingearthoutcome.hacks.MyGridView;

public class NewResearchActivity extends AppCompatActivity {
    public static boolean dynamic = true;
    static int COUNT;

    public static void calcCOUNT() {
        COUNT = 10;
        COUNT += Research.OUTER_PLANETS_ENABLED ? 2 : 0; // +2 for outer planets
        COUNT += Research.STATIONS_ENABLED ? 3 : 0;
        COUNT -= Player.getPlayer().getResearchCount();
    }

    public static int getCOUNT() {
        return COUNT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int numColumns = COUNT > 4 || dynamic ? 4 : COUNT;
        if ( dynamic ) {
            setContentView(R.layout.activity_new_research);
            calcCOUNT();
            GridView gridView = (GridView) findViewById(R.id.gridview_new_research);
            gridView.setNumColumns( numColumns );
            gridView.setAdapter( new NewResearchAdapter(this));
        } else {
            setContentView(R.layout.activity_new_research_2);
            LinearLayout layout = (LinearLayout) findViewById(R.id.linear_new_research);
            GridView gridView = new MyGridView(this);
            gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setNumColumns(numColumns);
            gridView.setAdapter(new NewResearchAdapter(this));
            layout.addView(gridView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
