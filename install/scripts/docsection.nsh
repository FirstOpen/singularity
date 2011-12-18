Section  "Documents" SECDOC
  StrCpy $DOCINSTDIR "$INSTDIR\doc"
  SetOutPath "$DOCINSTDIR"

  File /r "..\..\document\*.pdf"

  WriteUninstaller "$DOCINSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DOCUninstallString" "$DOCINSTDIR"

SectionEnd


Section  "un.Documents" un.SECDOC
  ReadRegStr $DOCINSTDIR "${PRODUCT_UNINST_ROOT_KEY}" "${PRODUCT_UNINST_KEY}" "DOCUninstallString"
 

  RMDir /r "$DOCINSTDIR"


  DeleteRegValue ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DOCUninstallString"
SectionEnd

