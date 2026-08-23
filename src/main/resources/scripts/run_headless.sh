#export RTI_HOME=""
export RTI_LIB=$RTI_HOME/lib
export RTI_RID_FILE=$RTI_HOME/RTI.rid

if [[ -z "${RTI_HOME}" ]]; then
  echo "RTI_HOME must be set.  example: export RTI_HOME=/home/myuser/portico-2.1.4"
  exit
fi

java  -Ddevstudio.generatedcode.settings=FederateConfig.txt -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true -cp "libs/*" com.ivir.devpackage.DevPackageApplication --displayUi=false
