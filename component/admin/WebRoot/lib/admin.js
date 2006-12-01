
     	var subscriptionId = null;

        function rfidInit(){
         p_join_listen('/rfid/events', 'stream');
       
        }
 
       	// Join or leave depending on arg
		function joinLeave(what) {
			if (what == 'joinStream') {
			  p_join_listen(null, 'stream');
			} else if (what == 'joinPull') {
			  p_join_listen(null, 'pull');
			} else if (what == 'joinPoll') {
			  p_join_listen(null, 'poll');
		    } else if (what == 'leave') {
			  p_leave();
			  subscriptionId = null;
			  displayData('NO DATA (left)');
		    }
		}

		// Data Event Callback
		function onData(event) {
			// Write the event as HTML table into content Element
	  		// p_debug(flag, "pushlet-app", 'event received subject=' + event.getSubject() );
			displayData(event.toString());
		}

		// Subscribe acknowledgement Callback
		function onSubscribeAck(event) {
			subscriptionId = event.get('p_sid');
			displayControl(event.toString());
			displayData('WAITING FOR DATA...');
    	}

    	// Ack refresh
		function onRefreshAck(event) {
			displayData(event.toString());
    	}

	   	// Heartbeat on data channel
		function onHeartbeat(event) {
			displayData(event.toString());
    	}

		// Subscribe acknowledgement Callback
		function onUnsubscribeAck(event) {
			subscriptionId = null;
			displayControl(event.toString());
			displayData('NO DATA (unsubscribed)');
  		}

		// Catches all other callbacks
		function onEvent(event) {
			displayControl(event.toString());
		}

		// Subscribe/unsubscribe to/from subject
		function subscribeUnsubscribe(subject) {
		    if (p_getSessionId() == null) {
		      alert('you need to join first (join/leave combo)');
		      return;
		    }

		    if (subject == 'UNSUBSCRIBE') {
		       // Unsubscribe all
			   p_unsubscribe();
			} else if (subject != '') {
				// Unsubscribe first if already subscribed
				if (subscriptionId != null) {
					p_unsubscribe(subscriptionId);
				}
				p_subscribe(subject, 'mylabel');
			}
 		}


		// Toggle debug windows
		function setDebug(value) {
			if (value == 'none') {
				p_setDebug(false);
			} else if (value == 'full') {
				p_setDebug(true);
			} else if (value == 'net') {
				p_setDebug(false);
				p_setNetDebug(true);
			}
 		}

 		function displayData(aString) {
 		  document.dataEventDisplay.event.value = aString;
		}

   		function displayControl(aString) {
 		  document.controlEventDisplay.event.value = aString;
		}
   
   function tabDisplay(){
     with(org.ditchnet.jsp.TabUtils){
        for (var i = 0; i < tabPanes.length; i++) {
 
            var tab = tabs[i];
            var children = tab.childNodes;
            //alert(tab.innerHTML);
            var tabName = children[3].firstChild.nodeValue;
         //   alert(">>>" + tabName + "<<<");
            if(tabName == " \n"){
                    var display= tab.style.display ? '' : 'none';
                tab.style.display = display;
            }
        
        }//end for
                 
    }//end with
   }
   
   function myTabListener(evt) {
    // a tab was clicked, and made visible. take action here
    
    var selectedTabPane = evt.getTabPane(); // HTMLDivElement reference to 
                                            // the div containing the tab pane.

    var selectedTab     = evt.getTab();     // HTMLDivElement reference to 
                                            // the div that is the actual tab
                                            // at the top of the container with
                                            // the tab title.

    var tabContainer    = evt.getTabContainer(); // HTMLDivElement reference to 
                                                 // the div wrapping the entire 
                                                 // tab container.

  
  function addEvent( obj, type, fn ) {
   if ( obj.attachEvent ) {
     obj['e'+type+fn] = fn;
     obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
     obj.attachEvent( 'on'+type, obj[type+fn] );
   } else
     obj.addEventListener( type, fn, false );
 }
function removeEvent( obj, type, fn ) {
 if ( obj.detachEvent ) {
     obj.detachEvent( 'on'+type, obj[type+fn] );
     obj[type+fn] = null;
   } else
     obj.removeEventListener( type, fn, false );
 }
     
}


function  singularityInit(){
   //Do nothing
}