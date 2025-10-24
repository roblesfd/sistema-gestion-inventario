package com.roblez.inventorysystem.report;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;

public class AsciiTableRenderer {
	/**
	* Devuelve la tabla ASCII como String usando el schema para columnas.
    */
	public String renderTerminal(ReportSchema schema, List<GenericReportRow> rows) {
		AsciiTable table = new AsciiTable();
		table.addRule();
		
		// Header 
		List<ColumnDescriptor> columns = schema.getColumns();
		Object[] header = columns.stream().map(ColumnDescriptor::getLabel).toArray();
		table.addRow(header);
		table.addRule();
		
		// Rows
		for(GenericReportRow row : rows) {
			Object[] cells = columns.stream()
					.map(c -> c.format(row.get(c.getKey())))
					.toArray();
			table.addRow(cells);
			table.addRule();
		}
		return table.render();
	}
}
