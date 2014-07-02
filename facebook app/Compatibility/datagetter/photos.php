<?php
function get_photos_data($facebook_user)
{
	$array_of_created_time = array() ;
	$array_of_no_of_tags = array() ;
	$array_of_no_of_likes = array() ; 
	$array_of_no_of_comments = array() ; 
	foreach( $facebook_user['photos']['data'] as $photo) {
		array_push($array_of_created_time , $photo['created_time']) ;
		array_push($array_of_no_of_likes, count($photo['likes']['data'])) ; 
		array_push($array_of_no_of_tags , count($photo['tags']['data'])) ; 
		array_push($array_of_no_of_comments ,count($photo['comments']['data'])) ;
	}
	$array_photo_data = array() ; 
	array_push( $array_photo_data , $array_of_created_time , $array_of_no_of_tags , $array_of_no_of_likes , $array_of_no_of_comments ) ; 
	
	return  $array_photo_data; 
}
?>