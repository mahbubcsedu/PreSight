
##### Script for making script_commands ready for the correct version

# $1 = version number

to=$1

vertical_from=vertical__
vertical_to=vertical_$to

horizontal_from=horizontal__
horizontal_to=horizontal_$to

result_from=result__.txt
result_to=result$to.txt

#echo $vertical_from
#echo $vertical_to
#echo $horizontal_from
#echo $horizontal_to
#echo $result_from
#echo $result_to

rm script_commands

cat raw_commands | sed -e "s/$vertical_from/$vertical_to/g" > script_commands

sed -i "s/$horizontal_from/$horizontal_to/g" script_commands

sed -i "s/$result_from/$result_to/g" script_commands

