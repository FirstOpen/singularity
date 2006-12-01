#!/bin/sh
#
# 
# Copyright 2006 i-Konect LLC
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
# $Id: release.sh 1 2006-08-17 20:06:45Z jthomasrose $
#

 SINGULARITY_HOME=.
 RELEASE=singularity-1.0-M2
 mkdir $RELEASE
 
 cp -r $SINGULARITY_HOME/install  $RELEASE
 cp    $SINGULARITY_HOME/*.txt    $RELEASE

 mkdir $RELEASE/document

 for  file in `find $SINGULARITY_HOME/document -name *.pdf`
 do
  cp $file $RELEASE/document
 done

