  Var /Global hostname
  Var /Global port
  Var /Global username
  Var /Global password
  Var /Global database
  Var /Global mysqlhome


Section  "Event Process Manager" SECEPM

  StrCpy $EPMINSTDIR "$APPSVRDIR\server\$APPSVRNAME"

  SetOutPath "$EPMINSTDIR\bin"
  SetOverwrite try
  File /oname=EPM.cmd "..\common\win32\bin\Service.cmd"
  File /oname=EPMNoWrapper.cmd "..\common\win32\bin\ServiceNoWrapper.cmd"
  File /oname=InstallEPM.cmd "..\common\win32\bin\InstallService.cmd"
  File /oname=TestEPM.cmd "..\common\win32\bin\TestService.cmd"
  File /oname=UninstallEPM.cmd "..\common\win32\bin\UninstallService.cmd"
  File "..\common\win32\bin\wrapper.exe"
  File "..\common\win32\bin\StartService.cmd"
  File "..\common\win32\bin\Common.cmd"

  SetOutPath "$EPMINSTDIR\conf"

  SetOverwrite off ;if already exits just leave it
  File "..\common\conf\users.properties"
  File "..\common\conf\roles.properties"
  SetOverwrite on

  File "..\common\conf\service.policy"
  File "..\event-process-manager\jboss\conf\win32\wrapper.conf"
  File /oname=service.conf.bat "..\event-process-manager\jboss\conf\win32\service.conf"

  SetOutPath "$EPMINSTDIR\deploy"
  File "..\event-process-manager\jboss\deploy\singularity-epm-1.0-MAIN.ear"
  File "..\event-process-manager\jboss\deploy\ejb-management.jar"
  
  SetOverwrite off ;if already exits just leave it
  File "..\event-process-manager\jboss\deploy\singularity-mysql-ds.xml"
  SetOverwrite on

  SetOutPath "$EPMINSTDIR\lib"
  File "..\event-process-manager\jboss\lib\mysql-connector-java-3.1.12-bin.jar"


  SetOutPath "$APPSVRDIR\lib"
  File /oname=wrapper.dll "..\common\win32\lib\wrapper-3.1.2.dll"
  File "..\common\lib\wrapper-3.1.2.jar"
  File "..\common\lib\wrappertest-3.1.2.jar"

  ; Shortcuts
  SetOutPath "$EPMINSTDIR\bin"
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
     CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\EPM.lnk" "$EPMINSTDIR\bin\StartService.cmd" "" "$INSTDIR\icons\IconLibrary.dll" ${SING_SC_ICON}
  !insertmacro MUI_STARTMENU_WRITE_END

  WriteUninstaller "$EPMINSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EPMUninstallString" "$EPMINSTDIR"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EPMAppSvrUninstallString" "$APPSVRDIR"

  ;modify configyuration files
  ${AdvReplaceInFile}  "<appsvr_home>"  "$APPSVRDIR"         all all "$EPMINSTDIR\conf\wrapper.conf"
  ${AdvReplaceInFile}  "<servername>"   "$APPSVRNAME"        all all "$EPMINSTDIR\conf\wrapper.conf"
  ${AdvReplaceInFile}  "<wrapper_jar>"  "wrapper-3.1.2.jar"  all all "$EPMINSTDIR\conf\wrapper.conf"

  ${AdvReplaceInFile}  "<hostname>"  "$hostname" all all "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"
  ${AdvReplaceInFile}  "<port>"      "$port"     all all "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"
  ${AdvReplaceInFile}  "<database>"  "$database" all all "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"
  ${AdvReplaceInFile}  "<username>"  "$username" all all "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"
  ${AdvReplaceInFile}  "<pass-word>" "$password" all all "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"


   ;Install the Service
   nsExec::Exec  '"$EPMINSTDIR\bin\wrapper.exe" -i "$EPMINSTDIR\conf\wrapper.conf"'
   
   ;Write out Environment Variable
   !insertmacro WRITE_ENV_STR "JBOSS_HOME" "$APPSVRDIR"
   
SectionEnd



Section  "un.Event Process Manager" un.SECEPM
  ReadRegStr $EPMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "EPMUninstallString"
  ReadRegStr $APPSVRDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "EPMAppSvrUninstallString"

  nsExec::Exec  '"$EPMINSTDIR\bin\wrapper.exe" -r "$EPMINSTDIR\conf\wrapper.conf"'


  Delete "$EPMINSTDIR\uninst.exe"

  ;Delete Files
  Delete "$EPMINSTDIR\bin\EPM.cmd"
  Delete "$EPMINSTDIR\bin\EPMNoWrapper.cmd"
  Delete "$EPMINSTDIR\bin\InstallEPM.cmd"
  Delete "$EPMINSTDIR\bin\TestEPM.cmd"
  Delete "$EPMINSTDIR\bin\UninstallEPM.cmd"
  Delete "$EPMINSTDIR\bin\wrapper.exe"
  Delete "$EPMINSTDIR\bin\wrapper.log"
  Delete "$EPMINSTDIR\bin\StartService.cmd"
  Delete "$EPMINSTDIR\bin\Common.cmd"

  Delete "$EPMINSTDIR\conf\epm.policy"
  Delete "$EPMINSTDIR\conf\wrapper.conf"
  Delete "$EPMINSTDIR\conf\service.conf.bat"

  Delete "$EPMINSTDIR\deploy\singularity-epm-1.0-MAIN.ear"
  Delete "$EPMINSTDIR\deploy\singularity-mysql-ds.xml"

  Delete "$EPMINSTDIR\lib\mysql-connector-java-3.1.12-bin.jar"

  Delete "$APPSVRDIR\lib\wrapper-3.1.2.jar"
  Delete "$APPSVRDIR\lib\wrappertest-3.1.2.jar"
  Delete "$APPSVRDIR\lib\wrapper.dll"

  ;Remove Directories
  RMDir "$EPMINSTDIR\bin"

  ;Clean Registry
  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EPMUninstallString"
  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EPMAppSvrUninstallString"
  
  ;Env
  ;Push "JBOSS_HOME"
  ;Call un.DeleteEnvStr
SectionEnd


Function SetEPMCustom ;FunctionName defined with Page command
     ;set Header
    !insertmacro MUI_HEADER_TEXT "Event Process Manager" "Install Options - Currently supports JBoss 4.0.2 and 4.0.3SP1 "

    ;ensure the codebase is set
    ReadEnvStr $R0 "JBOSS_HOME"
    StrCmp $R0 "" skip
   !insertmacro MUI_INSTALLOPTIONS_WRITE "epmcustom.ini" "Field 2" "State" "$R0"
    skip:

    ;if EPM is not selected do not display
    SectionGetFlags "${SECEPM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue
    goto done

    continue:

    !insertmacro  MUI_INSTALLOPTIONS_INITDIALOG "epmcustom.ini"
    !insertmacro MUI_INSTALLOPTIONS_SHOW


    done:
FunctionEnd



Function ValidateEPMCustom

 !insertmacro MUI_INSTALLOPTIONS_READ $APPSVRDIR "epmcustom.ini" "Field 2" "State"
 !insertmacro MUI_INSTALLOPTIONS_READ $APPSVRNAME "epmcustom.ini" "Field 5" "State"

FunctionEnd

