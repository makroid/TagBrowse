package com.example.tagbrowse.executors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.model.FileEntryList;
import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.util.FileListSorter;


public class FileFinder extends AsyncTask<File, Integer, FileEntryList> {
	private TagBrowseApp mCaller;
	private File mCurrentDir;

	private static final String SELF = FileFinder.class.getName();

	public FileFinder(TagBrowseApp caller) {
		this.mCaller = caller;
		this.mCurrentDir = mCaller.getTagFileData().mCurrentDir;
	}

	@Override
	protected void onPostExecute(FileEntryList result) {
		FileEntryList childFilesList = result;
		Log.v(SELF, "Children for " + mCurrentDir.getAbsolutePath()+" received");
		mCaller.setEntries(childFilesList);    
	}

	@Override
	protected FileEntryList doInBackground(File... params) {             

		mCurrentDir = params[0];
		Log.v(SELF, "Received directory to list paths - " + mCurrentDir.getAbsolutePath());

		String[] fileNames = mCurrentDir.list();
		FileEntryList entryList = new FileEntryList(new ArrayList<FileListEntry>());
		List<FileListEntry> fileEntries = entryList.getEntries();

		//boolean showHidden = mCaller.getPreferenceHelper().isShowHidden();
		//boolean showSystem = mCaller.getPreferenceHelper().isShowSystemFiles();
		//Map<String, Long> dirSizes = Util.getDirSizes(currentDir);


		for(String fileName : fileNames) {
			if(".nomedia".equals(fileName)) {
				entryList.setExcludeFromMedia(true);
			}
			File f = new File(mCurrentDir.getAbsolutePath() + File.separator + fileName);

			if( ! f.exists()) {
				continue;
			}
			//if(Util.isProtected(f) && !showSystem) {
				//        continue;
				//}
			//if(f.isHidden() && !showHidden) {
			//        continue;
			//}

			String fname = f.getName();

			FileListEntry child = new FileListEntry();
			child.setName(fname);
			child.setFile(f);

//			if(f.isDirectory()) {
//				try {
//					Long dirSize = dirSizes.get(f.getCanonicalPath());
//					child.setSize(dirSize);
//				}
//				catch (Exception e) {
//					Log.w(TAG, "Could not find size for "+child.getPath().getAbsolutePath());
//							child.setSize(0);
//				}
//			}
//			else {
//				child.setSize(f.length());				
//			}
			
			child.setSize(f.length());
			
//			child.setLastModified(new Date(f.lastModified()));
			fileEntries.add(child);
		}
	
		Collections.sort(fileEntries, new FileListSorter());

		return entryList;
	}
}
