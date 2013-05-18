<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Toolbar example for mxGraph</title>
	<!-- Sets the basepath for the library if not in same directory -->
	<script type="text/javascript">
		mxBasePath = '../src';
	</script>

	<!-- Loads and initializes the library -->
	<script type="text/javascript" src="../src/js/mxClient.js"></script>

	<!-- Example code -->
	<script type="text/javascript">
		// Program starts here. Creates a sample graph in the
		// DOM node with the specified ID. This function is invoked
		// from the onLoad event handler of the document (see below).
		function main()
		{
			// Checks if browser is supported
			if (!mxClient.isBrowserSupported())
			{
				// Displays an error message if the browser is
				// not supported.
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				// Defines an icon for creating new connections in the connection handler.
				// This will automatically disable the highlighting of the source vertex.
				mxConnectionHandler.prototype.connectImage = new mxImage('images/connector.gif', 16, 16);

				// Creates the div for the toolbar
				var tbContainer = document.createElement('div');
				tbContainer.style.position = 'absolute';
				tbContainer.style.overflow = 'hidden';
				tbContainer.style.padding = '2px';
				tbContainer.style.left = '0px';
				tbContainer.style.top = '0px';
				tbContainer.style.width = '24px';
				tbContainer.style.bottom = '0px';
				
				document.body.appendChild(tbContainer);
			
				// Creates new toolbar without event processing
				var toolbar = new mxToolbar(tbContainer);
				toolbar.enabled = false
				
				// Creates the div for the graph
				var container = document.createElement('div');
				container.style.position = 'absolute';
				container.style.overflow = 'hidden';
				container.style.left = '24px';
				container.style.top = '0px';
				container.style.right = '0px';
				container.style.bottom = '0px';
				container.style.background = 'url("editors/images/grid.gif")';

				document.body.appendChild(container);
				
				// Workaround for Internet Explorer ignoring certain styles
				if (mxClient.IS_QUIRKS)
				{
					document.body.style.overflow = 'hidden';
					new mxDivResizer(tbContainer);
					new mxDivResizer(container);
				}
	
				// Creates the model and the graph inside the container
				// using the fastest rendering available on the browser
				var model = new mxGraphModel();
				var graph = new mxGraph(container, model);

				// Enables new connections in the graph
				graph.setConnectable(true);
				graph.setMultigraph(false);

				// Stops editing on enter or escape keypress
				var keyHandler = new mxKeyHandler(graph);
				var rubberband = new mxRubberband(graph);
				
				var addVertex = function(icon, w, h, style)
				{
					var vertex = new mxCell(null, new mxGeometry(0, 0, w, h), style);
					vertex.setVertex(true);
				
					var img = addToolbarItem(graph, toolbar, vertex, icon);
					img.enabled = true;
					
					graph.getSelectionModel().addListener(mxEvent.CHANGE, function()
					{
						var tmp = graph.isSelectionEmpty();
						mxUtils.setOpacity(img, (tmp) ? 100 : 20);
						img.enabled = tmp;
					});
				};
				
				addVertex('editors/images/rectangle.gif', 100, 40, '');
				addVertex('editors/images/rounded.gif', 100, 40, 'shape=rounded');
				addVertex('editors/images/ellipse.gif', 40, 40, 'shape=ellipse');
				addVertex('editors/images/rhombus.gif', 40, 40, 'shape=rhombus');
				addVertex('editors/images/triangle.gif', 40, 40, 'shape=triangle');
				addVertex('editors/images/cylinder.gif', 40, 40, 'shape=cylinder');
				addVertex('editors/images/actor.gif', 30, 40, 'shape=actor');
			}
		}

		function addToolbarItem(graph, toolbar, prototype, image)
		{
			// Function that is executed when the image is dropped on
			// the graph. The cell argument points to the cell under
			// the mousepointer if there is one.
			var funct = function(graph, evt, cell, x, y)
			{
				graph.stopEditing(false);

				var vertex = graph.getModel().cloneCell(prototype);
				vertex.geometry.x = x;
				vertex.geometry.y = y;
					
				graph.addCell(vertex);
				graph.setSelectionCell(vertex);
			}
			
			// Creates the image which is used as the drag icon (preview)
			var img = toolbar.addMode(null, image, function(evt, cell)
			{
				var pt = this.graph.getPointForEvent(evt);
				funct(graph, evt, cell, pt.x, pt.y);
			});
			
			// Disables dragging if element is disabled. This is a workaround
			// for wrong event order in IE. Following is a dummy listener that
			// is invoked as the last listener in IE.
			mxEvent.addListener(img, 'mousedown', function(evt)
			{
				// do nothing
			});
			
			// This listener is always called first before any other listener
			// in all browsers.
			mxEvent.addListener(img, 'mousedown', function(evt)
			{
				if (img.enabled == false)
				{
					mxEvent.consume(evt);
				}
			});
						
			mxUtils.makeDraggable(img, graph, funct);
			
			return img;
		}

	</script>
</head>

<!-- Calls the main function after the page has loaded. Container is dynamically created. -->
<body onload="main();">
</body>
</html>
