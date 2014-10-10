# Настройки сервера

# Ежедневные процедуры
0 1 * * * /bin/bash -x /opt/tomcat/temp/restart_tomcat.sh restart &> /opt/tomcat/temp/restart_tomcat.log