<html>
  <head> 

  </head>
<link href='http://fonts.googleapis.com/css?family=Nova+Mono' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>

<style>

.colorful { background: -webkit-linear-gradient(45deg, hsl(222, 36%, 71%) 0%, transparent 70%), -webkit-linear-gradient(135deg, hsl(117, 41%, 75%) 10%, transparent 80%), -webkit-linear-gradient(225deg, hsl(288, 41%, 66%) 10%, transparent 80%), -webkit-linear-gradient(315deg, hsl(56, 41%, 74%) 50%, transparent 100%); background: -ms-linear-gradient(45deg, hsl(222, 36%, 71%) 0%, transparent 70%), -ms-linear-gradient(135deg, hsl(117, 41%, 75%) 10%, transparent 80%), -ms-linear-gradient(225deg, hsl(288, 41%, 66%) 10%, transparent 80%), -ms-linear-gradient(315deg, hsl(56, 41%, 74%) 50%, transparent 100%); background: linear-gradient(45deg, hsl(222, 36%, 71%) 0%, transparent 70%), linear-gradient(135deg, hsl(117, 41%, 75%) 10%, transparent 80%), linear-gradient(225deg, hsl(288, 41%, 66%) 10%, transparent 80%), linear-gradient(315deg, hsl(56, 41%, 74%) 50%, transparent 100%); }

a {text-decoration:none}
 .round {
	border-radius: 50%;
    overflow: hidden;
    width: 50px;
    height: 50px;
    }


.round img {
	display: block;
    min-width: 100%;
    min-height: 100%;
    }

.font-style {
	font-family: Nova Mono;
	color: #BD5738;
	font-size: 38;
	padding: 5;
}
.font-style-for-link {
	font-family: Montserrat;
	color: #1F5ACF;
	font-size: 20;
	padding: 5;
}

</style>

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

echo <<<"INITIAL"
<div class="colorful">
<div  style="width: 100%;">
<div style="float:left; width: 50%">
     
INITIAL;

       echo "<h1 class='font-style'>People related to you : </h1></br>";
       $query =   "select user_id from ClusterandUser where cluster_id in ( select cluster_id  from ClusterandUser where user_id =  '".$_SESSION['user_id'] . "' ) and user_id not in ( '".$_SESSION['user_id']."' )";


       	// Execute query
	$result = mysqli_query ( $connection, $query );
       	
       while($row = mysqli_fetch_array($result)) {
                $query2 = "select name from User where user_id = '". $row['user_id'] . "'";
                // Execute query
                $result2 = mysqli_query ( $connection, $query2 );
                $rowquery2 = mysqli_fetch_array($result2) ; 
                echo "<a class='font-style-for-link' href=https://facebook.com/app_scoped_user_id/". $row['user_id'] . "><img class=round src='https://graph.facebook.com/".  $row['user_id'] ."/picture' width='50' height='50' align='center'/>  &nbsp; &nbsp; ". $rowquery2['name'] ."</a>" ; 
		echo "</br></br></br>";
	}

echo <<<"SECIN"
   </div>
   <div  style="float:left;">
SECIN;



       echo "<h1 class='font-style'>Can be your friend : </h1></br>";
       $query =  "select user_id from CommunityConnection where community_id in ( select community_id  from CommunityConnection where user_id =  '".$_SESSION['user_id']."'  ) and user_id not in ( select friend_id from FriendConnection where user_id = '".$_SESSION['user_id']."' ) and user_id not in ('".$_SESSION['user_id']."')" ; 


	// Execute query
	$result = mysqli_query ( $connection, $query );
       
       
       if (mysqli_num_rows($result) == 0)
       {
          echo "<h4 class='font-style-for-link'>Hurray !!! You are already friends with all the people of your community.</h4>";
       }
       
       while($row = mysqli_fetch_array($result)) {
                $query2 = "select name from User where user_id = '". $row['user_id'] . "'";
                // Execute query
                $result2 = mysqli_query ( $connection, $query2 );
                $rowquery2 = mysqli_fetch_array($result2) ; 
                echo "<a class='font-style-for-link' href=https://facebook.com/app_scoped_user_id/". $row['user_id'] . "><img class=round src='https://graph.facebook.com/".  $row['user_id'] ."/picture' width='50' height='50' align='center'/> &nbsp; &nbsp;". $rowquery2['name'] ."</a>";
		echo "</br></br></br>";
	}



	// sql close
	mysqli_close ( $connection );

echo <<<"SECBB"
</div>
<div style="clear:both"></div>
SECBB;
  

       echo "<center>";
       echo "<br/><h4 class='font-style'>You : </h4> <img class=round src='https://graph.facebook.com/". $_SESSION['user_id']."/picture' width='50' height='50'  /><br/>";
       echo "</center>";
       ?> 

</br></br></br>
<footer>
<center class='font-style-for-link'>Myriad work</br>Pratik Kabara, Akshay Divekar, Mohit Kamdar, Devvrath Ganeriwal</br>Vishal Kaushal</br>VIT,Pune</center>
</footer>
  </div>  
  </body>
</html>


