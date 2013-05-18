$("#createFile").click(function() {
	// alert( this.id.toString().replace("add_", ""));
	// $('#createFile').modal('show');
	$("#tree").jstree("create", null, "last", {
		"attr" : {
			"rel" : "default"
		}
	});

});
$("#createFolder").click(function() {
	// alert( this.id.toString().replace("add_", ""));
	// $('#createFile').modal('show');
	$("#tree").jstree("create", null, "last", {
		"attr" : {
			"rel" : "folder"
		}
	});

});

$("#edit").click(function() {
	$("#tree").jstree("rename", null, "last", {});
});
function saveCreate() {
	console.log(currentData);
	$.post("./rest/workflow/savefile", {
		"content" : $('#contentArea').val(),
		"path" : currentData.rslt.obj.attr("path")
	}, function(r) {
		if (r.status) {
//			$(data.rslt.obj).attr("id", "node_" + r.id);
//			$(data.rslt.obj).attr("path", r.path);
//			$('#myModalLabel').html(
//					"Edit File "
//							+ data.rslt.obj.attr("id").replace("node_", ""));
//			$.getJSON("./rest/workflow/readfile?path="
//					+ data.rslt.obj.attr("path"), function(data) {
//				$('#contentArea').val(data.content);
//
//			});
			$('#showFile').modal('hide');
		} else {
			$.jstree.rollback(data.rlbk);
		}
	})
};
function cancelCreate() {
	$("#tree").jstree("rename", null, "last", {});
}
$(function() {
	var dataBind;
	$
			.getJSON(
					"./rest/workflow/detail?workflowName=" + currentWorkFlow,
					function(data) {
						dataBind = data;
						$("#tree")
								.bind("before.jstree", function(e, data) {
									// $("#alog").append(data.func + "<br />");
								})
								.jstree(
										{
											// List of active plugins
											"plugins" : [ "themes",
													"json_data", "ui", "crrm",
													"cookies", "dnd", "types",
													"hotkeys"
											// ,"contextmenu"
											],

											"json_data" : {
												"data" : dataBind

											},
											// Configuring the search plugin
											"search" : {
												"ajax" : {
													"url" : "./server.php",
													"data" : function(str) {
														return {
															"operation" : "search",
															"search_str" : str
														};
													}
												}
											},
											// Using types - most of the time
											// this is an overkill
											// read the docs carefully to decide
											// whether you need
											// types
											"types" : {
												// I set both options to -2, as
												// I do not need depth
												// and children count checking
												// Those two checks may slow
												// jstree a lot, so use
												// only when needed
												"max_depth" : -2,
												"max_children" : -2,
												// I want only `drive` nodes to
												// be root nodes
												// This will prevent moving or
												// creating any other
												// type as a root node
												"valid_children" : [ "drive" ],
												"types" : {
													// The default type
													"default" : {
														// I want this type to
														// have no children (so
														// only leaf nodes)
														// In my case - those
														// are files
														"valid_children" : "none",
														// If we specify an icon
														// for the default
														// type it WILL OVERRIDE
														// the theme icons
														"icon" : {
															"image" : "./file.png"
														}
													},
													// The `folder` type
													"folder" : {
														// can have files and
														// other folders inside
														// of it, but NOT
														// `drive` nodes
														"valid_children" : [
																"default",
																"folder" ],
														"icon" : {
															"image" : "./folder.png"
														}
													},
													// The `drive` nodes
													"drive" : {
														// can have files and
														// folders inside, but
														// NOT other `drive`
														// nodes
														"valid_children" : [
																"default",
																"folder" ],
														"icon" : {
															"image" : "./root.png"
														},
														// those prevent the
														// functions with the
														// same
														// name to be used on
														// `drive` nodes
														// internally the
														// `before` event is
														// used
														"start_drag" : false,
														"move_node" : false,
														"delete_node" : false,
														"remove" : false
													}
												}
											},
											// UI & core - the nodes to
											// initially select and open
											// will be overwritten by the cookie
											// plugin

											// the UI plugin - it handles
											// selecting/deselecting/hovering
											// nodes
											"ui" : {
											// this makes the node with ID
											// node_4 selected onload
											// "initially_select" : [ "node_4" ]
											},
											// the core plugin - not many
											// options here
											"core" : {
												// just open those two nodes up
												// as this is an AJAX enabled
												// tree, both will be
												// downloaded from the server
												"initially_open" : [ "node_"
														+ currentWorkFlow ]
											}
										})
								.bind(
										"create.jstree",
										function(e, data) {
											console.log(data.rslt.parent
													.attr("path"));
											// return;
											$
													.post(
															"./rest/workflow/create",
															{
																"operation" : "create_node",
																"id" : data.rslt.parent
																		.attr(
																				"id")
																		.replace(
																				"node_",
																				""),
																"position" : data.rslt.position,
																"title" : data.rslt.name,
																"type" : data.rslt.obj
																		.attr("rel"),
																"path" : data.rslt.parent
																		.attr("path")
															},
															function(r) {
																if (r.status) {
																	$(
																			data.rslt.obj)
																			.attr(
																					"id",
																					"node_"
																							+ r.id);
																	$(
																			data.rslt.obj)
																			.attr(
																					"path",
																					r.path);
																	$(
																			'#myModalLabel')
																			.html(
																					"Edit File "
																							+ data.rslt.obj
																									.attr(
																											"id")
																									.replace(
																											"node_",
																											""));
																	$
																			.getJSON(
																					"./rest/workflow/readfile?path="
																							+ data.rslt.obj
																									.attr("path"),
																					function(
																							data) {
																						$(
																								'#contentArea')
																								.val(
																										data.content);

																					});
																	$(
																			'#showFile')
																			.modal(
																					'show');
																} else {
																	$.jstree
																			.rollback(data.rlbk);
																}
															});
										})

								.bind(
										"rename.jstree",
										function(e, data) {
											console.log(data.rslt.obj);
											if (data.rslt.obj.attr("rel") != "default")
												return;
											$('#myModalLabel')
													.html(
															"Edit File "
																	+ data.rslt.obj
																			.attr(
																					"id")
																			.replace(
																					"node_",
																					""));
											$
													.getJSON(
															"./rest/workflow/readfile?path="
																	+ data.rslt.obj
																			.attr("path"),
															function(data) {
																$(
																		'#contentArea')
																		.val(
																				data.content);

															});
											currentData=data;
											$('#showFile').modal('show');
										});
					});

});