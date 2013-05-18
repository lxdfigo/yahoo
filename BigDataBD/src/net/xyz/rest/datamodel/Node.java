package net.xyz.rest.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

@XmlRootElement
public class Node {
	private String id;
	private String rel;
	private String path;
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String toString() {
		StringBuilder rs = new StringBuilder();
		rs.append("{\"id\":\"").append(id).append("\",")
		.append("\"rel\":\"").append(rel).append("\",")
		.append("\"path\":\"").append(path.replaceAll("\\\\", "/")).append("\"}");
		return rs.toString();
	}
}
