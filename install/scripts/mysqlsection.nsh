
Section  "Initialize Database" SECDB

  SetOutPath "$INSTDIR\db"

   nsExec::ExecToLog '"$INSTDIR\db\load.cmd" "$mysqlhome\bin\mysql"  $username $password $port $database "$INSTDIR\db\singularity_mysql.sql" $hostname'

   nsExec::ExecToLog '"$INSTDIR\db\load.cmd" "$mysqlhome\bin\mysql"  $username $password $port $database "$INSTDIR\db\singularity_admin_mysql.sql" $hostname'

SectionEnd

Section  "un.Initialize Database" un.SECDB
  Exec '"$mysqlhome\bin\mysql -u$username -p$password -P$port -e drop database $database"'
SectionEnd

Function SetMySQLCustom ;FunctionName defined with Page command

    !insertmacro MUI_HEADER_TEXT "Database Configuration"  "Currently support  MySQL versions 4.x and 5.x"
    ;ensure the codebase is set
    Internet::GetLocalHostName ${VAR_2}
    !insertmacro MUI_INSTALLOPTIONS_WRITE  "mysqlcustom.ini" "Field 4" "State" "$2"


    ;System::Call "advapi32::GetUserName(t .r0, *i ${NSIS_MAX_STRLEN} r1) i.r2"
   ;!insertmacro MUI_INSTALLOPTIONS_WRITE  "mysqlcustom.ini" "Field 6" "State" "$0"

   !insertmacro MUI_INSTALLOPTIONS_WRITE  "mysqlcustom.ini" "Field 9" "State" "$PROGRAMFILES\MySQL\MySQL Server 5.0"
    ;if Event Process Manager or DB is not selected do not display
    SectionGetFlags "${SECEPM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDB}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    goto done

    continue:

    !insertmacro  MUI_INSTALLOPTIONS_INITDIALOG "mysqlcustom.ini"
    !insertmacro  MUI_INSTALLOPTIONS_SHOW



    done:
FunctionEnd



Function ValidateMySQLCustom


  !insertmacro MUI_INSTALLOPTIONS_READ  $hostname  "mysqlcustom.ini" "Field 4" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ  $port      "mysqlcustom.ini" "Field 5" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ  $username  "mysqlcustom.ini" "Field 6" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ  $password  "mysqlcustom.ini" "Field 7" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ  $database  "mysqlcustom.ini" "Field 8" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ  $mysqlhome "mysqlcustom.ini" "Field 9" "State"

FunctionEnd




