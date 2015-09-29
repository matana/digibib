package de.uni_koeln.spinfo.drc.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.drc.webapp.Configuration;

@Service
public class Indexer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	Configuration config;

	private IndexWriter writer;

	@Test
	public void test() throws IOException {
		String indexDir = config.getIndexDir();
		String data = config.getData();
		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer();
		indexer.init(indexDir);
		try {
			int numIndexed = indexer.index(data);
			indexer.close();
			long end = System.currentTimeMillis();
			logger.info("Indexing took " + (end - start) + " ms for " + numIndexed + " files.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Indexer() {
		// default constructor (required by Spring)
	}

	public void init(final String indexDir) throws IOException {
		// Directory repräsentiert den Speicherort des Indexes
		Directory dir = new SimpleFSDirectory(new File(indexDir));
		StandardAnalyzer analyzer = new StandardAnalyzer(
				config.getLuceneVersion());
		IndexWriterConfig writerConfig = new IndexWriterConfig(
				config.getLuceneVersion(), analyzer);
		writer = new IndexWriter(dir, writerConfig);
	}

	public int index(String source) throws Exception {
		File dataDir = new File(source);
		dataDir.mkdirs(); // For the first time should be called..

		File[] subDirs = dataDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});

		logger.info(dataDir.getName() + " > Anzahl Verzeichnisse=" + subDirs.length);
		for (File dir : subDirs) {
			dir.mkdirs(); // For the first time should be called..
			File[] files = dir.listFiles();
			logger.info(dir.getName() + " > Anzahl Dateien=" + files.length);
			FileFilter filter = getFileFilter();
			for (File f : files) {
				// Filtern der zu indexierenden Dateien (nicht rekursiv)
				if (filter.accept(f)) {
					indexFile(f);
				}
			}
		}
		// Gibt die Anzahl der indexierten Dateien zurrück.
		return writer.numDocs();
	}

	private FileFilter getFileFilter() {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return !f.isDirectory() && !f.isHidden() && f.exists()
						&& f.canRead() && f.getName().endsWith(".xml");
			}
		};
		return filter;
	}

	private Document getDocument(File f) throws Exception {
		Document doc = new Document();
		BufferedReader br = new BufferedReader(new FileReader(f));

		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();

		// Dem Dokument werden Felder übergeben. Der erste Parameter des Feldes
		// ist der Name des Feldes (Schlüssel), der zweite ein konkreter Wert.
		// Der dritte Parameter legt fest, ob der Originalwert mit indexiert
		// wird, d.h. ohne einen Analyzer zu gebrauchen

		doc.add(new TextField("contents", sb.toString(), Store.YES));
		doc.add(new StringField("filename", f.getName(), Store.YES));

		return doc;
	}

	private void indexFile(File f) throws Exception {
		//logger.info("Indexing " + f.getCanonicalPath());
		Document doc = getDocument(f);
		if (doc != null) {
			writer.addDocument(doc);
		}
	}

	public void close() throws IOException {
		writer.close();
	}

}
