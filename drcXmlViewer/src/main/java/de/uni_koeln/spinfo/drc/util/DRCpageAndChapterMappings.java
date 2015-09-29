package de.uni_koeln.spinfo.drc.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DRCpageAndChapterMappings {

	// vol -> (page -> filename)
	private static Map<String, LinkedHashMap<String, String>> pageMappings = 
			new HashMap<String, LinkedHashMap<String, String>>();
	
	// vol -> (filename -> page)
	private static Map<String, LinkedHashMap<String, String>> filenameMappings = 
			new HashMap<String, LinkedHashMap<String, String>>();

	// vol -> (chapter -> pages)
	private static Map<String, LinkedHashMap<String, List<String>>> chapterMappings = 
			new HashMap<String, LinkedHashMap<String, List<String>>>();

	
	public String fileName(String volume, String page) {
		return pageMappings.get(volume).get(page);
	}
	
	public String pageNumber(String volume, String filename) {
		return filenameMappings.get(volume).get(filename);
	}

	
	public void setPageMapping(String volume,
			LinkedHashMap<String, String> pageMappings2) {
		pageMappings.put(volume, pageMappings2);
	}

	public LinkedHashMap<String, String> getPageMappings(String volume) {
		return pageMappings.get(volume);
	}

	public void setFilenameMapping(String volume,
			LinkedHashMap<String, String> filenameMappings2) {
		filenameMappings.put(volume, filenameMappings2);
	}

	public LinkedHashMap<String, String> getFilenameMappings(String volume) {
		return filenameMappings.get(volume);
	}

	public void setChapterMapping(String volume,
			LinkedHashMap<String, List<String>> chapterMappings2) {
		chapterMappings.put(volume, chapterMappings2);
	}

	public LinkedHashMap<String, List<String>> getChapterMappings(String volume) {
		return chapterMappings.get(volume);
	}

	public List<String> getPageNumbersForChapter(String volume, String chapter) {
		return chapterMappings.get(volume).get(chapter);
	}

}
