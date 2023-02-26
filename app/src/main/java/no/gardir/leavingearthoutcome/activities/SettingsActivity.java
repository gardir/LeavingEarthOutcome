package no.gardir.leavingearthoutcome.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

import no.gardir.leavingearthoutcome.MainMenu;
import no.gardir.leavingearthoutcome.R;
import no.gardir.leavingearthoutcome.engine.Research;

/**
 * Created by gardir on 20.10.16.
 */
public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initSummary(getPreferenceScreen());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        switch (key) {
            case "seed":
                String seed = sharedPreferences.getString("seed", Research.getSEED());
                if ( !seed.equals(Research.getSEED()) ) {
                    Research.setSEED( seed );
                    MainMenu.resetAll();
                    System.out.println("Seed changed to " + seed );
                } else {
                    System.out.println("No change in seed " + seed);
                }
                break;
            case "dynamic_researches":
                NewResearchActivity.dynamic = sharedPreferences.getBoolean("dynamic_researches", NewResearchActivity.dynamic);
                break;
            case "outer_planets":
                Research.OUTER_PLANETS_ENABLED = sharedPreferences.getBoolean("outer_planets", Research.OUTER_PLANETS_ENABLED);
                break;
            case "stations":
                Research.STATIONS_ENABLED = sharedPreferences.getBoolean("stations", Research.STATIONS_ENABLED);
                break;
        }
        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            String text = editTextPref.getText();
            if ( editTextPref.getKey().equals("seed") ) {
                text = Research.getSEED();
                editTextPref.setText( text );
            }
            editTextPref.getEditText().setText(text);
            p.setSummary(text);
        }
        if (p instanceof MultiSelectListPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }

}
