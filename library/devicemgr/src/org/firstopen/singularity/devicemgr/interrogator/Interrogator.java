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

/**
 * Each Interrogator class must implement this interface to be used by 
 * a Device Manager.
 * 
 * @author Tom Rose
 * @since 1.0
 * 
 * $Id: Interrogator.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
package org.firstopen.singularity.devicemgr.interrogator;

import org.firstopen.singularity.config.DeviceProfile;

public interface Interrogator extends Runnable {

    public void setDeviceProfile(DeviceProfile deviceProfile);

    public DeviceProfile getDeviceProfile();

    public void setInterrogatorIO(InterrogatorIO interrogatorIO);

    public void on();

    public void off();

    public void queryRep();

    public void ack();

    public void query();

    public void queryAdjust();

    public void select();

    public void nak();

    public void reqRN();

    public void read();

    public void write();

    public void kill();

    public void lock();

    public void access();

    public void blockWrite();

    public void blockErase();
}
