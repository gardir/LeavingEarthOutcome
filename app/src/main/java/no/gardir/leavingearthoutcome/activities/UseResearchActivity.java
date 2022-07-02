package no.gardir.leavingearthoutcome.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import no.gardir.leavingearthoutcome.R;
import no.gardir.leavingearthoutcome.adapters.UseResearchAdapter;
import no.gardir.leavingearthoutcome.engine.Player;

public class UseResearchActivity extends AppCompatActivity {

    public static final String RESEARCH = "no.uio.gardir.RESEARCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_research);

        GridView gridView = (GridView) findViewById(R.id.gridview_use_research);
        int count = Player.getPlayer().getResearchCount();
        int numColumns = count > 4 ? 4 : count;
        gridView.setNumColumns(numColumns);
        gridView.setAdapter(new UseResearchAdapter(this));

        gridView.setOnItemClickListener(new ActivateResearch());

    }

    class ActivateResearch implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String clickedResearch = String.format("%d", position+1);
            Intent intent = new Intent(UseResearchActivity.this, DisplayResearchActivity.class);
            intent.putExtra( RESEARCH, clickedResearch);
            startActivity(intent);
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
