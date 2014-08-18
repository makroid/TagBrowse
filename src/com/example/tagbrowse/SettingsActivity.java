package com.example.tagbrowse;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()== android.R.id.home) {
			onBackPressed();
			return true;
		}
		else{
			return false;
		}
	}
}
