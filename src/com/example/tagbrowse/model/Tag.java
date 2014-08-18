package com.example.tagbrowse.model;

import android.graphics.Color;

public class Tag {
	private String mName;
	float mPriority;
	int mColor;
	
	public Tag(String name) {
		this.mName     = name;
		this.mPriority = 0;
		this.mColor    = Color.GRAY; 
	}
	
	public Tag(String name, float priority, int color) {
		this.mName = name;
		this.mPriority = priority;
		this.mColor = color;
	}
	
	public String getName() {
		return mName;
	}
	
	public float getPriority() {
		return mPriority;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public void setPriority(float priority) {
		this.mPriority = priority;
	}
	
	public int getColor() {
		return mColor;
	}
	
	public void setColor(int color) {
		mColor = color;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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
		Tag other = (Tag) obj;
		if (mName == null) {
			if (other.getName() != null)
				return false;
		} else if (!mName.equals(other.getName()))
			return false;
		return true;
	} 
}
