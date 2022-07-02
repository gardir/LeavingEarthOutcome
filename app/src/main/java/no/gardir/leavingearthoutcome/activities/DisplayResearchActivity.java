package no.gardir.leavingearthoutcome.activities;

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
import java.util.LinkedList;
import java.util.List;

import no.gardir.leavingearthoutcome.MainMenu;
import no.gardir.leavingearthoutcome.R;
import no.gardir.leavingearthoutcome.engine.Player;
import no.gardir.leavingearthoutcome.engine.Research;

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
    private final LinkedList<Button> removeButtons = new LinkedList<>();
    private int reveal = 0;

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
        reveal = r.reveal;
    }

    private LayerDrawable prepareOutcomes(final Research research, final List<Research.Outcome> results) {
        Drawable[] outcomes = new Drawable[ research.outcomes() ];
        LinearLayout layout = (LinearLayout) findViewById(R.id.removeButtonsList);

        int offset = 20;
        for (int i=0; i<outcomes.length; i++) {
            int imageId = outcomeImageIds.get(Research.Outcome.UNKNOWN);
            if (results != null && i + results.size() >= outcomes.length) {
                final Research.Outcome result = results.get(results.size() + i - outcomes.length);
                final Button removeButton = new Button(this);
                if (result == Research.Outcome.SUCCESS && research.outcomes() == 1) {
                    removeButton.setText("Remove?");
                } else {
                    int cost = result == Research.Outcome.SUCCESS ? 10 : 5;
                    removeButton.setText(String.format("Remove (%d$)?", cost));
                }
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Player.getPlayer().getResearch(s_id).removeOutcome(result);
                        results.remove(result);
                        clearRemoveButtons();
                        if (results.size() == 0) {
                            outcomeView.setImageDrawable(prepareOutcomes(research, null));
                            reveal = research.reveal;
                        } else {
                            reveal--;
                            outcomeView.setImageDrawable(prepareOutcomes(research, results));
                        }
                    }
                });
                removeButtons.add(removeButton);
                layout.addView(removeButton);
                imageId = outcomeImageIds.get( result );
            }
            outcomes[i] = new BitmapDrawable(
                    getResources(),
                    MainMenu.decodeSampledBitmapFromResource(
                            getResources(),
                            imageId,
                            135, 204));
        }
        LayerDrawable layer = new LayerDrawable(outcomes);
        for (int i=0; i<outcomes.length; i++)
        {
            int iOffset = i * offset;
            int leftOffset = iOffset;
            int topOffset = iOffset;
            int rightOffset = -iOffset;
            int botOffset = -iOffset;
            if (results != null && i + results.size() > outcomes.length)
            {
                int multiple = (i + results.size() - outcomes.length) % outcomes.length;
                leftOffset += multiple * 200;
                rightOffset -= multiple * 200;
            }
            layer.setLayerInset(i, leftOffset, topOffset, rightOffset, botOffset);
        }
        //layer.setLayerGravity(i, Gravity.LEFT | Gravity.TOP);
        return layer;
    }

    public void getOutcome(View view) {
        if (removeButtons.size() > 0) {
            clearRemoveButtons();
        }
        final Research research = Player.getPlayer().getResearch(s_id);

        if ( reveal > 0 && research.outcomes() > 0 ) {
            List<Research.Outcome> results = research.drawOutcome();
            outcomeView.setImageDrawable(prepareOutcomes(research, results));
            reveal = 0;
        } else if ( research.outcomes() > 0 ) {
            outcomeView.setImageDrawable(prepareOutcomes(research, null));
            reveal = research.reveal;
        }
    }

    private void clearRemoveButtons() {
        for (Button last : removeButtons) {
            ((ViewGroup) DisplayResearchActivity.this.findViewById(R.id.removeButtonsList)).removeView(last);
        }
        removeButtons.clear();
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
