<?php

$database_name = 'facebook_database';
$host = 'localhost';
$superuser_database = 'root';
$password = 'pratik';

//sql safe string
// PLEASE REFER TO
// http://php.net/manual/en/mysqli.real-escape-string.php
// for string like => "asd'sg"   // apostrophy , etc....
function sqlsafestring($string_value)
{
	global $database_name;
	global $host;
	global $superuser_database;
	global $password;
	// Connection created
	$connection = mysqli_connect ( $host,$superuser_database, $password, $database_name );
	// Check connection
	if (mysqli_connect_errno ()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error () . "<br/>";
	}

	$safe_string_value= mysqli_real_escape_string($connection, $string_value);

	// sql close
	mysqli_close ( $connection );

	return $safe_string_value ;
}

//redundant function from database create 
function query_to_mysql($query_string) {
	global $database_name;
	global $host;
	global $superuser_database;
	global $password;

	$query = $query_string;

	// Connection created
	$connection = mysqli_connect ( $host,$superuser_database, $password, $database_name );
	// Check connection
	if (mysqli_connect_errno ()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error () . "<br/>";
	}

	// Execute query
	if (mysqli_query ( $connection, $query )) {
		echo "successfully" . "<br/>";
	} else {
		echo "Error" . mysqli_error ( $connection ) . "<br/>";
	}

	// sql close
	mysqli_close ( $connection );
}


function add_user($facebook_user)
{

	$id = $facebook_user['id'];
	$name = $facebook_user['name'];
	$birthday = $facebook_user['birthday'];
	
	$datetoputinmysql = date('Y-m-d', strtotime((string)$birthday)); // "Year-month-date" mysql compatible
	
	$qury_to_add_user = "Insert into User values  ('" . $id ."','". $name ."','". $datetoputinmysql."') " ;
	//echo  $qury_to_add_user ; 
	query_to_mysql($qury_to_add_user);
}

function add_place($place_id,$placename)
{
	$query_to_add_place = "Insert into Place values  ('" . $place_id ."','". $placename ."') " ;

	query_to_mysql($query_to_add_place);
}

function add_status_tags($status_id, $uid)
{
	// add only one tag at a time
	$query_to_add_tags = "Insert into StatusTag values  ('" . $status_id ."','". $uid ."') " ;
	query_to_mysql($query_to_add_tags);
}

function add_status_like($status_id, $uid)
{
	// add only one like at a time
	$query_to_add_like = "Insert into StatusLike values  ('" . $status_id ."','". $uid ."') " ;
	query_to_mysql($query_to_add_like);
}

function add_status($userid, $idofstatus, $statusmessage , $placeid , $time)
{
	$query_to_add_status = "Insert into Status values  ('" . $userid ."','". $idofstatus ."','". $statusmessage ."','". $placeid ."','". $time ."'  ) " ;
	query_to_mysql($query_to_add_status);
}

function add_comment_of_status($comment_id, $usr_id , $comment_string, $like_count,$created_time,$status_id)
{
	// add only one comment of status at a time
	$query_to_add_comment_of_status = "Insert into StatusComment values  ('" . $status_id ."','". $comment_id ."','". $usr_id ."','". $comment_string ."','". $like_count ."','". $created_time ."' ) " ;
	query_to_mysql($query_to_add_comment_of_status);
}

function extract_status_related_things($facebook_user)
{  // Extraction function 
    // extracts status whole information 
    
// DATA MUST BE ADDED IN SEQUENCE AS GIVEN FOLLWING
//1.PLACE
		//TIME CHANGING TO MYSQL FORMAT 
//2.STATUS
//3.STATUS TAGS
//4.STATUS LIKE TAGS
//5.STATUS COMMENT'S
	$facebook_user_id = sqlsafestring($facebook_user['id']) ; 
	$status_array_only = $facebook_user['statuses']['data'] ;
	
	foreach( $status_array_only as $one_status_info) {
		$id_of_status = sqlsafestring($one_status_info['id']) ; 
		$message_of_status = sqlsafestring($one_status_info['message']) ;
		//for one status => place related to that status and name of the place    
		$place_id_of_status = sqlsafestring($one_status_info['place']['id']);
		$place_location_of_status  = sqlsafestring($one_status_info['place']['name']) ; 
		// add first place location to database
		add_place($place_id_of_status,$place_location_of_status);
		
		$updated_time = 	$one_status_info['updated_time'] ;
		//for tags 
		$tags_of_one_status = $one_status_info ['tags']['data'] ;
	
		
		// TIME STAMP CONVERSION TO MYSQL DEFAULT TIMESTAMP
		// Convert the Facebook IETF RFC 3339 datetime to timestamp format
		
		$date_source = strtotime($updated_time);
		$timestamp_for_mysql = date('Y-m-d H:i:s', $date_source);
		// adding stuff to status table
		add_status($facebook_user_id , $id_of_status,$message_of_status,$place_id_of_status,$timestamp_for_mysql) ; 
			
		foreach ($tags_of_one_status as $one_tag_data)
		{
			// add tags uid = $one_tag_data['id']    
			//print_r($one_tag_data) ; 
			add_status_tags( $id_of_status , sqlsafestring($one_tag_data['id'])) ; 		
		}
			
		// for likes
		$likes_of_one_status = $one_status_info ['likes']['data'] ;
		
		foreach ($likes_of_one_status as $one_like_data)
		{
			//print_r($one_like_data) ;
			add_status_like($id_of_status,sqlsafestring($one_like_data['id'])) ;
		}
		
		//for comment
		$comment_data = $one_status_info['comments']['data'] ; 
		foreach ($comment_data as $one_comment_at_a_time){
			$comment_id = sqlsafestring($one_comment_at_a_time['id']) ; 
			$user_id_of_comment = sqlsafestring($one_comment_at_a_time['from']['id']) ; 
			$message_of_comment = sqlsafestring($one_comment_at_a_time['message']) ;
			$like_count =  $one_comment_at_a_time['like_count'] ; 
			$created_time_comment = $one_comment_at_a_time['created_time'] ; 
			add_comment_of_status($comment_id, $user_id_of_comment , $message_of_comment, $like_count,$created_time_comment,$id_of_status) ; 
		}
		
	}
	

}


function add_photo_tags($photo_id, $uid)
{
	// add only one tag at a time
	$query_to_add_tags = "Insert into PhotoTag values  ('" . $photo_id ."','". $uid ."') " ;
	query_to_mysql($query_to_add_tags);
}

function add_photo_like($photo_id, $uid)
{
	// add only one like at a time
	$query_to_add_like = "Insert into PhotoLike values  ('" . $photo_id ."','". $uid ."') " ;
	query_to_mysql($query_to_add_like);
}

function add_photo($userid, $idofphoto, $photomessage , $placeid , $time)
{
	$query_to_add_photo = "Insert into Photo values  ('" . $userid ."','". $idofphoto ."','". $photomessage ."','". $placeid ."','". $time ."'  ) " ;
	query_to_mysql($query_to_add_photo);
}

function add_comment_of_photo($comment_id, $usr_id , $comment_string, $like_count,$created_time,$photo_id)
{
	// add only one comment of status at a time
	$query_to_add_comment_of_photo = "Insert into PhotoComment values  ('" . $photo_id ."','". $comment_id ."','". $usr_id ."','". $comment_string ."','". $like_count ."','". $created_time ."' ) " ;
	query_to_mysql($query_to_add_comment_of_photo);
}

function extract_photo_related_things($facebook_user)
{
	// Extraction function
	// extracts photo whole information
	
	// DATA MUST BE ADDED IN SEQUENCE AS GIVEN FOLLWING
	//1.PLACE
	//TIME CHANGING TO MYSQL FORMAT
	//2.PHOTO
	//3.PHOTO TAGS
	//4.PHOTO LIKE TAGS
	//5.PHOTO COMMENT'S
	$facebook_user_id = sqlsafestring($facebook_user['id']) ;
	$photo_array_only = $facebook_user['photos']['data']  ;
	
	foreach( $photo_array_only as $one_photo_info) {    
		$id_of_photo = sqlsafestring($one_photo_info['id']) ;
		$message_of_photo = sqlsafestring($one_photo_info['name']) ;
		//for one status => place related to that status and name of the place
		$place_id_of_photo = sqlsafestring($one_photo_info['place']['id']);
		$place_location_of_photo  = sqlsafestring($one_photo_info['place']['name']) ;
		// add first place location to database
		add_place($place_id_of_photo,$place_location_of_photo);
	
		$updated_time = 	$one_photo_info['updated_time'] ;
		//for tags
		$tags_of_one_photo = $one_photo_info ['tags']['data'] ;
	
	
		// TIME STAMP CONVERSION TO MYSQL DEFAULT TIMESTAMP
		// Convert the Facebook IETF RFC 3339 datetime to timestamp format
	
		$date_source = strtotime($updated_time);
		$timestamp_for_mysql = date('Y-m-d H:i:s', $date_source);
		// adding stuff to status table
		add_photo($facebook_user_id , $id_of_photo,$message_of_photo,$place_id_of_photo,$timestamp_for_mysql) ;
			
		foreach ($tags_of_one_photo as $one_tag_data)
		{
			// add tags uid = $one_tag_data['id']
			//print_r($one_tag_data) ;
			add_photo_tags( $id_of_photo , sqlsafestring($one_tag_data['id'])) ;
		}
			
		// for likes
		$likes_of_one_photo = $one_photo_info ['likes']['data'] ;
	
		foreach ($likes_of_one_photo as $one_like_data)
		{
			//print_r($one_like_data) ;
			add_photo_like($id_of_photo,$one_like_data['id']) ;
		}
	
		//for comment
		$comment_data = $one_photo_info['comments']['data'] ;
		foreach ($comment_data as $one_comment_at_a_time){
			$comment_id = sqlsafestring($one_comment_at_a_time['id'] );
			$user_id_of_comment = sqlsafestring($one_comment_at_a_time['from']['id']) ;
			$message_of_comment =sqlsafestring( $one_comment_at_a_time['message']) ;
			$like_count =  $one_comment_at_a_time['like_count'] ;
			$created_time_comment = $one_comment_at_a_time['created_time'] ;
			add_comment_of_photo($comment_id, $user_id_of_comment , $message_of_comment, $like_count,$created_time_comment,$id_of_photo) ;
		}
	
	}
	
	
}

?>