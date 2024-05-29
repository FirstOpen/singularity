#!/bin/sh
#
# 
# Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

PORT=3306
MHOST=`hostname`

mkdir="mkdir -p"

while getopts :m:u:p:d:P:h o
do	case "$o" in
	m)	MHOME="$OPTARG";;
	u)	MUSERNAME="$OPTARG";;
	p)	MPASSWORD="$OPTARG";;
        d)      MDB="$OPTARG";;
        P)	PORT="$OPTARG";;
        h)	MHOST="$OPTARG";;
        ?)      echo -e "Usage: $0 -m <mysqlhome> -u <username> -p <password> -d <database> [-P <port> ] [-h <hostname> ] 
            \n paramaters 
            \n -m MySQL home directory \
            \n -u user name  \
            \n -p password \
            \n -d database name \
            \n -P port (default 3306) \
            \n -h hostname (default localhost) \
            \n  "
		exit 1;;
	esac
done




"$MHOME/bin/mysql"  -u$MUSERNAME -p$MPASSWORD -P$PORT -D$MDB -h$MHOST -f < "./singularity_admin_mysql.sql"
"$MHOME/bin/mysql"  -u$MUSERNAME -p$MPASSWORD -P$PORT -D$MDB -h$MHOST -f < "./singularity_mysql.sql"
