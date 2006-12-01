/** @const String */
var TREE_WRAP_CLASS_NAME        =  "tree-wrap";
/** @const String */
var TREE_LIST_CLASS_NAME        =  "tree-list";
/** @const String */
var CONTAINER_ITEM_CLASS_NAME   =  "container-item";
/** @const String */
var CONTAINER_LINK_CLASS_NAME   =  "container-link";

/** @var HTMLElement */ 
var treeWrap;
/** @var HTMLElement */ 
var treeList;
/** @var HTMLElement */ 
var containerNode;
/** @var HTMLElement */ 
var containerNodeLink;
/** @var HTMLElement */ 
var subList;

/**
 *  @param Evt
 *  @return void
 */
function treeContainerNodeClicked(evt) {
    var evt = new Evt(evt);
    _findTreeElements(evt);
    _toggleTreeNode();
}

/**
 *  @param Evt
 *  @return void
 */
function _findTreeElements(evt) {
    containerNodeLink   = evt.getSource();  
    containerNode       = DomUtils.getFirstAncestorByClassName(
                                                containerNodeLink,
                                                CONTAINER_ITEM_CLASS_NAME);                                             
    treeList            = DomUtils.getFirstAncestorByClassName(
                                                containerNode,
                                                TREE_LIST_CLASS_NAME);
    treeWrap            = DomUtils.getFirstAncestorByClassName(
                                                containerNode,
                                                TREE_WRAP_CLASS_NAME);
    subList             = DomUtils.getFirstChildByTagName(containerNode,"div");
}

/**
 *  @return void
 */
function _toggleTreeNode() {
    DomUtils.toggle(subList);
    DomUtils.toggleClassName(containerNodeLink,"active");
}

/** @returns void */
function preloadImages() {
    var img = new Image();
    img.src = "/jstree/img/aqua_arrow-down.gif";
    img.src = "/jstree/img/aqua_arrow-down.png";
    img.src = "/jstree/img/aqua_arrow-side-active.png";
    img.src = "/jstree/img/aqua_arrow-side-active.gif";
    img.src = "/jstree/img/aqua_arrow-down-active.png";
    img.src = "/jstree/img/aqua_arrow-down-active.gif";
}
preloadImages();