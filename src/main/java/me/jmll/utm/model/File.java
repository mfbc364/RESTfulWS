package me.jmll.utm.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class File implements Serializable {

private static final long serialVersionUID = 1L;
private String name;
private String path;
private String fullPath;
private long sizeBytes;
private Link _link;

	public File() {
	}
	
	public File(String name, String path, String fullPath, long sizeBytes) {
		this.name = name;
		this.path = path;
		this.fullPath = fullPath;
		this.sizeBytes = sizeBytes;
	}
	// Getters
	public String getName(){
		return name;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getFullPath(){
		return fullPath;
	}
	
	public long getSizeBytes(){
		return sizeBytes;
	}
	
	@XmlElement(name = "link")
	public Link getLink() {
		return _link;
	}
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	
	public void setSizeBytes(long sizeBytes){
		this.sizeBytes = sizeBytes;
	}

	public void setLink(Link _link) {
		this._link = _link;
	}
}
