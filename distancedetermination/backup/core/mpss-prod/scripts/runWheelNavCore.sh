

# "Arg0 : MYSQL USER"
# "Arg1 : MYSQL PWD"
# "Arg2 : MYSQL Database"
# "Arg3 : Camera Parameters Mapping File"
# "Arg4 : Input Images Directory"
# "Arg5 : Working Directory for WheelNavCore"
# "Arg6 : Output Direcotry For Scripts"

cd /var/www/wheelnav/distance_determination/core/mpss-prod/bin

java CreateWheelNavCoreInput amold1 amold1123~ accessibility /var/www/wheelnav/distance_determination/core/mpss-prod/mappings /var/www/wheelnav/Image_Capture /var/www/wheelnav/distance_determination/core/mpss-prod/workingDirectory /var/www/wheelnav/distance_determination/core/mpss-prod/wheelnavcore

cd /var/www/wheelnav/distance_determination/core/mpss-prod/wheelnavcore
sh makeWorkingDirectory.sh
sh executewheelnavcore.sh
