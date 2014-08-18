package com.example.tagbrowse.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tagbrowse.R;
import com.example.tagbrowse.TagBrowseApp;
import com.example.tagbrowse.model.Tag;
import com.example.tagbrowse.model.TagWithCheck;

public class NewTagDialog extends DialogFragment implements ColorChooserDialog.OnAmbilWarnaListener  {
		
	private TagBrowseApp mParent;
	private Button mColorButton;
	private EditText mEditTextName;
	private int mTagColor;
	private AddTagsDialog mParentDialog;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());	    
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View view = inflater.inflate(R.layout.new_tag_dialog, null);
		builder.setView(view)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Tag newTag = new Tag(mEditTextName.getText().toString());
				newTag.setColor(mTagColor);
				TagWithCheck twC = new TagWithCheck(newTag, true);
				mParentDialog.addTagToList(twC);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				NewTagDialog.this.getDialog().dismiss();
			}
		}); 	

		mColorButton = (Button) view.findViewById(R.id.new_tag_color_button);
		mColorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorChooserDialog dialog = new ColorChooserDialog(mParent, Color.BLUE, NewTagDialog.this);
				dialog.show();
			}
		});
		
		mEditTextName = (EditText) view.findViewById(R.id.new_tag_name_edittext);
		
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

	public void setParentDialog(AddTagsDialog parent) {
		mParentDialog = parent;
	}
	
	@Override
	public void onCancel(ColorChooserDialog dialog) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onOk(ColorChooserDialog dialog, int color) {
		// TODO Auto-generated method stub
		mColorButton.setBackgroundColor(color);
		mTagColor = color;
	}
}

