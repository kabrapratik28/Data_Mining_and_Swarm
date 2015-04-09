<html>
<head> 
<link rel="stylesheet" type="text/css" href="css/compatibility.css">
<!--<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>-->
<!--<script src="http://connect.facebook.net/en_US/all.js"></script>-->
<script src="javascript/frienddata.js"></script>
<script src="javascript/facebookbutton.js"></script>
</head>
  <body>

    <?php
       session_start(); //for session to show friends on clicking button and go to another page
       /*
    	include 'datagetter/status.php' ; // to get access to all function
    	include 'datagetter/post.php' ;
    	include 'datagetter/photos.php' ;
    	include 'datagetter/groups.php' ;
    	include 'datagetter/inbox.php' ;       //NEEDS extra permissions 
    	include 'datagetter/tagged_places.php' ;
    	include 'datagetter/friends.php' ;
    	*/ 
    	include 'database/add_data.php' ; //database add
    	include 'database/getdata.php';   // get user data
       //echo '<link href="css/try.css" rel="stylesheet">';
   
       $app_id = "759326554108395";
       $app_secret = "fffa8ed3bb41ac6a97aa1044bf369688";

       try
       {
          include_once ('facebook-php-sdk/src/facebook.php');
       }
       catch(Exception $o)
       {
          print_r($o);
       }

       $facebook = new Facebook(array(
         'appId'  => $app_id,
         'secret' => $app_secret,
         'cookie' => true
       ));

      $user = $facebook->getUser();
      $loginUrl = $facebook->getLoginUrl(array
           (
                //taking permission as passed in string 
            'scope'         =>           
			'user_birthday,
             user_friends,
             user_status,
             user_photos,
      		 user_tagged_places'
          
      		/*'user_about_me,
      		user_actions.books,
      		user_actions.music,
      		user_actions.news,
      		user_actions.video,
      		user_activities,
      		user_birthday,
      		user_education_history,
      		user_events,
      		user_friends,
      		user_games_activity,
      		user_groups,
      		user_hometown,
      		user_interests,
      		user_likes,
      		user_location,
      		user_photos,
      		user_relationship_details,
      		user_relationships,
      		user_religion_politics,
      		user_status,
      		user_tagged_places,
      		user_videos,
      		user_website,
      		user_work_history'*/
           	/*   // JUST REMOVE STRING COMPLETING BEFORE ADDING .. 
           	 *   // THIS are extended permissions (NOT REQUIRE)
           		,
      		email,
			manage_notifications,
			manage_pages,
 			publish_actions,
			read_friendlists,
			read_insights,
			read_mailbox,
			read_page_mailboxes,
			read_stream    ,                      		
			rsvp_event'   
			*/                          
      		 )
     );

    if ($user)
    {
      try
      {
      	// PLEAse CHANGE THIS ...... ************************
        $g=$facebook->api('me?fields=id,name,birthday,photos.limit(100){id,name,place,updated_time,tags.limit(100),comments.limit(100),likes.limit(100)},statuses.limit(1000){id,message,place,updated_time,tags.limit(100),comments.limit(100),likes.limit(100)},tagged_places.limit(1000),albums.limit(1000){count,name},friends.limit(1000)');
        /*
         * email - require extended permission
         * inbox - read_mailbox requires extended permission 
         * $f=$facebook->api('/me?fields=id,name,birthday,email,statuses,posts,photos,groups,inbox,tagged_places,friends');
       	*/
         $access_token = $facebook->getAccessToken();
      } catch (FacebookApiException $e)
       {
         echo $e;
         $user = null;
       }
    }

    if (!$user) {
        echo "<script type='text/javascript'>top.location.href = '$loginUrl';</script>";
        //  if user not present give url as above and exit 
        exit;
    }

    // give session this id of user so button of show friends can suggest friends
    $_SESSION['user_id'] = $g['id'];

    //print name and profile pic
    //echo $g['name']."<br/><img src='https://graph.facebook.com/".$g['id']."/picture' width='50' height='50'  /><br/>";

    //print birthday
    //echo $g['birthday']."<br/><br/><br/>" ; 

    //add to database table User (id,name,birthday)
    add_user($g); 
    
    //$g = $facebook->api('/me?fields=photos.limit(5).fields(id,name,place,updated_time,tags.limit(1000),comments.limit(1000),likes.limit(1000)),statuses.limit(5).fields(id,message,place,updated_time,tags.limit(1000),comments.limit(1000),likes.limit(1000)),groups.limit(5).fields(id,name),tagged_places.limit(5),albums.limit(1000).fields(count,name),friends');
    extract_status_related_things($g) ; 
    
    extract_photo_related_things($g);
    
    //extract_group_related_things($g);
    
    extract_tagged_place($g) ; 
    
    extract_friend_count($g) ; 

	extract_friend_of_user($g);    

    extract_profile_pic_timeline_count($g)  ; 
    //this is print recursive array 
    //echo print_r($g)."<br/>";
    
    //recursive print statues
    //echo print_r($g[statuses])."<br/>";

    // get status info from status.php
    //$status_info =  get_status_data($g) ; //array
      
    //get post info from post.php
	//$posts_info  = get_post_data($g)  ;  //array

	//get photos from photos.php
	//$photos_info = get_photos_data($g) ;  //array
	
	// get no of groups fron groups.php
	//$no_of_groups = get_groups_data($g) ; //number
	
	/*   // NEEDED EXTENDED PERMISSION
	// inbox (no of personal messages)  // not all meg gettings from first day
	$no_of_messages = get_inbox_data($g) ; //number 
	*/
	
	// tagged places info from tagged_places.php
	//$tagged_places_info = get_tagged_places_data($g) ; // array of place and time 
	
	//friends getting from friends.php  // ***ONLY GET FRIENDS WHICH ARE USING THIS APP
	//$friends_name_id = get_facebook_friends($g); 
	

	// ***************************
	// PASSING DATE TO DATABASE 
	// ***************************
	// $datetoputinmysql = date('Y-m-d', strtotime('02/28/1994')); // "Year-month-date" mysql compatible
	// ***************************
	
    // random no between 0,1 (mt_rand() / mt_getrandmax())
    
    $one_user_all_values_array = get_userinfo_from_mysql($g['id']);
    
    $user_id_for_app = $one_user_all_values_array['user_id'];
    $nameofuser = $one_user_all_values_array['name'];
    
    $user_avg_like_status  =  $one_user_all_values_array['avg_no_of_like_per_status'] ; 
    $randuser_avg_like_status = (mt_rand() / mt_getrandmax())   *10 *   $user_avg_like_status;
    
    $user_avg_like_photo  =  $one_user_all_values_array['avg_no_of_like_per_photo'] ;
    $randuser_avg_like_photo =(mt_rand() / mt_getrandmax())   *10 *  user_avg_like_photo;
    
    $user_no_of_place = $one_user_all_values_array['no_of_place'] ;
    $randuser_no_of_place  =(mt_rand() / mt_getrandmax())   *10 *   $user_no_of_place;
    
 //   $user_avg_status_per_week =  $one_user_all_values_array['avg_status_per_week'] ;
  //  $randuser_avg_status_per_week =(mt_rand() / mt_getrandmax())   *10 * $user_avg_status_per_week ;
    
   $user_total_status   =$one_user_all_values_array['total_no_of_status'] ;
    $randuser_total_status =(mt_rand() / mt_getrandmax())   *10 * $user_total_status ;
    
 //   $user_avg_photo   =$one_user_all_values_array['avg_photo_per_week'] ;
  //  $randuser_avg_photo =(mt_rand() / mt_getrandmax())   *10 * $user_avg_photo  ;
    
    $user_total_photo   =$one_user_all_values_array['total_no_of_photo'] ;
    $randuser_total_photo =(mt_rand() / mt_getrandmax())   *10 *  $user_total_photo;
    
    $user_frnd_cnt   =$one_user_all_values_array['friendcount'];
    $randuser_frnd_cnt  =(mt_rand() / mt_getrandmax())   *10 * $user_frnd_cnt  ;
    
    $user_prfile_pic_cnt   =$one_user_all_values_array['profile_pic_count'];
    $randuser_prfile_pic_cnt =(mt_rand() / mt_getrandmax())   *10 * $user_prfile_pic_cnt ;
    
    $user_cvr_pic_cnt   =$one_user_all_values_array['cover_pic_count'];
    $randuser_cvr_pic_cnt  =(mt_rand() / mt_getrandmax())   *10 *  $user_cvr_pic_cnt  ;
    
echo <<<"INITIAL"
	<div class="the-return">
    <div class="container_size">
    <div class="compatibility_name" >
	<h1>COMPATIBILITY FINDER</h1>
	<h4><font color="red">Send request to your friends. Once they accept app request, you can Compare with them.  </font></h4>
	<h5><font color="red">(See bottom left button for sending request)</font></h5>
      </div>

      <div class="left_user_details">

	<div id="page-wrapper">

	  <h2>$nameofuser</h2>
	  <p class="styled">
	    <span style="float : right;">$user_avg_like_status</span>
	    <progress dir=rtl value="$user_avg_like_status" max="$randuser_avg_like_status" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_avg_like_photo</span>
	    <progress dir=rtl value="$user_avg_like_photo" max="$randuser_avg_like_photo" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_no_of_place</span>
	    <progress dir=rtl value="$user_no_of_place" max="$randuser_no_of_place" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_total_status</span>
	    <progress dir=rtl value="$user_total_status" max="$randuser_total_status" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_total_photo</span>
	    <progress dir=rtl value="$user_total_photo" max="$randuser_total_photo" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_frnd_cnt</span>
	    <progress dir=rtl value="$user_frnd_cnt" max="$randuser_frnd_cnt" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_prfile_pic_cnt</span>
	    <progress dir=rtl value="$user_prfile_pic_cnt" max="$randuser_prfile_pic_cnt" ></progress> <!--dir=rtl for right to left -->
	  </p>

	  <p class="styled">
	    <span style="float : right;">$user_cvr_pic_cnt</span>
	    <progress dir=rtl value="$user_cvr_pic_cnt" max="$randuser_cvr_pic_cnt" ></progress> <!--dir=rtl for right to left -->
	  </p>

	</div><!-- page wrapper ends  -->
      </div><!-- user left ends  -->

      <div class="middle_comparision_name">
	<div id="page-wrapper">

	  <h2>No of Parameter</h2>
	  
	  <p>Average No of likes per Status<p>
	    <br>
	  <p>Average No of likes per Photo<p> 
	    <br>  
	  <p>No of Places Went to<p> 
	    <br>  
	  <p>Total No of Status<p> 
	    <br> 
	  <p>Total No of Photo<p> 
	    <br>
	  <p>No of Friend<p> 
	    <br>
	  <p>Profile Picture Count<p> 
	    <br>
	  <p>Cover Picture Count<p> 
	    <br>
	    
	</div>
      </div>

      <div class="right_user_details" style="overflow-y : scroll;">
	<div id="page-wrapper">
	    <form action="database/getdata.php" class="js-ajax-php-json" method="post" accept-charset="utf-8" >
	     
	    <div class="buttonHolder">
	     <input type="submit" name="submit" value="Compare !!!"  class="button_example"/><br><br>
	    </div>
	     
INITIAL;
    
    $friends_name_id = $g ['friends'] ['data'] ;
    function cmp($a, $b)   // assend by name
    {
    	return strcmp($a["name"], $b["name"]);
    }
    usort($friends_name_id, "cmp");
    //echo print_r($friends_name_id)."<br/>";
    //$friends_name_id = array() ; 
    //echo count($friends_name_id) ; 
    if (count($friends_name_id)!=0)
    {
   		 foreach ($friends_name_id as $one_friend)
    	{
    	$id_of_one_friend = $one_friend['id'] ;
    	echo <<<"FORMMIDDLE"
<input type="radio" name="user_id_value" value="$id_of_one_friend">
FORMMIDDLE;
    	echo $one_friend['name']."  <img src='https://graph.facebook.com/".$id_of_one_friend."/picture' width='50' height='50'  /><br/><br/>";
    	}
    }else {

    	echo <<<"NOTICE"
    	<h3><font color="red">Send request to your friends. Once they accept app request, you can Compare with them. </font></h3>
    <div class="button_example">
      <input type="button"
        onclick="invitefriends(); return false;"
        value="Send Request to Friends"
      /><br/>
    	</div>
NOTICE;
    	
    }
echo <<<"FORMEND"
    
	</div><!-- page wrapper ends  -->
      </div><!-- user right ends  -->

    </div>
    			
</div><!-- upto this replace take place -->
FORMEND;
    
    
    ?> 
	
<!--Put the following in the <head>-->	
<script>
 var fir_user_name = "<?php echo $nameofuser; ?>";
 var fir_avg_like_of_stat = "<?php echo $user_avg_like_status; ?>";
 var fir_avg_like_of_photo = "<?php echo $user_avg_like_photo; ?>";
 var fir_no_of_place = "<?php echo $user_no_of_place; ?>";
 var fir_total_status = "<?php echo $user_total_status; ?>";
 var fir_total_no_of_photo= "<?php echo $user_total_photo; ?>";
 var fir_friendcount = "<?php echo $user_frnd_cnt; ?>";
 var fir_profile_pic_count = "<?php echo $user_prfile_pic_cnt; ?>";
 var fir_cover_pic_count = "<?php echo $user_cvr_pic_cnt; ?>";
</script>	
<br/><br/><br/>


<a href="/facebookfriendsdisplay/">Suggest Friend</a>


	
	  <br/>
      <div class="button_example">
      <input type="button"
        onclick="invitefriends(); return false;"
        value="Send Request to Friends"
      /><br/>
      
      <input type="button"
        onclick="publicresults(); return false;"
        value="Post Results"
      /> 
      </div>
  </body>
</html>
