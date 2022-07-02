package no.gardir.leavingearthoutcome;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import no.gardir.leavingearthoutcome.activities.NewResearchActivity;
import no.gardir.leavingearthoutcome.activities.SettingsActivity;
import no.gardir.leavingearthoutcome.activities.UseResearchActivity;
import no.gardir.leavingearthoutcome.engine.Player;
import no.gardir.leavingearthoutcome.engine.Research;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = "MainMenu";
    public final static HashMap<Research.ResearchType, Integer> imageResIds = createResearchImageIds();
    private static HashMap<Research.ResearchType, Integer> createResearchImageIds() {
        HashMap<Research.ResearchType, Integer> imageIds = new HashMap<>();
        imageIds.put(Research.ResearchType.JUNO, R.drawable.juno);
        imageIds.put(Research.ResearchType.ATLAS, R.drawable.atlas);
        imageIds.put(Research.ResearchType.SOYUZ, R.drawable.soyuz);
        imageIds.put(Research.ResearchType.SATURN, R.drawable.saturn);
        imageIds.put(Research.ResearchType.LANDING, R.drawable.landing);
        imageIds.put(Research.ResearchType.SURVEYING, R.drawable.surveying);
        imageIds.put(Research.ResearchType.RENDEZVOUS, R.drawable.rendezvous);
        imageIds.put(Research.ResearchType.REENTRY, R.drawable.reentry);
        imageIds.put(Research.ResearchType.LIFE_SUPPORT, R.drawable.life_support);
        imageIds.put(Research.ResearchType.ION_THRUSTERS, R.drawable.ion_thrusters);
        imageIds.put(Research.ResearchType.PROTON, R.drawable.proton);
        imageIds.put(Research.ResearchType.AEROBREAKING, R.drawable.aerobreaking);
        imageIds.put(Research.ResearchType.SPACE_SHUTTLE, R.drawable.space_shuttle);
        imageIds.put(Research.ResearchType.ROVER, R.drawable.rover);
        imageIds.put(Research.ResearchType.SYNTHESIS, R.drawable.synthesis);
        return imageIds;
    }

    public static int x, y;
    public static float scale;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (inSampleSize != 0 &&
                    (halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int toDP(int in) {
        // Convert the dps to pixels, based on density scale
        return (int) (in * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        scale = getResources().getDisplayMetrics().density;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        x = size.x;
        y = size.y;
        PreferenceManager.setDefaultValues(this, R.xml.preferences,
                false);
        Log.v(TAG, "onCreate");
    }

    /**
     * Called when the user clicks the 'New Research' button
     * @param view
     */
    public void newResearch(View view) {
        Intent intent = new Intent(this, NewResearchActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the 'Use Research' button
     * @param view
     */
    public void useResearch(View view) {
        Intent intent = new Intent(this, UseResearchActivity.class);
        startActivity(intent);
    }

    public void removeThreeOutcomes(View view) {
        Research.removeOutcomes(3);
    }

    public void removeOneOutcome(View view) {
        Research.removeOutcomes(1);
    }

    public void reset(View view) {
        resetAll();
    }

    /**
     * Called when the user clicks the 'reset' button
     */
    public static void resetAll() {
        Player.reset();
        Research.reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
