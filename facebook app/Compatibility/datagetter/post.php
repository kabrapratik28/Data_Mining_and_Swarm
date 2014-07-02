<?php
function get_post_data($facebook_user)
{
	$array_of_posts_time = array() ;
	foreach( $facebook_user['posts']['data'] as $post) {
		array_push($array_of_posts_time , $post['created_time']) ;
	}
	return $array_of_posts_time  ; 
}
?>