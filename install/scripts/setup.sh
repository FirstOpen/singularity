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
APPSVRCONFIG="singularity"              #default

mkdir="mkdir -p"

while getopts :i:h:s:cdep o
do	case "$o" in
	i)	INSTDIR="$OPTARG/singularity/1.0-M2";;
	h)	APPSVRDIR="$OPTARG";;
	s)	APPSVRCONFIG="$OPTARG";;
        c)      CM="yes";;
        d)	DM="yes";;
        e)      EM="yes";;
        p)      EPM="yes";;
	?)	echo -e "Usage: $0 [-i <installdir> ]  [ -h <appserver-home> ]  [-s <configuration> ] [options]
            \n [options]  default will install all
            \n -p Install Event Process Manager \
            \n -c Install Config Manager \
            \n -d Install Device Manager \
            \n -e Install Emulator Manager \
            \n  \
            \n [defaults] \
            \n <installdir>     = $INSTDIR \
            \n <appserver-home> = $APPSVRDIR \
            \n <configuration>  = $APPSVRCONFIG "
		exit 1;;
	esac
done

#echo $INSTDIR $APPSVRDIR $APPSVRCONFIG


$mkdir $INSTDIR

echo "$INSTDIR/.uninst -i $INSTDIR -h $APPSVRDIR -s $APPSVRCONFIG" >$INSTDIR/uninstall

cp uninstall.sh $INSTDIR/.uninst

#ln -s $INSTDIR/uninstall.sh uninstall
chmod -R 755 $INSTDIR


if [ "x$DM" = "x" ] && [ "x$CM" = "x" ] && [ "x$EM" = "x" ] && [ "x$EPM" = "x" ]; then
    #none are set then take the defaults
    echo "installing all components..."
    DM="yes"
    CM="yes"
    EM="yes"
    EPM="yes"
    
fi



  DBINSTDIR="$INSTDIR/db"

  OutPath="$DBINSTDIR"
  $mkdir $OutPath

  cp  "../db/singularity_admin_mysql.sql" $OutPath
  cp  "../db/singularity_mysql.sql"  $OutPath
  cp  "../db/linux/load.sh"  $OutPath

  DOCINSTDIR="$INSTDIR/doc"

  OutPath="$DOCINSTDIR"
  $mkdir $OutPath

  for  file in `find ../../document -name *.pdf`
  do
     cp $file $OutPath
  done

if [ "$EM" = "yes" ]; then


#Section Emulator

  EMINSTDIR="$INSTDIR/em"
  $mkdir $EMINSTDIR

  OutPath="$EMINSTDIR/bin"
  $mkdir $OutPath

  cp  "../common/linux/bin/service.sh" $OutPath
  cp  "../common/linux/bin/common.sh"  $OutPath
  
  OutPath="$EMINSTDIR/conf"
  $mkdir  $OutPath

  cp "../common/conf/service.policy" $OutPath
  cp "../common/conf/log4j.properties" $OutPath
  cp "../emulator/conf/linux/awservice.conf" $OutPath
  cp "../emulator/conf/linux/ipservice.conf" $OutPath

  OutPath="$EMINSTDIR/lib"
  $mkdir $OutPath

  cp "../common/lib/wrapper-3.1.2.jar" $OutPath
  cp "../common/lib/singularity-devicemgr-1.0-MAIN.jar" $OutPath
  cp "../common/lib/singularity-system-1.0-MAIN.jar" $OutPath
  cp "../common/lib/singularity-util-1.0-MAIN.jar" $OutPath
  cp "../common/lib/commons-logging-1.0.4.jar" $OutPath
  cp "../common/lib/log4j-1.2.9.jar" $OutPath

fi



if [ "$DM" = "yes" ]; then

#Section  "Device Manager" SECDM
  

  DMINSTDIR="$INSTDIR/dm"
  $mkdir $DMINSTDIR

  OutPath="$DMINSTDIR/bin"
  $mkdir $OutPath

  cp  "../common/linux/bin/service.sh" $OutPath
  cp  "../common/linux/bin/common.sh"  $OutPath

  
  OutPath="$DMINSTDIR/conf"  
  $mkdir $OutPath

  cp "../common/conf/service.policy" $OutPath
  cp "../common/conf/id.properties" $OutPath
  cp "../common/conf/log4j.properties" $OutPath
  cp "../device-manager/conf/start-transient-DeviceManager.config" $OutPath
  cp "../device-manager/conf/transient-DeviceManager.config" $OutPath
  cp "../device-manager/conf/linux/service.conf" $OutPath

  OutPath="$DMINSTDIR/lib" 
  $mkdir $OutPath

  cp "../common/lib/jini-core-2.1.jar" $OutPath
  cp "../common/lib/jini-ext-2.1.jar" $OutPath
  #cp "../common/lib/sharedvm-2.1.jar" $OutPath
  cp "../common/lib/start-2.1.jar" $OutPath
  cp "../common/lib/sun-util-2.1.jar" $OutPath
  #cp "../common/lib/tools-2.1.jar" $OutPath
  cp "../common/lib/wrapper-3.1.2.jar" $OutPath
  #cp "../common/lib/jbossmq-client-4.0.3SP1.jar" $OutPath
  #cp "../common/lib/jboss-j2ee-4.0.3SP1.jar" $OutPath
  #cp "../common/lib/jboss-common-client-4.0.3SP1.jar" $OutPath
  #cp "../common/lib/jnp-client-4.0.3SP1.jar" $OutPath
  
  #cp "../common/lib/jbossall-client-4.0.3SP1.jar" $OutPath
  #cp "../common/lib/commons-logging-1.0.4.jar" $OutPath
  #cp "../common/lib/log4j-1.2.9.jar" $OutPath

fi

#SectionEnd


if [ "$CM" = "yes" ]; then

#Section  "Config Manager" SECCM

  CMINSTDIR="$INSTDIR/cm"
  $mkdir $CMINSTDIR

  OutPath="$CMINSTDIR/bin"
  $mkdir $OutPath

  cp  "../common/linux/bin/service.sh" $OutPath
  cp  "../common/linux/bin/common.sh"  $OutPath
  
  OutPath="$CMINSTDIR/conf"
  $mkdir $OutPath

  cp "../common/conf/service.policy" $OutPath
  cp "../common/conf/id.properties" $OutPath
  cp "../common/conf/log4j.properties" $OutPath
  
  cp "../config-manager/conf/jrmp-reggie.config" $OutPath
  cp "../config-manager/conf/start-transient-ConfigManager.config" $OutPath
  cp "../config-manager/conf/transient-ConfigManager.config" $OutPath
  cp "../config-manager/conf/linux/service.conf" $OutPath

  OutPath="$CMINSTDIR/lib"
  $mkdir $OutPath

  cp "../common/lib/commons-collections-3.1.jar" $OutPath
  cp "../common/lib/commons-configuration-1.2.jar" $OutPath
  cp "../common/lib/commons-lang-2.1.jar" $OutPath
  cp "../common/lib/commons-logging-1.0.4.jar" $OutPath
  cp "../common/lib/hibernate-3.1.jar" $OutPath
  cp "../common/lib/jbossall-client-4.0.3SP1.jar" $OutPath
  cp "../common/lib/jini-core-2.1.jar" $OutPath
  cp "../common/lib/jini-ext-2.1.jar" $OutPath
  cp "../common/lib/jsk-dl-2.1.jar" $OutPath
  cp "../common/lib/log4j-1.2.9.jar" $OutPath
  cp "../common/lib/reggie-dl-2.1.jar" $OutPath
  cp "../common/lib/reggie-2.1.jar" $OutPath
  cp "../common/lib/RXTXcomm-2.1-pre17.jar" $OutPath
  cp "../common/lib/sharedvm-2.1.jar" $OutPath
  cp "../common/lib/singularity-config-1.0-MAIN.jar" $OutPath
  cp "../common/lib/singularity-devicemgr-1.0-MAIN.jar" $OutPath
  cp "../common/lib/singularity-system-1.0-MAIN.jar" $OutPath
  cp "../common/lib/singularity-util-1.0-MAIN.jar" $OutPath
  cp "../common/lib/start-2.1.jar" $OutPath
  cp "../common/lib/sun-util-2.1.jar" $OutPath
  cp "../common/lib/tools-2.1.jar" $OutPath
  cp "../common/lib/wrapper-3.1.2.jar" $OutPath
  cp "../common/lib/xercesImpl-2.7.1.jar" $OutPath

fi
#SectionEnd

if [ "$EPM" = "yes" ]; then

#Section  "Event Process Manager" SECEPM

  EPMINSTDIR="$APPSVRDIR/server/$APPSVRCONFIG"
  $mkdir $EPMINSTDIR
   
  OutPath="$EPMINSTDIR/bin"
  $mkdir $OutPath

  cp  "../common/linux/bin/service.sh" $OutPath
  cp  "../common/linux/bin/common.sh"  $OutPath
  
  OutPath="$EPMINSTDIR/conf"
  $mkdir $OutPath

  cp -i "../common/conf/users.properties"  $OutPath
  cp -i "../common/conf/roles.properties" $OutPath

  cp "../common/conf/service.policy" $OutPath
  cp "../event-process-manager/jboss/conf/linux/service.conf" $OutPath


  OutPath="$EPMINSTDIR/deploy"
  $mkdir $OutPath

  cp "../event-process-manager/jboss/deploy/singularity-epm-1.0-MAIN.ear" $OutPath
  cp "../event-process-manager/jboss/deploy/ejb-management.jar" $OutPath
  cp -i "../event-process-manager/jboss/deploy/singularity-mysql-ds.xml" $OutPath

 
  OutPath="$EPMINSTDIR/lib"
  $mkdir $OutPath

  cp "../event-process-manager/jboss/lib/mysql-connector-java-3.1.12-bin.jar"  $OutPath
  cp "../common/lib/wrapper-3.1.2.jar" $OutPath
 

fi    
#SectionEnd

#--------------------------------End Install Sesctions -------------------------------



