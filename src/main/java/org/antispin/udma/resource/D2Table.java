package org.antispin.udma.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class D2Table {

	Map<String, List<String>> columns = new HashMap<String, List<String>>();

	public D2Table(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		// parse headers
		String[] columnNames = br.readLine().split("\t");
		for(String columnName: columnNames) {
			columns.put(columnName, new ArrayList<String>());
		}
		
		String line;
		while((line = br.readLine()) != null) {
			String[] values = line.split("\t");
			for(int c = 0; c < values.length; ++c) {
				columns.get(columnNames[c]).add(values[c]);
			}
		}
	}
	
	public List<String> getColumn(String column) {
		return columns.get(column);
	}
	
}
