; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Singularity"
!define PRODUCT_VERSION "1.0-M2"
!define PRODUCT_PUBLISHER "i-Konect LLC"
!define PRODUCT_WEB_SITE "http://www.i-konect.com/singularity"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\wrapper.exe"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"


 XPStyle on


; MUI 1.67 compatible ------

!include "MUI.nsh"
!include "AdvReplaceInFile.nsh"
!include "defines.nsh"
!include "sections.nsh"

!define SING_SC_ICON 4

; MUI Settings
 !define MUI_ABORTWARNING
;!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
 !define MUI_ICON "SingularityInstall.ico"
; !define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"
 !define MUI_UNICON "UnSingularityInstall.ico"
 !define MUI_DEVICON "${NSISDIR}\Contrib\Graphics\Icons\device.ico"

BrandingText "i-Konect - Singularity 1.0-M2"


; Welcome page
 ;!define MUI_WELCOMEPAGE_TITLE "title"
 !define MUI_WELCOMEPAGE_TITLE_3LINES "3lines"
 !define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the installation of $(^NameDA) Device Manager and Emulator Only.\r\n\r\nIt is recommended \
 that you close all other applications before starting Setup.\
 This will make it possible to update relevant system files without having to reboot your computer.\r\n \r\n\
 Singularity is Java(TM) based technology, although will run as native operating system services to ease administration and use. There is no native code in Singularity so it can also be run at the command prompt like any other Java(TM) application.\r\n\r\n$_CLICK"

 !define MUI_WELCOMEFINISHPAGE_BITMAP "splash.bmp"
 !define MUI_WELCOMEFINISHPAGE_BITMAP_NOSTRETCH
!insertmacro MUI_PAGE_WELCOME

; License page
!insertmacro MUI_PAGE_LICENSE "license.txt"

; Components page
 !define MUI_PAGE_CUSTOMFUNCTION_LEAVE  ValidateComponent
 !insertmacro MUI_PAGE_COMPONENTS

;Display the InstallOptionsEx dialog
 !define MUI_PAGE_CUSTOMFUNCTION_PRE  PreDirPage
 !define MUI_PAGE_CUSTOMFUNCTION_SHOW ShowDirPage
 !insertmacro MUI_PAGE_DIRECTORY

;Custom pages
 Page custom SetDMCustom         ValidateDMCustom
 

; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "Singularity"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP

; Instfiles page
!insertmacro MUI_PAGE_INSTFILES

; Finish page
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_FINISHPAGE_TEXT "$(^NameDA) has been installed on your computer.\r\n\r\nClick Finish to close this wizard.\r\n\r\n\
  Please check your service manager for the installed components.\r\n\r\nComponent service names : \r\n${PRODUCT_NAME} <ComponentName> ${PRODUCT_VERSION}"

;!define MUI_FINISHPAGE_RUN
;!define MUI_FINISHPAGE_RUN_FUNCTION FinishInstall
;!define MUI_FINISHPAGE_RUN_TEXT "Services"
!define MUI_FINISHPAGE_LINK "singularity.i-konect.com"
!define MUI_FINISHPAGE_LINK_LOCATION "http://singularity.i-konect.com"
!insertmacro MUI_PAGE_FINISH

;------------------ End Pages ----------------

;Uninstall Pages
 !insertmacro MUI_UNPAGE_CONFIRM

 !insertmacro MUI_UNPAGE_COMPONENTS

 !insertmacro MUI_UNPAGE_INSTFILES

 ; UnFinish page
 !define MUI_UNFINISHPAGE_NOAUTOCLOSE
 !insertmacro MUI_UNPAGE_FINISH

; Language files
 !insertmacro MUI_LANGUAGE "English"


;-------------------- MUI End ----------------------

!define ALL_USERS
!include WriteEnvStr.nsh


Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "..\DMSetup.exe"
InstallDir "$PROGRAMFILES\Singularity-1.0-M2"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails hide
ShowUnInstDetails hide

;------------------ Main Declerations End --------
!include "globalvar.nsh"
;----------------- Sections ----------------------

!include "emsection.nsh"
!include "dmsection.nsh"
!include "comsection.nsh"


Section  ""
 CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
 SetOutPath "$INSTDIR\icons"
 File "IconLibrary.dll"
SectionEnd

;----------- Post Uninstall Sections -----------------
Var /Global TestAll
Section  -un.Post
   StrCpy $TestAll "1"
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP

   ReadRegStr $EMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "EMUninstallString"
   StrCmp $EMINSTDIR "" n0
     StrCpy $TestAll "0"
     goto m0
   n0:
   Delete "$SMPROGRAMS\$ICONS_GROUP\EMAWID.lnk"
   Delete "$SMPROGRAMS\$ICONS_GROUP\EMIPICO.lnk"
   m0:

   ReadRegStr $DMINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "DMUninstallString"
   StrCmp $DMINSTDIR "" n3
     StrCpy $TestAll "0"
     goto m3
   n3:
   Delete "$SMPROGRAMS\$ICONS_GROUP\DM.lnk"
   m3:
   StrCmp $TestAll "0" stop

  ;Delete the files
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"

  ;Clean up shortcuts
  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Website.lnk"
  Delete "$INSTDIR\icons\IconLibrary.dll"

  ;Remove the Directories
  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir "$INSTDIR\icons"
  RMDir "$INSTDIR"

  ;Clean the registry
  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  SetAutoClose true

  stop:
SectionEnd
;----------- End Post Uninstall Sections -----------------

; Section descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SECDM}  "Device Managers to support Reader/Interrogator integration, and can be installed independent of other components"
  !insertmacro MUI_DESCRIPTION_TEXT ${SECEM}  "Emulators for various readers, used for testing purposes."
!insertmacro MUI_FUNCTION_DESCRIPTION_END

; UNSection descriptions
!insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${un.SECDM}  "Uninstall Device Manager"
  !insertmacro MUI_DESCRIPTION_TEXT ${un.SECEM}  "Uninstall Emulators"
!insertmacro MUI_UNFUNCTION_DESCRIPTION_END

;-----------------End Sections--------------------

;--------------- Functions -----------------------

!include "commonfunc.nsh"

Function .onInit

; Extract InstallOptions INI files
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "dmcustom.ini"

 System::Call 'kernel32::CreateMutexA(i 0,i 0, t "myMutex") i .r1 ? e'
 Pop $R0

 StrCmp $R0 0 +3
 Messagebox MB_OK|MB_IcONEXCLAMATION "The installer is already running."
 Abort

  Call GetJREVersion
  Pop $R0
  StrCmp $R0 "1.5" +3
  Messagebox MB_OK|MB_IcONEXCLAMATION "Java $R0 found, need JDK 1.5"
  Abort

   Call CheckJavaHome
FunctionEnd
