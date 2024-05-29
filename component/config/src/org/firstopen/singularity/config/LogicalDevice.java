/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package org.firstopen.singularity.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.util.Named;

/**
 * 
 * @author TomRose
 * @version $Id$
 * 
 * 
 * @hibernate.class table="LogicalDevice" lazy="false"
 * 
 */
public class LogicalDevice implements Named, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4900481360859118719L;

    private String name = null;

    private Set<Reader> readerSet = new HashSet<Reader>();

    private String id = null;

    /**
     * @hibernate.id 
     *    generator-class="uuid.hex" length="128" length="64"
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
     * 
     */
    public LogicalDevice() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @hibernate.set inverse="false" lazy="false" cascade="all"
     * @hibernate.collection-key column="logical_device_id"
     * @hibernate.collection-many-to-many class="org.firstopen.singularity.system.Reader"
     * 
     * @return Returns the readerSet.
     */
    public Set<Reader> getReaderSet() {
        return readerSet;
    }

    /**
     * @param readerSet
     *            The readerSet to set.
     */
    public void setReaderSet(Set<Reader> readers) {
        this.readerSet = readers;
    }

    public void addReader(Reader reader) {
        if (readerSet == null) readerSet = new HashSet<Reader>();
        readerSet.add(reader);
    }

    public void removeReader(Reader reader) {
        if (readerSet != null) {
            readerSet.remove(reader);
        }
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * clear the id, readerSet, and name from this object.
     * 
     */
    public void clear() {
        name = null;
        readerSet = null;
        id = null;
    }

}
