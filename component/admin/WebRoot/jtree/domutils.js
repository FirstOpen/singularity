
// First, make sure all browsers have necessary ECMAScript v3 
// and DOM Level 1 properties

/** @type undefined */
var undefined;

/** @type Object */
var Node = Node ? Node : {};
/** @type number */
Node.ELEMENT_NODE 					= 1;
/** @type number */
Node.ATTRIBUTE_NODE 				= 2;
/** @type number */
Node.TEXT_NODE 						= 3;
/** @type number */
Node.CDATA_SECTION_NODE 			= 4;
/** @type number */
Node.ENTITY_REFERENCE_NODE 			= 5;
/** @type number */
Node.ENTITY_NODE 					= 6;
/** @type number */
Node.PROCESSING_INSTRUCTION_NODE 	= 7;
/** @type number */
Node.COMMENT_NODE 					= 8;
/** @type number */
Node.DOCUMENT_NODE 					= 9;
/** @type number */
Node.DOCUMENT_TYPE_NODE 			= 10;
/** @type number */
Node.DOCUMENT_FRAGMENT_NODE 		= 11;
/** @type number */
Node.NOTATION_NODE 					= 12;


/**
 *	String convenience method to trim leading and trailing whitespace.
 *
 *	@returns string
 */
String.prototype.trim = function () {
	return this.replace(/^\s*(.+)/gi,"$1").replace(/\s*$/gi,"");
};

/**
 *	String convenience method for checking if the end of this string equals
 *	a given string.
 *
 *	@returns boolean
 */
String.prototype.endsWith = function (s) {
	if ("string" != typeof s) {
		throw("IllegalArgumentException: Must pass a string to " +
				"String.endsWith()");
	}
	var start = this.length - s.length;
	return this.substring(start) == s;
};

/**
 *	Array convenience method to check for membership.
 *
 *	@param object element
 *	@returns boolean
 */
Array.prototype.contains = function (element) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == element) {
			return true;
		}
	}
	return false;
};

/**
 *	Array convenience method to remove element.
 *
 *	@param object element
 *	@returns boolean
 */
Array.prototype.remove = function (element) {
	var result = false;
	var array = [];
	for (var i = 0; i < this.length; i++) {
		if (this[i] == element) {
			result = true;
		} else {
			array.push(this[i]);
		}
	}
	this.clear();
	for (var i = 0; i < array.length; i++) {
		this.push(array[i]);
	}
	array = null;
	return result;
};

/**
 *	Array convenience method to clear membership.
 *
 *	@param object element
 *	@returns void
 */
Array.prototype.clear = function () {
	this.length = 0;
};

/**
 *	Array convenience method to add stack functionality to <code>Array</code>s
 *	in browsers that do not support ECMAScript v3.
 *
 *	@param object element
 *	@returns number
 */
Array.prototype.push = function (element) {
	this[this.length] = element;
	return this.length;
};

/**
 *	Array convenience method to add set functionality to <code>Array</code>s
 *	... if the element is already a member of this array, return false. 
 *	Otherwise, add it and return true.
 *
 *	@param object element
 *	@returns boolean
 */
Array.prototype.add = function (element) {
	if (this.contains(element)) {
		return false;
	}
	this.push(element);
	return false;
};

/**
 *	Array convenience method to add set functionality to <code>Array</code>s
 *	... Uniquely adds all elements in the array parameter to this array. 
 *	Returns true if any new elements were added to this array. False otherwise.
 *
 *	@param Array that
 *	@returns boolean
 */
Array.prototype.addAll = function (that) {
	var result = false;
	for (var i = 0; i < that.length; i++) {
		if (this.add(that[i])) {
			result = true;
		}
	}
	return true;
};

/**
 *	A utility class that exists to combine common DOM algorithms and utilities
 *	into a single class as static methods and constants. This class is not 
 *	meant to be subclassed.
 *
 *	@constructor 
 */
function DomUtils() {}

/**
 *	@param Element target
 *	@returns void
 */
DomUtils.show = function (target) {
	target.style.display = "";
};

/**
 *	@param Element target
 *	@returns void
 */
DomUtils.hide = function (target) {
	target.style.display = "none";
};

/**
 *	@param Element target
 *	@returns boolean
 */
DomUtils.isShowing = function (target) {
	return target.style.display.toLowerCase() != "none";
};

/**
 *	Toggles <code>target</code>'s visibility.
 *	@param Element target
 *	@returns void
 */
DomUtils.toggle = function (target) {
	if (DomUtils.isShowing(target)) {
		DomUtils.hide(target);
	} else {
		DomUtils.show(target);
	}
};

/**
 *	@param Node target
 *	@param string k
 *	@returns void
 */
DomUtils.addClassName = function (target,k) {
	if(!DomUtils.isElementNode(target)) {
		throw "Attempting to add a className to a non-Element Node";
	}
	var classNames = target.className.split(/\s+/g);
	if (classNames.contains(k)) {
		return;
	} else {
		classNames.push(k);
	}
	target.className = classNames.join(" ");
	target.className = target.className.trim();
};

/**
 *	@param Node target
 *	@param string k
 *	@returns void
 */
DomUtils.removeClassName = function (target,k) {
	if (!DomUtils.isElementNode(target)) {
		throw "Attempting to remove a className to a non-Element Node";
	}
	var classNames = target.className.split(/\s+/g);
	if (!classNames.contains(k)) {
		return;
	} else {
		classNames.remove(k);
	}
	target.className = classNames.join(" ");
	target.className = target.className.trim();
};

/**
 *	@param Node target
 *	@param string k
 *	@returns void
 */
DomUtils.toggleClassName = function (target,k) {
	if (DomUtils.hasClassName(target,k)) {
		DomUtils.removeClassName(target,k);
	} else {
		DomUtils.addClassName(target,k);
	}
};

/**
 *	Tests to see if <code>target</code>'s <code>getNodeType()</code> 
 *	method returns <code>Node.ELEMENT_NODE</code>.
 *	@param Element target
 *	@returns boolean
 */
DomUtils.isElementNode = function (target) {
	return Node.ELEMENT_NODE == target.nodeType;
};

/**
 *	Tests to see if <code>target</code>'s <code>getTagName()</code> 
 *	method returns <code>Node.ELEMENT_NODE</code>.
 *	@param Element target
 *	@returns boolean
 */
DomUtils.hasTagName = function (target,tagName) {
	return target.tagName.toLowerCase() == tagName.toLowerCase();
};

/**
 *	@param Element target
 *	@param string id
 *	@returns boolean
 */
DomUtils.hasId = function (target,id) {
	return target.id == id;
};

/**
 *	@param Element target
 *	@param string className
 *	@returns boolean
 */
DomUtils.hasClassName = function (target,className) {
	
	function _isLastOfMultpleClassNames(all,className) {
		var spaceBefore = all.lastIndexOf(className)-1;
		return all.endsWith(className) && 
			all.substring(spaceBefore,spaceBefore+1) == " ";
	}

	className = className.trim();
	var cn = target.className;
	if (!cn) {
		return false;
	}
	cn = cn.trim();
	if (cn == className) {
		return true;
	}
	if (cn.indexOf(className + " ") > -1) {
		return true;
	}
	if (_isLastOfMultpleClassNames(cn,className)) {
		return true;
	}
	return false;
};

/**
 *	@param Node target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstAncestorOrSelfByClassName = function (target,
													   className) {
	var parent = target;
	do {
		if (DomUtils.isElementNode(parent) && 
				DomUtils.hasClassName(parent,className)) {
			return parent;
		}
	} while (parent = parent.parentNode);
	return null;
};

/**
 *	@param Node target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstAncestorByClassName = function (target,className) {
	var parent = target;
	while (parent = parent.parentNode) {
		if (DomUtils.isElementNode(parent) && 
			DomUtils.hasClassName(parent,className)) {
			return parent;
		}
	}
	return null;
};

/**
 *	@param Node target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstChildByClassName = function (target,className) {
	var kids = target.childNodes;
	for (var i = 0; i < kids.length; i++) {
		var kid = kids[i];
		if (DomUtils.isElementNode(kid) && 
			DomUtils.hasClassName(kid,className)) {
			return kid;
		}
	}
	return null;
};

/**
 *	@param Node target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstChildByTagName = function (target,tagName) {
	var kids = target.childNodes;
	for (var i = 0; i < kids.length; i++) {
		var kid = kids[i];
		if (DomUtils.isElementNode(kid) && 
			DomUtils.hasTagName(kid,tagName)) {
			return kid;
		}
	}
	return null;
};

/**
 *	@param Node target
 *	@param string className
 *	@returns Array
 */
DomUtils.getChildrenByClassName = function (target,className) {
	var result = [];
	var kids = target.childNodes;
	for (var i = 0; i < kids.length; i++) {
		var kid = kids[i];
		if (DomUtils.isElementNode(kid) && 
			DomUtils.hasClassName(kid,className)) {
			result.push(kid);
		}
	}
	return result;
};



/**
 *	Retreives <code>target</code>'s first descendant element with an
 *	HTML <code>class</code> attribute value that includes <code>
 *	className</code> using a breadth-first algorithm.
 *	
 *	@param Element target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstDescendantByClassNameBreadthFirst = function (target,
															   className) {
	var result;
	if (result = DomUtils.getFirstChildByClassName(target,className)) {
		return result;
	}
	for (var i = 0; i < target.childNodes.length; i++) {
		result = DomUtils.getFirstDescendantByClassNameBreadthFirst(
									target.childNodes.item(i),
									className );
		if (result) {
			return result;
		}
	}
	return null;
};

/**
 *	Retreives <code>target</code>'s first descendant element with an
 *	HTML <code>class</code> attribute value that includes <code>
 *	className</code> using a depth-first algorithm.
 *	
 *	@param Element target
 *	@param string className
 *	@returns Element
 */
DomUtils.getFirstDescendantByClassNameDepthFirst = function (target,
															 className) {
	var child;
	var result;
	for (var i = 0; i < target.childNodes.length; i++) {
		child = target.childNodes.item(i);
		if (DomUtils.isElementNode(child) && 
			DomUtils.hasClassName(child,className)) {
			return child;
		}
		result = DomUtils.getFirstDescendantByClassNameDepthFirst(
												target.childNodes.item(i),
												className );
		if (result) {
			return result;
		}
	}
	return null;
};

/**
 *	Returns all descendant elements of <code>target</code> with HTML <code>
 *	class</code> attribute values that contain <code>className</code>
 *	as an Array. NOTE that unlike the
 *	algorithms specified in the DOM spec, a <code>NodeList</code> is NOT
 *	returned. This method searched for all descendants of <code>target
 *	</code> meeting the criteria using a breadth-first algorithm.
 *
 *	@param Element target
 *	@param string className
 *	@returns Element
 */
DomUtils.getDescendantsByClassName = function (target,className) {
	var result = [];
	result.addAll(DomUtils.getChildrenByClassName(target,className));
	for (var i = 0; i < target.childNodes.length; i++) {
		result.addAll(DomUtils.getDescendantsByClassName(
										target.childNodes.item(i),
										className));
	}
	return result;
};


/**
 *	@constructor
 *	@param MouseEvent evt
 */
function Evt(evt) {
	this._evt 	 = evt ? evt : window.event;
	this._source = this._evt.currentTarget ? 
				   this._evt.currentTarget : this._evt.srcElement;
}

/**
 *	@returns Element
 */
Evt.prototype.getSource = function () {
	return this._source;
};

/**
 *	@returns void
 */
Evt.prototype.consume = function () {
	if (this._evt.stopPropagation) {
		this._evt.stopPropagation();
		this._evt.preventDefault();
	} else if (this._evt.cancelBubble) {
		this._evt.cancelBubble = true;
		this._evt.returnValue  = false;
	}
};







/**
 *	<p>Bean that represents individual Cookie objects, packaging their 
 *	attributes into a convenient API that can more easily be manipulated and
 *	set.</p><p>Also contains 2 static convenience methods for setting and
 *	the cookies for the current page.
 *	@author		Todd Ditchendorf
 *	@constructor	Accepts initial values for the <code>name</code> and 
 *				<code>value</code> attribute values.
 *	@param	String	name		Initial <code>name</code> attribute value.	
 *	@param	String	value	Initial <code>value</code> attribute value.	
 */	
Cookie = function (name,value) {
	this._name;
	this._value;
	this._path;
	this._secure;
	this._domain;
	this._expires;
	if (name) {
		this.setName(name);
	}
	if (value) {
		this.setValue(value);
	}
};

// Static attributes
Cookie.EQUALS 			= "=";
Cookie.DELIM 				= "; ";
Cookie.PATH 				= "path";
Cookie.SECURE 			= "secure";
Cookie.DOMAIN 			= "domain";
Cookie.EXPIRES 			= "expires";
Cookie.SUB_VALUE_DELIM 	= ":"

/**
 *	Static convenience method that sets the given cookie for the current page.
 *	@param	Cookie	c	<code>Cookie</code> object to be set.
 *	@returns	void
 */
Cookie.addPageCookie = function (c) {
/*	if (!(c instanceof Cookie)) {
		throw new Error("IllegalArgumentException: Cookie.set() accepts only" +
			" one parameter of type Cookie");
	}*/
	document.cookie = c.toCookieString();
};

/**
 *	Static convenience method that returns all the <code>document.cookie</code>
 *	String for the current page.
 *	@returns	String
 */
Cookie.getPageCookieString = function () {
	return document.cookie;
};

/**
 *	Static convenience method that checks the <code>document.cookie</code>
 *	String for the current page to see if the give <code>Cookie</code> exists.
 *	@param	Cookie	c
 *	@returns	boolean
 */
Cookie.pageHasCookieWithNameAndValue = function (c) {
	var s = c.toNameValueString();
	return (Cookie.get().indexOf(s) > -1);
};

Cookie.pageHasCookieWithName = function (c) {
	var s = c.getName();
	return (Cookie.get().indexOf(s) > -1);
};

/**
 *	Returns a String representation of this <code>Cookie</cookie> in the 
 *	standard cookie syntax.
 *	@returns	String
 */
Cookie.prototype.toCookieString = function () {
	var buff = new StringBuffer();
	// name=value;
	buff.append(this.getName()).append(Cookie.EQUALS);
//		if (this.getValue() instanceof Array) {
	if (typeof this.getValue() == "object" && 
		this.getValue().constructor == Array) {
		var values = this.getValue();
		for (var i = 0; i < values.length; i++) {
			buff.append(values[i])
				.append(i == values.length-1 ? "" : Cookie.SUB_VALUE_DELIM);
		}
	} else {
		buff.append(this.getValue())
	}
	buff.append(Cookie.DELIM);
	// path=pathvalue;
	buff.append(Cookie.PATH).append(Cookie.EQUALS)
		.append(this.getPath()).append(Cookie.DELIM);
	// expires=expiresvalue
	buff.append(Cookie.EXPIRES).append(Cookie.EQUALS)
		.append(this.getExpires()).append(Cookie.DELIM);
	if (this.getDomain()) {
		// domain=domainvalue
		buff.append(Cookie.DOMAIN).append(Cookie.EQUALS)
			.append(this.getDomain()).append(Cookie.DELIM);
	}
	if (this.isSecure()) {
		// secure;
		buff.append(Cookie.SECURE).append(Cookie.DELIM);
	}
	return buff.toString();
};

/**
 *	Returns a String representation of this <code>Cookie</cookie> in the 
 *	standard cookie syntax -- but only the name and value with no trailing
 *	semicolon.
 *	@returns	String
 */
Cookie.prototype.toNameValueString = function () {
	var buff = new StringBuffer();
	// name=value -- no trailing semicolon
	buff.append(this.getName()).append(Cookie.EQUALS)
		.append(this.getValue());
	return buff.toString();
};

/**	
 *	Get the <code>name</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype.getName = function () {
	return this._name;
};

/**	
 *	Set the <code>name</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	String 	name
 *	@returns	void
 */	
Cookie.prototype.setName = function (name) {
	this._name = name;
};

/**	
 *	Get the <code>value</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype.getValue = function () {
	return this._value;
};

/**	
 *	Set the <code>value</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	String 	value
 *	@returns	void
 */	
Cookie.prototype.setValue = function (value) {
	this._value = value;
};

/**	
 *	Get the <code>path</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype.getPath = function () {
	if (this._path === undefined) {
		return "/";
	} else {
		return this._path;
	}
};

/**	
 *	Set the <code>path</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	String 	path
 *	@returns	void
 */	
Cookie.prototype.setPath = function (path) {
	this._path = path;
};

/**	
 *	Get the <code>secure</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns boolean
 */	
Cookie.prototype.isSecure = function () {
	return this._secure;
};

/**	
 *	Set the <code>secure</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	boolean 	secure
 *	@returns	void
 */	
Cookie.prototype.setSecure = function (secure) {
	this._secure = secure;
};

/**	
 *	Get the <code>domain</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype.getDomain = function () {
	return this._domain;
};

/**	
 *	Set the <code>domain</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	String 	domain
 *	@returns	void
 */	
Cookie.prototype.setDomain = function (domain) {
	this._domain = domain;
};

/**	
 *	Get the <code>expires</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype.getExpires = function () {
	if (this._expires === undefined) {
		return this._getDefaultExpires();
	} else {
		return this._expires;
	}
};

/**	
 *	Set the <code>expires</code> attribute value for this <code>Cookie</code> 
 *	object.
 *	@param	String 	expires
 *	@returns	void
 */	
Cookie.prototype.setExpires = function (expires) {
	this._expires = expires;
};

/**	
 *	Private method to get the devault value for the <code>domain</code>
 *	attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype._getDefaultDomain = function () {
	return window.location.hostname;
};

/**	
 *	Private method to get the devault value for the <code>expires</code>
 *	attribute value for this <code>Cookie</code> 
 *	object.
 *	@returns String
 */	
Cookie.prototype._getDefaultExpires = function () {
	var date = new Date();
	date.setFullYear(date.getFullYear()+10);
	return date.toGMTString();
};

/**
 *	A utility class that mirrors the <code>java.util.StringBuffer</code> class.
 *	Improves performance of string concatenation by storing strings in a
 *	private internal array.
 *	@author	Todd Ditchendorf
 *	@constructor			Accepts optional inital parameter to store in the 
 *						buffer.
 *	@param	String	s	Initial buffer value.
 */
StringBuffer = function (s) {
	this._array = [];
	if (s && typeof s != "string") {
		throw new Error("IllegalArgumentException: StringBuffer's " +
			"constructor accepts an optional String argument. Given:" +
			(typeof s));
	}
	if (s) {
		this.append(s);
	}
};

/**
 *	Appends an additional String to the buffer and returns a reference to this
 *	object for method chaining.
 *	@param	String	s	New String to be added to buffer.
 *	@returns	StringBuffer
 */
StringBuffer.prototype.append = function (s) {
	this._array.push(s);
	return this;
};

/**
 *	Returns a String representation of this <code>StringBuffer</code>
 *	@returns	String
 */
StringBuffer.prototype.toString = function () {
	return this._array.join("");
};

/**
 *	An object that maps keys to values. A map cannot contain duplicate keys;
 *	each key can map to at most one value.
 *	@author	Todd Ditchendorf
 */
Map = function (o) {
/*	if (o && !(o instanceof Object)) {
		throw new Error("IllegalArgumentException: Map's " +
					"constructor's only argument must be an Object");
	}*/
	this._obj = (o) ? (o) : new Object();
};

/**
 *	Associates the specified value with the specified key in this map.
 *	@param	Object key	New key to enter,
 *	@param	Object Value	New value to enter.
 *	@returns Object
 */
Map.prototype.put = function (key,value) {
	this._obj[key] = value;
};

/**
 *	Returns the value to which this map maps the specified key.
 *	@param	Object key	Get the value for this key in this Map.
 *	@returns Object
 */
Map.prototype.get = function (key) {
	if (!this._obj[key]) return null;
	return this._obj[key];
};

/**
 *	Returns the number of key-value mappings in this map.
 *	@returns number
 */
Map.prototype.size = function () {
	var count = 0;
	for (var key in this._obj)
		count++;
	return count;
};

/**
 *	Returns true if this map contains no key-value mappings.
 *	@returns	boolean
 */
Map.prototype.isEmpty = function () {
	return this.size() == 0;
};

/**
 *	Returns a string representation of this map.
 *	@returns String
 */
Map.prototype.toString = function () {
	var buff = new StringBuffer();
	count = 0;
	for ( var key in this._obj ) {
		buff.append(key).append(" = ").append(this._obj[key])
			.append(count == this.size() - 1 ? "" : "\r\n");
		count++;
	}
	return buff.toString();
};

/**
 *	Returns true if this map contains a mapping for the specified  key.
 *	@param	Object	key	Testing this Map for key with this value.
 *	@returns	boolean
 */
Map.prototype.containsKey = function (key) {
	if (this._obj[key] !== undefined) {
		return true;
	}
	return false;
};

/**
 *	Returns true if this map maps one or more keys to the  specified value.
 *	@param	Object	value	Testing this Map for key with this value.
 *	@returns	boolean
 */
Map.prototype.containsValue = function (value) {
	for (var key in this._obj) {
		if (this._obj[key] == value) {
			return true;
		}
	}
	return false;
};

/**
 *	Removes the mapping for this key from this map if it is present
 *	@param	Object	key	Testing this Map for key with this value.
 *	@returns	boolean
 */
Map.prototype.remove = function (key) {
	if (this.containsKey(key)) {
		delete this._obj[key];
	}
};

/**
 *	Returns a set view of the values contained in this map.
 *	@returns	Array
 */
Map.prototype.keySet = function () {
	var keys = [];
	for (var key in this._obj) {
		keys.push(key);
	}
	return keys;
};

/**
 *	Returns a collection view of the values contained in this map.
 *	@returns	Collection
 */
Map.prototype.values = function () {
	var values = new Collection();
	for (var key in this._obj) {
		values.add(this._obj[key]);
	}
	return values;
};

/**
 *	Map implementation that makes working with URL params easy.
 *	@author	Todd Ditchendorf
 *	@constructor		should not be called directly
 */
ParameterMap = function (q) {
	this._map = new Map();
	this._q;
	if (q) {
		this._q = q.substring(1);
	}
	var pairs = this._q.split(/&+/g);
	var a;
	for (var i = 0; i < pairs.length; i++) {
		a = pairs[i].split(/=+/g);
		this._map.put(a[0],a[1]);
	}
	/*this._re = /(\w+)\=([^&]+)&?/g;
	this._current = [];
	if(!isIE5Mac && !isIEWin50) {
		while (this._current = this._re.exec(this._q)) {
			this._map.put(this._current[1],this._current[2]);
		}
	}*/
};

/**
 *	Static convenience method to get a parameter map containing the current
 *	pages URL params.
 *	@returns	ParameterMap
 */
ParameterMap.getPageParameterMap = function () {
	return new ParameterMap(
								window.location.search.toString());
};

/**
 *	Returns string representation of this parameter map in the standard URL 
 *	query string format (e.g. '?name=value&name2=value'
 *	@returns	String
 */
ParameterMap.prototype.toQueryString = function () {
	var buff = new StringBuffer("?");
	var count = 0;
	var key,value;
	var keys = this._map.keySet();
	for (var i = 0; i < keys.length; i++) {
		key = keys[i];
		value = this._map.get(key);
		buff.append(key).append("=").append(value)
			.append(count == this._map.size() - 1 ? "" : "&");
		count++;
	}
	return buff.toString();
};