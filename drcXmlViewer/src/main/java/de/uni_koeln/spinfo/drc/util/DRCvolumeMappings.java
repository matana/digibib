package de.uni_koeln.spinfo.drc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DRCvolumeMappings {

	private static List<String> volNames = new ArrayList<String>();
	private static Map<String, String> idToName = new HashMap<String, String>();
	private static Map<String, String> nameToId = new HashMap<String, String>();

	/** RF labels, ordered by Octopus naming */
	static {
		volNames.add("I - Surselvisch, Subselvisch, Sursettisch. 1. Lieferung: Das XVII. Jahrhundert.");
		volNames.add("I - 2. und 3. Lieferung: Das XVIII. und XIX. Jahrhundert.");
		volNames.add("II - Surselvisch, Subselvisch. 1. Lieferung.");
		volNames.add("II - 2. und 3. Lieferung.");
		volNames.add("III - Surselvisch, Subselvisch. Die Weisen der Volkslieder.");
		volNames.add("IV - Surselvisch, Subselvisch, Volksmedizin, Nachträge.");
		volNames.add("V - Oberengadinisch, Unterengadinisch. Das XVI. Jahrhundert.");
		volNames.add("VI - Oberengadinisch, Unterengadinisch. Das XVII. Jahrhundert.");
		volNames.add("VII - Oberengadinisch, Unterengadinisch. Das XVIII. Jahrhundert.");
		volNames.add("VIII - Oberengadinisch, Unterengadinisch. Das XIX. Jahrhundert.");
		volNames.add("IX - Oberengadinisch, Unterengadinisch. Volkslieder, Sprichwörter.");
		volNames.add("X - Sursettisch, Sutsettisch, Münsterisch. Erste Hälfte.");
		volNames.add("X - Sursettisch, Sutsettisch, Münsterisch. Zweite Hälfte.");
		volNames.add("XI - Bergellisch, Unterengadinisch");
		volNames.add("XII - Die modernen Dichter der Surselva");
		volNames.add("XIII - Surselvisch, Subselvisch. (I. Ergänzungsband)");
		volNames.add("XIV - Schamser Band");
	}

	static {
		idToName.put(
				"PPN345572629_0004",
				"I - Surselvisch, Subselvisch, Sursettisch. 1. Lieferung: Das XVII. Jahrhundert.");
		idToName.put("PPN345572629_0008",
				"I - 2. und 3. Lieferung: Das XVIII. und XIX. Jahrhundert.");
		idToName.put("PPN345572629_0009",
				"II - Surselvisch, Subselvisch. 1. Lieferung.");
		idToName.put("PPN345572629_0011", "II - 2. und 3. Lieferung.");
		idToName.put("PPN345572629_0014_02",
				"III - Surselvisch, Subselvisch. Die Weisen der Volkslieder.");
		idToName.put("PPN345572629_0030",
				"IV - Surselvisch, Subselvisch, Volksmedizin, Nachträge.");
		idToName.put("PPN345572629_0012",
				"V - Oberengadinisch, Unterengadinisch. Das XVI. Jahrhundert.");
		idToName.put("PPN345572629_0017",
				"VI - Oberengadinisch, Unterengadinisch. Das XVII. Jahrhundert.");
		idToName.put("PPN345572629_0018",
				"VII - Oberengadinisch, Unterengadinisch. Das XVIII. Jahrhundert.");
		idToName.put("PPN345572629_0024",
				"VIII - Oberengadinisch, Unterengadinisch. Das XIX. Jahrhundert.");
		idToName.put("PPN345572629_0027",
				"IX - Oberengadinisch, Unterengadinisch. Volkslieder, Sprichwörter.");
		idToName.put("PPN345572629_0035",
				"X - Sursettisch, Sutsettisch, Münsterisch. Erste Hälfte.");
		idToName.put("PPN345572629_0036",
				"X - Sursettisch, Sutsettisch, Münsterisch. Zweite Hälfte.");
		idToName.put("PPN345572629_0037", "XI - Bergellisch, Unterengadinisch");
		idToName.put("PPN345572629_0038",
				"XII - Die modernen Dichter der Surselva");
		idToName.put("PPN345572629_0033",
				"XIII - Surselvisch, Subselvisch. (I. Ergänzungsband)");
		idToName.put("PPN345572629_0014_RC", "XIV - Schamser Band");
	}

	static {
		nameToId.put(
				"I - Surselvisch, Subselvisch, Sursettisch. 1. Lieferung: Das XVII. Jahrhundert.",
				"PPN345572629_0004");
		nameToId.put(
				"I - 2. und 3. Lieferung: Das XVIII. und XIX. Jahrhundert.",
				"PPN345572629_0008");
		nameToId.put("II - Surselvisch, Subselvisch. 1. Lieferung.",
				"PPN345572629_0009");
		nameToId.put("II - 2. und 3. Lieferung.", "PPN345572629_0011");
		nameToId.put(
				"III - Surselvisch, Subselvisch. Die Weisen der Volkslieder.",
				"PPN345572629_0014_02");
		nameToId.put("IV - Surselvisch, Subselvisch, Volksmedizin, Nachträge.",
				"PPN345572629_0030");
		nameToId.put(
				"V - Oberengadinisch, Unterengadinisch. Das XVI. Jahrhundert.",
				"PPN345572629_0012");
		nameToId.put(
				"VI - Oberengadinisch, Unterengadinisch. Das XVII. Jahrhundert.",
				"PPN345572629_0017");
		nameToId.put(
				"VII - Oberengadinisch, Unterengadinisch. Das XVIII. Jahrhundert.",
				"PPN345572629_0018");
		nameToId.put(
				"VIII - Oberengadinisch, Unterengadinisch. Das XIX. Jahrhundert.",
				"PPN345572629_0024");
		nameToId.put(
				"IX - Oberengadinisch, Unterengadinisch. Volkslieder, Sprichwörter.",
				"PPN345572629_0027");
		nameToId.put(
				"X - Sursettisch, Sutsettisch, Münsterisch. Erste Hälfte.",
				"PPN345572629_0035");
		nameToId.put(
				"X - Sursettisch, Sutsettisch, Münsterisch. Zweite Hälfte.",
				"PPN345572629_0036");
		nameToId.put("XI - Bergellisch, Unterengadinisch", "PPN345572629_0037");
		nameToId.put("XII - Die modernen Dichter der Surselva",
				"PPN345572629_0038");
		nameToId.put("XIII - Surselvisch, Subselvisch. (I. Ergänzungsband)",
				"PPN345572629_0033");
		nameToId.put("XIV - Schamser Band", "PPN345572629_0014_RC");
	}

	/**
	 * 
	 * @param fileName
	 *            Filename of volume (id)
	 * @return the name to be displayed
	 */

	public String displayName(String fileName) {
		return idToName.get(fileName);
	}

	/**
	 * 
	 * @param displayedName
	 *            name of volume as displayed
	 * @return the original filename/id
	 */

	public String fileName(String displayedName) {
		return nameToId.get(displayedName);
	}

	/**
	 * 
	 * @return list of volumes to be displayed
	 */
	public List<String> listVolumeNames() {
		return volNames;
	}

}
