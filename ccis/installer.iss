; InnoSetup Script for Maven Project
; This script will create an installer for your Maven Java application
; -------------------------------------------

#define MyAppName "ccisApp"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "ccis"
#define MyAppURL "https://www.yourcompany.com"
#define MyAppExeName "launcher.bat"
#define MyAppIcon "C:\SFE\SFE-DUT\ccis\src\main\resources\images\CCIS-logo-Copy-_2_ (1).ico"
#define MyAppId "{{YOUR-GUID-HERE}}"

[Setup]
; Basic setup information
AppId={#MyAppId}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
LicenseFile=license.txt
; Uncomment this line to include an EULA file
; InfoBeforeFile=readme.txt
OutputDir=installer
OutputBaseFilename={#MyAppName}-{#MyAppVersion}-setup
SetupIconFile={#MyAppIcon}
Compression=lzma
SolidCompression=yes
UninstallDisplayIcon={app}\{#MyAppIcon}
UninstallDisplayName={#MyAppName}
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"
Name: "startmenuicon"; Description: "Create a Start Menu icon"; GroupDescription: "{cm:AdditionalIcons}"

[Files]
; Include all files from your Maven target directory
Source: "target\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#MyAppIcon}"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\SFE\SFE-DUT\ccis\javafx-sdk-24.0.1\*"; DestDir: "{app}\javafx-sdk-24.0.1"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "src\main\resources\db.sqlite"; DestDir: "{app}"; Flags: ignoreversion
; Include JRE if needed (optional)
; Source: "jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs

; Include additional files
Source: "license.txt"; DestDir: "{app}"; Flags: ignoreversion
; Source: "readme.txt"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
; Create shortcuts
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppIcon}"
Name: "{group}\Uninstall {#MyAppName}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppIcon}"; Tasks: desktopicon
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppIcon}"; Tasks: startmenuicon

[Run]
; Run the application after installation (optional)
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
; Clean up any additional files created by your application during runtime
Type: filesandordirs; Name: "{app}\logs"
Type: filesandordirs; Name: "{app}\temp"
Type: filesandordirs; Name: "{localappdata}\{#MyAppName}"

[Code]
// This function runs when the installer starts
function InitializeSetup(): Boolean;
begin
  // Check for prerequisites if needed
  // For example, check if Java is installed
  // if not CheckJavaInstalled() then
  //   MsgBox('Java is required but not detected. Please install Java first.', mbError, MB_OK);
  //   Result := False;
  // else
    Result := True;
end;

// Optional custom uninstall logic
procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
begin
  if CurUninstallStep = usPostUninstall then
  begin
    // Delete any remaining files/folders after uninstallation
    DelTree(ExpandConstant('{localappdata}\{#MyAppName}'), True, True, True);
  end;
end;