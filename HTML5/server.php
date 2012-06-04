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
		$output = "DRAW";
	}else{
		$output = "FRND";
	}
}
$_SESSION['connected'] = true;




$x = $images[count($images)-1];
if(!empty($x)){
	$x = explode("_", $x);
	
	if($x[1] != session_id()){
		$output = "DRAW";
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



echo $output;
















?>