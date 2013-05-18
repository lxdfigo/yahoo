package net.xyz.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.xyz.config.Config;
import net.xyz.rest.datamodel.FileModel;
import net.xyz.rest.datamodel.Node;

@Path("/workflow")
public class WorkflowHandler {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHello() {
		return "{'sdf':'sdf'}";
	}

	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getWorkflow() {
		return "{'sdf':'sdf'}";
	}

	private FileModel getChildrenFile(File file) {
		FileModel rs = new FileModel();
		rs.setData(file.getName());
		Node node = new Node();
		if (file.isDirectory()) {
			rs.setRel("folder");
			node.setRel("folder");
			File[] listFiles = file.listFiles();
			for (File f : listFiles) {
				rs.getChildren().add(getChildrenFile(f));
			}

		} else {
			rs.setRel("default");
			node.setRel("default");

		}
		node.setPath(file.getAbsolutePath());
		node.setId(file.getName());
		rs.setState("closed");
		rs.setAttr(node);
		return rs;
	}

	@Path("/detail")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getFile(@QueryParam("workflowName") String workflowName) {
		if (workflowName != null) {
			String inFolder = Config.JobFolder + "/" + workflowName;
			File file = new File(inFolder);
			FileModel childrenFile = getChildrenFile(file);
			// System.out.println(childrenFile);
			return "[" + childrenFile.toString() + "]";
		}
		return "[]";
	}

	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String createFile(@FormParam("path") String path,
			@FormParam("type") String type, @FormParam("title") String title) {
		File file = new File(path + "/" + title);
		if (type.equals("default")) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			file.mkdir();
		}
		return "{\"status\":1,\"id\":\"" + title + "\",\"path\":\""
				+ file.getAbsolutePath().replaceAll("\\\\", "/") + "\"}";
	}

	@Path("/readfile")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String createFile(@QueryParam("path") String path) {
		File file = new File(path);
		String data = null;
		if(file.exists()){
			BufferedReader br = null;
			StringBuffer buffer = null;
			  try{
			   buffer = new StringBuffer();
			   InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"utf-8");
			   br = new BufferedReader(isr); 
			   int s;
			   while((s = br.read())!=-1){
			    buffer.append((char)s);
			   }
			   data = buffer.toString();
			  }catch(Exception e){
			   e.printStackTrace();
			  }
		}
		return "{\"status\":1,\"content\":\""+data+"\"}";
	}
	@Path("/savefile")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String createFile(@FormParam("content") String content,@FormParam("path") String path) {
		File file = new File(path);
		try {
			FileWriter w = new FileWriter(file);
			w.write(content);
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(content);
		return "{\"status\":1}";
	}
}
