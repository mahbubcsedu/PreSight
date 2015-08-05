

# $1 = mysql user
# $2 = mysql pwd
# $3 = mysql db
# $4 = loadHits vertical hit input file path
# $5 = loadHits horizontal hit input file path
# $6 = resultIDs
# $7 = vertical hit question file 
# $8 = horizontal hit question file
# $9 = hit properties file


echo ''
echo 'loadHHitsFromDB.sh - Begin'

cd /var/www/wheelnav/distance_determination/amt/bin
java GenerateTurkInputFile $1 $2 $3 $4 $5 $6

cd /usr/local/aws-mturk-clt-1.3.1/bin
export JAVA_HOME=/usr
export MTURK_CMD_HOME=/usr/local/aws-mturk-clt-1.3.1

#vertical images HIT
echo ''
echo 'Vertical Images HITS....'
sh loadHITs.sh -input $4 -question $7  -properties $9 

#horizontal images HIT
echo ''
echo 'Horizontal Images HITS....'
sh loadHITs.sh -input $5 -question $8  -properties $9

echo ''
echo 'loadHitsFromDB.sh - Done'
