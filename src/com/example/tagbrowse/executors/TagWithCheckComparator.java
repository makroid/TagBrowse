package com.example.tagbrowse.executors;

import java.util.Comparator;

import com.example.tagbrowse.model.TagWithCheck;

public class TagWithCheckComparator implements Comparator<TagWithCheck> {
	
	/*
	 * rules:
	 * sort all tags that are check in front
	 * sort all checked tags wrt priority
	 * sort all unchecked tags wrt alphabet
	 */
	
	@Override
    public int compare(TagWithCheck t1, TagWithCheck t2) {
        if (t1.equals(t2)) {
        	return 0;
        }
        if (t1.mHasTag && ( ! t2.mHasTag)) {
        	return -1;
        }
        else if (( ! t1.mHasTag) && t2.mHasTag) {
        	return 1;
        }
        else if (t1.mHasTag && t2.mHasTag) {
        	return (t1.mTag.getPriority() > t2.mTag.getPriority()) ?  -1 : 1;
        }
        else {
        	return  t1.mTag.getName().compareTo(t2.mTag.getName());
        }
    }
}
