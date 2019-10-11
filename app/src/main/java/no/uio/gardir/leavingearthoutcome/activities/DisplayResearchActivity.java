package no.uio.gardir.leavingearthoutcome.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import no.uio.gardir.leavingearthoutcome.MainMenu;
import no.uio.gardir.leavingearthoutcome.R;
import no.uio.gardir.leavingearthoutcome.engine.Player;
import no.uio.gardir.leavingearthoutcome.engine.Research;

public class DisplayResearchActivity extends AppCompatActivity {
    private static HashMap<Research.Outcome, Integer> outcomeImageIds = createImageIds();
    private static HashMap<Research.Outcome, Integer> createImageIds() {
        HashMap<Research.Outcome, Integer> dict = new HashMap<>();
        dict.put(Research.Outcome.UNKNOWN, R.drawable.outcome);
        dict.put(Research.Outcome.SUCCESS, R.drawable.success);
        dict.put(Research.Outcome.MAJOR_FAILURE, R.drawable.major_failure);
        dict.put(Research.Outcome.MINOR_FAILURE, R.drawable.minor_failure);
        return dict;
    }


    private ImageView outcomeView;
    private String s_id;
    private Button last = null;
    private boolean reveal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_research);

        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_display_research);
        Intent intent = getIntent();
        s_id = intent.getStringExtra( UseResearchActivity.RESEARCH );
        Research r = Player.getPlayer().getResearch(s_id);
        layout.setBackgroundDrawable(new BitmapDrawable(
                        getResources(),
                        MainMenu.decodeSampledBitmapFromResource(
                                getResources(),
                                MainMenu.imageResIds.get(r.research),
                                MainMenu.x, MainMenu.y)
                )
        );

        outcomeView = (ImageView) findViewById(R.id.outcome_list);
        outcomeView.setImageDrawable(prepareOutcomes(r, null));
        reveal = true;
    }

    private LayerDrawable prepareOutcomes(Research r, Research.Outcome result) {
        Drawable[] outcomes = new Drawable[ r.outcomes() ];
        int i=0;
        while ( i < outcomes.length ) {
            int imageId = reveal && i+1 == outcomes.length ?
                    outcomeImageIds.get( result ) :
                    outcomeImageIds.get( Research.Outcome.UNKNOWN );
            outcomes[i++] = new BitmapDrawable(
                getResources(),
                MainMenu.decodeSampledBitmapFromResource(
                        getResources(),
                        imageId,
                        135, 204));
        }
        LayerDrawable layer = new LayerDrawable(outcomes);
        i=0;
        int offset = 20;
        while ( i < outcomes.length ) {
            int iOffset = i * offset;
            layer.setLayerInset(i, iOffset, iOffset, -iOffset, -iOffset);
            //layer.setLayerGravity(i, Gravity.LEFT | Gravity.TOP);
            i++;
        }
        return layer;
    }

    public void getOutcome(View view) {
        if ( last != null ) {
            ((ViewGroup)DisplayResearchActivity.this.findViewById(R.id.activity_display_research)).removeView(last);
            last = null;
        }
        final Research research = Player.getPlayer().getResearch(s_id);

        if ( reveal && research.outcomes() > 0 ) {
            Research.Outcome result = research.drawOutcome();
            LinearLayout layout = (LinearLayout) findViewById(R.id.activity_display_research);
            Button remove = new Button(this);
            if (result == Research.Outcome.SUCCESS && research.outcomes() == 1 ) {
                remove.setText("Remove?");
            } else {
                int cost = result == Research.Outcome.SUCCESS ? 10 : 5;
                remove.setText(String.format("Remove (%d$)?", cost));
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player.getPlayer().getResearch(s_id).removeLastOutcome();
                    ((ViewGroup) DisplayResearchActivity.this.findViewById(R.id.activity_display_research)).removeView(v);
                    outcomeView.setImageDrawable(prepareOutcomes( research, null) );
                    reveal = true;
                }
            });
            last = remove;
            remove.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(remove);
            outcomeView.setImageDrawable(prepareOutcomes(research, result) );
            reveal = false;
        } else if ( research.outcomes() > 0 ) {
            outcomeView.setImageDrawable(prepareOutcomes(research, null));
            reveal = true;
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
