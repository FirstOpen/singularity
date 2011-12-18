/* 
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.util;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: HTTPConnector.java 776 2005-10-18 17:31:07Z TomRose $
 */

public class HTTPConnector {

	private String url = null;

	private NameValuePair[] methodNVPairs = null;

	HttpClient httpclient = null;

	int result = 0;

	/**
	 * @param url
	 */
	public HTTPConnector(String url, NameValuePair[] methodNVPairs) {

		setUrl(url);
		setMethodNVPairs(methodNVPairs);
		httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());

	} // end constructor

	/**
	 * @param url
	 */
	public HTTPConnector(String url) {
		/*
		 * System.setProperty( "org.apache.commons.logging.Log",
		 * "org.apache.commons.logging.impl.SimpleLog");
		 * 
		 * System.setProperty(
		 * "org.apache.commons.logging.simplelog.showdatetime", "true");
		 * 
		 * System.setProperty(
		 * "org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
		 * 
		 * System.setProperty(
		 * "org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient",
		 * "debug");
		 */
		setUrl(url);
		httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());

	} // end constructor

	/**
	 * 
	 */
	public HTTPConnector() {
		httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpclient.getState().setCookiePolicy(CookiePolicy.RFC2109);

	} // end HTTPConnector

	public String post() throws HttpException, IOException {

		PostMethod post = new PostMethod(url);
		post.setRequestBody(methodNVPairs);

		post.setFollowRedirects(true);

		return this.post(post);
	}

	/**
	 * @return Executes the url, with the set of NV Post values
	 */
	public String post(PostMethod post) throws HttpException, IOException {
		String sResult = null;

		Logger x = Logger.getRootLogger();

		// httpclient.getHostConfiguration().setProxy("localhost", 81);

		// Execute request
		result = httpclient.executeMethod(post);

		x.debug("Response status code: " + result);

		// Display status code
		sResult = post.getResponseBodyAsString();

		post.releaseConnection();
		// post.recycle();

		return sResult;

	}

	public void setHost(String host) {
		httpclient.getHostConfiguration().setHost(host);
	}

	public void setCookie(String domain, String name, String value) {
		Cookie cookie = new Cookie(domain, name, value);
		httpclient.getState().addCookie(cookie);

	}

	public void setCookiePath(String path) {

		Cookie[] cookies = httpclient.getState().getCookies();
		cookies[0].setPath(path);

	}

	public void setCookieExpDate(Date date) {

		Cookie[] cookies = httpclient.getState().getCookies();
		cookies[0].setExpiryDate(date);

	}

	public String get() {

		GetMethod get = new GetMethod(url);
		get.setFollowRedirects(true);

		return this.get(get);
	}

	/**
	 * @return
	 */
	public String get(GetMethod get) {

		Logger x = Logger.getRootLogger();
		 String sResult = null;
		StringBuffer sbPath = new StringBuffer(url);
		for (int i = 0; (methodNVPairs != null) && i < methodNVPairs.length; i++) {

			sbPath.append("?" + methodNVPairs[i].getName() + "="
					+ methodNVPairs[i].getValue());

		}

		get.setPath(sbPath.toString());

		x.debug("URL Path: " + sbPath);

		// Execute request
		Header headers[] = get.getRequestHeaders();
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			x.debug("id-> " + header.getName() + "  value-> "
					+ header.getValue());
		}

			try {
				result = httpclient.executeMethod(get);
	
			// Display status code
			x.debug("Response status code: " + result + " content-length: "
					+ get.getResponseHeader("accept"));

			// Release current connection to the connection pool once you are
			// done
			 sResult = get.getResponseBodyAsString();

			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		get.releaseConnection();
		// get.recycle();
		return sResult;
	} // end getData

	/**
	 * @return
	 */
	public NameValuePair[] getMethodNVPairs() {
		return methodNVPairs;
	}

	/**
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param pairs
	 */
	public void setMethodNVPairs(NameValuePair[] pairs) {
		methodNVPairs = pairs;
	}

	/**
	 * @param string
	 */
	public void setUrl(String string) {
		url = string;
	}

	/**
	 * @return
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param i
	 */
	public void setResult(int i) {
		result = i;
	}

	public void setProxy(String host, int port) {
		httpclient.getHostConfiguration().setProxy(host, port);
	}
}// end HTTPConnector
