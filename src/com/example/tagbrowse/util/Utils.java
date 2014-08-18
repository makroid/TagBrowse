package com.example.tagbrowse.util;

import java.io.File;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.example.tagbrowse.R; 

public class Utils {
	
	public static Drawable getIcon(Context mContext, File file) {
		if (file.isDirectory()) {
			return mContext.getResources().getDrawable(R.drawable.directory_icon);
		} else if (Utils.isMusic(file)) {
			return mContext.getResources().getDrawable(R.drawable.filetype_music);
		} else if (Utils.isVideo(file)) {
			return mContext.getResources().getDrawable(R.drawable.filetype_video);
		} else if (Utils.isPicture(file)) {
			return mContext.getResources().getDrawable(R.drawable.filetype_image);
		} else {
			return mContext.getResources().getDrawable(R.drawable.filetype_generic);
		}
	}
	
	public static boolean isProtected(File path) {
        return ( ! path.canRead() &&  ! path.canWrite());
    }
	
	static boolean isMusic(File file) {
		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

		if(type == null)
			return false;
		else
			return (type.toLowerCase().startsWith("audio/"));

	}

	static boolean isVideo(File file) {

		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

		if(type == null)
			return false;
		else
			return (type.toLowerCase().startsWith("video/"));
	}

	public static boolean isPicture(File file) {

		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

		if(type == null)
			return false;
		else
			return (type.toLowerCase().startsWith("image/"));
	}
	
	public static String breakString(String str, int charsPerLine) {
		String result = "";
		int start = 0;
		int len = str.length();
		
		while (start < len) {
			result += str.substring(start, Math.min(start + charsPerLine, len)) + System.getProperty("line.separator");
			start += charsPerLine;
		}
		return result;
	}
	
	public static int convertDpToPx(int dp, Context context) {
	    // Get the screen's density scale
	    final float scale = context.getResources().getDisplayMetrics().density;
	    // Convert the dps to pixels, based on density scale
	    return (int) (dp * scale + 0.5f);
	}
	
	public static boolean isRoot(File file) {       
        return file.isDirectory() && file.getAbsolutePath().equals("/");
	}
	
	
	
	public static String maxTextForWidth(TextView textView, int desiredWidth, int nrows) {
	    Paint paint = new Paint();	    

	    String finalText = "";
	    
	    paint.setTypeface(textView.getTypeface());
	    float textSize = textView.getTextSize();
	    paint.setTextSize(textSize);
	    String text = textView.getText().toString();	    
	    
	    int pos = 0;
	    	    
	    String em = " ";
		int w  = (int) Math.ceil(paint.measureText(em));
		System.out.println("space width=" + w);
	    
	    for (int i=0; i<nrows; i++) {
	    	String line = "";
	    	int lineWidth = 0;
	    	
	    	while (true) {
	    		String nextChar = "\u0020";
	    		if (pos < text.length()) {
	    			nextChar = text.substring(pos, pos+1);
	    		} 
	    		++pos;
	    		
	    		int b1 = (int) Math.ceil(paint.measureText(nextChar));
	    		
	    		if (lineWidth + b1 < desiredWidth) {
	    			line += nextChar;
	    			lineWidth = (int) Math.ceil(paint.measureText(line));
	    		} else {
	    			break;
	    		}
	    	}	 
	    	finalText += line;
	    }
	    
	    return finalText;
	}
}
