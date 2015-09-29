package de.uni_koeln.spinfo.drc.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.spinfo.drc.search.Searcher;
import de.uni_koeln.spinfo.drc.util.FileHelper;

@Controller
public class MainController {

	@Autowired
	private FileHelper fileHelper;

	@Autowired
	private Searcher searcher;

	/**
	 * Size of a byte buffer to read/write file
	 */
	private static final int BUFFER_SIZE = 4096;

	Logger logger = LoggerFactory.getLogger(getClass());

	public MainController() {
		logger.info("MainController initialized...");
	}

	@RequestMapping(value = "/")
	public ModelAndView init() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/volumeSelectionSomething")
	public @ResponseBody
	String getSomethingElse() {
		return "Hintergrundinfos zur Biblioteca Digitala...";
	}

	@RequestMapping(value = "/about.html")
	public @ResponseBody
	ModelAndView getInfo() {
		ModelAndView mv = new ModelAndView("about");
		return mv;
	}

	@RequestMapping(value = "/contact.html")
	public @ResponseBody
	ModelAndView getContacts() {
		ModelAndView mv = new ModelAndView("contact");
		return mv;
	}

	@RequestMapping(value = "/search")
	public ModelAndView search(
			@RequestParam("search") String searchPhrase,
			@RequestParam(value = "resultPage", defaultValue = "1") int resultPage,
			HttpServletResponse response) {

		logger.info("search=" + searchPhrase);
		logger.info("resultPage=" + resultPage);

		int hitsPerPage = 10;
		int offset = (resultPage - 1) * hitsPerPage;
		Map<String, String> map = null;
		Map<String, String> displayNames = null;
		int totalHits = 0;
		try {
			map = searcher.search("index", searchPhrase, offset);
			totalHits = searcher.getTotalHits();
			// convert filenames to display names:
			if (map != null) {
				displayNames = fileHelper.convertResultMap(map);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView("search");
		mv.addObject("searchPhrase", searchPhrase);
		mv.addObject("result", displayNames);
		mv.addObject("resultPage", resultPage);
		mv.addObject("totalHits", totalHits);
		mv.addObject("offset", offset);
		return mv;
	}

	@RequestMapping(value = "/drc", method = RequestMethod.GET)
	public ModelAndView getVolumes() {
		ModelAndView mv = new ModelAndView("volumes");
		mv.addObject("volumes", fileHelper.listVolumeNames());
		return mv;
	}

	@RequestMapping(value = "/volumeSelected", method = RequestMethod.GET)
	public ModelAndView getXmlForVolume(
			@RequestParam("volume") String volumeName,
			HttpServletResponse response) throws Exception {

		String volume = fileHelper.fileName(volumeName);
		logger.info("selected volume " + volumeName + "/" + volume);

		String chapterSelected = fileHelper.listChapters(volume).get(0);
		String firstPage = fileHelper.firstPageOfChapter(volume,
				chapterSelected);
		String xml = fileHelper.pageToFilename(volume, firstPage);
		String toDisplay = fileHelper.readXml(volume, xml);
		String xmlDisplay = fileHelper.readTEIXml(volume, xml);

		ModelAndView mv = new ModelAndView("chapters");
		mv.addObject("volume", volume);
		mv.addObject("volumeName", fileHelper.getDisplayName(volume));
		mv.addObject("chapterSelected", chapterSelected);
		mv.addObject("pageNumbers", fileHelper.listPageNumbers(volume));
		mv.addObject("chapters", fileHelper.listChapters(volume));
		mv.addObject("xmls", fileHelper.listXml(volume));
		mv.addObject("toDisplay", toDisplay);
		mv.addObject("xmlDisplay", xmlDisplay);
		mv.addObject("pageNumber", firstPage);
		mv.addObject("selected", xml);
		return mv;
	}

	@RequestMapping(value = "/chapterSelected", method = RequestMethod.GET)
	public ModelAndView getXmlForChapter(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String volume = request.getParameter("volume");
		String chapterSelected = request.getParameter("chapter");
		logger.info("selected chapter " + chapterSelected + "/" + volume);

		String firstPage = fileHelper.firstPageOfChapter(volume,
				chapterSelected);
		String xml = fileHelper.pageToFilename(volume, firstPage);
		String toDisplay = fileHelper.readXml(volume, xml);
		String xmlDisplay = fileHelper.readTEIXml(volume, xml);

		ModelAndView mv = new ModelAndView("chapters");
		mv.addObject("volume", volume);
		mv.addObject("volumeName", fileHelper.getDisplayName(volume));
		mv.addObject("chapterSelected", chapterSelected);
		mv.addObject("pageNumbers", fileHelper.listPageNumbers(volume));
		mv.addObject("chapters", fileHelper.listChapters(volume));
		mv.addObject("xmls", fileHelper.listXml(volume));
		mv.addObject("toDisplay", toDisplay);
		mv.addObject("xmlDisplay", xmlDisplay);
		mv.addObject("pageNumber", firstPage);
		mv.addObject("selected", xml);
		return mv;
	}

	@RequestMapping(value = "/xmlSelected", method = RequestMethod.GET)
	public ModelAndView getXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String lastSearch = request.getParameter("lastSearch");
		String toHighlight = request.getParameter("toHighlight");
		String volume = request.getParameter("volume");
		String pageAsDisplayed = request.getParameter("pageNumber");
		// logger.info("selected page " + pageAsDisplayed + "/" + volume);

		String xml = fileHelper.pageToFilename(volume, pageAsDisplayed);
		String toDisplay = fileHelper.readXml(volume, xml);
		String xmlDisplay = fileHelper.readTEIXml(volume, xml);

		if (toHighlight != null) {
			toDisplay = searcher.highlightFullPage(toHighlight, toDisplay);
		}
		String chapterSelected = fileHelper.getChapterForPage(volume,
				pageAsDisplayed);

		ModelAndView mv = new ModelAndView("xmls");
		mv.addObject("volume", volume);
		mv.addObject("volumeName", fileHelper.getDisplayName(volume));
		mv.addObject("chapterSelected", chapterSelected);
		mv.addObject("pageNumbers", fileHelper.listPageNumbers(volume));
		mv.addObject("chapters", fileHelper.listChapters(volume));
		mv.addObject("xmls", fileHelper.listXml(volume));
		mv.addObject("toDisplay", toDisplay);
		mv.addObject("xmlDisplay", xmlDisplay);
		mv.addObject("pageNumber", pageAsDisplayed);
		mv.addObject("selected", xml);
		mv.addObject("lastSearch", lastSearch);
		return mv;
	}

	@RequestMapping(value = "/downloadXml", method = RequestMethod.GET)
	public void downloadXml(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String xml = request.getParameter("xmls");
		if (xml == null)
			return;

		String volume = xml.split("-")[0];
		File downloadFile = fileHelper.createFile(fileHelper.createPath(volume,
				xml));
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = request.getContentType();
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		logger.info("downloading " + downloadFile.getCanonicalPath()
				+ ", MIME type: " + mimeType);

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();
	}

	@RequestMapping("/showxml/{volume}/{page}")
	public ModelAndView showxml(@PathVariable("volume") String volume,
			@PathVariable("page") String xml) {
		if (!xml.endsWith(".xml")) {
			xml = xml + ".xml";
		}
		List<String> allXml = fileHelper.listXml(volume);
		String content = fileHelper.readXml(volume, xml);
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("content", content);
		mv.addObject("volume", volume);
		mv.addObject("current", xml);
		mv.addObject("allXml", allXml);
		return mv;
	}

}
