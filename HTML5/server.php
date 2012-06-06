<?php


// A function to list the contents of a directory, ignoring the . and .. entry.
function getDirectoryList(){
	// Opening the directory.
	$handle = opendir('./game/');
	// Loop through each directory entry.
	while(false !== ($entry = readdir($handle))) {
		if($entry != "." && $entry != ".."){
			// Put the entry into our dir array.
			$dir[] = $entry;
		}
	}
	// Close the directory.
	closedir($handle);
	// Return the list of directory entries.
	return $dir;
}




























// Start a session.
session_start();

// Get the listing from our directory.
$dir = getDirectoryList();


//$parts = array("head", "body", "left arm", "right arm", "left leg", "right leg");
$parts = array("head", "body", "legs");

if(empty($dir)){
	file_put_contents("./game/".session_id().".client", "");
	$dir = getDirectoryList();
}

foreach($dir as $v){
	$v = explode(".", $v);
	
	if($v[1] == "client"){
		$clients[] = $v[0];
	}
	
	if($v[1] == "png"){
		$images[] = $v[0];
	}
}

if(count($images) > 0){
	sort($images, SORT_NUMERIC);
}


if(!in_array(session_id(), $clients)){
	file_put_contents("./game/".session_id().".client", "");
}






if(count($clients) == 2){
	if($clients[0] == session_id()){
		$output = $parts[0];
	}else{
		$output = "FRND";
	}
}




$x = $images[count($images)-1];
if(!empty($x)){
	$x = explode("_", $x);
	
	if($x[1] != session_id()){
		$output = $parts[count($images)];//"DRAW";
	}else{
		$output = "";
	}
}




if(!empty($_POST['i'])){
	$i = explode(",", $_POST['i']);
	$i = str_replace(" ", "+", $i[1]);
	
	$file_name = count($images)+1 . "_" . session_id() . ".png";
	$output = "";
	
	file_put_contents("./game/".$file_name, base64_decode($i));
}

if(count($images) == count($parts)){
	$output = "FINN\r\n";
	foreach($images as $v){
		$output .= $v . "\r\n";
	}
}

/*$output = "FINN
1_e6thl2v45151kho4bjn6m6rih1
2_i6mp72kaotol34tl2eia29ckm6
3_e6thl2v45151kho4bjn6m6rih1
4_i6mp72kaotol34tl2eia29ckm6
5_e6thl2v45151kho4bjn6m6rih1
6_i6mp72kaotol34tl2eia29ckm6";
*/
echo $output;
















?>