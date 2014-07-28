<?php

// ******************************** PLEASE CHANGE THIS
// ********************************  remove forigen key and add table status like


$database_name = 'facebook_database';
$host = 'localhost';
$superuser_database = 'root';
$password = 'pratik';
function create_database() {
	global $database_name;
	global $host;
	global $superuser_database;
	global $password;
	
	$sql_database_query = "CREATE DATABASE " . $database_name;
	
	// Database Created
	$connection = mysqli_connect ( $host, $superuser_database, $password ); // <=== this is different is this function
	if (! $connection) {
		die ( "Could not connect: " . mysqli_error () );
	}
	
	if (mysqli_query ( $connection, $sql_database_query )) {
		echo "Database  created " . $database_name . " successfully <br/>";
	} else {
		echo "Error creating database: " . mysqli_error () . "<br/>";
	}
	
	// sql close
	mysqli_close ( $connection );
}
function query_to_mysql($query_string) {
	global $database_name;
	global $host;
	global $user;
	global $password;
	
	$query = $query_string;
	
	// Connection created
	$connection = mysqli_connect ( $host, $user, $password, $database_name );
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

function create_table() {
	$array_of_table_creation_query = array ();
	
	// Create table
	$tablequery = 
"CREATE TABLE User
(
user_id varchar(50)  ,
name varchar(200),
birthday date,
PRIMARY KEY (user_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// add rest of table creation here
	
	// Create table
	$tablequery =
	"CREATE TABLE Place
(
place_id varchar(50),
place_name varchar(200),
PRIMARY KEY (place_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"CREATE TABLE Status
(
user_id varchar(50)  ,
status_id varchar(50) , 
status varchar(2500),
place_id varchar(50),
time_stamp timestamp ,
PRIMARY KEY (status_id),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (place_id) REFERENCES Place(place_id) 
)";
	array_push ( $array_of_table_creation_query, $tablequery );	
	
	// Create table
	$tablequery =
	"CREATE TABLE StatusTag
(
status_id varchar(50) , 
user_id varchar(50) ,
FOREIGN KEY (status_id) REFERENCES Status(status_id),
FOREIGN KEY (user_id) REFERENCES User(user_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
		
	// Create table
	$tablequery =
	"Create Table StatusComment
(
status_id varchar(50),
status_comment_id varchar(50),
user_id varchar(50),
comment  varchar(200),
like_count int,
time_stamp timestamp,
PRIMARY KEY (status_comment_id),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (status_id) REFERENCES Status(status_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"CREATE Table TaggedPlace
(
tagged_place_id varchar(50),
user_id varchar(50),
place_id varchar(50),
time_stamp timestamp,
PRIMARY KEY (tagged_place_id),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (place_id) REFERENCES Place(place_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"CREATE Table Groups
(
group_id varchar(50),
group_name varchar(50),
PRIMARY KEY (group_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"Create Table GroupUser
(
user_id varchar(50),
group_id varchar(50),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (group_id) REFERENCES Groups(group_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"Create Table Photo
(
photo_id varchar(50),
user_id varchar(50),
photo_caption varchar(50),
time_stamp timestamp,
PRIMARY KEY  (photo_id),
FOREIGN KEY (user_id) REFERENCES User(user_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"Create Table PhotoTag
(
photo_id varchar(50),
user_id varchar(50),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (photo_id) REFERENCES Photo(photo_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"Create Table PhotoLike
(
photo_id varchar(50),
user_id varchar(50),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (photo_id) REFERENCES Photo(photo_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// Create table
	$tablequery =
	"Create Table PhotoComment
(
photo_id varchar(50),
comment_id varchar(50),
user_id varchar(50),
comment  varchar(200),
like_count integer,
PRIMARY KEY (comment_id),
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (photo_id) REFERENCES Photo(photo_id)
)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	
	foreach ( $array_of_table_creation_query as $value ) {
		query_to_mysql ( $value );
	}
}

create_database();
create_table();
?>