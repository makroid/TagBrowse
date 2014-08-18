package com.example.tagbrowse.model;

import java.io.File;
import java.util.Date;

public class FileListEntry {

	private File mFile;
	private String mName;
	private long mSize;
//	private Date mLastModified;

	public FileListEntry(String dirbase) {
		mFile = new File(dirbase);
		mName = mFile.getName();
		mSize = 0;
	}

	public FileListEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public File getFile() {
		return mFile;
	}
	
	public void setFile(File path) {
		this.mFile = path;
	}

	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}

	public long getSize() {
		return mSize;
	}

	public void setSize(long size) {
		this.mSize = size;
	}

//	public Date getLastModified() {
//		return mLastModified;
//	}
//
//	public void setLastModified(Date lastModified) {
//		this.mLastModified = lastModified;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((mFile == null) ? 0 : mFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileListEntry other = (FileListEntry) obj;
		if (mFile == null) {
			if (other.mFile != null)
				return false;
		} else if (!mFile.equals(other.mFile))
			return false;
		return true;
	} 

}