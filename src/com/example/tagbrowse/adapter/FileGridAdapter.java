package com.example.tagbrowse.adapter;

import java.util.List;

import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.model.SelectedFileChangedListener;
import com.example.tagbrowse.util.*;
import com.example.tagbrowse.R;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileGridAdapter extends BaseAdapter implements SelectedFileChangedListener {
	private TagBrowseApp mContext;
	private List<FileListEntry> mFiles;
	private FileListEntry mSelectedFile;
	private LayoutInflater mInflator;
	
	public static class GridCellViews {		
		public ImageView resIcon;
		public TextView resName;
    }
	
	private GridCellViews mGridCellViews;
	
	public FileGridAdapter(TagBrowseApp app) {
		super();
		mContext      = app;
		mFiles        = app.getTagFileData().mFiles;
		mSelectedFile = null;
		mInflator     = mContext.getLayoutInflater(); 
	}
	
	@Override
	public int getCount() {
		if(mFiles == null) {
			return 0;
		}
		else {
			return mFiles.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		if(mFiles == null) {
			return null;
		} else {
			return mFiles.get(arg0);
		}
	}

	public List<FileListEntry> getItems() {
		return mFiles;
	}
    
    @Override
    public long getItemId(int position) {
        return position;     
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mGridCellViews = null;
		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.file_grid, parent, false);
			mGridCellViews = new GridCellViews();
			mGridCellViews.resIcon = (ImageView) convertView.findViewById(R.id.grid_file_icon);
			mGridCellViews.resName = (TextView) convertView.findViewById(R.id.grid_file_name);			
			convertView.setTag(mGridCellViews);
		} else {
			mGridCellViews = (GridCellViews) convertView.getTag();
		}
		final FileListEntry currentFile = mFiles.get(position);
		
		Drawable icon = Utils.getIcon(mContext, mFiles.get(position).getFile());
		mGridCellViews.resName.setLines(2);		
		mGridCellViews.resName.setText(currentFile.getName());		
		mGridCellViews.resIcon.setImageDrawable(icon);
		
		// remember selected view (necessary because views are dynamically generated and removed)
		if (mSelectedFile != null && currentFile.getFile().getAbsolutePath().equals(mSelectedFile.getFile().getAbsolutePath())) {
			convertView.setBackgroundColor(Color.RED);
		} else {
			convertView.setBackgroundColor(0x00000000);
		}
		
		return convertView;
	}
	
	public void selectedFileChanged(FileListEntry newSelection) {
		this.mSelectedFile = newSelection;
	}
}
