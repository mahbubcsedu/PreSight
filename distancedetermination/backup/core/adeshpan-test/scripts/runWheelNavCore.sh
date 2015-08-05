

# "Arg0 : MYSQL USER"
# "Arg1 : MYSQL PWD"
# "Arg2 : MYSQL Database"
# "Arg3 : Camera Parameters Mapping File"
# "Arg4 : Input Images Directory"
# "Arg5 : Working Directory for WheelNavCore"
# "Arg6 : Output Direcotry For Scripts"


cd /home/adeshpan/Desktop/core/adeshpan-test/bin

java CreateWheelNavCoreInput amold1 amold1123~ wheelnav_test /home/adeshpan/Desktop/core/adeshpan-test/mappings /home/adeshpan/Desktop/mpss-image-capture/test/Image_Capture /home/adeshpan/Desktop/core/adeshpan-test/workingDirectory /home/adeshpan/Desktop/core/adeshpan-test/wheelnavcore

cd /home/adeshpan/Desktop/core/adeshpan-test/wheelnavcore
sh makeWorkingDirectory.sh
sh executewheelnavcore.sh
