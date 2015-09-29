package de.uni_koeln.spinfo.drc.webapp;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.drc.search.Indexer;
import de.uni_koeln.spinfo.drc.util.FileHelper;

@Service
public class Appinitializer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	Configuration config;

	@Autowired
	private FileHelper fileHelper;

	@Autowired
	private Indexer indexer;

	@PostConstruct
	public void postConstruct() throws Exception {
		logger.info("Reading meta files:");
		fileHelper.initMappings();
		logger.info("Page mappings created.");

		File index = new File(config.getIndexDir());
		if (!index.exists()) {
			logger.info("Building Index");
			buildIndex(config.getIndexDir());
		} else {
			logger.info("Using existing Index");
		}
	}

	private void buildIndex(String indexDir) throws IOException, Exception {
		long start = System.currentTimeMillis();
		indexer.init(indexDir);
		int numIndexed = indexer.index(config.getData());
		indexer.close();
		long end = System.currentTimeMillis();
		logger.info("Indexing took " + (end - start) + " ms for " + numIndexed
				+ " files.");
	}

}
