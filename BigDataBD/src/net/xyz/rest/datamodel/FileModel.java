package net.xyz.rest.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

@XmlRootElement
public class FileModel {
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public ArrayList<FileModel> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<FileModel> children) {
		this.children = children;
	}


	private String data;
	private String state = "closed";
	private ArrayList<FileModel> children= new ArrayList<FileModel>();
	private String rel;
	private Node attr;
	public Node getAttr() {
		return attr;
	}

	public void setAttr(Node attr) {
		this.attr = attr;
	}

	public String toString() {
		StringBuilder rs = new StringBuilder();
		if(children.size()!=0) 		rs.append("{\"children\":").append(children.toString()).append(",");
		else rs.append("{\"children\":").append("null").append(",");
		rs.append("\"data\":\"").append(data).append("\",")
		.append("\"attr\":").append(attr).append(",")
		.append("\"state\":\"").append(state).append("\",")
		.append("\"type\":\"").append(rel).append("\"}");
		return rs.toString();
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
}
