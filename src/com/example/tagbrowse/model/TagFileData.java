package com.example.tagbrowse.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.graph.JSONTagGraphIO;
import com.example.tagbrowse.graph.TagGraph;

public class TagFileData {
	private TagBrowseApp mContext;
	
	public File mCurrentDir;
	private FileListEntry mSelectedFile;
	public List<FileListEntry> mFiles;
	public TagGraph mTagGraph;
	
	private List<SelectedFileChangedListener> mChangeListeners;
	
	public TagFileData(TagBrowseApp context) {
		mContext      = context;
		mCurrentDir   = mContext.getPrefWrapper().getStartDir();
		mSelectedFile = null;
		mFiles        = new ArrayList<FileListEntry>();
		mTagGraph     = new TagGraph();
		
		mChangeListeners = new ArrayList<SelectedFileChangedListener>();
		
		try {
			File myFile = mContext.getPrefWrapper().getSaveFile();
			if (myFile.exists()) {			
				FileInputStream fin = new FileInputStream(myFile);			
				JSONTagGraphIO.readJsonStream(fin, mTagGraph);
				fin.close();
				System.out.println("graph read");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			File myFile = mContext.getPrefWrapper().getSaveFile();
			FileOutputStream fout = new FileOutputStream(myFile);
			JSONTagGraphIO.writeJsonStream(fout, mTagGraph);
			fout.close();
			System.out.println("graph written");			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void callChangeListeners() {
		for (SelectedFileChangedListener sfcl : mChangeListeners) {
			sfcl.selectedFileChanged(mSelectedFile);
		}
	}
	
	public void addChangeListener(SelectedFileChangedListener listener) {
		mChangeListeners.add(listener);
	}
}
