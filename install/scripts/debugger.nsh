
!define MUI_CUSTOMFUNCTION_GUIINIT myGUIInit

Function myGUIInit

  InitPluginsDir
  ; Remember to include the "/NOUNLOAD" option.
  ; This is to keep the debugger running.
  nsisdbg::init /NOUNLOAD
  ; To start the debugger hidden pass "hidded"
  ; to the stack like this:
  ;   nsisdbg::init /NOUNLOAD "hidden"
  ; You will be able to show it again by using the
  ; system menu of the installer. Read about this below.

  ; old fashion way (pre-2.0)
  ;   GetTempFileName $9 ; $9 will globally be used in the script
  ;   File /oname=$9 ${NSISDIR}\Plugins\nsisdbg.dll
  ;   CallInstDLL $9 /NOUNLOAD init

  ; optionally set a few option by calling setoption
  nsisdbg::setoption /NOUNLOAD "notifymsgs" "1"
  ; read more about the setoption function below
FunctionEnd