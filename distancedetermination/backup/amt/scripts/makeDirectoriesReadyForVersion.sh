
##### Script for making directories ready for the next version of tests

# $1 = version number

version=$1

base_path=/var/www/wheelnav/distance_determination/amt/amt_working_directory/loadHits

src_horizontal_dir=$base_path/horizontal_1
dst_horizontal_dir=$base_path/horizontal_$version

src_vertical_dir=$base_path/vertical_1
dst_vertical_dir=$base_path/vertical_$version


mkdir -p "$dst_horizontal_dir"
mkdir -p "$dst_vertical_dir"

sudo cp "$src_horizontal_dir/horizontal_questionnaire.question"  "$dst_horizontal_dir"
sudo cp "$src_vertical_dir/vertical_questionnaire.question"  "$dst_vertical_dir"



