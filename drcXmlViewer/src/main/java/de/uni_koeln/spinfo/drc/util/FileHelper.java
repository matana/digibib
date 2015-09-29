package de.uni_koeln.spinfo.drc.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uni_koeln.spinfo.drc.webapp.Configuration;

@Service
public class FileHelper {

	@Autowired
	DRCvolumeMappings volMapping;

	@Autowired
	DRCpageAndChapterMappings pageAndChapterMapping;

	@Autowired
	Configuration config;

	Logger logger = LoggerFactory.getLogger(getClass());

	private Pattern pattern;

	public FileHelper() {
		pattern = Pattern.compile("<lb/>");
	}

	public List<String> listVolumeNames() {
		return volMapping.listVolumeNames();
	}

	public String fileName(String displayedName) {
		return volMapping.fileName(displayedName);
	}

	public String getDisplayName(String fileName) {
		return volMapping.displayName(fileName);
	}

	/**
	 * @param volume
	 * @param page
	 * @return filename for given pagination
	 */
	public String pageToFilename(String volume, String page) {
		return pageAndChapterMapping.fileName(volume, page);
	}

	/**
	 * @param volume
	 * @param filename
	 * @return pagination for given filename
	 */
	public String filenameToPage(String volume, String filename) {
		return pageAndChapterMapping.pageNumber(volume, filename);
	}

	public List<String> listXml(final String fileName) {
		String pathToFile = createPath(fileName);
		File file = createFile(pathToFile);
		File[] listFiles = file.listFiles();
		List<String> toReturn = new ArrayList<String>();
		for (File f : listFiles) {
			if (f.getAbsolutePath().endsWith(".xml") && f.exists())
				toReturn.add(f.getName());
		}
		Collections.sort(toReturn);
		return toReturn;
	}

	public String readXml(final String volume, final String xml) {
		String pathToFile = createPath(volume, xml);
		File file = createFile(pathToFile);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			String replaceAll = pattern.matcher(sb.toString()).replaceAll(
					"<br>");
			return replaceAll;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String readTEIXml(final String volume, final String xml) {
		String out = readXml(volume, xml);
		String teiHeader = readFile(getTeiDir() + "tei-header.xml");

		out = out.replaceAll("\t", "");
		out = out.replaceAll("<br>", "==BR==");
		out = stripTags(out);
		
		teiHeader = teiHeader.replaceAll("<!--CONTENT-->", out);
		teiHeader = teiHeader.replaceAll("<!--PAGE COUNT-->", "1"); //TODO!
		
		teiHeader = teiHeader.replaceAll("<", "&lt;");
		teiHeader = teiHeader.replaceAll(">", "&gt;");
		teiHeader = teiHeader.replaceAll("==BR==", "&lt;lb/&gt;<br>");//display <lb/> in tab

		return "<div style=\"font-family:monospace;\">"
				+ teiHeader + "</div>";  
	}
	
	private String stripTags(String xmlCode){
		xmlCode = xmlCode.replaceAll("(<.*?>)", "");
		xmlCode = xmlCode.replaceAll("(</.*?>)", "");
		return xmlCode;
	}
	
	private String readFile(final String filePath) {
		File file = createFile(filePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public File createFile(final String path) {
		return new File(path);
	}

	public String createPath(final String volume) {
		return getBaseDir() + volume;
	}

	public String createPath(final String volume, final String xml) {
		return getBaseDir() + volume + "/" + xml;
	}

	public String createPathToMetaFile(final String volume) {
		return getMetaDir() + volume + ".xml";
	}

	private String getBaseDir() {
		return config.getData();
	}

	private String getMetaDir() {
		return config.getMeta();
	}
	
	private String getTeiDir(){
		return config.getTei();
	}

	public List<String> listPageNumbers(final String volume) throws Exception {

		List<String> toReturn = new ArrayList<String>();
		LinkedHashMap<String, String> pageMappings = pageAndChapterMapping
				.getPageMappings(volume);
		LinkedHashMap<String, String> filenameMappings = pageAndChapterMapping
				.getFilenameMappings(volume);

		if (pageMappings == null && filenameMappings == null) {
			pageMappings = new LinkedHashMap<String, String>();
			filenameMappings = new LinkedHashMap<String, String>();
		} else {
			toReturn = new ArrayList<String>(pageMappings.keySet());
			return toReturn;
		}

		String pathToFile = createPathToMetaFile(volume);
		String xml = readMeta(pathToFile);
		Document d = loadXMLFromString(xml);
		Node first = d.getFirstChild();
		NodeList nl = first.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node content = nl.item(i);
			if (content.getNodeName() == "content") {
				NodeList nl2 = content.getChildNodes();

				for (int j = 0; j < nl2.getLength(); j++) {
					Node pages = nl2.item(j);
					if (pages.getNodeName() == "pages") {

						NodeList nl3 = pages.getChildNodes();
						for (int k = 0; k < nl3.getLength(); k++) {
							Node page = nl3.item(k);
							if (page.getNodeName() == "page") {
								String id = page.getAttributes()
										.getNamedItem("id").getNodeValue();
								String physId = page.getAttributes()
										.getNamedItem("physid").getNodeValue();
								toReturn.add(physId);
								pageMappings.put(physId, (id + ".xml"));
								filenameMappings.put(id, physId);
							}
						}
					}
				}
			}
		}
		// logger.info("created pagemapping for " + volume);
		pageAndChapterMapping.setPageMapping(volume, pageMappings);
		pageAndChapterMapping.setFilenameMapping(volume, filenameMappings);
		return toReturn;
	}

	public List<String> listChapters(final String volume) throws Exception {

		List<String> toReturn = new ArrayList<String>();
		LinkedHashMap<String, List<String>> chapterMappings = pageAndChapterMapping
				.getChapterMappings(volume);
		if (chapterMappings == null) {
			chapterMappings = new LinkedHashMap<String, List<String>>();
		} else {
			toReturn = new ArrayList<String>(chapterMappings.keySet());
			return toReturn;
		}

		String pathToFile = createPathToMetaFile(volume);
		String xml = readMeta(pathToFile);

		Document d = loadXMLFromString(xml);
		Node first = d.getFirstChild();

		NodeList nl = first.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node content = nl.item(i);
			if (content.getNodeName() == "content") {

				NodeList nl2 = content.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node chapters = nl2.item(j);

					if (chapters.getNodeName() == "chapters") {

						NodeList nl3 = chapters.getChildNodes();
						for (int k = 0; k < nl3.getLength(); k++) {
							Node page = nl3.item(k);
							if (page.getNodeName() == "chapter") {
								List<String> chapterPages = new ArrayList<String>();
								ArrayList<String> list = new ArrayList<String>(
										pageAndChapterMapping.getPageMappings(
												volume).keySet());

								String begin = page.getAttributes()
										.getNamedItem("begin").getNodeValue();
								String end = page.getAttributes()
										.getNamedItem("end").getNodeValue();
								String id = page.getAttributes()
										.getNamedItem("id").getNodeValue();
								String title = page.getAttributes()
										.getNamedItem("title").getNodeValue();

								int startIndex = list
										.indexOf(pageAndChapterMapping
												.pageNumber(volume, begin));
								int endIndex = list
										.indexOf(pageAndChapterMapping
												.pageNumber(volume, end));

								for (int l = startIndex; l <= endIndex; l++) {
									chapterPages.add(list.get(l));
									chapterMappings.put((id + " - " + title),
											chapterPages);
								}
								toReturn.add(id + " - " + title);
							}
						}
					}
				}
			}
		}
		// logger.info("created chaptermapping for " + volume);
		pageAndChapterMapping.setChapterMapping(volume, chapterMappings);
		return toReturn;
	}

	private String readMeta(String pathToFile) {
		File file = createFile(pathToFile);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			String replaceAll = pattern.matcher(sb.toString()).replaceAll(
					"<br>");
			return replaceAll;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

	public List<String> listPageNumbersForChapter(String volume, String chapter) {
		return pageAndChapterMapping.getPageNumbersForChapter(volume, chapter);
	}

	public String firstPageOfChapter(String volume, String chapter) {
		return pageAndChapterMapping.getPageNumbersForChapter(volume, chapter)
				.get(0);
	}

	public String getChapterForPage(String volume, String pageAsDisplayed) {

		LinkedHashMap<String, List<String>> chapterMappings = pageAndChapterMapping
				.getChapterMappings(volume);
		for (String chapter : chapterMappings.keySet()) {
			if (chapterMappings.get(chapter).contains(pageAsDisplayed)) {
				return chapter;
			}
		}
		logger.error("No chapter mapping found!");
		return "";
	}

	public void initMappings() throws Exception {
		List<String> volumeNames = listVolumeNames();
		for (String volumeName : volumeNames) {
			String volume = fileName(volumeName);
			logger.info(volume);
			listPageNumbers(volume);
			listChapters(volume);
		}
	}

	public Map<String, String> resultMap(List<String> resultList) {
		Map<String, String> result = new HashMap<String, String>(
				resultList.size());

		String volume, volumeName, pageNumber;
		StringBuilder toDisplay;

		for (String hit : resultList) {

			volume = hit.split("-")[0];
			volumeName = getDisplayName(volume);
			pageNumber = filenameToPage(volume, hit.split("\\.")[0]);

			toDisplay = new StringBuilder("Band ");
			toDisplay.append(volumeName.split("-")[0]);
			toDisplay.append(", ");
			toDisplay.append("Seite ");
			toDisplay.append(pageNumber);

			result.put(hit, toDisplay.toString());
		}
		return result;
	}

	public Map<String, String> convertResultMap(Map<String, String> map) {

		Map<String, String> result = new HashMap<String, String>(map.size());

		String volume, volumeName, pageNumber;
		StringBuilder toDisplay;

		for (String hit : map.keySet()) {

			volume = hit.split("-")[0];
			volumeName = getDisplayName(volume);
			pageNumber = filenameToPage(volume, hit.split("\\.")[0]);

			toDisplay = new StringBuilder();
			toDisplay.append(volumeName.split("-")[0]);
			toDisplay.append("-");
			toDisplay.append(pageNumber);
			toDisplay.append("-");
			toDisplay.append(volume);

			result.put(toDisplay.toString(), map.get(hit));
		}
		return result;
	}

}
