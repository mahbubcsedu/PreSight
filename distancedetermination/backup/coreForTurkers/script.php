<html>
<head>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>

<script type="text/javascript">

	function gup(name) {
		var regexS = "[\\?&]" + name + "=([^&#]*)";
		var regex = new RegExp(regexS);
		var tmpURL = window.location.href;
		var results = regex.exec(tmpURL);
		if (results == null)
			return "";
		else
			return results[1];
	}

	//
	// This method decodes the query parameters that were URL-encoded
	//
	function decode(strToDecode) {
		var encoded = strToDecode;
		return unescape(encoded.replace(/\+/g, " "));
	}
	function storeresult(){
		//alert("called storeresult");
		var requestid = decode(gup('requestID'));
		var assignmentId = decode(gup('assignmentId'));
		var workerId = decode(gup('workerId'));


		var pt1_x = decode(gup('stp1x'));
		var pt1_y = decode(gup('stp1y'));
		var pt2_x = decode(gup('stp2x'));
		var pt2_y = decode(gup('stp2y'));
		var pt3_x = decode(gup('stp3x'));
		var pt3_y = decode(gup('stp3y'));
		var pt4_x = decode(gup('stp4x'));
		var pt4_y = decode(gup('stp4y'));
		var unit = decode(gup('aunit'));
		var submit_url = 'https://covail.cs.umbc.edu/wheelnav/appserver/applink/beviewsubmit.php?';
		
		submit_url += 'requestID='+requestid;
		submit_url += '&assignmentId='+assignmentId;
		submit_url += '&rworkerId='+workerId;
		submit_url += '&stp1x=' + pt1_x;
		submit_url += '&stp1y=' + pt1_y;
		submit_url += '&stp2x=' + pt2_x;
		submit_url += '&stp2y=' + pt2_y;
		submit_url += '&stp3x=' + pt3_x;
		submit_url += '&stp3y=' + pt3_y;
		submit_url += '&stp4x=' + pt4_x;
		submit_url += '&stp4y=' + pt4_y;
		
		var im_src=document.getElementById('bev_image').src;
		submit_url += '&imsrc=' + im_src;
		alert(im_src);
		$('#submitButton').attr("disabled","disabled");
		$('#submitButton').val('Thank you for submitting.');
		//alert("ready to insert into database");
		$.get(submit_url);//location.href=submit_url;
		return false;
	}

</script>
</head>

<body>


<?php
$request = $_GET['requestID'];
$assignmentId = $_GET['assignmentId'];
$workerId = $_GET['workerId'];

$sidewalkTileLength = $_GET['stlength'];
$sidewalkTileWidth = $_GET['stwidth'];
$stPoint1_x = intval($_GET['stp1x']);
$stPoint1_y = intval($_GET['stp1y']);
$stPoint2_x = intval($_GET['stp2x']);
$stPoint2_y = intval($_GET['stp2y']);
$stPoint3_x = intval($_GET['stp3x']);
$stPoint3_y = intval($_GET['stp3y']);
$stPoint4_x = intval($_GET['stp4x']);
$stPoint4_y = intval($_GET['stp4y']);
$canvas_height=intval($_GET['cvheight']);
$canvas_width=intval($_GET['cvwidth']);
$image_height=intval($_GET['iheight']);
$image_width=intval($_GET['iwidth']);
$device_type=$_GET['dtype'];
$pitch=$_GET['pitch'];
$roll=$_GET['roll'];
$bbleft=$_GET['bbleft'];
$bbright=$_GET['bbright'];
$bbtop=$_GET['bbtop'];
$bbbottom=$_GET['bbbottom'];
$answer_unit=$_GET['aunit'];

$mappingsFile='/var/www/wheelnav/distance_determination/coreForTurkers/mappings';
$image_dir='/var/www/wheelnav/appserver/applink/beview';
$working_dir='/var/www/wheelnav/distance_determination/coreForTurkers/workingDirectory';
$output_scripts_dir='/var/www/wheelnav/distance_determination/coreForTurkers/wheelnavcore';

$uniq_dir_name = uniqid('dir_');
$workingDirectory = $working_dir . '/' . $uniq_dir_name;
exec('mkdir -p '. $workingDirectory);



$output_image = 'https://covail.cs.umbc.edu/wheelnav/distance_determination/coreForTurkers/workingDirectory/' . $uniq_dir_name . '/photo' . $request . '_tdv.jpg';

$inputs;
$inputs = 'java ' . 'GenerateScriptForTurkersPHP ' . $mappingsFile . ' ';
$inputs = $inputs . $image_dir . ' ' . $workingDirectory . ' ' . $request . ' ' . $sidewalkTileLength . ' ';
$inputs = $inputs . $sidewalkTileWidth . ' ' . $canvas_height . ' ' . $canvas_width . ' ' . $image_height . ' ' . $image_width . ' ';
$inputs = $inputs . $stPoint1_x . ' ' . $stPoint1_y . ' ' . $stPoint2_x . ' ' . $stPoint2_y . ' ' . $stPoint3_x . ' ' . $stPoint3_y . ' ';
$inputs = $inputs . $stPoint4_x . ' ' . $stPoint4_y . ' ' . $device_type . ' ' . $pitch . ' ' . $roll . ' ';
$inputs = $inputs . $bbleft . ' ' . $bbright . ' ' . $bbtop . ' ' . $bbbottom . ' ' . $answer_unit . ' 2>&1';

exec($inputs, $output, $return);
$command = implode("\n", $output);
exec($command, $output, $return);

$arr = implode("\n", $output);
//<form id="mturk_form" method="POST" action="https://www.mturk.com/mturk/externalSubmit">
$txt = '<img id="bev_image" style="max-width:100%;height:100%;" src="';
echo $txt;
echo $output_image;
$txt='"/>';
//$b64image = base64_encode(file_get_contents($output_image));
echo $txt;
?>

<form id="mturk_form" method="POST" action="http://www.mturk.com/mturk/externalSubmit">
<input type="hidden" id="assignmentId" name="assignmentId" value="">
<table border="0" height="100%" width="100%">
<tr><td>
<p>
<input id="submitButton" type="submit" name="Submit" value="Submit" onclick="storeresult()">
<p>
</td></tr>
<tr><td height="100%">
<iframe id="pageFrame" width="01%" height="01%"></iframe>
</td></tr>
</table>
</form>
<script language="Javascript">
    document.getElementById('assignmentId').value = gup('assignmentId');
    //
    // Check if the worker is PREVIEWING the HIT or if they've ACCEPTED the HIT
    //
    if (gup('assignmentId') == "ASSIGNMENT_ID_NOT_AVAILABLE")
    {
	// If we're previewing, disable the button and give it a helpful message
	document.getElementById('submitButton').disabled = true;
	document.getElementById('submitButton').value = "You must ACCEPT the HIT before you can submit the results.";
    } else {
       var form = document.getElementById('mturk_form');
       if (document.referrer && ( document.referrer.indexOf('workersandbox') != -1) ) {
       	form.action = "http://workersandbox.mturk.com/mturk/externalSubmit";
       }
    }

</script>

	

</body>
</html>