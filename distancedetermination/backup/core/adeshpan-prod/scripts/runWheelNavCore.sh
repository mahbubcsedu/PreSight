

# "Arg0 : MYSQL USER"
# "Arg1 : MYSQL PWD"
# "Arg2 : MYSQL Database"
# "Arg3 : Camera Parameters Mapping File"
# "Arg4 : Input Images Directory"
# "Arg5 : Working Directory for WheelNavCore"
# "Arg6 : Output Direcotry For Scripts"


cd /home/adeshpan/Desktop/core/adeshpan-prod/bin

java CreateWheelNavCoreInput amold1 amold1123~ accessibility /home/adeshpan/Desktop/core/adeshpan-prod/mappings /home/adeshpan/Desktop/mpss-image-capture/prod/Image_Capture /home/adeshpan/Desktop/core/adeshpan-prod/workingDirectory /home/adeshpan/Desktop/core/adeshpan-prod/wheelnavcore

cd /home/adeshpan/Desktop/core/adeshpan-prod/wheelnavcore
sh makeWorkingDirectory.sh
sh executewheelnavcore.sh
