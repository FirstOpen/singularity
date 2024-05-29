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
package org.firstopen.custom.business;

/**
 * @hibernate.class 
 *   table="CartnersNote"
 *   lazy="false"
 * 
 * @author Tom Rose (tom.rose@i-konect.com) 
 * 
 */
public class CarterNote {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3841467057018557632L;
	private String id = null;
	private String shipper = "shipper";
	private String carrier = "carrier";
	private String bookingReference = "bookingReference";
	private String importer = "importer";
	private String countryCode = "countryCode";
	private String exporter = "exporter";
	private String dangerousGoods = "yes";
	private String descriptionOfGoods = "Testing";
	private String portOfLoadingETA = "Auckland";
	private String entryNumber = "123";
	private String grossWeight = "1000KG";
	private String humidityControl = "yes";
	private String internalPackageDesc = "2";
	private String numInternalPackages = "2";
	private String overDeminsionDetail = "12";
	private String packageId = "1234";
	private String portOfDischarge = "test";
	private String portOfLoading = "test";
	private String numberUN = "123";
	private String ventSettings = "test";
	private String vessel = "test";
	private String weightUnits = "KG";
	private String weight = "1";
	private String sealNumber = "test";
	private String printedTagId = "test";
	
	
	
	
	public CarterNote() {
	
	}
	
	
	/**
	 *
	 * @hibernate.property
	 * @return Returns the bookingReference.
	 */
	public String getBookingReference() {
		return bookingReference;
	}


	/**
	 * @param bookingReference The bookingReference to set.
	 */
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the countryCode.
	 */
	public String getCountryCode() {
		return countryCode;
	}


	/**
	 * @param countryCode The countryCode to set.
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the dangerousGoods.
	 */
	public String getDangerousGoods() {
		return dangerousGoods;
	}


	/**
	 * @param dangerousGoods The dangerousGoods to set.
	 */
	public void setDangerousGoods(String dangerousGoods) {
		this.dangerousGoods = dangerousGoods;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the descriptionOfGoods.
	 */
	public String getDescriptionOfGoods() {
		return descriptionOfGoods;
	}


	/**
	 * @param descriptionOfGoods The descriptionOfGoods to set.
	 */
	public void setDescriptionOfGoods(String descriptionOfGoods) {
		this.descriptionOfGoods = descriptionOfGoods;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the entryNumber.
	 */
	public String getEntryNumber() {
		return entryNumber;
	}


	/**
	 * @param entryNumber The entryNumber to set.
	 */
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the exporter.
	 */
	public String getExporter() {
		return exporter;
	}


	/**
	 * @param exporter The exporter to set.
	 */
	public void setExporter(String exporter) {
		this.exporter = exporter;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the grossWeight.
	 */
	public String getGrossWeight() {
		return grossWeight;
	}


	/**
	 * @param grossWeight The grossWeight to set.
	 */
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the humidityControl.
	 */
	public String getHumidityControl() {
		return humidityControl;
	}


	/**
	 * @param humidityControl The humidityControl to set.
	 */
	public void setHumidityControl(String humidityControl) {
		this.humidityControl = humidityControl;
	}


	/**
	 *
	 * @hibernate.id
	 * generator-class="uuid.hex" length="128"
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the importer.
	 */
	public String getImporter() {
		return importer;
	}


	/**
	 * @param importer The importer to set.
	 */
	public void setImporter(String importer) {
		this.importer = importer;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the internalPackageDesc.
	 */
	public String getInternalPackageDesc() {
		return internalPackageDesc;
	}


	/**
	 * @param internalPackageDesc The internalPackageDesc to set.
	 */
	public void setInternalPackageDesc(String internalPackageDesc) {
		this.internalPackageDesc = internalPackageDesc;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the numberUN.
	 */
	public String getNumberUN() {
		return numberUN;
	}


	/**
	 * @param numberUN The numberUN to set.
	 */
	public void setNumberUN(String numberUN) {
		this.numberUN = numberUN;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the numInternalPackages.
	 */
	public String getNumInternalPackages() {
		return numInternalPackages;
	}


	/**
	 * @param numInternalPackages The numInternalPackages to set.
	 */
	public void setNumInternalPackages(String numInternalPackages) {
		this.numInternalPackages = numInternalPackages;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the overDeminsionDetail.
	 */
	public String getOverDeminsionDetail() {
		return overDeminsionDetail;
	}


	/**
	 * @param overDeminsionDetail The overDeminsionDetail to set.
	 */
	public void setOverDeminsionDetail(String overDeminsionDetail) {
		this.overDeminsionDetail = overDeminsionDetail;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the packageId.
	 */
	public String getPackageId() {
		return packageId;
	}


	/**
	 * @param packageId The packageId to set.
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the portOfDischarge.
	 */
	public String getPortOfDischarge() {
		return portOfDischarge;
	}


	/**
	 * @param portOfDischarge The portOfDischarge to set.
	 */
	public void setPortOfDischarge(String portOfDischarge) {
		this.portOfDischarge = portOfDischarge;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the portOfLoading.
	 */
	public String getPortOfLoading() {
		return portOfLoading;
	}


	/**
	 * @param portOfLoading The portOfLoading to set.
	 */
	public void setPortOfLoading(String portOfLoading) {
		this.portOfLoading = portOfLoading;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the portOfLoadingETA.
	 */
	public String getPortOfLoadingETA() {
		return portOfLoadingETA;
	}


	/**
	 * @param portOfLoadingETA The portOfLoadingETA to set.
	 */
	public void setPortOfLoadingETA(String portOfLoadingETA) {
		this.portOfLoadingETA = portOfLoadingETA;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the ventSettings.
	 */
	public String getVentSettings() {
		return ventSettings;
	}


	/**
	 * @param ventSettings The ventSettings to set.
	 */
	public void setVentSettings(String ventSettings) {
		this.ventSettings = ventSettings;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the vessel.
	 */
	public String getVessel() {
		return vessel;
	}


	/**
	 * @param vessel The vessel to set.
	 */
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the weight.
	 */
	public String getWeight() {
		return weight;
	}


	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}


	/**
	 *
	 * @hibernate.property
	 * @return Returns the weightUnits.
	 */
	public String getWeightUnits() {
		return weightUnits;
	}


	/**
	 * @param weightUnits The weightUnits to set.
	 */
	public void setWeightUnits(String weightUnits) {
		this.weightUnits = weightUnits;
	}



	public String toString(){
		
		return id;
	}

	/**
	 *
	 * @hibernate.property
	 * @return Returns the shipper.
	 */
	public String getShipper() {
		return shipper;
	}

	/**
	 * @param shipper The shipper to set.
	 */
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	/**
	 *
	 * @hibernate.property
	 * @return Returns the carrier.
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier The carrier to set.
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	/**
	 *
	 * @hibernate.property
	 * @return Returns the sealNumber.
	 */
	public String getSealNumber() {
		return sealNumber;
	}

	/**
	 * @param sealNumber The sealNumber to set.
	 */
	public void setSealNumber(String sealNumber) {
		this.sealNumber = sealNumber;
	}


	/**
	 * @hibernate.property
	 * @return Returns the printedTagId.
	 */
	public String getPrintedTagId() {
		return printedTagId;
	}


	/**
	 * @param printedTagId The printedTagId to set.
	 */
	public void setPrintedTagId(String printedTagId) {
		this.printedTagId = printedTagId;
	}
	
	public void clear(){
		id = null;
		shipper = null;
		carrier = null;
		bookingReference = null;
		importer = null;
		countryCode = null;
		exporter = null;
		dangerousGoods = null;
		descriptionOfGoods = null;
		portOfLoadingETA = null;
		entryNumber = null;
		grossWeight = null;
		humidityControl = null;
		internalPackageDesc = null;
		numInternalPackages = null;
		overDeminsionDetail = null;
		packageId = null;
		portOfDischarge = null;
		portOfLoading = null;
		numberUN = null;
		ventSettings = null;
		vessel = null;
		weightUnits = null;
		weight = null;
		sealNumber = null;
		printedTagId = null;
	}
	
	public String next(){
		return "success";
	}
}
