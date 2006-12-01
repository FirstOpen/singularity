@rem
@rem 
@rem Copyright 2005 i-Konect LLC
@rem 
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem 
@rem 	http://www.apache.org/licenses/LICENSE-2.0
@rem 
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem 
@rem

 set DM_CONFIG=../conf/start-transient-DeviceManager.config
 set CM_CONFIG=../conf/start-transient-ConfigManager.config
 set LU_CONFIG=../conf/jrmp-reggie.config


if "%JAVA_HOME%" == "" ( 
  goto FAIL
) 


 set JAVA_BIN=%JAVA_HOME%/bin
 set JAVA_LIB_EXT=%JAVA_HOME%/lib/ext

 set JAVA_OPTS=-server -cp ../conf -Djava.security.manager ^
-Djava.security.policy=../conf/service.policy ^
-Dcom.sun.management.jmxremote ^
-Dorg.firstopen.singularity.httpd.port=8081 ^
-Dorg.firstopen.singularity.home=.. ^
-Dorg.firstopen.singularity.classserver=<codebase> ^
-Djava.ext.dirs=%JAVA_LIB_EXT%;../lib

set SERVICE_CMD=%JAVA_BIN%\java

goto SUCCESS

FAIL:
echo Must set CoMPUTERNAME environment variable...cannot be localhost!

SUCESS: