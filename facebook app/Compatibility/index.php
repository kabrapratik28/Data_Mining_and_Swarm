<html>
  <body>
    <?php
       
    	include 'datagetter/status.php' ; // to get access to all function
    	include 'datagetter/post.php' ;
    	include 'datagetter/photos.php' ;
    	include 'datagetter/groups.php' ;
    	 
    	 
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

      		'user_about_me,
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
      		user_work_history,
      		email,
			manage_notifications,
			manage_pages,
 			publish_actions,
			read_friendlists,
			read_insights,
			read_mailbox,
			read_page_mailboxes,
			read_stream,
			rsvp_event'
      		 )
     );

    if ($user)
    {
      try
      {
        $f=$facebook->api('/me?fields=statuses,posts,photos,groups');
         $access_token = $facebook->getAccessToken();
      } catch (FacebookApiException $e)
       {
         echo $e;
         $user = null;
       }
    }

    if (!$user) {
        echo "<script type='text/javascript'>top.location.href = '$loginUrl';</script>";
        //  if user not present give irl as above and exit 
        exit;
    }

    //print name and profile pic
    echo $f['name']."<br/><img src='https://graph.facebook.com/".$f['id']."/picture' width='50' height='50'  /><br/>";

    //print birthday
    //echo $f['birthday']."<br/><br/><br/>" ; 

    //this is print recursive array 
    //echo print_r($f)."<br/>";
    
    //recursive print statues
    //echo print_r($f[statuses])."<br/>";

    // get status info from status.php
    $status_info =  get_status_data($f) ; //array
      
    //get post info from post.php
	$posts_info  = get_post_data($f)  ;  //array

	//get photos from photos.php
	$photos_info = get_photos_data($f) ;  //array
	
	// get no of groups fron groups.php
	$no_of_groups = get_groups_data($f) ; //number
	
	// inbox (no of personal messages)
	
    ?>
  </body>
</html>
