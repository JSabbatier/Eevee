<?php

if(isset($_POST['json'])){
	$obj = json_decode($json);
	$name="test";
	$resume="test";
	$picture="test";
	$distance="test";
	
	$tab = array("pois"=>array("1"=>array("name"=>$name,"coordinates"=>array("lat"=>"13.6548", "lon"=>"2.6578"),"resume"=>$resume,"picture"=>$picture,"distance"=>$distance)));

	echo json_encode($tab);
	header("HTTP/1.1 200 OK");
}
else
	header("HTTP/1.1 204 prob");
	


//echo $obj->a00b[0]->coordinates[0]->lat.'</br>';
//echo $obj["$key"]["coordinates"][0]["lat"];
?>