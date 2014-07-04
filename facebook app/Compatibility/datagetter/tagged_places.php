<?php
function get_tagged_places_data($facebook_user)
{	
	$array_of_tagged_places = array() ;
	$array_of_time_created= array() ; 
	
	foreach( $facebook_user['tagged_places']['data'] as $place) {
		array_push($array_of_tagged_places , $place['place']['name']) ; 
		array_push($array_of_time_created, $place['created_time']) ; 
	}
	$array_of_place_and_time = array() ; 
	array_push($array_of_place_and_time,$array_of_tagged_places ,$array_of_time_created) ; 
	
	return $array_of_place_and_time ; 
}
?>