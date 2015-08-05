
# Arg1: File containing the locations of wheelnavcore-outputs 
# Arg2: Result file path


cd /var/www/wheelnav/distance_determination/core/mpss-prod/bin

java GetWheelNavCoreResults /var/www/wheelnav/distance_determination/core/mpss-prod/wheelnavcore-test/outputs.map /var/www/wheelnav/distance_determination/core/mpss-prod/wheelnavcore-test-results.xls
