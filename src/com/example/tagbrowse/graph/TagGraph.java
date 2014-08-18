package com.example.tagbrowse.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.tagbrowse.model.*;
import com.example.tagbrowse.util.FileListSorter;

public class TagGraph {
	private HashMap<Tag, HashSet<FileListEntry>> tag2file;
	private HashMap<FileListEntry, HashSet<Tag>> file2tag;
	
	private HashSet<Tag> empty = new HashSet<Tag>();
	
	public TagGraph() {
		tag2file = new HashMap<Tag, HashSet<FileListEntry>>();
		file2tag = new HashMap<FileListEntry, HashSet<Tag>>();
	}
	
	public HashSet<Tag> getTagsFor(FileListEntry fle) {
		if (file2tag.containsKey(fle)) {
			return file2tag.get(fle);
		} else {
			return empty;
		}
	}
	
	public FileEntryList getFilesFor(Tag tag) {
		FileEntryList entryList = new FileEntryList(new ArrayList<FileListEntry>());
		if (tag2file.containsKey(tag)) {
			ArrayList<FileListEntry> l = new ArrayList<FileListEntry>(tag2file.get(tag));
			Collections.sort(l, new FileListSorter());
			entryList.setEntries(l);
		}
		return entryList;
	}
	
	public List<String> getAllTagsAsStrings() {
		ArrayList<String> l = new ArrayList<String>();
		for (Tag tag : tag2file.keySet()) {
			l.add(tag.getName());
		}
		return l;
	}
	
	public HashSet<TagWithCheck> getAllTagsAndSelectedFor(FileListEntry file) {
		HashSet<TagWithCheck> allTags = new HashSet<TagWithCheck>();
		if (file == null) {
			return allTags;
		}
		HashSet<Tag> fileTags = file2tag.get(file);
		
		for (Tag tag : tag2file.keySet()) {
			boolean check = (fileTags != null) && fileTags.contains(tag);
			TagWithCheck twC = new TagWithCheck(tag, check); 
			allTags.add(twC);
		}
		return allTags;
	}
	
	public boolean addTag(Tag tag, FileListEntry fle) {
		boolean result = false;
		
		if (tag2file.containsKey(tag)) {
			result = tag2file.get(tag).add(fle);
			System.out.println("has Tag: " + tag.getName());
		} else {
			tag2file.put(tag, new HashSet<FileListEntry>());
			tag2file.get(tag).add(fle);
			result = true;
			System.out.println("has NOT tag: " + tag.getName());
		}
		if (file2tag.containsKey(fle)) {
			System.out.println("contains file: " + fle.getName());
			return file2tag.get(fle).add(tag)  && result;		
		} else {
			System.out.println("contains NOT file: " + fle.getName());
			file2tag.put(fle, new HashSet<Tag>());
			file2tag.get(fle).add(tag);
			return result;
		}		
	}
	
	public boolean removeTag(Tag tag, FileListEntry fle) {
		boolean result = false;
		
		if (tag2file.containsKey(tag)) {
			result = tag2file.get(tag).remove(fle);
			if (tag2file.get(tag).isEmpty()) {
				tag2file.remove(tag);
			}
		}
		if (file2tag.containsKey(fle)) {
			result = file2tag.get(fle).remove(tag);
			if (file2tag.get(fle).isEmpty()) {
				file2tag.remove(fle);
			}
		}
		
		return result;
	}
	
	public HashMap<Tag, HashSet<FileListEntry>> getTag2FileMap() {
		return tag2file;
	}
}
