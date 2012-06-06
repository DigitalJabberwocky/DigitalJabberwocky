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

$dir = getDirectoryList();
foreach($dir as $v){
	$v = explode(".", $v);
	if($v[1] == "png"){
		$images[] = $v[0];
	}
}
if(count($images) > 0){
	sort($images, SORT_NUMERIC);
}












// Create image instances
$src = imagecreatefrompng('./game/'.$images[count($images)-1].'.png');
$dest = imagecreatetruecolor(800, 10);

// Copy
imagecopy($dest, $src, 0, 0, 0, 590, 800, 50);

// Output and free from memory
header('Content-Type: image/png');
imagepng($dest);

imagedestroy($dest);
imagedestroy($src);





?>