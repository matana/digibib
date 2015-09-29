package de.uni_koeln.spinfo.drc.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class Configuration {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResourceLoader resourceLoader;

	private Properties properties;

	private static final String TO_INDEX = "xml.files";

	private static final String META = "meta.files";

	private static final String INDEX = "lucene.index";
	
	private static final String TEI = "tei.header";

	private static final Version LUCENE_VERSION = Version.LUCENE_42;

	@PostConstruct
	public void init() {
		String propertiesFile = System.getProperty("drc.xml.viewer.properties");
		try {
			if (propertiesFile == null) {
				loadProperties(resourceLoader.getResource(
						"/config/app.properties").getFile());
			} else {
				loadProperties(new File(propertiesFile));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadProperties(File file) throws IOException {
		logger.info("Loading properties from file " + file.getName());
		properties = new Properties();
		FileInputStream fis = new FileInputStream(file);
		properties.load(fis);
		fis.close();
	}

	public String getData() {
		return properties.getProperty(TO_INDEX);
	}

	public String getMeta() {
		return properties.getProperty(META);
	}

	public String getIndexDir() {
		return properties.getProperty(INDEX);
	}
	
	public String getTei(){
		return properties.getProperty(TEI);
	}

	public Version getLuceneVersion() {
		return LUCENE_VERSION;
	}

}
