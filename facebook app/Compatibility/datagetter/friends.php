<?php
function get_facebook_friends($facebook_user) {
	$array_of_friends_name = array ();
	$array_of_friends_id = array ();

	foreach ( $facebook_user ['friends'] ['data'] as $post ) {
		array_push ( $array_of_friends_name, $post ['name'] );
		array_push ($array_of_friends_id ,$post['id']);
	}
	
	$array_of_friends = array() ; 
	
	array_push ($array_of_friends ,$array_of_friends_name , $array_of_friends_id);
	
	return $array_of_friends;
}

?>