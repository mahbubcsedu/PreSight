

# "Arg0 : MYSQL USER"
# "Arg1 : MYSQL PWD"
# "Arg2 : MYSQL Database"
# "Arg3 : Camera Parameters Mapping File"
# "Arg4 : Input Images Directory"
# "Arg5 : Working Directory for WheelNavCore"
# "Arg6 : Output Direcotry For Scripts"

cd /var/www/wheelnav/distance_determination_test/core/mpss-test/bin

java CreateWheelNavCoreTestInput amold1 amold1123~ wheelnav_test /var/www/wheelnav/distance_determination_test/core/mpss-test/mappings /var/www/smsdb/wheelnav_test/Image_Capture /var/www/wheelnav/distance_determination_test/core/mpss-test/testWorkingDirectory /var/www/wheelnav/distance_determination_test/core/mpss-test/wheelnavcore-test

cd /var/www/wheelnav/distance_determination_test/core/mpss-test/wheelnavcore-test
sh makeWorkingDirectory.sh
sh executewheelnavcore-test.sh
