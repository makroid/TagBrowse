package com.example.tagbrowse.graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;

import com.example.tagbrowse.model.FileListEntry;
import com.example.tagbrowse.model.Tag;

import android.util.JsonReader;
import android.util.JsonWriter;

/*
 * The file has the following structure:
 * 	[
 *		{
 *			"tag":{
 *					"name":"abc",
 *					"priority":0.1,
 *					"color":20202
 *				  }
 *			"files":[
 *				{
 *					"name":"/home/d/data",
 *					"size":10
 *				},
 *				{	
 *					"name":"/home/pics/test.png",
 *					"size":202
 *				}
 *			]
 *		},
 *		{
 *			"tag":{
 *				"name": ...
 *			      }
 *			"files":[
 *				....
 *			]
 *		}
 *	]
 */

public class JSONTagGraphIO {
	
	/*
	 * Writer functions
	 */
	
	public static void writeJsonStream(OutputStream out, TagGraph tagGraph) throws IOException {
	     JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	     writer.setIndent("  ");
	     writeTagGraph(writer, tagGraph);
	     writer.close();
	}
	
	public static void writeTagGraph(JsonWriter writer, TagGraph tagGraph) throws IOException {
		 writer.beginArray();
	     for (Map.Entry<Tag,HashSet<FileListEntry>> entry : tagGraph.getTag2FileMap().entrySet()) {
	       writeEntry(writer, entry);
	     }
	     writer.endArray();
	}
	
	public static void writeEntry(JsonWriter writer, Map.Entry<Tag, HashSet<FileListEntry>> entry) throws IOException {
		writer.beginObject();
		writeTag(writer, entry.getKey());
		writer.name("files");
		writer.beginArray();
		for (FileListEntry file: entry.getValue()) {
			writeFile(writer, file);
		}
		writer.endArray();
		writer.endObject();		
	}
	
	public static void writeTag(JsonWriter writer, Tag tag) throws IOException {
		writer.name("tag");
		writer.beginObject();
		writer.name("name").value(tag.getName());
		writer.name("priority").value(tag.getPriority());
		writer.name("color").value(tag.getColor());
		writer.endObject();
	}
	
	public static void writeFile(JsonWriter writer, FileListEntry file) throws IOException {
		writer.beginObject();
		writer.name("name").value(file.getFile().getAbsolutePath());
		writer.name("size").value(file.getSize());
		writer.endObject();
	}
	
	
	/*
	 * Reader functions
	 */
	
	public static void readJsonStream(InputStream in, TagGraph tagGraph) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		readEntryArray(reader, tagGraph);
		reader.close();
	}
	
	public static void readEntryArray(JsonReader reader, TagGraph tagGraph) throws IOException {
		reader.beginArray();
		while (reader.hasNext()) {
			readEntry(reader, tagGraph);
		}
		reader.endArray();
	}
	
	public static void readEntry(JsonReader reader, TagGraph tagGraph) throws IOException {		
		Tag tag = null;
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("tag")) {
				tag = readTag(reader);
			} else if (name.equals("files")) {
				readFiles(reader, tag, tagGraph);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	public static Tag readTag(JsonReader reader) throws IOException {
		String tagName = null;
		float priority = 0;
		int color = 0;
		
		reader.beginObject();
	    while (reader.hasNext()) {
	       String name = reader.nextName();
	       if (name.equals("name")) {
	    	   tagName = reader.nextString();
	       } else if (name.equals("priority")) {
	    	   priority = (float) reader.nextDouble();
	       } else if (name.equals("color")) {
	    	   color = reader.nextInt();
	       } else {
	    	   reader.skipValue();
	       }
	    }
	    reader.endObject();
	    return new Tag(tagName, priority, color);
	}
	
	public static void readFiles(JsonReader reader, Tag tag, TagGraph tagGraph) throws IOException {
		reader.beginArray();
		while (reader.hasNext()) {
			FileListEntry file = readFile(reader);
			tagGraph.addTag(tag, file);
		}
		reader.endArray();
	}
	
	public static FileListEntry readFile(JsonReader reader) throws IOException {
		String fileName = null;
		int fileSize = 0;

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("name")) {
				fileName = reader.nextString();
			} else if (name.equals("size")) {
				fileSize = reader.nextInt();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return new FileListEntry(fileName);
	}
}
