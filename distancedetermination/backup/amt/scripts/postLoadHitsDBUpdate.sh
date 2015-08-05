
# $1 = mysql user
# $2 = mysql pwd
# $3 = mysql db
# $4 = requestIDs
# $5 = isPostedToTurk value
# $6 = loadHitsOutput - vertical
# $7 = loadHitsOutput - horizontal 


echo ''
echo 'UpdateIsPostedToTurkField - Begin'

cd /var/www/wheelnav/distance_determination/amt/bin
java UpdateIsPostedToTurkField $1 $2 $3 $4 $5

#vetical images hits
echo ''
echo 'Vertical Images Hits....'
cd /var/www/wheelnav/distance_determination/amt/bin
java PopulateHITSToDB $1 $2 $3 $6

#horizontal images hits
echo ''
echo 'Horizontal Images Hits....'
cd /var/www/wheelnav/distance_determination/amt/bin
java PopulateHITSToDB $1 $2 $3 $7

echo ''
echo 'UpdateIsPostedToTurkField - Done'
