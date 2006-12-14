/*
     General AJAX technique samples--how to talk to the server + return data and do stuff with it.
     Direct questions, answers, rants, flames, kudos to sbenfield@clearnova.com
     Steve Benfield

      1.0: 2005-07-14   Initial Release
      1.1: 2005-07-15   Fixed bugs, changed text for clarity, added ability to register for updates
     
*/
     
var _ms_XMLHttpRequest_ActiveX = ""; // Holds type of ActiveX to instantiate
var _ajax;                           // Reference to a global XMLHTTPRequest object for some of the samples
var _logger = true;                  // write output to the Activity Log
var _status_area;                    // will point to the area to write status messages to

if (!window.Node || !window.Node.ELEMENT_NODE) {
    var Node = { ELEMENT_NODE: 1, ATTRIBUTE_NODE: 2, TEXT_NODE: 3, CDATA_SECTION_NODE: 4, ENTITY_REFERENCE_NODE: 5,
                 ENTITY_NODE: 6, PROCESSING_INSTRUCTION_NODE: 7, COMMENT_NODE: 8, DOCUMENT_NODE: 9, DOCUMENT_TYPE_NODE: 10, DOCUMENT_FRAGMENT_NODE: 11, NOTATION_NODE: 12 };
}

// initialize the global AJAX object
// lots of samples that discuss AJAX use a global variable
// so I started these samples with one
// some examples use the "global" one and others use an encapsulated one that requires a new (a better approach!)
_ajax = startAJAX();

// From prototype.js @ www.conio.net | Returns an object reference to one or more strings
// ignore the fact that there are no arguments to this method -- javascript doesn't care how many you send (not strongly typed)
// The method checks the actual # of arguments -- returns a single object or an array
function $() {
    var elements = new Array();

    for (var i = 0; i < arguments.length; i++) {
        var element = arguments[i];

        if (typeof element == 'string')
            element = document.getElementById(element);

        if (arguments.length == 1)
            return element;

        elements.push(element);
    }

    return elements;
}

function getTextFromXML( oNode, deep ) {
    var s = "";
    var nodes = oNode.childNodes;

    for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];

        if (node.nodeType == Node.TEXT_NODE) {
            s += node.data;
        } else if (deep == true && (node.nodeType == Node.ELEMENT_NODE || node.nodeType == Node.DOCUMENT_NODE
                                       || node.nodeType == Node.DOCUMENT_FRAGMENT_NODE)) {
            s += getTextFromXML(node, true);
        };
    }

    ;
    return s;
}

;

// If you plan on doing anything outside of the US of A, then you'd better encode the things you pass back and forth
// the escape() method in Javascript is deprecated -- should use encodeURIComponent
function encode( uri ) {
    if (encodeURIComponent) {
        return encodeURIComponent(uri);
    }

    if (escape) {
        return escape(uri);
    }
}

function decode( uri ) {
    uri = uri.replace(/\+/g, ' ');

    if (decodeURIComponent) {
        return decodeURIComponent(uri);
    }

    if (unescape) {
        return unescape(uri);
    }

    return uri;
}

function logger( text, clear ) {
    if (_logger) {
        if (!_status_area) {
            _status_area = document.getElementById("status_area");
        }

        if (_status_area) {
            if (clear) {
                _status_area.value = "";
            }

            var old = _status_area.value;
            _status_area.value = text + ((old) ? "\r\n" : "") + old;
        }
    }
}

function initAJAX( method, url, changeHandler, async ) {
    if (typeof async == "undefined")
        async = true;

    if (changeHandler) {
        _ajax.onreadystatechange = changeHandler;
    }

    if (!method) {
        method = "POST";
    }

    method = method.toUpperCase();
    method = (method.charAt(0) == "G") ? "GET" : "POST";

    logger("----------------------------------------------------------------------");
    logger(((async) ? "Async" : "Sync") + " " + method + ": URL: " + url);

    _ajax.open(method, url, async);

    if (method == "POST") {
        _ajax.setRequestHeader("Connection", "close"); //overcome a bug in recent Firefox builds
        _ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        _ajax.setRequestHeader("Method", "POST " + url + "HTTP/1.1");
    }
}

function startAJAX() {
    var _myAjax = null;

    if (window.XMLHttpRequest) {
        _myAjax = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        // Instantiate the latest MS ActiveX Objects
        var versions = ["Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP",
                        "Microsoft.XMLHTTP"];

        if (_ms_XMLHttpRequest_ActiveX) {
            _myAjax = new ActiveXObject(_ms_XMLHttpRequest_ActiveX);
        } else {
            for (var i = 0; i < versions.length ; i++) {
                try {
                    _myAjax = new ActiveXObject(versions[i]);

                    if (_myAjax) {
                        _ms_XMLHttpRequest_ActiveX = versions[i];
                        break;
                    }
                }
                catch (objException) {
                // trap; try next one
                }

                ;
            }

            ;
        }
    }

    return _myAjax;
}



// handle some key press events
function handleKeyUp( e ) {
    e = (!e) ? window.event : e;
    target = (!e.target) ? e.srcElement : e.target;
    logger('keyup');

    if (e.type == "keyup") {
        // skip shift, alt, control keys
        if (e.keyCode == 16 || e.keyCode == 17 || e.keyCode == 18) {
        // do nothing
        }

        else {
            if (target.name == "state1" && !$('state1').value) {
                clearCustomersByState();
            } else if (target.name == "state2" && !$('state2').value) {
                clearCustomersByStateXML();
            } else if (target.name == "google_search") {
                if (target.value) {
                    getSuggest(target);
                } else {
                    $('google_suggest_target').innerHTML = "";
                }
            }
        }
    }
}

<!------------------------------- BEGIN PING ---------------------------------------------------->


function ping() {
    var url = '/custom/servlet/ReaderListener';
    initAJAX("post", url, function () {
	    if ( _ajax.readyState == 4 ) {
		    logger("Response from the server: " + _ajax.responseText);
		    //$("ping_status").innerHTML =  _ajax.responseText;
            $('content:ping_status').value =  _ajax.responseText;
  		    
	    }
    });
    logger("Sent To The Server: value=" + encode('teste'));
    _ajax.send("value=" + encode('54545'));
   
}


function pingjsf() {
    var url = '/admin/jsp/body/sealMonitor.jsf';
    initAJAX("post", url, function () {
        if ( _ajax.readyState == 4 ) {
            logger("Response from the server: " + _ajax.responseText);
            //$("ping_status").innerHTML =  _ajax.responseText;
           $('content-panel').innerHTML =  _ajax.responseText;
            
        }
    });
    logger("Sent To The Server: value=" + encode('teste'));
    _ajax.send("value=" + encode('54545'));
   pingjsfAgain();
}



function buildReport(req) {
    var select = document.getElementById("ping_status");
    var items = req.responseXML.getElementsByTagName("report");
    // loop through <item> elements, and add each nested
    // <title> element to Topics select element
    for (var i = 0; i < items.length; i++) {
        appendToSelect(select, i,
            document.createTextNode(getElementTextNS("", "epc", items[i], 0)));
    }
    // clear detail display
    //document.getElementById("ping_status").innerHTML = "";
}

// retrieve text of an XML document element, including
// elements using namespaces
function getElementTextNS(prefix, local, parentElem, index) {
    var result = "";
    if (prefix && isIE) {
        // IE/Windows way of handling namespaces
        result = parentElem.getElementsByTagName(prefix + ":" + local)[index];
    } else {
        // the namespace versions of this method 
        // (getElementsByTagNameNS()) operate
        // differently in Safari and Mozilla, but both
        // return value with just local name, provided 
        // there aren't conflicts with non-namespace element
        // names
        result = parentElem.getElementsByTagName(local)[index];
    }
    if (result) {
        // get text, accounting for possible
        // whitespace (carriage return) text nodes 
        if (result.childNodes.length > 1) {
            return result.childNodes[1].nodeValue;
        } else {
            return result.firstChild.nodeValue;    		
        }
    } else {
        return "n/a";
    }
}
function spam( num ) {
    $('spam_time').innerHTML = "Initiating " + num + " XMLHttpRequests.";
    var startTime = new Date();
    startAJAX();

    //_logger = false;
    for (var i = 1; i <= num; i++) {
        initAJAX("post", '/ThinkCAP/servlet/ping', null, false);
        _ajax.send("hide=1&num=" + i);
    }

    var endTime = new Date()
    var millis = endTime - startTime;
    _logger = true;
    logger("time to spam the server with " + num + " requests: " + (millis / 1000).toFixed(2) + " seconds. " + (millis / 1000 / num).toFixed(2) + " seconds per request.");
    $('spam_time').innerHTML = num + " requests took " + (millis / 1000).toFixed(2) + " seconds. " + (millis / 1000 / num).toFixed(2) + " seconds per request.";
}

function getBigFile() {
    // This is a fully encapsulated XMLHTTP Request
    // The return function is included inside this
    // Also we add a pointer back to this function's instance of _myAjax so that it can be aborted
    var self = this;
    // we don't use "this" throughout the function because it won't work in the anonymous function declaration
    self._myAjax = startAJAX();
    self._myCount = 0;
    self._big_file_status = $('big_file_status');
    self._big_file_status.value = 'Retrieving Large File...';
    $('big_file_status_button').style.display = 'inline';
    $('big_file_status_button').ajaxHandle = self._myAjax;

    self._myAjax.onreadystatechange = function( ) {
        // document.all is only valid on IE
        // currently IE fails when readyState 3 is checked
        if (!(document.all) && self._myAjax.readyState == 3) {
            self._myCount++;

            if ((self._myCount % 10) == 0) {
                self._big_file_status.value = "readyState = 3 - called " + _myCount + " times.";
            }
        } else if (self._myAjax.readyState == 4) {
            try {
                if (self._myAjax.status == 200) {
                    self._big_file_status.value = "Done (" + self._myAjax.responseText.length + " bytes) | Server Message: " + self._myAjax.statusText;
                } else {
                    alert("There was a problem retrieving the XML data:\n" + self._myAjax.statusText);
                    self._big_file_status.value = "** ERROR **" + self._myAjax.statusText;
                }
            }
            catch (e) {
            //
            }

            $('big_file_status_button').style.display = 'none';
        }
    };


    // show that in mozilla you can specify an on error event
    // IE has no such event
    // You can also specify onload and onprogress as well in mozilla
    // to see this function run, kill your app server while downloading the big file
    // both onerror and onreadystatechange will be fired 
    if (!document.all) {
        self._myAjax.onerror = function( ) {
            alert('Error: ' + self._myAjax.status + " count:" + self._myCount)
        }
    }

    self._myAjax.open("POST", "/ThinkCAP/servlet/getBigFile", true);
    self._myAjax.send('');
}

function stopBigFile() {
    var status = $('big_file_status_button');

    if (status) {
        status.style.display = 'none';

        if (status.ajaxHandle) {
            status.ajaxHandle.abort();
            status.ajaxHandle = null;
        }
    }
}

<!--------------------------------- END PING ---------------------------------------------------->

<!--------------------------------- BEGIN TRACK CHANGES------------------------------------------>


function trackChanges( field, event ) {
    var self = this;
    self.field = field;

    if (event) {
        if (event.type == "keyup") {
            if (event.keyCode == 16 || event.keyCode == 17 || event.keyCode == 18) {
                // do nothing
                return;
            }
        }
    }

    var type = field.type;
    var last_value = field.value;
    logger("--- track changes for " + field.name + ", value=" + field.value);

    if (type == "checkbox") {
        last_value = field.checked;
    }

    var url = '/ThinkCAP/servlet/field_tracker?field_name=' + encode(field.name) + '&last_value=' + encode(last_value);

    initAJAX("POST", url, function( ) {
        if (_ajax.readyState == 4) {
            if (_ajax.status == 200) {
                var s = self.field.name + "_info";
                $(s).innerHTML = "  -> From Server: <b>" + _ajax.responseText + "</b>";
            } else {
                alert("There was a problem retrieving the XML data:\n" + _ajax.statusText);
            }
        }
    });

    _ajax.send('');
}

<!----------------------------------- END TRACK CHANGES------------------------------------------>


<!------------------------------------- BEGIN SELECT ------------------------------------------->


function clearSelect() {
    for (var i = 0; i < arguments.length; i++) {
        var element = arguments[i];

        if (typeof element == 'string')
            element = document.getElementsByName(element)[0];

        if (element && element.options) {
            element.options.length = 0;
            element.selectedIndex = -1;
        }
    }
}

/*
 * getSelect1: Creates a local XMLHTTPRequest and sets the onreadystatechange
 */

function getSelect1( key, dataset, target ) {
    // This is an example of a fully encapsulated request + response
    var self = this; // allows us to reference this method within the processGetSelect method
    self._myAjax = startAJAX();
    self._myAjax.onreadystatechange = function( ) {
        if (self._myAjax.readyState == 4) {
            if (self._myAjax.status == 200) {
                var response = self._myAjax.responseText;
                logger(response);
                eval(response);
                return true;
            } else {
                alert("There was a problem retrieving the XML data:\n" + self._myAjax.statusText);
                return false;
            }
        }
    }

    self.URL = '/ThinkCAP/servlet/get_select';
    self.data = "dataset=" + dataset + '&target=' + target + '&key=' + key;
    self._myAjax.open("POST", self.URL, true);
    self._myAjax.setRequestHeader("Connection", "close");
    self._myAjax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    logger('Sending request to: ' + self.URL);
    self._myAjax.send(self.data);
}

/*
 * AJAXRequest: An encapsulated AJAX request. To run, call
 * new AJAXRequest( method, url, async, process, data )
 *
 */

function executeReturn( AJAX ) {
    if (AJAX.readyState == 4) {
        if (AJAX.status == 200) {
            logger('execute return: ' + AJAX.readyState + "/" + AJAX.status + "/" + AJAX.statusText);
            eval(AJAX.responseText);
        }
    }
}

function AJAXRequest( method, url, data, process, async ) {
    var self = this;
    self.AJAX = startAJAX();

    if (typeof process == 'undefined') {
        process = executeReturn;
    }

    self.process = process;

    self.AJAX.onreadystatechange = function( ) {
        logger("AJAXRequest Handler: State =  " + self.AJAX.readyState);

        if (self.AJAX.readyState == 4) {
            self.AJAXStatus = self.AJAX.status;
            self.AJAXResponseText = self.AJAX.responseText;
            self.AJAXResponseXML = self.AJAX.responseXML;
            self.AJAXStatusText = self.AJAX.statusText;
            self.process(self.AJAX);
        }
    }

    if (!method) {
        method = "POST";
    }

    method = method.toUpperCase();

    if (typeof async == 'undefined') {
        async = true;
    }

    logger("----------------------------------------------------------------------");
    logger("AJAXREQUEST: " + ((async) ? "Async" : "Sync") + " " + method + ": URL: " + url + ", Data: " + data);

    self.AJAX.open(method, url, async);

    if (method == "POST") {
        self.AJAX.setRequestHeader("Connection", "close");
        self.AJAX.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        self.AJAX.setRequestHeader("Method", "POST " + url + "HTTP/1.1");
    }

    self.AJAX.send(data);
}


/*
 *  getSelect2: Calls the AJAXRequest and passes a pointer to the executeReturn function
 */

function getSelect2( key, dataset, target ) {
    return AJAXRequest("POST",  '/ThinkCAP/servlet/get_select',
                       'dataset=' + dataset + '&target=' + target + '&key=' + key, executeReturn,
                       true);
}

/*
 *  getSelect3: Calls the AJAXRequest and passes a new function 
 */
function getSelect3( key, dataset, target ) {
    return AJAXRequest("POST", '/ThinkCAP/servlet/get_select',
                       'dataset=' + dataset + '&target=' + target + '&key=' + key, function( _ajax ) {
        if (_ajax.readyState == 4 && _ajax.status == 200) {
            eval(_ajax.responseText)
        }
    });
}

<!------------------------------------- END SELECT --------------------------------------------->

<!--------------------------------- BEGIN "GET FORM VALUES" ------------------------------------>


function getFormData( value, dataset, target, parm2, parm3 ) {
    var url = '/ThinkCAP/servlet/get_formdata?dataset=' + dataset + '&target=' + target + '&value=' + value;
    logger(_ajax.readyState);
    initAJAX("post", url, processFormData);
    _ajax.send(null);
}

function processFormData() {
    logger('process form data:' + _ajax.readyState)

    if (_ajax.readyState == 4) {
        if (_ajax.status == 200) {
            var response = _ajax.responseText;
            logger("return from processFormData");
            logger(response);
            eval(response);
        } else {
            alert("There was a problem retrieving the XML data:\n" + _ajax.statusText);
        }
    }
}

function getFormValues( form ) {
    form = $(form);
    var field = "";
    var value = "";
    var valueString = "";
    var replaceID = "";
    var parentNode = form.parentNode;

    for (var i = 0; i < form.elements.length; i++) {
        var field = form.elements[i];

        if (!field.disabled) {
            filedName = encode(field.name);

            if (field.type == 'select-one') {
                if (field.selectedIndex > -1) {
                    value = field.options[field.selectedIndex].value;
                }
            } else {
                value = field.value;
            }

            valueString += ((i) ? '&' : '') + field.name + '=' + encode(value);
        }
    }

    return valueString
}

function saveForm( form, extravalues, process ) {
    // process is an optional parameter
    var form = $(form);

    if (!form.method) {
        form = $(form.form);
    }

    if (form) {
        var valueString = getFormValues(form) + (! !(extravalues) ? extravalues : '');
        valueString += "&AJAX_FORM_NAME=" + encode(form.id);
        var functionToCall = null;

        if (typeof process == 'function') {
            functionToCall = process;
        } else {
            functionToCall = executeReturn; // generic function will execute what is returned
        }

        new AJAXRequest('POST', form.action, valueString, functionToCall);
    }
}

function saveCustomerForm( form ) {
    saveForm(form, null, function( AJAX ) {
        if (AJAX.readyState == 4) {
            if (AJAX.status == 200) {
                $('customerFormStatus').innerHTML = "<img src='./checkbox.gif' border=0 /> " + AJAX.statusText;
                $('customerFormStatus').style.color = null;
                form.save.disabled = true
            } else {
                $('customerFormStatus').innerHTML = AJAX.statusText
                $('customerFormStatus').style.color = 'red';
            }
        }
    });

    // do not submit form if this is a submit button
    return false;
}

<!----------------------------------- END "GET FORM VALUES" ------------------------------------->

<!----------------------------------- BEGIN CUSTOMERS BY STATE (NON-XML) ------------------------>


function getCustomersByState( state, target ) {
    // if no state given then clear out
    if (!state) {
        clearCustomersByState();
    } else {
        var state1_div = $('state1_div');
        state1_div.style.visibility = 'visible';
        return new AJAXRequest("post", "/ThinkCAP/servlet/get_customers", "state=" + encode(state),
                               processGetCustomersByState);
    }
}

function clearCustomersByState() {
    var state1_div = $('state1_div');
    state1_div.innerHTML = "";
    state1_div.style.visibility = 'hidden';
    document.getElementById("state1_info").innerHTML = "";
}

function processGetCustomersByState( myAJAX ) {
    if (myAJAX.readyState == 4) {
        if (myAJAX.status == 200) {
            var response = myAJAX.responseText;
            // Resonse text is in this format:
            //    state=XXX&customers=cust1!cust2!cust3!cust4
            var customers;

            var returnParms = response.split('&');

            for (var i = 0; i < returnParms.length; i++) {
                var pair = returnParms[i].split('=');

                if (pair[0] == "customers") {
                    customers = pair[1];

                    if (customers == "") {
                        custList = "No customers found.";
                        document.getElementById("state1_info").innerHTML = "";
                    } else {
                        var custList = customers.split("!");
                        document.getElementById("state1_info").innerHTML
                            = "&nbsp;&nbsp;&nbsp;" + custList.length + " customer" + (custList.length == 1 ? "" : "s") + " found";
                    }
                }
            }

            var div = document.getElementById("state1_div");

            if (div) {
                div.innerHTML
                    = "<ol style=\"padding-left:2em\"><li>" + decode(custList.join("<\/li><li>")) + "<\/li><\/ol>";
                div.style.visibility = "visible"
            }
        } else {
            alert("There was a problem retrieving the XML data:\n" + myAJAX.statusText);
        }
    }
}

<!------------------------------------- END CUSTOMERS BY STATE (NON-XML) ------------------------>


<!----------------------------------- BEGIN CUSTOMERS BY STATE WITH XML  ------------------------>


function getCustomersByStateXML( state ) {
    if (!state) {
        clearCustomersByStateXML();
    } else {
        return new AJAXRequest("post", "/ThinkCAP/servlet/get_customersXML", "state=" + encode(state), processGetCustomersByStateXML);
    }
}

function clearCustomersByStateXML() {
    var state2_div = document.getElementById("state2_div");
    state2_div.innerHTML = "Please enter a state...";
    $("state2_info").innerHTML = "";
}

function processGetCustomersByStateXML( myAJAX ) {
    if (myAJAX.readyState == 4) {
        if (myAJAX.status == 200) {
            logger(myAJAX.responseText);
            xml = myAJAX.responseXML;
            // Resonse text is in this format:
            //    state=XXX&customers=cust1!cust2!cust3!cust4

            var div = "";

            if (xml.documentElement) {
                var state = xml.documentElement.getElementsByTagName("state")[0].firstChild.nodeValue;
                var customers = xml.documentElement.getElementsByTagName("customer");

                if (customers.length <= 0) {
                    div += "<tr><td class=\"customers_by_state_error\">No customers found for state: <strong>" + state
                               + "<\/strong><\/td><\/tr>";
                } else {
                    document.getElementById("state2_info").innerHTML
                        = "&nbsp;&nbsp;&nbsp;" + customers.length + " customer" + 
				(customers.length == 1 ? "" : "s") + " found"

                    div += "<table>";

                    for (var i = 0; i < customers.length; i++) {
                        var cust = customers[i];
                        var id = customers[i].getAttributeNode("custnum").nodeValue;
                        var name = decode(customers[i].firstChild.firstChild.nodeValue);

                        div += "<tr>"
                        div += "<td class=\"cust_num\">" + id + "<\/td><td class=\"cust_name\">" + name + "<\/td>";
                        div += "<\/tr>";
                    }
                }

                div += "<\/table>";

                var state2_div = document.getElementById("state2_div");

                if (state2_div) {
                    state2_div.innerHTML = div;
                    state2_div.style.display = 'block';
                }
            }
        } else {
            alert("There was a problem retrieving the XML data:\n" + myAJAX.statusText);
        }
    }
}

<!------------------------------------- END CUSTOMERS BY STATE (XML) ------------------------>

<!------------------------------------- BEGIN SUGGEST --------------------------------------------->


// this function is called when the call to the google_suggest servlet (AJAXSuggest) is finished
// google returns 'sendRPCDone( .... )'
function sendRPCDone( notUsed, search_term, term_array, results_array, unused_array ) {
    var div = "<table>";

    if (results_array.length == 0) {
        div += "<tr><td class=\"search_error\">No results found for <strong>" + search_term + "<\/strong><\/td><\/tr>";
    } else {
        for (var i = 0; i < results_array.length; i++) {
            div += "<tr><td class=\"search_term\"><a href='http://www.google.com/search?q=" + encode(term_array[i]);
            div += "'>" + term_array[i] + '<\/a><\/td><td class="number_of_results">' + results_array[i]
                       + "<\/td><\/tr>";
        }
    }

    div += "<\/table>";
    var google_suggest_target = document.getElementById("google_suggest_target");

    if (google_suggest_target) {
        google_suggest_target.innerHTML = div;
    }
}

function clearSuggest() {}

function getSuggest( field ) {
    return new AJAXRequest("POST", "/ThinkCAP/servlet/ajax_suggest", "qu=" + encode(field.value));
}

<!------------------------------------- END SUGGEST --------------------------------------------->

_description = new Object();
_description.div_ping = function( ) {
    var desc = "<b>Ping</b><br>Ping sends the current date to the server. Basic AJAX functionality. See server console for date.";
    desc += "<br><br><b>Spam</b><br>Sends 10 pings to the server to show how long it takes to send 1 request to the server.";
    desc += "<br><br><b>Big File</b><br>Returns up to 10 megs from the server. Shows # of times onreadystatechange has been called (its a lot!).";
    desc += "<br>While the file is downloaded, you can work on other fields and see server activity. This shows that the behavior is truly asynchronous and multiple requests can be handled.";
    desc += "<br>The server stops sending data after 60 seconds so your file size might be smaller if you have a slower connection.";
    desc += "<br>If you shut down the server while downloading the big file, you will see an error message.";
    return desc;
};

_description.div_track_changes = function( ) {
    var desc = "<b>Track Changes As Fields Change</b><br>";
    desc += "As each field changes, the change is sent to the server. If all is well, the word OK shows up next to the field along with the text sent to the server.";
    desc += "<br>The server console shows the values sent to the server.";
    desc += "<br><br><b>Track Changes On Key Up</b><br>";
    desc += "As each key is released, the current value of the field is sent to the server. If all is well, the word OK shows up next to the field.";
    desc += "<br>The server console shows the values sent to the server.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.div_dropdowns = function( ) {
    var desc = "<b>Drop Downs & Form Handling</b><br>";
    desc += "An AJAX call is made to the server to get the states and populate the select box (drop down). ";
    desc += "The server dynamically creates Javascript that is executed through an eval statement.";
    desc += "The final line of the Javascript calls the onchange event of the state drop down which then retrieves all the cities for that state.";
    desc += "<br><br>The city drop down is populated and its onchange event retrieves all the customers for a given city.";
    desc += "<br><br>When the customer changes, the server returns Javascript that replaces all customer fields with the appropriate values.";
    desc += "If you open the Track Changes section, you'll notice that both address fields on the page are updated.";
    desc += "<br><br>In the samples, if you add a new INPUT field that matches a column in the customer table, it will be populated for you during runtime";
    desc += "<br><br>The routines to retrieve the state, city, and customer dropdowns all use variations of encapsulating XMLHTTPRequests instead of using a global XMLHTTPRequest (_ajax).";
    desc += "<br><br><b>Save</b><br>Sends the contents of the customer form back to the server. A message is shown indicating a successful save or a failure.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.div_customers_by_state = function( ) {
    var desc = "<b>Retrieve Customers By State (non-XML)</b><br>";
    desc += "This calls the server when the state is changed. The server returns a data set that includes the customer ID and the customer name. ";
    desc += "<br><br>Each pair is delimited by a comma, and each set is delimited by a !";
    desc += "<br><br>To see all customers, enter a '%' sign.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.div_customers_by_state_xml = function( ) {
    var desc = "<b>Retrieve Customers By State (XML)</b><br>";
    desc += "This calls the server when the state is changed. The server returns a data set that includes the customer ID and the customer name. ";
    desc += "<br><br>The data is returned back as an XML document and the returnXML property of the XMLHTTPRequest is parsed to pull out the data.";
    desc += "<br><br>To see all customers, enter a '%' sign.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.div_google_suggest_hack = function( ) {
    var desc = "<b>Google Suggests Hack</b><br>";
    desc += "This fires on each keystroke and calls the server passing the current search field.";
    desc += "<br><br>The server in turn calls the google suggest URL and then passes the results back to the user's browser.";
    desc += "<br><br>Google returns a Javascript method call to sendRPCDone(). We have our own sendRPCDone that loops through the results and presents the top 10 hits + # of pages.";
    desc += "<br><br>The reason Google is not called directly from the browser is that XMLHTTPRequest can only be used to call the orignal server or a trusted server setup in the Browser settings.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.div_activity_log = function( ) {
    var desc = "<b>Activity Log</b><br>";
    desc += "Shows various activity logs for each of the examples.";
    desc += "<br><br>The log is shown with the most recent log item at the top of the list.";
    desc += "<br><br>To write to this log, use the logger() function in the Javascript.";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    return desc;
};

_description.default_text = function( ) {
    var desc = "<b>Basic AJAX Examples</b><br>";
    desc += "<br><b>The downloadable samples include full JavaScript & Java Source, a sample database for any SQL database, and a PowerPoint presentation.</b>";
    desc += "<br><br>This sampler shows some basic techniques to demonstrate some of the possibilities of using AJAX/XMLHTTPRequest techniques in web applications.";
    desc += "<br><br>Basic browser & server interaction is demonstrated along with dynamic population of visual elements such as DIVs and SELECT drop downs.";
    desc += "<br><br>In addition, many ways of instantiating & encapsulating XMLHTTPRequest objects are shown in the Javascript.";
    desc += "<br><br>This sample is freeware and has no copyright restrictions whatsoever. It is doesn't carry a warranty or a guarantee.";
    desc += "<br><br>Written by Steve Benfield.<br><a href='mailto:sbenfield@clearnova.com&subject=AJAX Samples' style='color:white' alt='email the author'>sbenfield@clearnova.com</a>";
    desc += "<br><br>";
    desc += "ClearNova provides ThinkCAP&trade;, a framework & visual workbench ideally suited for building Rich Internet Applications using AJAX techniques.";
    desc += "<br>For information on ThinkCAP, visit <a style='color:white' href='http://www.clearnova.com/thinkcap'>www.clearnova.com<\/a>";
    desc += "<br><br><h3>For Best Performance, Hide the Activity Log</h3>"
    desc += "<br><br><h3>This page is running on a development server--uptime is not guaranteed.</h3>"
    desc += "<br><br>Last Updated: July 15, 2005 | Version 1.1";

    return desc;
}

function toggleDiv( element ) {
    var e = $(element);

    if (e) {
        e.style.display = ((e.style.display != 'block') ? 'block' : 'none');
        $('div_upper_right').innerHTML = _description[e.id]();
    }
} 

function monitorSubmit() {
   

    document.forms['linkDummyForm'].submit();
    
 
}   
  function submitAgain() {
   tStart   = new Date();

   timerID  = setTimeout("monitorSubmit();", 5000);
}

 function pingjsfAgain() {
   tStart   = new Date();

   timerID  = setTimeout("pingjsf();", 10000);
}




