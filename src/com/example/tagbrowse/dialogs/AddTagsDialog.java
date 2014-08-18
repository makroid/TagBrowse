package com.example.tagbrowse.dialogs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tagbrowse.R;
import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.executors.TagWithCheckComparator;
import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.model.TagWithCheck;

public class AddTagsDialog extends DialogFragment {

	private ListView mTagListView;
	private MyCustomAdapter mDataAdapter;
	private ArrayList<TagWithCheck> mTagCheckList;
	private TagBrowseApp mParent;
	private FileListEntry mEntry;
	
	private Button mNewTagButton;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());	    
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View view = inflater.inflate(R.layout.add_tag_dialog, null);
		builder.setView(view)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				for (TagWithCheck twC : mTagCheckList) {
					if (twC.mHasTag) {
						mParent.getTagFileData().mTagGraph.addTag(twC.mTag, mEntry);
					} else {
						mParent.getTagFileData().mTagGraph.removeTag(twC.mTag, mEntry);
					}
				}
				mParent.updateCmdListAdapter();
				mParent.getControlTagView().updateTagView(mEntry);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				AddTagsDialog.this.getDialog().cancel();
			}
		});
//		.setNeutralButton(R.string.new_tag, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				NewTagDialog newTagDialog = new NewTagDialog();				
//			    newTagDialog.show(mParent.getFragmentManager().beginTransaction(), "newTagDialog");
//			}
//		}); 

		mDataAdapter = new MyCustomAdapter(mParent, R.layout.tag_check_entry);
		mTagListView = (ListView) view.findViewById(R.id.add_tags_listview);
		mTagListView.setAdapter(mDataAdapter);
		mDataAdapter.sortAdapter();

		mTagListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
				TagWithCheck twC = (TagWithCheck) parent.getItemAtPosition(position);
				Toast.makeText(mParent.getApplicationContext(),
						"Clicked on Row: " + twC.mTag.getName(), 
						Toast.LENGTH_LONG).show();
			}
		});
		
		mNewTagButton = (Button) view.findViewById(R.id.add_tag_dialog_new_button);
		mNewTagButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewTagDialog newTagDialog = new NewTagDialog();
				newTagDialog.setParentDialog(AddTagsDialog.this);
			    newTagDialog.show(mParent.getFragmentManager().beginTransaction(), "newTagDialog");	
			}
		});
		
		return builder.create();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try { 
			mParent = (TagBrowseApp) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " invalid parent");
		}
	}

	public void setTagCheckList(ArrayList<TagWithCheck> list) {
		mTagCheckList = list;
	}
	
	public void setTagCheckList(HashSet<TagWithCheck> set) {
		mTagCheckList = new ArrayList<TagWithCheck> (set);
	}
	
	public void setFileListEntry(FileListEntry entry) {
		mEntry = entry;
	}

	// called by NewTagDialog
	public void addTagToList(TagWithCheck twC) {
		mTagCheckList.add(twC);
		//mDataAdapter.notifyDataSetChanged();		
		mDataAdapter.sortAdapter();
	}

	private class MyCustomAdapter extends ArrayAdapter<TagWithCheck> {

		public MyCustomAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId, mTagCheckList);
		}

		private class ViewHolder {
			TextView tagName;
			CheckBox tagBox;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mParent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tag_check_entry, null);

				holder = new ViewHolder();
				holder.tagName = (TextView) convertView.findViewById(R.id.tag_entry_name);				
				holder.tagBox = (CheckBox) convertView.findViewById(R.id.tag_entry_checkbox);
				convertView.setTag(holder);

				holder.tagBox.setOnClickListener( new View.OnClickListener() {  
					public void onClick(View v) {  
						CheckBox cb = (CheckBox) v ;  
						TagWithCheck twC = (TagWithCheck) cb.getTag();  
//						Toast.makeText(mParent.getApplicationContext(),
//								"Clicked on Checkbox: " + cb.getText() +
//								" is " + cb.isChecked(), 
//								Toast.LENGTH_LONG).show();
						twC.mHasTag = cb.isChecked();
						sortAdapter();
					}  
				});  
			} 
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			TagWithCheck twC = mTagCheckList.get(position);
			holder.tagName.setText(twC.mTag.getName());
			convertView.setBackgroundColor(twC.mTag.getColor());
			holder.tagBox.setChecked(twC.mHasTag);
			holder.tagBox.setTag(twC);

			return convertView;
		}		
		
		public void sortAdapter() {
			this.sort(new TagWithCheckComparator());
		}
	}
}
