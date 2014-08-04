<?php
$database_name = 'facebook_database';
$host = 'localhost';
$superuser_database = 'root';
$password = 'pratik';


function get_userinfo_from_mysql($user_id_of_user) {
	global $database_name;
	global $host;
	global $superuser_database;
	global $password;

	$query = "select * from user_info where user_id = '".$user_id_of_user."'";

	// Connection created
	$connection = mysqli_connect ( $host,$superuser_database, $password, $database_name );
	// Check connection
	if (mysqli_connect_errno ()) {
		//echo "Failed to connect to MySQL: " . mysqli_connect_error () . "<br/>";
	}

	// Execute query
	$result = mysqli_query ( $connection, $query );
	/*
	while($row = mysqli_fetch_array($result)) {
		echo $row['FirstName'] . " " . $row['LastName'];
		echo "<br>";
	}
	*/
	$row = mysqli_fetch_array($result) ; 
	
	// sql close
	mysqli_close ( $connection );
	
	return $row ; 	
}


//AJAX ... 

if (is_ajax()) {
	if (isset($_POST["action"]) && !empty($_POST["action"])) { //Checks if action value exists
		$action = $_POST["action"];
		switch($action) { //Switch case for value of action
			case "getuserinfo": get_user_info_function(); break;
		}
	}
}

//Function to check if the request is an AJAX request
function is_ajax() {
	return isset($_SERVER['HTTP_X_REQUESTED_WITH']) && strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) == 'xmlhttprequest';
}

function get_user_info_function(){
	$return = $_POST;

	//Do what you need to do with the info. The following are some examples.
	//if ($return["favorite_beverage"] == ""){
	//  $return["favorite_beverage"] = "Coke";
		//}
		//$return["favorite_restaurant"] = "McDonald's";

		$return = 	get_userinfo_from_mysql($return['user_id_value']) ; 
		$return["json"] = json_encode($return);

		//THIS ECHO RESULTS WILL FETCH BY JQUERY  ******** SO DONT REMOVE THIS ECHO
		echo json_encode($return);
}

?>