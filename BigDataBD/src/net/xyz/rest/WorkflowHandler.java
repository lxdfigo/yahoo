package net.xyz.rest;

import java.io.File;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
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
			//System.out.println(childrenFile);
			return "["+childrenFile.toString()+"]";
		}
		return "[]";
	}
}
