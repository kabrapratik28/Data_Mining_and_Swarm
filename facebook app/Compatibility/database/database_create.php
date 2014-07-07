<?php
$database_name = 'facebook_database';
$host = 'localhost';
$user = 'root';
$password = 'pratik';
function create_database() {
	global $database_name;
	global $host;
	global $user;
	global $password;
	
	$sql_database_query = "CREATE DATABASE " . $database_name;
	
	// Database Created
	$connection = mysqli_connect ( $host, $user, $password ); // <=== this is different is this function
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
	$tablequery = "CREATE TABLE Persons ( uid BIGINT UNSIGNED  ,
		     name VARCHAR(500),
			 birthday DATE,
			 email VARCHAR(100) ,
			 PRIMARY KEY ( uid )
			)";
	array_push ( $array_of_table_creation_query, $tablequery );
	
	// add rest of table creation here
	
	
	foreach ( $array_of_table_creation_query as $value ) {
		query_to_mysql ( $value );
	}
}

create_database();
create_table();
?>