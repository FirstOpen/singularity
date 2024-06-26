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
 * @author TomRose <tom.rose@i-konect.com>
 * @version $Id$
 * 
 */
public class GeographicCRS {

    double latitude = 0;
    double longitude = 0;
    double height = 0;
    
    /**
     * @return Returns the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude to set.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return Returns the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude to set.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     */
    public GeographicCRS() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
