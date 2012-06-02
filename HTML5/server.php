<?php


$log = file_get_contents("log.txt");
session_start();

$log = explode("\r\n", $log);

foreach($log as $v){
	
	
	$v = explode("\t", $v);
	
	
	if($v[0] == "JOIN"){
		$clients[] = $v[1];
	}
	
}






if(!in_array(session_id(), $clients)){
	file_put_contents("log.txt", "JOIN\t" . session_id() . "\r\n", FILE_APPEND);
}






//if($_SESSION['connected'] == false){
	if(count($clients) == 2){
		if($clients[0] == session_id()){
			echo "DRAW";
		}else{
			echo "FRND";
		}
	}
	$_SESSION['connected'] = true;
//}





if(!empty($_POST['i'])){
	$i = explode(",", $_POST['i']);
	$i = str_replace(" ", "+", $i[1]);
	file_put_contents("image.png", base64_decode($i));
}







/*







if($_POST['c'] == "1"){
	echo "FRND";
}


if($_POST['w'] == "1"){
	echo "1";
}
*/




























?>