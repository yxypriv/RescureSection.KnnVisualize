package utils.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import algorithm.interfaces.Visualizable;

public class CSVFileParser {
	public static List<Visualizable> parse(String path, Class<? extends Visualizable> type) {
		InputStream stream = CSVFileParser.class.getClassLoader().getResourceAsStream(path);
		return parse(stream, 2, new int[] { 0, 1, 2, 3 }, 4, type);
	}

	public static List<Visualizable> parseOutsource(String path, Class<? extends Visualizable> type) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return parse(stream, 2, new int[] { 0, 1, 2, 3 }, 4, type);
	}

	public static List<Visualizable> parse(InputStream stream, int skipline, int[] featureColumnIndex, int labelColumn, Class<? extends Visualizable> type) {
		List<Visualizable> result = new ArrayList<Visualizable>();

		CSVParser parse;
		List<CSVRecord> records = null;
		try {
			parse = CSVFormat.EXCEL.parse(new InputStreamReader(stream, "ISO-8859-1"));
			records = parse.getRecords();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (CSVRecord record : records) {
			if (record.getRecordNumber() <= skipline)
				continue;
			double[] features = new double[featureColumnIndex.length];

			for (int i = 0; i < featureColumnIndex.length; i++) {
				double feature = Double.parseDouble(record.get(featureColumnIndex[i]));
				features[i] = feature;
			}
			Visualizable instance = null;
			try {
				instance = type.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("visible default constructor needed~");
				e.printStackTrace();
			}
			instance.setLabel(record.get(labelColumn));
			instance.setPlots(features);
			result.add(instance);
		}
		return result;
	}

}
