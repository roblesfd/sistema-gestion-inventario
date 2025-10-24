package com.roblez.inventorysystem.report;

import java.util.List;

public class ReportSchema {
	private final List<ColumnDescriptor> columns;
	
	//Constructor
	public ReportSchema(List<ColumnDescriptor> columns) {
		this.columns = columns;
	}
	
	public List<ColumnDescriptor> getColumns() {
		return columns;
	}
}
