package com.example.tagbrowse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.example.tagbrowse.dialogs.AddTagsDialog;
import com.example.tagbrowse.executors.FileFinder;
import com.example.tagbrowse.model.Tag;
import com.example.tagbrowse.model.TagFileData;
import com.example.tagbrowse.model.FileEntryList;
import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.util.PreferenceWrapper;
import com.example.tagbrowse.util.SplitPaneLayout;
import com.example.tagbrowse.util.Utils;
import com.example.tagbrowse.views.TagView;
import com.example.tagbrowse.views.TagBrowseFileGrid;
import com.example.tagbrowse.R;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TagBrowseApp extends ActionBarActivity {

	private TagFileData mTagFileData;
	private TagBrowseFileGrid mFileGridView;
	private TagView mControlTagView;
	private AutoCompleteTextView mCmdView;
	private SplitPaneLayout mLayout;
	private PreferenceWrapper mPrefWrapper;
	private OnSharedPreferenceChangeListener mPrefListener;
	
	private class GridClickListener implements TagBrowseFileGrid.Listener {	
		
		private View mPreviousView = null;
		
		public void onClick(AdapterView<?> parent, View view, int position) {
			FileListEntry fle = (FileListEntry) mFileGridView.getAdapter().getItem(position);
			File clickedFile = fle.getFile();
			
			if (mPreviousView != null) {
				mPreviousView.setBackgroundColor(0x00000000);
			}
			if (clickedFile.isDirectory()) {
			} else {
				view.setBackgroundColor(Color.RED);
			}			

			doFileAction(clickedFile);				
			
			mControlTagView.updateTagView(fle);
			mPreviousView = view;
		}
		
		public void onLongClick(AdapterView<?> parent, View view, int position) {
			FileListEntry fle =  (FileListEntry) mFileGridView.getAdapter().getItem(position);
			AddTagsDialog addTagsDialog = new AddTagsDialog();
			addTagsDialog.setTagCheckList(getTagFileData().mTagGraph.getAllTagsAndSelectedFor(fle));
			addTagsDialog.setFileListEntry(fle);
			addTagsDialog.show(getFragmentManager().beginTransaction(), "addTagsDialog");
			
			mControlTagView.updateTagView(fle);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPrefWrapper = new PreferenceWrapper(this);
		mTagFileData = new TagFileData(this);
		
		initViews();
		setLayout();
		initListeners();
		
		setContentView(mLayout);
	
		refresh();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
		mTagFileData.save();
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(mPrefListener);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("onStop");
		mTagFileData.save();
	}
	
	private void initViews() {
		mFileGridView = new TagBrowseFileGrid(this);
		mFileGridView.setListener(new GridClickListener());
		
		mControlTagView = new TagView(this);
		
		setUpActionBar();
	}

	private void setLayout() {
		mLayout = new SplitPaneLayout(TagBrowseApp.this);
		mLayout.setSplitterDraggingDrawable(new PaintDrawable(0xFF000000));
		mLayout.setSplitterDrawable(new PaintDrawable(0xFF000000));		
		
		mLayout.setOrientation(LinearLayout.VERTICAL);
				
		mLayout.addView(mFileGridView);
		ScrollView tagScrollView = new ScrollView(this);
		//tagScrollView.addView(mTagView);
		tagScrollView.addView(mControlTagView);
		mLayout.addView(tagScrollView);
		
		mLayout.setSplitterPositionPercent(0.7f);
	}
	
	private void initListeners() {
		mPrefListener = new OnSharedPreferenceChangeListener() {			
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				if (PreferenceWrapper.PREF_START_DIR.equals(key)) {
					// TODO:
				}
			}
		};
		
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mPrefListener);
		
		mTagFileData.addChangeListener(mFileGridView.getAdapter());
	}
	
	private void setUpActionBar() {
		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.actionbar_autocomplete_view);
		mCmdView = (AutoCompleteTextView) actionBar.getCustomView().findViewById(R.id.autocompleteview);

		configureCmdView();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
	}
	
	private void configureCmdView() {
		List<String> tagNames = new ArrayList<String>(mTagFileData.mTagGraph.getAllTagsAsStrings());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_dropdown_item_1line, tagNames);
		mCmdView.setAdapter(adapter);
		mCmdView.setThreshold(1);
		
	    mCmdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	        @Override
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	            if (actionId == EditorInfo.IME_ACTION_DONE) {
	            	handleActionCommand( mCmdView.getText().toString().trim() );
	            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(mCmdView.getWindowToken(), 0);
	                return true;
	            }
	            // hide keyboard after pressing enter
	            if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) && (actionId == KeyEvent.KEYCODE_ENTER) ) {               	            
	            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(mCmdView.getWindowToken(), 0);
	            	return true;
	            }
	            return false;
	        }
	    });
	    
	    mCmdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				String tagName = (String) parent.getItemAtPosition(position);
				handleActionCommand(tagName);
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(mCmdView.getWindowToken(), 0);
			}
		});
	    
	    
//	    mCmdView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//	        @Override
//	        public void onFocusChange(View v, boolean hasFocus) {
//	            if (hasFocus) {
//	                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//	            }
//	        }
//	    });
//	    mCmdView.requestFocus();
	}
	
	private void handleActionCommand(String command) {
		FileEntryList fel = mTagFileData.mTagGraph.getFilesFor(new Tag(command));
		if (fel.getEntries().size() > 0) {
			setEntries(fel);
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent prefsIntent = new Intent(TagBrowseApp.this, SettingsActivity.class);
			startActivity(prefsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onBackPressed() {     
        gotoParent();
    }

	public TagFileData getTagFileData() {
		return mTagFileData;
	}
	
	private void doFileAction(File file) {
		if (Utils.isProtected(file)) {
			String text = "file is protected!";
			Toast.makeText(this, text, Toast.LENGTH_LONG).show();
			return;
		}
		if (file.isDirectory()) {
			gotoDir(file);
		} else {
			openFile(file);
		}
	}
	
	 private void openFile(File file) {
         Intent intent = new Intent();
         intent.setAction(android.content.Intent.ACTION_VIEW);
         Uri uri = Uri.fromFile(file);
         String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
         intent.setDataAndType(uri, type == null ? "*/*" : type);
         if (intent.resolveActivity(getPackageManager()) != null) {
        	 startActivity(intent);
         }
	 }
	
	private void listContent(File dir) {
		if ( ! dir.isDirectory() || Utils.isProtected(dir)) {
            return;
		}
		System.out.println("listContent of" + dir.getAbsolutePath());
		new FileFinder(this).execute(dir);
	}
	
	public void refresh() {
		doFileAction(mTagFileData.mCurrentDir);
	}
	
	public void setEntries(FileEntryList fileList) {
		List<FileListEntry> entries = fileList.getEntries();
		mTagFileData.mFiles.clear();
		mTagFileData.mFiles.addAll(entries);
		mFileGridView.getAdapter().notifyDataSetChanged();
	}
	
	private void gotoParent() {
		File f = mTagFileData.mCurrentDir;
        if (Utils.isRoot(f)) {
        	gotoDir(f);
        } else {
        	if (f.isDirectory()) {
        		File parent = f.getParentFile(); 
                listContent(parent);
                if (parent != null) {
                	mTagFileData.mCurrentDir = parent;
                }
        	}
        }
	}
	
	private void gotoDir(File f) {
		if ( ! f.isDirectory()) {
			return;
		}
		listContent(f);
		mTagFileData.mCurrentDir = f;
	}
	
	public Button createTagButton(Tag tag) {
		Button tagButton = new Button(this);
		tagButton.setText(tag.getName());
		tagButton.setBackgroundColor(tag.getColor());
		final Tag myTag = tag;
		tagButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				setEntries(mTagFileData.mTagGraph.getFilesFor(myTag));
			}
		});
		return tagButton;
	}
	
	public TagView getControlTagView() {
		return mControlTagView;
	}
	
	public PreferenceWrapper getPrefWrapper() {
		return mPrefWrapper;
	}
	
	// update tag-autocompleteview adapter with latest tags
	public void updateCmdListAdapter() {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) mCmdView.getAdapter();
		adapter.clear();
		adapter.addAll(mTagFileData.mTagGraph.getAllTagsAsStrings());
		adapter.notifyDataSetChanged();
	}
}

