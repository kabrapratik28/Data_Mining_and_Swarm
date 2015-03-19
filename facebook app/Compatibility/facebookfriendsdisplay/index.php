<html>
  <head> 

  </head>
  <body>

    <?php
       session_start();
     
       
       $database_name = 'facebook_database';
       $host = 'localhost';
       $superuser_database = 'root';
       $password = 'pratik';

        // Connection created
	$connection = mysqli_connect ( $host,$superuser_database, $password, $database_name );
	// Check connection
	if (mysqli_connect_errno ()) {
		//echo "Failed to connect to MySQL: " . mysqli_connect_error () . "<br/>";
	}


       echo "<h1>People related to you : </h1></br>";
       $query =   "select user_id from ClusterandUser where cluster_id in ( select cluster_id  from ClusterandUser where user_id =  '".$_SESSION['user_id'] . "' ) and user_id not in ( '".$_SESSION['user_id']."' )";


       	// Execute query
	$result = mysqli_query ( $connection, $query );
       	
       while($row = mysqli_fetch_array($result)) {
                $query2 = "select name from User where user_id = '". $row['user_id'] . "'";
                // Execute query
                $result2 = mysqli_query ( $connection, $query2 );
                $rowquery2 = mysqli_fetch_array($result2) ; 
                echo "<a href=https://facebook.com/app_scoped_user_id/". $row['user_id'] . ">". $rowquery2['name'] ."</a>" ; 
                echo "<br/><img src='https://graph.facebook.com/".  $row['user_id'] ."/picture' width='50' height='50'  /><br/>";
		echo "</br>";
	}

       echo "<h1>Can be your friend : </h1></br>";
       $query =  "select user_id from CommunityConnection where community_id in ( select community_id  from CommunityConnection where user_id =  '".$_SESSION['user_id']."'  ) and user_id not in ( select friend_id from FriendConnection where user_id = '".$_SESSION['user_id']."' ) and user_id not in ('".$_SESSION['user_id']."')" ; 


	// Execute query
	$result = mysqli_query ( $connection, $query );
       
       
       if (mysqli_num_rows($result) == 0)
       {
          echo "<h4>Hurray !!! You are already friends with all the people of your community.</h4>";
       }
       
       while($row = mysqli_fetch_array($result)) {
                $query2 = "select name from User where user_id = '". $row['user_id'] . "'";
                // Execute query
                $result2 = mysqli_query ( $connection, $query2 );
                $rowquery2 = mysqli_fetch_array($result2) ; 
                echo "<a href=https://facebook.com/app_scoped_user_id/". $row['user_id'] . ">". $rowquery2['name'] ."</a>" ; 
                echo "<br/><img src='https://graph.facebook.com/".  $row['user_id'] ."/picture' width='50' height='50'  /><br/>";
		echo "</br>";
	}



	// sql close
	mysqli_close ( $connection );


       echo "<br/><h4>You : </h4> <img src='https://graph.facebook.com/". $_SESSION['user_id']."/picture' width='50' height='50'  /><br/>";
       ?> 
    
  </body>
</html>


