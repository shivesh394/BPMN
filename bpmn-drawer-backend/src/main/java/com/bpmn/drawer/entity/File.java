package com.bpmn.drawer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@Entity
@Table(name = "file")
public class File {

	  @Id
	  @Column(name = "id" , length=50)
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private int id;
	  @Column(name = "name" , length=50)
	  private String name;
	  @Column(name = "data" , length=60)
	  @Lob
	  private byte[] data;
	  public File(int id, String name, byte[] data) {
			super();
			this.id = id;
			this.name = name;
			this.data = data;
		}
	  public File() {
			super();
			// TODO Auto-generated constructor stub
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
		public byte[] getData() {
			return data;
		}
		public void setData(byte[] data) {
			this.data = data;
		}

}
