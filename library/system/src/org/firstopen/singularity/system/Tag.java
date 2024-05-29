/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

package org.firstopen.singularity.system;

/**
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: Tag.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 * @hibernate.class table="tag" lazy="false"
 */
public class Tag {

	private String id;

	private String type;

	private String value;

	private String printedId;

	private byte[] binvalue = new String().getBytes();

	private int[] intvalue;

	private int count = 0;

	private boolean tampered = false;

	private int state = TagState.UNAPPLIED_NEW;

	/**
	 * @hibernate.property
	 * @return Returns the tampered.
	 */
	public boolean isTampered() {
		return tampered;
	}

	/**
	 * @param tampered
	 *            The tampered to set.
	 */
	public void setTampered(boolean tampered) {
		if (state == TagState.APPLIED && this.tampered == true) {
			this.tampered = true; // no opt assignment;
		} else {
			this.tampered = tampered;
		}
	}

	public void clear() {
		id = null;
		value = null;
		printedId = null;
		binvalue = null;
		intvalue = null;
		count = 0;
	}

	public void increment() {
		count++;
	}

	public void decrement() {
		count--;
	}

	public void increment(int amt) {
		count = count + amt;
	}

	public void decrement(int amt) {
		count = count - amt;
		if (count < 0 ) count = 0;
	}
	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return Returns the intvalue.
	 */
	public int[] getIntvalue() {
		return intvalue;
	}

	/**
	 * @param intvalue
	 *            The intvalue to set.
	 */
	public void setIntvalue(int[] intvalue) {
		this.intvalue = intvalue;
		for (int i = 0; i < intvalue.length; i++) {
			value = value + intvalue[i];
		}
	}

	/**
	 * 
	 * 
	 */
	public Tag() {
		super();
	}

	/**
	 * 
	 * @param value
	 */
	public Tag(String value) {
		super();
		this.setValue(value);
	}

	/**
	 * @return Returns the binvalue.
	 */
	public byte[] getBinvalue() {
		return binvalue;
	}

	/**
	 * @param binvalue
	 *            The binvalue to set.
	 */
	public void setBinvalue(byte[] binvalue) {
        this.binvalue = binvalue;
       
	}

	/**
	 * @hibernate.id generator-class="uuid.hex" length="128"
	 * 
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @hibernate.property
	 * 
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = value.trim();
		} else {
			this.value = null;
		}

	}

	/**
	 * @hibernate.property
	 * @return Returns the printedId.
	 */
	public String getPrintedId() {
		return printedId;
	}

	/**
	 * @param printedId
	 *            The printedId to set.
	 */
	public void setPrintedId(String printedId) {
		this.printedId = printedId;
	}

	/**
	 * @hibernate.property
	 * @return Returns the state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(int state) {
		switch (state) {
		case TagState.APPLIED:
			if (this.state == TagState.UNAPPLIED_NEW) {
				this.state = state;
				this.tampered = false;
			}
			break;
		case TagState.UNAPPLIED_USED:
			this.state = state;
			break;
		}

	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

}
