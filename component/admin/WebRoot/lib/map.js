
    var map;
    

    function loadMap() {
      
      // Display Info Windows Above Markers
      //
      // In this example, we show a custom info window above each marker by
      // listening to the click event for each marker. We take advantage of function
      // closures to customize the info window content for each marker.
      
      // Center the map on Palo Alto
      map = new GMap(document.getElementById("map"));
      map.addControl(new GSmallMapControl());
      map.addControl(new GMapTypeControl());
      point = new GPoint(-122.1471, 37.443526789);
      map.centerAndZoom(point,4);
     


      GEvent.addListener(map, 'click', function() {
        alert("You clicked the map.");
        });
        
      // Creates a marker whose info window displays the given number
     

     return true;
    }

  function singularityInit(){
        loadMap();
        var myxml = document.getElementById("map-view:coords").value;
        updateMap(myxml);
        
  }
  function updateMap(xml) {
  
  
    var xmlDoc = xmltodoc(xml);
   
    var markers = xmlDoc.documentElement.getElementsByTagName("marker");
    
    for (var i = 0; i < markers.length; i++) {
    var point = new GPoint(parseFloat(markers[i].getAttribute("lng")),
                           parseFloat(markers[i].getAttribute("lat")));
    
    var marker = createMarker(point, i);
    
    map.addOverlay(marker);
    }
  }

      function createMarker(point, number) {
        var marker = new GMarker(point);
      
        // Show this marker's index in the info window when it is clicked
        var html = "Marker #<b>" + number + "</b>";
        GEvent.addListener(marker, "click", function() {
          marker.openInfoWindowHtml(html);
        });
      
        return marker;
      }

      function xmltodoc(xml){
        if (typeof DOMParser == "undefined") {
            DOMParser = function () {}
    
            DOMParser.prototype.parseFromString = function (str, contentType) {
                if (typeof ActiveXObject != "undefined") {
                var d = new ActiveXObject("Microsoft.XMLDOM");
                    d.loadXML(str);
                    return d;
            } else if (typeof XMLHttpRequest != "undefined") {
               var req = new XMLHttpRequest;
                req.open("GET", "data:" + (contentType || "application/xml") +
                               ";charset=utf-8," + encodeURIComponent(str), false);
                 if (req.overrideMimeType) {
                    req.overrideMimeType(contentType);
                 }
                req.send(null);
                return req.responseXML;
            }//end else if
        }//end function
      }//end if
      var parser = new DOMParser();
      var doc = parser.parseFromString(xml, "text/xml");
      return doc;
    }

      function addRandom(){
       //Add 10 random markers in the map viewport
      var bounds = map.getBoundsLatLng();
      var width = bounds.maxX - bounds.minX;
      var height = bounds.maxY - bounds.minY;
      for (var i = 0; i < 10; i++) {
        var point = new GPoint(bounds.minX + width * Math.random(),
                              bounds.minY + height * Math.random());
        var marker = createMarker(point, i + 1);
        
        map.addOverlay(marker);
      }
}
