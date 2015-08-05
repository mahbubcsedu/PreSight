<?php

//header('Content-type: text/json');

$request = $_GET['requestID'];
$sidewalkTileLength = $_GET['stlength'];
$sidewalkTileWidth = $_GET['stwidth'];
$stPoint1_x = $_GET['stp1x'];
$stPoint1_y = $_GET['stp1y'];
$stPoint2_x = $_GET['stp2x'];
$stPoint2_y = $_GET['stp2y'];
$stPoint3_x = $_GET['stp3x'];
$stPoint3_y = $_GET['stp3y'];
$stPoint4_x = $_GET['stp4x'];
$stPoint4_y = $_GET['stp4y'];
$canvas_height=$_GET['cvheight'];
$canvas_width=$_GET['cvwidth'];
$image_height=$_GET['iheight'];
$image_width=$_GET['iwidth'];
$device_type=$_GET['dtype'];
$pitch=$_GET['pitch'];
$roll=$_GET['roll'];
$bbleft=$_GET['bbleft'];
$bbright=$_GET['bbright'];
$bbtop=$_GET['bbtop'];
$bbbottom=$_GET['bbbottom'];
$answer_unit=$_GET['aunit'];

$mappingsFile='/var/www/wheelnav/distance_determination/coreForTurkers/mappings';
$image_dir='/var/www/wheelnav/appserver/beview_sa';
$working_dir='/var/www/wheelnav/distance_determination/coreForTurkers/workingDirectory';
$output_scripts_dir='/var/www/wheelnav/distance_determination/coreForTurkers/wheelnavcore';

$uniq_dir_name = uniqid('dir_');
$workingDirectory = $working_dir . '/' . $uniq_dir_name;
exec('mkdir -p '. $workingDirectory);

//echo '<br/><br/><br/>';
//echo $uniq_dir_name;

$output_image = 'https://covail.cs.umbc.edu/wheelnav/distance_determination/coreForTurkers/workingDirectory/' . $uniq_dir_name . '/' . $request . '_tdv.jpg';

//echo '<br/><br/><br/>';
//echo $output_image;

$inputs;
$inputs = 'java ' . 'GenerateScriptForTurkersPHP ' . $mappingsFile . ' ';
$inputs = $inputs . $image_dir . ' ' . $workingDirectory . ' ' . $request . ' ' . $sidewalkTileLength . ' ';
$inputs = $inputs . $sidewalkTileWidth . ' ' . $canvas_height . ' ' . $canvas_width . ' ' . $image_height . ' ' . $image_width . ' ';
$inputs = $inputs . $stPoint1_x . ' ' . $stPoint1_y . ' ' . $stPoint2_x . ' ' . $stPoint2_y . ' ' . $stPoint3_x . ' ' . $stPoint3_y . ' ';
$inputs = $inputs . $stPoint4_x . ' ' . $stPoint4_y . ' ' . $device_type . ' ' . $pitch . ' ' . $roll . ' ';
$inputs = $inputs . $bbleft . ' ' . $bbright . ' ' . $bbtop . ' ' . $bbbottom . ' ' . $answer_unit . ' 2>&1';

//echo '<br/><br/><br/>';
echo $inputs;

exec($inputs, $output, $return);
$command = implode("\n", $output);

//echo '<br/><br/><br/>';
//echo $command;

exec($command, $output, $return);
$arr = implode("\n", $output);

//echo '<br/><br/><br/>';
//echo $output_image;
//$arr = array('url' => $output_image);
//echo  $_GET['callback'].'('.json_encode($arr) .')';

//header("Location: " . $output_image);

?>
