; NSI scripts are compiled by NSIS for Windows
Name "Simple Secret Sharing"

SilentInstall silent

OutFile "Secrets.exe"

RequestExecutionLevel user

LoadLanguageFile "${NSISDIR}\Contrib\Language files\English.nlf"
  VIProductVersion "1.0.0.0"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "Simple Secret Sharing"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "Comments" "Local webapp launcher"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "Dyne.org Foundation"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalTrademarks" "Written by Jaromil @ Dyne.org"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "Copyright (C) 2017 Dyne.org"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "Run secrets.dyne.org offline"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "1.0"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductVersion" "1.0"

Section ""
  DetailPrint "Starting Secrets.dyne.org"
  Exec "javaw.exe -Xmx128M -jar target/uberjar/fxc-0.1.0-SNAPSHOT-standalone.jar"
SectionEnd
