package com.bpmn.drawer.DrawerDTO;

public class ResponseFile {
	  private String name;
	  private String url;
	  private int id;
	  private byte[] data;
	public ResponseFile(String name, int id, String url, byte[] data) {
		super();
		this.name = name;
		this.url = url;
		this.id = id;
		this.data = data;
	}
	public ResponseFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}