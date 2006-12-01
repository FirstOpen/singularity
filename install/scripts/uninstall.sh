#!/bin/sh
#
# 
# Copyright 2005 i-Konect LLC
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this cp except in compliance with the License.
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

seplist="/t"	# default separator is tab
INSTDIR="/usr/local/singularity/1.0-M2" #default
APPSVRDIR="/usr/local/jboss"            #default
APPSVRNAME="singularity"                #defualt

RMDIR="rmdir"

while getopts i:h:s: o
do	case "$o" in
	i)	INSTDIR="$OPTARG";;
	h)	APPSVRDIR="$OPTARG";;
	s)	APPSVRNAME="$OPTARG";;
	?)	echo "Usage: $0 [-i <installdir> ]  [ -h <jboss-home> ]  [-s <servername>" ]
		exit 1;;
	esac
done

#echo $INSTDIR $APPSVRDIR $APPSVRNAME

 DBINSTDIR="$INSTDIR/db"
 rm -rf "$DBINSTRDIR"

 DOCINSTDIR="$INSTDIR/doc"
 rm -rf "$DOCINSTRDIR"

#Section  "un.Config Manager" un.SECCM

  CMINSTDIR="$INSTDIR/cm"
  
  rm -rf  "$CMINSTDIR/bin/service.sh"
  rm -rf  "$CMINSTDIR/bin/common.sh" 

  rm -rf "$CMINSTDIR/lib/xercesImpl-2.7.1.jar"
  rm -rf "$CMINSTDIR/lib/wrapper-3.1.2.jar"
  rm -rf "$CMINSTDIR/lib/tools-2.1.jar"
  rm -rf "$CMINSTDIR/lib/sun-util-2.1.jar"
  rm -rf "$CMINSTDIR/lib/start-2.1.jar"
  rm -rf "$CMINSTDIR/lib/singularity-util-1.0-MAIN.jar"
  rm -rf "$CMINSTDIR/lib/singularity-system-1.0-MAIN.jar"
  rm -rf "$CMINSTDIR/lib/singularity-devicemgr-1.0-MAIN.jar"
  rm -rf "$CMINSTDIR/lib/singularity-config-1.0-MAIN.jar"
  rm -rf "$CMINSTDIR/lib/sharedvm-2.1.jar"
  rm -rf "$CMINSTDIR/lib/RXTXcomm-2.1-pre17.jar"
  rm -rf "$CMINSTDIR/lib/reggie-2.1.jar"
  rm -rf "$CMINSTDIR/lib/reggie-dl-2.1.jar"
  rm -rf "$CMINSTDIR/lib/jsk-dl-2.1.jar"
  rm -rf "$CMINSTDIR/lib/jini-ext-2.1.jar"
  rm -rf "$CMINSTDIR/lib/jini-core-2.1.jar"
  rm -rf "$CMINSTDIR/lib/jbossall-client-4.0.3SP1.jar"
  rm -rf "$CMINSTDIR/lib/hibernate-3.1.jar"
  rm -rf "$CMINSTDIR/lib/log4j-1.2.9.jar"
  rm -rf "$CMINSTDIR/lib/commons-logging-1.0.4.jar"
  rm -rf "$CMINSTDIR/lib/commons-lang-2.1.jar"
  rm -rf "$CMINSTDIR/lib/commons-configuration-1.2.jar"
  rm -rf "$CMINSTDIR/lib/commons-collections-3.1.jar"

  rm -rf "$CMINSTDIR/conf/service.conf"
  rm -rf "$CMINSTDIR/conf/transient-ConfigManager.config"
  rm -rf "$CMINSTDIR/conf/start-transient-ConfigManager.config"
  rm -rf "$CMINSTDIR/conf/log4j.properties"
  rm -rf "$CMINSTDIR/conf/jrmp-reggie.config"
  rm -rf "$CMINSTDIR/conf/id.properties"
  rm -rf "$CMINSTDIR/conf/service.policy"
  
  rm -rf "$CMINSTDIR/bin/configmgr.log"

  
  $RMDIR  "$CMINSTDIR/lib"
  $RMDIR  "$CMINSTDIR/conf"
  $RMDIR  "$CMINSTDIR/bin"
  $RMDIR  "$CMINSTDIR"
  

 
#SectionEnd


#Section  "un.Emulator" un.SECEM
  EMINSTDIR="$INSTDIR/em"
 
  rm -rf  "$EMINSTDIR/bin/service.sh"
  rm -rf  "$EMINSTDIR/bin/common.sh" 

  rm -rf "$EMINSTDIR/lib/wrapper-3.1.2.jar"
  rm -rf "$EMINSTDIR/lib/singularity-devicemgr-1.0-MAIN.jar"
  rm -rf "$EMINSTDIR/lib/singularity-system-1.0-MAIN.jar"
  rm -rf "$EMINSTDIR/lib/singularity-util-1.0-MAIN.jar"
  rm -rf "$EMINSTDIR/lib/commons-logging-1.0.4.jar"
  rm -rf "$EMINSTDIR/lib/log4j-1.2.9.jar"


  rm -rf "$EMINSTDIR/conf/awservice.conf"
  rm -rf "$EMINSTDIR/conf/ipservice.conf"
  rm -rf "$EMINSTDIR/conf/log4j.properties"
  rm -rf "$EMINSTDIR/conf/service.policy"
  
  $RMDIR  "$EMINSTDIR/lib"
  $RMDIR  "$EMINSTDIR/conf"
  $RMDIR  "$EMINSTDIR/bin"
  $RMDIR  "$EMINSTDIR"

#SectionEnd

#Section  "un.Device Manager" un.SECDM
  DMINSTDIR="$INSTDIR/dm"
 
  rm -rf  "$DMINSTDIR/bin/service.sh"
  rm -rf  "$DMINSTDIR/bin/common.sh" 

  rm -rf "$DMINSTDIR/lib/wrapper-3.1.2.jar"
  rm -rf "$DMINSTDIR/lib/tools-2.1.jar"
  rm -rf "$DMINSTDIR/lib/sun-util-2.1.jar"
  rm -rf "$DMINSTDIR/lib/start-2.1.jar"
  rm -rf "$DMINSTDIR/lib/sharedvm-2.1.jar"
  rm -rf "$DMINSTDIR/lib/jini-ext-2.1.jar"
  rm -rf "$DMINSTDIR/lib/jini-core-2.1.jar"
  #rm -rf "$DMINSTDIR/lib/jbossmq-client-4.0.3SP1.jar"
  #rm -rf "$DMINSTDIR/lib/jboss-j2ee-4.0.3SP1.jar"
  #rm -rf "$DMINSTDIR/lib/jboss-common-client-4.0.3SP1.jar"
  #rm -rf "$DMINSTDIR/lib/jnp-client-4.0.3SP1.jar"
  rm -rf "$DMINSTDIR/lib/jbossall-client-4.0.3SP1.jar"
  rm -rf "$DMINSTDIR/lib/log4j-1.2.9.jar"
  rm -rf "$DMINSTDIR/lib/commons-logging-1.0.4.jar"
    


  rm -rf "$DMINSTDIR/conf/service.conf"
  rm -rf "$DMINSTDIR/conf/transient-DeviceManager.config"
  rm -rf "$DMINSTDIR/conf/start-transient-DeviceManager.config"
  rm -rf "$DMINSTDIR/conf/log4j.properties"
  rm -rf "$DMINSTDIR/conf/id.properties"
  rm -rf "$DMINSTDIR/conf/service.policy"

  rm -rf "$DMINSTDIR/bin/devicemgr.log"


  $RMDIR  "$DMINSTDIR/lib"
  $RMDIR  "$DMINSTDIR/conf"
  $RMDIR  "$DMINSTDIR/bin"
  $RMDIR  "$DMINSTDIR"
  

#SectionEnd

#Section  "un.Event Process Manager" un.SECEPM
 
EPMINSTDIR="$APPSVRDIR/server/$APPSVRNAME"

  rm -rf  "$EPMINSTDIR/bin/service.sh"
  rm -rf  "$EPMINSTDIR/bin/common.sh" 
  
  rm -rf "$EPMINSTDIR/conf/service.policy"
 #rm -i "$EPMINSTDIR/conf/users.properties"
 #rm -i "$EPMINSTDIR/conf/roles.properties"

  rm -rf "$EPMINSTDIR/deploy/singularity-epm-1.0-MAIN.ear"
  rm -rf "$EPMINSTDIR/deploy/singularity-mysql-ds.xml"

  rm -rf "$EPMINSTDIR/lib/mysql-connector-java-3.1.12-bin.jar"
  
  $RMDIR  "$EPMINSTDIR/bin"

#SectionEnd


#Section un.Post
 
  rm -rf "$INSTDIR/.uninst"
  rm -rf "$INSTDIR/uninstall.sh"
  rm -rf uninstall
  cd /
  rmdir -p  $INSTDIR
 
#SectionEnd

#-------------------------End Sections-----------------------------


