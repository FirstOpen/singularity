

;Common Functions

Function un.onUninstSuccess
 ; HideWindow
;  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd

Function un.onInit
 System::Call 'kernel32::CreateMutexA(i 0,i 0, t "myMutex") i .r1 ? e'
 Pop $R0

 StrCmp $R0 0 +3
 Messagebox MB_OK|MB_IcONEXCLAMATION "The installer is already running."
 Abort

  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Abort

FunctionEnd


Function PreDirPage ;FunctionName defined with Page command

    ;if Device/Config Manager is not selected do not display
    SectionGetFlags "${SECCM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDB}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue
    
    ;neither CM or DM is installed no neeed to show page
    Abort

    continue:
    ;show the directory page


FunctionEnd

LangString TEXT_IO_TITLE ${LANG_ENGLISH} "Installation Destination for Emulator, Device, and Config Managers"
LangString TEXT_IO_SUBTITLE ${LANG_ENGLISH} "install destination"

Function ShowDirPage ;FunctionName defined with Page command


    ;show the directory page
    !insertmacro MUI_HEADER_TEXT "$(TEXT_IO_TITLE)" "$(TEXT_IO_SUBTITLE)"


FunctionEnd


Function ValidateComponent
 
    ;if Device Manager is not selected do not display
    SectionGetFlags "${SECCM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECEPM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDB}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECEM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    SectionGetFlags "${SECDOC}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} continue

    ;noting selected
     Messagebox MB_OK|MB_IcONEXCLAMATION "At least one component must be selected"
     Abort

 continue:

FunctionEnd

Function GetJREVersion
  Push $R0
  Push $R1

  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  Pop $R1
  Exch $R0
FunctionEnd

Function CheckJavaHome
  Push $R0
  Push $R1

  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\java.exe"

  IfErrors 0 JreFound

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R1" "JavaHome"
  !insertmacro WRITE_ENV_STR "JAVA_HOME" "$R0"

  JreFound:
  Pop $R1
  Exch $R0

FunctionEnd
Function FinishInstall

 ; nsExec::Exec   '"$SYSDIR\services.msc" /s'

FunctionEnd
/*
Function un.onSelChange
    ;if Device Manager is not selected do not display
    SectionGetFlags "${un.SECALL}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} check
    goto continue

    check:
    SectionGetFlags "${un.SECDM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} stop

    SectionGetFlags "${un.SECCM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} stop

    SectionGetFlags "${un.SECEPM}" $0
    IntOp $0 $0 & ${SF_SELECTED}
    IntCmp $0 ${SF_SELECTED} stop
    goto continue

 stop:
    ;noting selected
     Messagebox MB_OK|MB_IcONEXCLAMATION "Cannot Select All and others"
    Abort

 continue:
FunctionEnd
*/