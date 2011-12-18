Section  "Emulator" SECEM

;Emulator Page

  StrCpy $EMINSTDIR "$INSTDIR\em"
  SetOutPath "$EMINSTDIR\bin"
  SetOverwrite try

  File "..\common\win32\bin\wrapper.exe"
  File /oname=TestAWEmulator.cmd  "..\common\win32\bin\TestService.cmd"
  File /oname=TestIPEmulator.cmd  "..\common\win32\bin\TestService.cmd"
  File /oname=StartAWEmulator.cmd "..\common\win32\bin\StartService.cmd"
  File /oname=StartIPEmulator.cmd "..\common\win32\bin\StartService.cmd"
  File "..\common\win32\bin\Common.cmd"

  SetOutPath "$EMINSTDIR\conf"
  File "..\common\conf\service.policy"
  File "..\common\conf\log4j.properties"
  File "..\emulator\conf\win32\awwrapper.conf"
  File "..\emulator\conf\win32\ipwrapper.conf"
  File /oname=awservice.conf.bat "..\emulator\conf\win32\awservice.conf"
  File /oname=ipservice.conf.bat "..\emulator\conf\win32\ipservice.conf"

  SetOutPath "$EMINSTDIR\lib"
  File "..\common\lib\wrapper-3.1.2.jar"
  File "..\common\lib\singularity-devicemgr-1.0-MAIN.jar"
  File "..\common\lib\singularity-system-1.0-MAIN.jar"
  File "..\common\lib\singularity-util-1.0-MAIN.jar"
  File "..\common\lib\commons-logging-1.0.4.jar"
  File "..\common\lib\log4j-1.2.9.jar"
  File /oname=wrapper.dll "..\common\win32\lib\wrapper-3.1.2.dll"

  ;rewite config files

   ;modify configyuration files
  !insertmacro AdvReplaceInFile "wrapper.conf" "awwrapper.conf" all all "$EMINSTDIR\bin\TestAWEmulator.cmd"
  !insertmacro AdvReplaceInFile "wrapper.conf" "ipwrapper.conf" all all "$EMINSTDIR\bin\TestIPEmulator.cmd"
  !insertmacro AdvReplaceInFile "service.conf" "ipservice.conf" all all "$EMINSTDIR\bin\StartIPEmulator.cmd"
  !insertmacro AdvReplaceInFile "service.conf" "awservice.conf" all all "$EMINSTDIR\bin\StartAWEmulator.cmd"
  !insertmacro AdvReplaceInFile "<codebase>"   "$codebase"      all all "$EMINSTDIR\bin\Common.cmd"
  
  ; Shortcuts
  SetOutPath "$EMINSTDIR\bin"
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
      CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\EMAWID.lnk" "$EMINSTDIR\bin\StartAWEmulator.cmd" "" "$INSTDIR\icons\IconLibrary.dll" ${SING_SC_ICON}
      CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\EMIPICO.lnk" "$EMINSTDIR\bin\StartIPEmulator.cmd" "" "$INSTDIR\icons\IconLibrary.dll" ${SING_SC_ICON}
  !insertmacro MUI_STARTMENU_WRITE_END

  ;Registry
  WriteUninstaller "$EMINSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EMUninstallString" "$EMINSTDIR"

  ;Modify configuration files

  ;Install the Service
   nsExec::Exec  '"$EMINSTDIR\bin\wrapper.exe" -i "$EMINSTDIR\conf\awwrapper.conf"'
   nsExec::Exec  '"$EMINSTDIR\bin\wrapper.exe" -i "$EMINSTDIR\conf\ipwrapper.conf"'
SectionEnd


Section  "un.Emulator" un.SECEM
  ReadRegStr $EMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "EMUninstallString"
  nsExec::Exec  '"$EMINSTDIR\bin\wrapper.exe" -r "$EMINSTDIR\conf\awwrapper.conf"'
  nsExec::Exec  '"$EMINSTDIR\bin\wrapper.exe" -r "$EMINSTDIR\conf\ipwrapper.conf"'

  Delete "$EMINSTDIR\uninst.exe"

  ;DM cleanup
  Delete "$EMINSTDIR\lib\wrapper-3.1.2.jar"
  Delete "$EMINSTDIR\lib\singularity-devicemgr-1.0-MAIN.jar"
  Delete "$EMINSTDIR\lib\singularity-system-1.0-MAIN.jar"
  Delete "$EMINSTDIR\lib\singularity-util-1.0-MAIN.jar"
  Delete "$EMINSTDIR\lib\commons-logging-1.0.4.jar"
  Delete "$EMINSTDIR\lib\log4j-1.2.9.jar"
  Delete "$EMINSTDIR\lib\wrapper.dll"

  Delete "$EMINSTDIR\conf\awwrapper.conf"
  Delete "$EMINSTDIR\conf\ipwrapper.conf"
  Delete "$EMINSTDIR\conf\awservice.conf.bat"
  Delete "$EMINSTDIR\conf\ipservice.conf.bat"
  Delete "$EMINSTDIR\conf\log4j.properties"
  Delete "$EMINSTDIR\conf\service.policy"

  Delete "$EMINSTDIR\bin\wrapper.log"
  Delete "$EMINSTDIR\bin\wrapper.exe"
  Delete "$EMINSTDIR\bin\TestIPEmulator.cmd"
  Delete "$EMINSTDIR\bin\TestAWEmulator.cmd"
  Delete "$EMINSTDIR\bin\Common.cmd"
  Delete "$EMINSTDIR\bin\StartService.cmd"

  RMDir "$EMINSTDIR\lib"
  RMDir "$EMINSTDIR\conf"
  RMDir "$EMINSTDIR\bin"
  RMDir "$EMINSTDIR"

  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EMUninstallString"
SectionEnd
