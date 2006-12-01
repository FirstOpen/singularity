Section  "Config Manager" SECCM
  StrCpy $CMINSTDIR "$INSTDIR\cm"
  SetOutPath "$CMINSTDIR\bin"
  File /oname=ConfigManager.cmd "..\common\win32\bin\Service.cmd"
  File /oname=ConfigManagerNoWrapper.cmd "..\common\win32\bin\ServiceNoWrapper.cmd"
  File /oname=InstallConfigManager.cmd "..\common\win32\bin\InstallService.cmd"
  File /oname=TestConfigManager.cmd "..\common\win32\bin\TestService.cmd"
  File /oname=UninstallConfigManager.cmd "..\common\win32\bin\UninstallService.cmd"
  File "..\common\win32\bin\wrapper.exe"
  File "..\common\win32\bin\startservice.cmd"
  File "..\common\win32\bin\common.cmd"

  SetOutPath "$CMINSTDIR\conf"
  File "..\common\conf\service.policy"
  File "..\common\conf\id.properties"
  File "..\common\conf\log4j.properties"

  File "..\config-manager\conf\jrmp-reggie.config"
  File "..\config-manager\conf\start-transient-ConfigManager.config"
  File "..\config-manager\conf\transient-ConfigManager.config"
  File "..\config-manager\conf\win32\wrapper.conf"
  File /oname=service.conf.bat "..\config-manager\conf\win32\service.conf"


  SetOutPath "$CMINSTDIR\lib"
  File "..\common\lib\commons-collections-3.1.jar"
  File "..\common\lib\commons-configuration-1.2.jar"
  File "..\common\lib\commons-lang-2.1.jar"
  File "..\common\lib\commons-logging-1.0.4.jar"
  File "..\common\lib\hibernate-3.1.jar"
  File "..\common\lib\jbossall-client-4.0.3SP1.jar"
  File "..\common\lib\jini-core-2.1.jar"
  File "..\common\lib\jini-ext-2.1.jar"
  File "..\common\lib\jsk-dl-2.1.jar"
  File "..\common\lib\log4j-1.2.9.jar"
  File "..\common\lib\reggie-dl-2.1.jar"
  File "..\common\lib\reggie-2.1.jar"
  File "..\common\lib\RXTXcomm-2.1-pre17.jar"
  File "..\common\lib\sharedvm-2.1.jar"
  File "..\common\lib\singularity-config-1.0-MAIN.jar"
  File "..\common\lib\singularity-devicemgr-1.0-MAIN.jar"
  File "..\common\lib\singularity-system-1.0-MAIN.jar"
  File "..\common\lib\singularity-util-1.0-MAIN.jar"
  File "..\common\lib\start-2.1.jar"
  File "..\common\lib\sun-util-2.1.jar"
  File "..\common\lib\tools-2.1.jar"
  File "..\common\lib\wrapper-3.1.2.jar"
  File "..\common\lib\xercesImpl-2.7.1.jar"
  File /oname=wrapper.dll "..\common\win32\lib\wrapper-3.1.2.dll"

;ReWrite Config Files



; Shortcuts
  SetOutPath "$CMINSTDIR\bin"
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
      CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\CM.lnk" "$CMINSTDIR\bin\startservice.cmd" "" "$INSTDIR\icons\IconLibrary.dll" ${SING_SC_ICON}
  !insertmacro MUI_STARTMENU_WRITE_END

  WriteUninstaller "$CMINSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "CMUninstallString" "$CMINSTDIR"


  ;modify configuration files
  ${AdvReplaceInFile} "<codebase>" "$codebase" all all "$CMINSTDIR\conf\wrapper.conf"
  ${AdvReplaceInFile} "<epmhost>"  "$epmhost"  all all "$CMINSTDIR\conf\id.properties"
  
  ;set the hostname for codebase server
  ${AdvReplaceInFile} "<codebase>" "$codebase" all all "$CMINSTDIR\bin\common.cmd"

  ;Install the Service
   nsExec::Exec  '"$CMINSTDIR\bin\wrapper.exe" -i "$CMINSTDIR\conf\wrapper.conf"'

SectionEnd


Section  "un.Config Manager" un.SECCM
  ReadRegStr $CMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "CMUninstallString"
  nsExec::Exec  '"$CMINSTDIR\bin\wrapper.exe" -r "$CMINSTDIR\conf\wrapper.conf"'

  ;Delete Files
  Delete "$CMINSTDIR\uninst.exe"

  Delete "$CMINSTDIR\lib\xercesImpl-2.7.1.jar"
  Delete "$CMINSTDIR\lib\wrapper-3.1.2.jar"
  Delete "$CMINSTDIR\lib\wrapper.dll"
  Delete "$CMINSTDIR\lib\tools-2.1.jar"
  Delete "$CMINSTDIR\lib\sun-util-2.1.jar"
  Delete "$CMINSTDIR\lib\start-2.1.jar"
  Delete "$CMINSTDIR\lib\singularity-util-1.0-MAIN.jar"
  Delete "$CMINSTDIR\lib\singularity-system-1.0-MAIN.jar"
  Delete "$CMINSTDIR\lib\singularity-devicemgr-1.0-MAIN.jar"
  Delete "$CMINSTDIR\lib\singularity-config-1.0-MAIN.jar"
  Delete "$CMINSTDIR\lib\sharedvm-2.1.jar"
  Delete "$CMINSTDIR\lib\RXTXcomm-2.1-pre17.jar"
  Delete "$CMINSTDIR\lib\reggie-2.1.jar"
  Delete "$CMINSTDIR\lib\reggie-dl-2.1.jar"
  Delete "$CMINSTDIR\lib\jsk-dl-2.1.jar"
  Delete "$CMINSTDIR\lib\jini-ext-2.1.jar"
  Delete "$CMINSTDIR\lib\jini-core-2.1.jar"
  Delete "$CMINSTDIR\lib\jbossall-client-4.0.3SP1.jar"
  Delete "$CMINSTDIR\lib\hibernate-3.1.jar"
  Delete "$CMINSTDIR\lib\log4j-1.2.9.jar"
  Delete "$CMINSTDIR\lib\commons-logging-1.0.4.jar"
  Delete "$CMINSTDIR\lib\commons-lang-2.1.jar"
  Delete "$CMINSTDIR\lib\commons-configuration-1.2.jar"
  Delete "$CMINSTDIR\lib\commons-collections-3.1.jar"

  Delete "$CMINSTDIR\conf\wrapper.conf"
  Delete "$CMINSTDIR\conf\service.conf.bat"
  Delete "$CMINSTDIR\conf\transient-ConfigManager.config"
  Delete "$CMINSTDIR\conf\start-transient-ConfigManager.config"
  Delete "$CMINSTDIR\conf\log4j.properties"
  Delete "$CMINSTDIR\conf\jrmp-reggie.config"
  Delete "$CMINSTDIR\conf\id.properties"
  Delete "$CMINSTDIR\conf\service.policy"

  Delete "$CMINSTDIR\bin\wrapper.log"
  Delete "$CMINSTDIR\bin\wrapper.exe"
  Delete "$CMINSTDIR\bin\UninstallConfigManager.cmd"
  Delete "$CMINSTDIR\bin\TestConfigManager.cmd"
  Delete "$CMINSTDIR\bin\InstallConfigManager.cmd"
  Delete "$CMINSTDIR\bin\configmgr.log"
  Delete "$CMINSTDIR\bin\ConfigManagerNoWrapper.cmd"
  Delete "$CMINSTDIR\bin\ConfigManager.cmd"
  Delete "$CMINSTDIR\bin\startservice.cmd"
  Delete "$CMINSTDIR\bin\common.cmd"


  RMDir "$CMINSTDIR\lib"
  RMDir "$CMINSTDIR\conf"
  RMDir "$CMINSTDIR\bin"
  RMDir "$CMINSTDIR"


  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "CMUninstallString"
SectionEnd

Function SetCMCustom ;FunctionName defined with Page command

    Internet::GetLocalHostName ${VAR_2}
    !insertmacro MUI_INSTALLOPTIONS_WRITE "cmcustom.ini" "Field 2" "State" "$2"

    ;ensure the codebase is set
    Internet::GetLocalHostName ${VAR_2}
    !insertmacro MUI_INSTALLOPTIONS_WRITE "cmcustom.ini" "Field 5" "State" "$2"


     ;set Header
    !insertmacro MUI_HEADER_TEXT "Config Manager Options" "Install Options "


    ;if Device Manager is not selected do not display
    SectionGetFlags "${SECCM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue
    goto done

    continue:
    !insertmacro  MUI_INSTALLOPTIONS_INITDIALOG "cmcustom.ini"
    !insertmacro MUI_INSTALLOPTIONS_SHOW


    done:
FunctionEnd



Function ValidateCMCustom

  !insertmacro MUI_INSTALLOPTIONS_READ $epmhost "cmcustom.ini" "Field 2" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ $codebase "cmcustom.ini" "Field 5" "State"


FunctionEnd

