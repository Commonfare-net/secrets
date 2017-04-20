; NSI scripts are compiled by NSIS for Windows
Name "Simple Secret Sharing"

SilentInstall silent

OutFile "Secrets.exe"

RequestExecutionLevel user

Section ""
  DetailPrint "Starting Secrets on http://localhost:8080"
  Exec "javaw.exe -Xmx20M -jar target/uberjar/fxc-0.1.0-SNAPSHOT-standalone.jar"
  Sleep 3000
  ExecShell "open" "http://localhost:8080"
SectionEnd
