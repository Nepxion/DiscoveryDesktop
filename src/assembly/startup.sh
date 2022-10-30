echo -e '\033[32m'
echo '============================================================='
echo '$                                                           $'
echo '$                     Nepxion Discovery                     $'
echo '$                                                           $'
echo '$                                                           $'
echo '$                                                           $'
echo '$  Nepxion Studio All Right Reserved                        $'
echo '$  Copyright (C) 2017-2050                                  $'
echo '$                                                           $'
echo '============================================================='

echo -n $'\e'"]0;Nepxion Discovery"$'\a'

JARPATH=`find lib -name *.jar|xargs|sed "s/ /:/g"`
CONFIGPATH=`find config -name *.properties|xargs|sed "s/ /:/g"`
CLASSPATH="$JARPATH:$CONFIGPATH"
PATH=

$JAVA_HOME/bin/java -Dfile.encoding=GBK -Ddefault.client.encoding=GBK -Duser.language=zh -Duser.region=CN -Djava.security.policy=java.policy -Djava.library.path=${PATH} -Xms128m -Xmx512m -classpath ${CLASSPATH} com.nepxion.discovery.console.desktop.ConsoleLauncher

function pause(){
  echo 'Press any key to continue...'
  read -n 1 -p "$*" str_inp
  if [ -z "$str_inp" ];then
    str_inp=1
  fi
    if [ $str_inp != '' ];then
      echo -ne '\b \n'
    fi
}

pause