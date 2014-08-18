package com.example.tagbrowse.model;

import java.util.List;


public class FileEntryList {
	
	private List<FileListEntry> entries;
	private boolean isExcludeFromMedia = false;

	public FileEntryList(List<FileListEntry> children) {
		this.entries = children;
	}

	public List<FileListEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<FileListEntry> entries) {
		this.entries = entries;
	}

	public boolean isExcludeFromMedia() {
		return isExcludeFromMedia;
	}

	public void setExcludeFromMedia(boolean isExcludeFromMedia) {
		this.isExcludeFromMedia = isExcludeFromMedia;
	}
}
