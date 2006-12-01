package com.refactored_networks.epc.epcnetwork;
/**
 * Copyright 2005 Refactored Networks, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import com.sun.jndi.dns.*;


public class CompanyPrefixIndexDB {

	protected Properties configuration;

	public CompanyPrefixIndexDB(Properties pIndexLookupConf) 
		throws Sixty4BitLookupException 
	{
		this.configuration = pIndexLookupConf;
	}

	public String Lookup(String index) throws Sixty4BitLookupException,NoSuchIndexException {
		String method = configuration.getProperty("method");
		if(method.equalsIgnoreCase("xml")) {
			String sFilename = configuration.getProperty("filename");
    			try {       
      				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      				DocumentBuilder parser = builderFactory.newDocumentBuilder();
          			Document d = parser.parse(sFilename); 
				NodeList lNodes = d.getElementsByTagName("entry");
				Element item;
				String elemindex;
				for(int i=0;i<lNodes.getLength();i++) {
					item = (Element) lNodes.item(i);
					elemindex = item.getAttribute("index");
					if(elemindex.equalsIgnoreCase(index)) {
						return(item.getAttribute("companyPrefix").trim());
					}
				}
				throw new NoSuchIndexException();
        		} catch (SAXException e) {
        			throw new Sixty4BitLookupException("Bad XML error: " + e.getMessage());
        		} catch (IOException e) {
        			throw new Sixty4BitLookupException("IO error: " + e.getMessage());
        		} catch (ParserConfigurationException e) {
      				throw new Sixty4BitLookupException("You need to install a JAXP aware parser:" + e.getMessage());
			}
		} else if(method.equalsIgnoreCase("ons")) {
			String sONSRoot = configuration.getProperty("root");
			String sNameServer = configuration.getProperty("nameserver");
			String sDomainName = index + ".64bit.id";
	                NamingEnumeration attributeEnum = null;
			Hashtable env = new Hashtable();
			javax.naming.directory.Attributes returnAttributes = null;
			String authoritySection;
			// JNDI Properties
                	env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

                	if(sNameServer == null || sNameServer.equals("")) {
                        	authoritySection = "";
                	} else {
                        	authoritySection = "//"+ sNameServer;
                	}

                	String providerURL = "dns:"+authoritySection+"/"+sONSRoot;
                	env.put("java.naming.provider.url", providerURL);

                	try {
                        	DirContext ictx = new InitialDirContext(env);
                        	returnAttributes = ictx.getAttributes(sDomainName, new String[]{"TXT"});

                        	if(returnAttributes.size() > 0) {
                                	attributeEnum = returnAttributes.get("TXT").getAll();
					String result;
					if(attributeEnum.hasMore()) {
						result = ((String) attributeEnum.next()).trim();
						if(attributeEnum.hasMore()) {
							throw new Sixty4BitLookupException("Error! the ONS query gave more than one result!");
						}
                                        	return(result);
                                	} else {
						throw new Sixty4BitLookupException("Error! Index not found!");
					}
                        	} else {
					throw new NoSuchIndexException();
				}
		
                	} catch(NameNotFoundException notfound) {
				throw new NoSuchIndexException();
			} catch(NamingException ne) {
                        	throw new Sixty4BitLookupException(ne);
			}
		} else {
			throw new Sixty4BitLookupException("Error in configuration. Method = " + method);
		}
	}

	public String ReverseLookup(String prefix) throws Sixty4BitLookupException,NoSuchIndexException {
		String method = configuration.getProperty("method");
		if(method.equalsIgnoreCase("xml")) {
			String sFilename = configuration.getProperty("filename");
				try {       
	  				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	  				DocumentBuilder parser = builderFactory.newDocumentBuilder();
	      			Document d = parser.parse(sFilename); 
				NodeList lNodes = d.getElementsByTagName("entry");
				Element item;
				String elemprefix;
				for(int i=0;i<lNodes.getLength();i++) {
					item = (Element) lNodes.item(i);
					elemprefix = item.getAttribute("companyPrefix");
					if(elemprefix.equalsIgnoreCase(prefix)) {
						return(item.getAttribute("index").trim());
					}
				}
				throw new NoSuchIndexException();
	    		} catch (SAXException e) {
	    			throw new Sixty4BitLookupException("Bad XML error: " + e.getMessage());
	    		} catch (IOException e) {
	    			throw new Sixty4BitLookupException("IO error: " + e.getMessage());
	    		} catch (ParserConfigurationException e) {
	  				throw new Sixty4BitLookupException("You need to install a JAXP aware parser:" + e.getMessage());
			}
		} else if(method.equalsIgnoreCase("ons")) {
			String sONSRoot = configuration.getProperty("root");
			String sNameServer = configuration.getProperty("nameserver");
			String sDomainName = prefix + ".64bit.id";
	                NamingEnumeration attributeEnum = null;
			Hashtable env = new Hashtable();
			javax.naming.directory.Attributes returnAttributes = null;
			String authoritySection;
			// JNDI Properties
	            	env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
	
	            	if(sNameServer == null || sNameServer.equals("")) {
	                    	authoritySection = "";
	            	} else {
	                    	authoritySection = "//"+ sNameServer;
	            	}
	
	            	String providerURL = "dns:"+authoritySection+"/"+sONSRoot;
	            	env.put("java.naming.provider.url", providerURL);
	
	            	try {
	                    	DirContext ictx = new InitialDirContext(env);
	                    	returnAttributes = ictx.getAttributes(sDomainName, new String[]{"TXT"});
	
	                    	if(returnAttributes.size() > 0) {
	                            	attributeEnum = returnAttributes.get("TXT").getAll();
					String result;
					if(attributeEnum.hasMore()) {
						result = ((String) attributeEnum.next()).trim();
						if(attributeEnum.hasMore()) {
							throw new Sixty4BitLookupException("Error! the ONS query gave more than one result!");
						}
	                                    	return(result);
	                            	} else {
						throw new Sixty4BitLookupException("Error! Index not found!");
					}
	                    	} else {
					throw new NoSuchIndexException();
				}
		
	            	} catch(NameNotFoundException notfound) {
				throw new NoSuchIndexException();
			} catch(NamingException ne) {
	                    	throw new Sixty4BitLookupException(ne);
			}
		} else {
			throw new Sixty4BitLookupException("Error in configuration. Method = " + method);
		}
	}
}
