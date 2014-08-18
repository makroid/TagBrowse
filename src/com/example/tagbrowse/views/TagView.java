package com.example.tagbrowse.views;

import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.model.Tag;
import com.example.tagbrowse.util.RowLayout;

import android.widget.LinearLayout;

public class TagView extends LinearLayout {
	private TagBrowseApp mParent;
	private RowLayout mTagView;
	
	public TagView(TagBrowseApp parent) {
		super(parent);
		
		mParent = parent;
		mTagView = new RowLayout(mParent, null);
		
//		mLayout = new LinearLayout(mParent);
//		mLayout.setLayoutParams(new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(mTagView);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lp);
	}
	
	public RowLayout getTagView() {
		return mTagView;
	}
	
	public void updateTagView(FileListEntry fle) {
		mTagView.removeAllViews();
		for (Tag tag : mParent.getTagFileData().mTagGraph.getTagsFor(fle)) {
			mTagView.addView(mParent.createTagButton(tag));
		}
	}
	
}
