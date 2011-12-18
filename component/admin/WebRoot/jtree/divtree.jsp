<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/jtree/div-tree.css"/>
    <script type="text/javascript" src="<%= request.getContextPath() %>/jtree/domutils.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/jtree/div-tree.js"></script>
  

<f:view>
<div class="tree-wrap">
	<div class="tree-header">
		Menu
	</div>
	<div class="ul tree-list">
		<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Zone</a>
			<div class="ul" style="display:none;">
				<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Area</a>
					<div class="ul" style="display:none;">
						<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Ohio</a>
							<div class="ul" style="display:none;">
								<div class="li"><a href="#">Customs</a></div>
								<div class="li"><a href="#">Dock1</a></div>
								<div class="li"><a href="#">Dock2</a></div>
							</div>
						</div>
						<div class="li"><a href="#">Alabama</a></div>
						<div class="li"><a href="#">California</a></div>
					</div>
				</div>
				<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Dad's side</a>
					<div class="ul" style="display:none;">
						<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">New York</a>
							<div class="ul" style="display:none;">
								<div class="li"><a href="#">Manhatton</a></div>
								<div class="li"><a href="#">Rochester</a></div>
								<div class="li"><a href="#">Buffalo</a></div>
							</div>
						</div>
						<div class="li"><a href="#">Ohio</a></div>
						<div class="li"><a href="#">Florida</a></div>
					</div>
				</div>
			</div>
		</div>
		<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Summer Vacation</a>
			<div class="ul" style="display:none;">
				<div class="li"><a href="#">Bermuda</a></div>
				<div class="li"><a href="#">Bahama</a></div>
				<div class="li"><a href="#">Aruba</a></div>
				<div class="li"><a href="#">Papa New Guinea</a></div>
			</div>
		</div>
		<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Winter Vacation</a>
			<div class="ul" style="display:none;">
				<div class="li"><a href="#">Djibouti</a></div>
				<div class="li"><a href="#">New Dehli</a></div>
				<div class="li"><a href="#">Siberia</a></div>
			</div>
		</div>
		<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Home</a>
			<div class="ul" style="display:none;">
				<div class="li"><a href="#">Garden</a></div>
				<div class="li"><a href="#">Back Yard</a></div>
				<div class="li"><a href="#">Front Yard</a></div>
			</div>
		</div>
		<div class="li"><a class="last" href="#">Miscellaneous</a></div>
	</div>
</div>

<div id="canvas">
	<img src="" alt=""/>
	<img src="" alt=""/>
	<img src="" alt=""/>
</div>
</f:view>
