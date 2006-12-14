package org.firstopen.singularity.system;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @hibernate.class table="ReaderEvent" lazy="false"
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: ReaderEvent.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public class ReaderEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -921381780292997203L;

	private long timestamp = System.currentTimeMillis();

	private String id = null;

	private String readerName = null;
    
    private String geocoord = null;

	private HashMap<String,Tag> tagMap = new HashMap<String,Tag>();
	
	
	private Sensor source;

	public ReaderEvent(Sensor source) {
		this.source = source;
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
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @hibernate.many-to-one class="org.firstopen.singularity.system.Sensor"
	 *                        cascade="all"
	 * 
	 * @return
	 */
	public Sensor getSensor() {
		return (Sensor) source;
	}

	public void setSensor(Sensor sensor) {
		source = sensor;
	}

	/**
	 * 
	 * @hibernate.property
	 * @return Returns the timestamp.
	 */
	public Date getDate() {
		return new Date(timestamp);
	}

	/**
	 * 
	 */
	public void setDate(Date date) {
		this.timestamp = date.getTime();
	}

	/**
	 * 
	 * @hibernate.property
	 * @return Returns the timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            The timestamp to set.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @hibernate.list table="ReaderEventTags" lazy="false"
	 *                 cascade="save-update"
	 * 
	 * @hibernate.collection-key column="reader_event_id"
	 * @hibernate.collection-index column="position"
	 * @hibernate.collection-many-to-many
	 *     class="org.firstopen.singularity.system.Tag"
	 *     column="tag"
	 */   
	public Collection<Tag> getTagIds() {
		return tagMap.values();
	}

	public void setTagIds(Collection<Tag> tags) {
		tagMap.clear();
	    addTags(tags);
		
	}

	/**
	 * @hibernate.property
	 * @return Returns the readerName.
	 */
	public String getReaderName() {
		return readerName;
	}

	/**
	 * @param readerName
	 *            The readerName to set.
	 */
	public void setReaderName(String readerId) {
		this.readerName = readerId;
	}

	/**
	 * 
	 */
	public ReaderEvent() {
		super();
		
	}

	public void addTags(Collection<Tag> tags){
		String key = null;
		for (Iterator<Tag> iter = tags.iterator(); iter.hasNext();) {
			Tag element =  iter.next();
			key = element.getValue().trim();
			Tag storedTag = tagMap.get(key);
			if (storedTag != null){
				storedTag.increment(element.getCount());
			}else{
			   tagMap.put(key,element);
			}
		}
	}

	/**
	 * @return Returns the tagMap.
	 */
	public HashMap<String, Tag> getTagMap() {
		return tagMap;
	}

	/**
	 * @param tagMap The tagMap to set.
	 */
	public void setTagMap(HashMap<String, Tag> tagMap) {
		this.tagMap = tagMap;
	}

    /**
     * @return Returns the geocoord.
     */
    public String getGeocoord() {
        return geocoord;
    }

    /**
     * @param geocoord The geocoord to set.
     */
    public void setGeocoord(String geocoord) {
        this.geocoord = geocoord;
    }
}
