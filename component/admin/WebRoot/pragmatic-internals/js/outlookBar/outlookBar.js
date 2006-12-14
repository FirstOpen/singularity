//<script type="text/javascript">
var g_currentPanel;		

function isBrowser(b,v) {  
	browserOk = false;
	versionOk = false;		

	browserOk = (navigator.appName.indexOf(b) != -1);		
	if (v == 0) versionOk = true;
	else  versionOk = (v <= parseInt(navigator.appVersion));
	return browserOk && versionOk;
}

function showPanel(id) {
	var panel;
	if (isBrowser('Microsoft', 0))
		panel = document.getElementById(id).parentNode.nextSibling.firstChild;
	else
		panel = document.getElementById(id).parentNode.nextSibling.nextSibling.firstChild.nextSibling;			
	if (g_currentPanel) g_currentPanel.className = 'groupPanelHidden';        
	panel.className = 'groupPanel';        
	g_currentPanel = panel;
}		

function showItem(frame, url) {		
	if (frame == '_self')
		location.href=url;				
	else {
		var foundIndex;
		for (var i = 0, found = false; i < parent.frames.length && found == false; i++) {
			if (parent.frames[i].name == frame) {
				found = true;								
				foundIndex = i;
			}
		}
		parent.frames[foundIndex].location.href=url;
	}
}        	
//</script>