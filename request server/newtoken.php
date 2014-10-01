<?php

/* Les données PUT arrivent du flux */
$putdata = fopen("php://input", "r");
echo $putdata.'</br>';

echo '<pre>';
print_r($GLOBALS);
echo '<pre>';

/* Fermeture du flux */
fclose($putdata);

if(isset($putdata))
	header("HTTP/1.1 200 OK");
else
	header("HTTP/1.1 204 prob");
	


//echo $obj->a00b[0]->coordinates[0]->lat.'</br>';
//echo $obj["$key"]["coordinates"][0]["lat"];
?>