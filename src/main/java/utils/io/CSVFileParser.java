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
	public static CSVParseResult parse(String path, Class<? extends Visualizable> type) {
		InputStream stream = CSVFileParser.class.getClassLoader().getResourceAsStream(path);
		return parse(stream, 1, new int[] { 0, 1, 2, 3 }, 4, type);
	}

	public static CSVParseResult parseOutsource(String path, Class<? extends Visualizable> type) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return parse(stream, 1, new int[] { 0, 1, 2, 3 }, 4, type);
	}

	public static CSVParseResult parse(InputStream stream, int skipline, int[] featureColumnIndex, int labelColumn, Class<? extends Visualizable> type) {
		List<Visualizable> data_result = new ArrayList<Visualizable>();
		List<String> title_result = new ArrayList<String>();

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
			if (record.getRecordNumber() == skipline + 1) {
				for (int i = 0; i < featureColumnIndex.length; i++) {
					title_result.add(record.get(featureColumnIndex[i]));
				}
			} else {
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
				data_result.add(instance);
			}
		}
		return new CSVParseResult(data_result, title_result);
	}

	public static class CSVParseResult {
		List<Visualizable> data;
		List<String> titles;

		public CSVParseResult(List<Visualizable> data, List<String> titles) {
			super();
			this.data = data;
			this.titles = titles;
		}

		public List<Visualizable> getData() {
			return data;
		}

		public void setData(List<Visualizable> data) {
			this.data = data;
		}

		public List<String> getTitles() {
			return titles;
		}

		public void setTitles(List<String> titles) {
			this.titles = titles;
		}

	}
}
