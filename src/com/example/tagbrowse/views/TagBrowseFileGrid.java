package com.example.tagbrowse.views;

import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.adapter.FileGridAdapter;
import com.example.tagbrowse.util.Utils;

import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;


public class TagBrowseFileGrid extends GridView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	private Listener mListener;
	private FileGridAdapter mAdapter;
	
	public TagBrowseFileGrid(TagBrowseApp context) {
		super(context);
		mAdapter = new FileGridAdapter(context);
		this.setAdapter(mAdapter);
		this.setNumColumns(GridView.AUTO_FIT);
		this.setColumnWidth(Utils.convertDpToPx(60, context));
		this.setSelector(new PaintDrawable(Color.TRANSPARENT));
		//this.setHorizontalSpacing(10);
		//this.setStretchMode(GridView.STRETCH_SPACING);
		
		setOnItemClickListener(this);
		setOnItemLongClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (mListener != null) {
			mListener.onClick(parent, v, position);
		}
	}
	
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		if (mListener != null) {
			mListener.onLongClick(parent, v, position);
		}
		return true;
	}

	public void setListener(Listener l){
		mListener = l;
	}

	public interface Listener{
		void onClick(AdapterView<?> parent, View view, int position);
		void onLongClick(AdapterView<?> parent, View view, int position); 
	}
	
	public FileGridAdapter getAdapter() {
		return mAdapter;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	            MeasureSpec.AT_MOST);
	    super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
