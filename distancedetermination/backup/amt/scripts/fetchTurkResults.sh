

# $1 = mysql user
# $2 = mysql pwd
# $3 = mysql db
# $4 = getResults Input file
# $5 = getResults Output file 

echo ''
echo 'fetchTurkResults.sh - Begin'

cd /var/www/wheelnav/distance_determination/amt/bin
java GenerateReviewableHITSInputFile $1 $2 $3 $4

cd /usr/local/aws-mturk-clt-1.3.1/bin
export JAVA_HOME=/usr
export MTURK_CMD_HOME=/usr/local/aws-mturk-clt-1.3.1

sh getResults.sh -successfile $4 -outputfile $5

cd /var/www/wheelnav/distance_determination/amt/bin
java SaveHITResultsToDB $1 $2 $3 $5

echo ''
echo 'fetchTurkReulst.sh - Done'
