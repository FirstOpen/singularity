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
 */
public class LocStock implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4696837962024558606L;

	private String id;
	
	private LocStockPK pk;

	private String locCode;

	private String stockId;

	private Double quantity;

	private Integer reOrderLevel;

	public LocStock() {
	}

	/**
	 * @hibernate.id generator-class="uuid.hex" length="128"
	 */
	public String getId() {
		return id;
	}

	public void setId(String x) {
		id = x;
	}

	/**
	 * @hibernate.property
	 * @hibernate.column id="Quantity"
	 */
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double x) {
		quantity = x;
	}

	/**
	 * @hibernate.property
	 * @hibernate.column id="ReOrderLevel"
	 */
	public Integer getReOrderLevel() {
		return reOrderLevel;
	}

	public void setReOrderLevel(Integer x) {
		reOrderLevel = x;
	}

	/**
	 * @return Returns the pk.
	 */
	public LocStockPK getPk() {
		return pk;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setPk(LocStockPK pk) {
		this.pk = pk;
	}

}
