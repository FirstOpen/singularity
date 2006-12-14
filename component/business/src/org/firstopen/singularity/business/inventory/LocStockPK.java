/*
 * Copyright 2005 Wavechain Consulting LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.business.inventory;

/**
 * 
 * @author Tom Rose (tom.rose@i-konect.com) 
 * @version $Id: LocStockPK.java 776 2005-10-18 17:31:07Z TomRose $
 *
 */
public class LocStockPK implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3326174206802954448L;

	private String locCode = null;

	private String stockId = null;

	public LocStockPK() {
	}

	public LocStockPK(String aLocCode, String aStockId) {
		locCode = aLocCode;
		stockId = aStockId;
	}

	/**
	 * @hibernate.property column="LocCode"
	 */
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String x) {
		locCode = x;
	}

	/**
	 * @hibernate.property column="StockID"
	 */
	public String getStockId() {
		return stockId;
	}

	public void setStockId(String x) {
		stockId = x;
	}

	public int hashCode() {
		return locCode.hashCode() ^ stockId.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof LocStockPK))
			return false;

		LocStockPK pk = (LocStockPK) obj;

		if ((this.locCode.equals(pk.locCode))
				&& this.stockId.equals(pk.stockId))
			return true;
		else
			return false;
	}
}
