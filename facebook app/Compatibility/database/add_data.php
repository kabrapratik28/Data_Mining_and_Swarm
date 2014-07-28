<?php

$database_name = 'facebook_database';
$host = 'localhost';
$superuser_database = 'root';
$password = 'pratik';




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


function extract_status_related_things($facebook_user)
{  // Extraction function 
    // extracts status whole information 
    
// DATA MUST BE ADDED IN SEQUENCE AS GIVEN FOLLWING
//1.PLACE
		//TIME CHANGING TO MYSQL FORMAT 
//2.STATUS
//3.STATUS TAGS
//4.STATUS LIKE TAGS
	$facebook_user_id = $facebook_user['id'] ; 
	$status_array_only = $facebook_user['statuses']['data']  ;
	
	foreach( $status_array_only as $one_status_info) {
		$id_of_status = $one_status_info['id'] ; 
		$message_of_status = $one_status_info['message'] ;
		//for one status => place related to that status and name of the place    
		$place_id_of_status = $one_status_info['place']['id'];
		$place_location_of_status  = $one_status_info['place']['name'] ; 
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
			add_status_tags( $id_of_status , $one_tag_data['id']) ; 		
		}
			
		// for likes
		$likes_of_one_status = $one_status_info ['likes']['data'] ;
		
		foreach ($likes_of_one_status as $one_like_data)
		{
			add_status_like($id_of_status,$one_like_data['id']) ; 
		}
		
		//for comment
		
	}
	

}
?>