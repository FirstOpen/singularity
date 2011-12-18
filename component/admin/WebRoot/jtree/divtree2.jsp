<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Tree</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/jtree/div-tree.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/jtree/domutils.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/jtree/div-tree.js"></script>
  
	

<script type="text/javascript">


preloadImages();

</script>
</head>
<body>

<div class="tree-wrap">
	<div class="tree-header">
		Photo Albums
	</div>
	<div class="ul tree-list">
		<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Family</a>
			<div class="ul" style="display:none;">
				<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Mom's side</a>
					<div class="ul" style="display:none;">
						<div class="li container-item"><a class="container-link" href="#" onclick="treeContainerNodeClicked(event); return false;">Ohio</a>
							<div class="ul" style="display:none;">
								<div class="li"><a href="#">Akron</a></div>
								<div class="li"><a href="#">Apple Creek</a></div>
								<div class="li"><a href="#">Barberton</a></div>
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

</body>
</html>
