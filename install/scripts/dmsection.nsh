Section  "Device Manager" SECDM

;CustomDM Page

  StrCpy $DMINSTDIR "$INSTDIR\dm"
  SetOutPath "$DMINSTDIR\bin"
  SetOverwrite try
  File /oname=DeviceManager.cmd "..\common\win32\bin\Service.cmd"
  File /oname=DeviceManagerNoWrapper.cmd "..\common\win32\bin\ServiceNoWrapper.cmd"
  File /oname=InstallDeviceManager.cmd "..\common\win32\bin\InstallService.cmd"
  File /oname=TestDeviceManager.cmd "..\common\win32\bin\TestService.cmd"
  File /oname=UninstallDeviceManager.cmd "..\common\win32\bin\UninstallService.cmd"
  File "..\common\win32\bin\wrapper.exe"
  File "..\common\win32\bin\StartService.cmd"
  File "..\common\win32\bin\Common.cmd"

  SetOutPath "$DMINSTDIR\conf"
  File "..\common\conf\service.policy"
  File "..\common\conf\id.properties"
  File "..\common\conf\log4j.properties"
  File "..\device-manager\conf\start-transient-DeviceManager.config"
  File "..\device-manager\conf\transient-DeviceManager.config"
  File "..\device-manager\conf\win32\wrapper.conf"
  File /oname=service.conf.bat "..\device-manager\conf\win32\service.conf"


  SetOutPath "$DMINSTDIR\lib"
  File "..\common\lib\jini-core-2.1.jar"
  File "..\common\lib\jini-ext-2.1.jar"
  ;File "..\common\lib\sharedvm-2.1.jar"
  File "..\common\lib\start-2.1.jar"
  File "..\common\lib\sun-util-2.1.jar"
  ;File "..\common\lib\tools-2.1.jar"
 ; File "..\common\lib\jbossmq-client-4.0.3SP1.jar"
 ; File "..\common\lib\jboss-j2ee-4.0.3SP1.jar"
 ; File "..\common\lib\jnp-client-4.0.3SP1.jar"
 ; File "..\common\lib\jboss-common-client-4.0.3SP1.jar"
 ; File "..\common\lib\jbossall-client-4.0.3SP1.jar"
 ; File "..\common\lib\commons-logging-1.0.4.jar"
 ; File "..\common\lib\log4j-1.2.9.jar"

  File "..\common\lib\wrapper-3.1.2.jar"
  File /oname=wrapper.dll "..\common\win32\lib\wrapper-3.1.2.dll"

  ; Shortcuts
   SetOutPath "$DMINSTDIR\bin"
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
      CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\DM.lnk" "$DMINSTDIR\bin\StartService.cmd" "" "$INSTDIR\icons\IconLibrary.dll" ${SING_SC_ICON}
  !insertmacro MUI_STARTMENU_WRITE_END

  ;Registry
  WriteUninstaller "$DMINSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DMUninstallString" "$DMINSTDIR"


  ;Modify configuration files
  !insertmacro AdvReplaceInFile "<id>" "$id" all all "$DMINSTDIR\conf\id.properties"
  !insertmacro AdvReplaceInFile "<geocoord>" "$geocoord" all all "$DMINSTDIR\conf\id.properties"
  !insertmacro AdvReplaceInFile "<codebase>" "$codebase" all all "$DMINSTDIR\conf\wrapper.conf"
  !insertmacro AdvReplaceInFile "<codebase>" "$codebase" all all "$DMINSTDIR\bin\Common.cmd"

  ;Install the Service
   nsExec::Exec  '"$DMINSTDIR\bin\wrapper.exe" -i "$DMINSTDIR\conf\wrapper.conf"'

SectionEnd

Section  "un.Device Manager" un.SECDM
  ReadRegStr $DMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "DMUninstallString"
  nsExec::Exec  '"$DMINSTDIR\bin\wrapper.exe" -r "$DMINSTDIR\conf\wrapper.conf"'

  Delete "$DMINSTDIR\uninst.exe"

  ;DM cleanup

  ;Delete "$DMINSTDIR\lib\wrapper-3.1.2.jar"
  Delete "$DMINSTDIR\lib\wrapper.dll"
  ;Delete "$DMINSTDIR\lib\tools-2.1.jar"
  ;Delete "$DMINSTDIR\lib\sun-util-2.1.jar"
  Delete "$DMINSTDIR\lib\start-2.1.jar"
  ;Delete "$DMINSTDIR\lib\sharedvm-2.1.jar"
  ;Delete "$DMINSTDIR\lib\jini-ext-2.1.jar"
  ;Delete "$DMINSTDIR\lib\jini-core-2.1.jar"
  ;Delete "$DMINSTDIR\lib\jbossmq-client-4.0.3SP1.jar"
  ;Delete "$DMINSTDIR\lib\jboss-j2ee-4.0.3SP1.jar"
  ;Delete "$DMINSTDIR\lib\jnp-client-4.0.3SP1.jar"
  ;Delete "$DMINSTDIR\lib\jboss-common-client-4.0.3SP1.jar"
  ;Delete "$DMINSTDIR\lib\jbossall-client-4.0.3SP1.jar"
  ;Delete "$DMINSTDIR\lib\log4j-1.2.9.jar"
  ;Delete "$DMINSTDIR\lib\commons-logging-1.0.4.jar"

  Delete "$DMINSTDIR\conf\wrapper.conf"
  Delete "$DMINSTDIR\conf\service.conf.bat"
  Delete "$DMINSTDIR\conf\transient-DeviceManager.config"
  Delete "$DMINSTDIR\conf\start-transient-DeviceManager.config"
  Delete "$DMINSTDIR\conf\log4j.properties"
  Delete "$DMINSTDIR\conf\id.properties"
  Delete "$DMINSTDIR\conf\service.policy"

  Delete "$DMINSTDIR\bin\wrapper.log"
  Delete "$DMINSTDIR\bin\wrapper.exe"
  Delete "$DMINSTDIR\bin\UninstallDeviceManager.cmd"
  Delete "$DMINSTDIR\bin\TestDeviceManager.cmd"
  Delete "$DMINSTDIR\bin\InstallDeviceManager.cmd"
  Delete "$DMINSTDIR\bin\devicemgr.log"
  Delete "$DMINSTDIR\bin\DeviceManagerNoWrapper.cmd"
  Delete "$DMINSTDIR\bin\DeviceManager.cmd"
  Delete "$DMINSTDIR\bin\StartService.cmd"
  Delete "$DMINSTDIR\bin\Common.cmd"

  RMDir "$DMINSTDIR\lib"
  RMDir "$DMINSTDIR\conf"
  RMDir "$DMINSTDIR\bin"
  RMDir "$DMINSTDIR"


  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DMUninstallString"
SectionEnd



Function SetDMCustom ;FunctionName defined with Page command

    ;ensure the codebase is set
    Internet::GetLocalHostName ${VAR_2}
    !insertmacro MUI_INSTALLOPTIONS_WRITE "dmcustom.ini" "Field 5" "State" "$2"


     ;set Header
    !insertmacro MUI_HEADER_TEXT "Device Manager Options" "Install Options - Each Device Manager installed must have a unique ID "


    ;if Device Manager is not selected do not display
    SectionGetFlags "${SECDM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue
    goto done
    
    continue:
    !insertmacro  MUI_INSTALLOPTIONS_INITDIALOG "dmcustom.ini"
    !insertmacro MUI_INSTALLOPTIONS_SHOW


    done:
FunctionEnd



Function ValidateDMCustom

  !insertmacro MUI_INSTALLOPTIONS_READ $geocoord "dmcustom.ini" "Field 2" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ $codebase "dmcustom.ini" "Field 5" "State"
  !insertmacro MUI_INSTALLOPTIONS_READ $id       "dmcustom.ini" "Field 8" "State"


FunctionEnd
