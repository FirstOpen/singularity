; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Singularity"
!define PRODUCT_VERSION "1.0-M2"
!define PRODUCT_PUBLISHER "i-Konect LLC"
!define PRODUCT_WEB_SITE "http://www.i-konect.com/singularity"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\TestDeviceManager.cmd"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"

XPStyle on
; MUI 1.67 compatible ------
!include "MUI.nsh"
!include "AdvReplaceInFile.nsh"
!include "defines.nsh"


  Var /Global codebase
  Var /Global geocoord

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME

; License page
!insertmacro MUI_PAGE_LICENSE "..\..\..\license.txt"

; Components page
!insertmacro MUI_PAGE_COMPONENTS

;CustomDM Page
 Page custom SetCustom ValidateCustom
 
; Directory page
!insertmacro MUI_PAGE_DIRECTORY

; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "Singularity Services"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"


!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES

; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\dm\bin\TestDeviceManager.cmd"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "ServiceSetup.exe"
InstallDir "$PROGRAMFILES\singularity-1.0-M2"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show


Section "Device Manager" SEC01
 
  
  SetOutPath "$INSTDIR\dm\bin"
  SetOverwrite try
  File "bin\DeviceManager.cmd"
  File "bin\DeviceManagerNoWrapper.bat"
  File "bin\InstallDeviceManager.cmd"
  File "bin\TestDeviceManager.cmd"
  File "bin\UninstallDeviceManager.cmd"
  File "bin\wrapper.exe"
  File "bin\wrapper.log"
  SetOutPath "$INSTDIR\dm\conf"
  File "conf\DeviceManager.policy"
  File "conf\id.properties"
  File "conf\log4j.properties"
  File "conf\start-transient-DeviceManager.config"
  File "conf\transient-DeviceManager.config"
  File "conf\wrapper.conf"
  SetOutPath "$INSTDIR\dm\lib"
  File "lib\jini-core.jar"
  File "lib\jini-ext.jar"
  File "lib\sharedvm.jar"
  File "lib\start.jar"
  File "lib\sun-util.jar"
  File "lib\tools.jar"
  File "lib\wrapper.dll"
  File "lib\wrapper.jar"


; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Singularity Services.lnk" "$INSTDIR\dm\bin\TestDeviceManager.cmd"
  CreateShortCut "$DESKTOP\Singularity Services.lnk" "$INSTDIR\dm\bin\TestDeviceManager.cmd"
  !insertmacro MUI_STARTMENU_WRITE_END
  
  
SectionEnd
/*
Section "Configuration Service" SEC02
  SetOutPath "$INSTDIR\dm\conf"
  SetOverwrite ifnewer
; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section "Lookup Service" SEC03


; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd
*/
Section -AdditionalIcons
  SetOutPath $INSTDIR
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk" "$INSTDIR\uninst.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\dm\bin\TestDeviceManager.cmd"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\dm\bin\wrapper.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
  Exec "$INSTDIR\dm\bin\InstallDeviceManager.cmd"

   ;MessageBox MB_ICONINFORMATION|MB_OK "$geocoord"
   ;MessageBox MB_ICONINFORMATION|MB_OK "$codebase"

   ${AdvReplaceInFile} "geocoord=" "geocoord=$geocoord" all all "$INSTDIR\dm\conf\id.properties"

   ${AdvReplaceInFile} "classserver=masondevone.masoncorp.i-konect.com" "classserver=$codebase" all all "$INSTDIR\dm\conf\wrapper.conf"


SectionEnd

; Section descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SEC01} "Device Manager"
  ;!insertmacro MUI_DESCRIPTION_TEXT ${SEC02} "Supports DM request for confguration"
  ;!insertmacro MUI_DESCRIPTION_TEXT ${SEC03} "Jini Lookup Services"
!insertmacro MUI_FUNCTION_DESCRIPTION_END








; Functions
Function .onInit
 System::Call 'kernel32::CreateMutexA(i 0,i 0, t "myMutex") i .r1 ? e'
 Pop $R0
 
 StrCmp $R0 0 +3
 Messagebox MB_OK|MB_IcONEXCLAMATION "The installer is already running."
 Abort

 ; Extract InstallOptions INI files
 ; !insertmacro MUI_INSTALLOPTIONS_EXTRACT "customdm.ini"
  
  Call GetJRE
  Pop $R0
  StrCmp $R0 "1.5" +3
  Messagebox MB_OK|MB_IcONEXCLAMATION "Java  $R0 found, need 1.5"

FunctionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Abort
FunctionEnd




Function SetCustom ;FunctionName defined with Page command

    Internet::GetLocalHostName ${VAR_2}

    WriteINIStr $EXEDIR\custom.ini "Field 5" "State" "$2"
    ;Display the InstallOptionsEx dialog

    Push $R0
    InstallOptionsEx::dialog $EXEDIR\custom.ini
    Pop $R0

FunctionEnd



Function ValidateCustom

  ReadINIStr $geocoord $EXEDIR\custom.ini "Field 2" "State"
  ReadINIStr $codebase $EXEDIR\custom.ini "Field 5" "State"
   
FunctionEnd


Function GetJRE
;
;  Find JRE (Java.exe)
;  1 - in .\jre directory (JRE Installed with application)
;  2 - in JAVA_HOME environment variable
;  3 - in the registry
;  4 - assume java.exe in current dir or PATH

  Push $R0
  Push $R1

  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  Pop $R1
  Exch $R0
FunctionEnd








 ;Uninstall Section
 
Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP
  ExecWait   "$INSTDIR\dm\bin\UninstallDeviceManager.cmd"
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\dm\lib\wrapper.jar"
  Delete "$INSTDIR\dm\lib\wrapper.dll"
  Delete "$INSTDIR\dm\lib\tools.jar"
  Delete "$INSTDIR\dm\lib\sun-util.jar"
  Delete "$INSTDIR\dm\lib\start.jar"
  Delete "$INSTDIR\dm\lib\sharedvm.jar"
  Delete "$INSTDIR\dm\lib\jini-ext.jar"
  Delete "$INSTDIR\dm\lib\jini-core.jar"
  Delete "$INSTDIR\dm\conf\wrapper.conf"
  Delete "$INSTDIR\dm\conf\transient-DeviceManager.config"
  Delete "$INSTDIR\dm\conf\start-transient-DeviceManager.config"
  Delete "$INSTDIR\dm\conf\log4j.properties"
  Delete "$INSTDIR\dm\conf\id.properties"
  Delete "$INSTDIR\dm\conf\DeviceManager.policy"
  Delete "$INSTDIR\dm\bin\wrapper.log"
  Delete "$INSTDIR\dm\bin\wrapper.exe"
  Delete "$INSTDIR\dm\bin\UninstallDeviceManager.cmd"
  Delete "$INSTDIR\dm\bin\TestDeviceManager.cmd"
  Delete "$INSTDIR\dm\bin\InstallDeviceManager.cmd"
  Delete "$INSTDIR\dm\bin\DeviceManagerNoWrapper.bat"
  Delete "$INSTDIR\dm\bin\DeviceManager.cmd"

  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Website.lnk"
  Delete "$DESKTOP\Singularity Services.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Singularity Services.lnk"

  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir "$INSTDIR\dm\lib"
  RMDir "$INSTDIR\dm\conf"
  RMDir "$INSTDIR\dm\bin"

  
  
  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  SetAutoClose true
SectionEnd