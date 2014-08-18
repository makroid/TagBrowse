package com.example.tagbrowse.util;

import java.util.Comparator;

import com.example.tagbrowse.model.FileListEntry;

public class FileListSorter implements Comparator<FileListEntry> {

	@Override
	public int compare(FileListEntry fle1, FileListEntry fle2) {
		
		if(fle1.getFile().isDirectory() && fle2.getFile().isFile()) {
                return -1;
        }
        else if(fle2.getFile().isDirectory() && fle1.getFile().isFile()) {
                return 1;
        }
		
		return fle1.getName().compareToIgnoreCase(fle2.getName());
	}
	
}
