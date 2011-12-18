#!/bin/sh
#
# 
# Copyright 2005 i-Konect LLC
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# 	http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
#

 DM_CONFIG=../conf/start-transient-DeviceManager.config
 CM_CONFIG=../conf/start-transient-ConfigManager.config
 LU_CONFIG=../conf/jrmp-reggie.config

if [  "$JAVA_HOME"=="" ] ; then 
     JAVA_HOME=/usr/java
fi

 JAVA_HOME=$JAVA_HOME
 JAVA_BIN=$JAVA_HOME/bin
 JAVA_LIB_EXT=/usr/java/jre/lib/ext
 JINI_HOME=$DM_HOME/jini-home
 DM_HTTP_PORT=8081

 JAVA_OPTS="-server -cp ../conf -Djava.security.manager \
-Djava.security.policy=../conf/service.policy \
-Dcom.sun.management.jmxremote \
-Dorg.firstopen.singularity.httpd.port=8081 \
-Dorg.firstopen.singularity.home=.. \
-Dorg.firstopen.singularity.classserver=`hostname` \
-Djava.ext.dirs=$JAVA_HOME/lib/ext:../lib"

SERVICE_CMD=$JAVA_BIN/java

export SERVICE_CMD JAVA_HOME JAVA_BIN JAVA_LIB_EXT JAVA_OPTS DM_HTTP BASE

