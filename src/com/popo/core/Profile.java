package com.popo.core;

import java.io.Serializable;
import java.util.Properties;

public class Profile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
