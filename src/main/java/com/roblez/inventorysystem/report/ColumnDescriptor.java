package com.roblez.inventorysystem.report;

import java.util.function.Function;

public class ColumnDescriptor {
	private final String key;
	private final String label;
	private final Function<Object, String> formatter;
	
    public ColumnDescriptor(String key, String label, Function<Object,String> formatter) {
        this.key = key; 
        this.label = label; 
        this.formatter = formatter;
    }
    
    public String getKey() {
		return key;
	}
    
    public String getLabel() {
		return label;
	}
    
    public String format(Object v) {
    	if(v == null) return "";
    	try {
    		return formatter != null ? formatter.apply(v) : v.toString();
    	}catch(Exception e) {
			return String.valueOf(v);
		}
    }
}
