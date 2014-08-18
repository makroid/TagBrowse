package com.example.tagbrowse.util;

import java.io.File;

import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;

public class PreferenceWrapper {
	private Activity mContext;
	
	public static final String PREF_START_DIR = "startDir";
	public static final String PREF_SAVE_FILE = "saveFile";
	public static final String DEF_SAVE_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()  + "/tagbrowse.json";
	public static final String PREF_CELL_LABEL_ROWS = "cellLabelRows";
	
	public PreferenceWrapper(Activity activity) {
		mContext = activity;
	}
	
	public File getStartDir() {
		String dirPath = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PREF_START_DIR, "/");
		File startDir = new File(dirPath);

		if (startDir.exists() && startDir.isDirectory()) {
			return startDir;
		} else {
			return new File("/");
		}
	}
	
	public File getSaveFile() {
		String savePath = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PREF_SAVE_FILE, DEF_SAVE_FILE);
		File saveFile = new File(savePath);

		if ( ! saveFile.isDirectory()) {
			return saveFile;
		} else {
			return new File(DEF_SAVE_FILE);
		}
	}
	
	public int getGridCellLabelRows() {
		return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(PREF_CELL_LABEL_ROWS, 2);
	}
}
