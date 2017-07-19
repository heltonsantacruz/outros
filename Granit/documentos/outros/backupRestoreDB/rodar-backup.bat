set datestr=%date:~6,4%%date:~0,2%%date:~3,2%
set timestr=%time:~0,2%%time:~3,2%
set data=%datestr%-%timestr%
mkdir c:\backupgranit
"c:\Program Files\MySQL\MySQL Server 5.0\bin\mysqldump.exe" -ujpa -pjpa jpa > c:\backupgranit\backup%data%.sql