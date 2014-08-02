<html>

  <body>


    <?php
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
      		user_work_history'
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
        $f=$facebook->api('/me?fields=id,name,birthday,statuses,posts,photos,groups,tagged_places,friends');
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
        //  if user not present give irl as above and exit 
        exit;
    }

    //print name and profile pic
    echo $f['name']."<br/><img src='https://graph.facebook.com/".$f['id']."/picture' width='50' height='50'  /><br/>";

    //print birthday
    echo $f['birthday']."<br/><br/><br/>" ; 

    //add to database table User (id,name,birthday)
    add_user($f); 
    
    $g = $facebook->api('/me?fields=photos.limit(5).fields(id,name,place,updated_time,tags.limit(1000),comments.limit(1000),likes.limit(1000)),statuses.limit(5).fields(id,message,place,updated_time,tags.limit(1000),comments.limit(1000),likes.limit(1000)),groups.limit(5).fields(id,name),tagged_places.limit(5),albums.limit(1000).fields(count,name),friends');
    extract_status_related_things($g) ; 
    
    extract_photo_related_things($g);
    
    extract_group_related_things($g);
    
    extract_tagged_place($g) ; 
    
    extract_friend_count($g) ; 
    
    extract_profile_pic_timeline_count($g)  ; 
    //this is print recursive array 
    //echo print_r($f)."<br/>";
    
    //recursive print statues
    //echo print_r($f[statuses])."<br/>";

    // get status info from status.php
    //$status_info =  get_status_data($f) ; //array
      
    //get post info from post.php
	//$posts_info  = get_post_data($f)  ;  //array

	//get photos from photos.php
	//$photos_info = get_photos_data($f) ;  //array
	
	// get no of groups fron groups.php
	//$no_of_groups = get_groups_data($f) ; //number
	
	/*   // NEEDED EXTENDED PERMISSION
	// inbox (no of personal messages)  // not all meg gettings from first day
	$no_of_messages = get_inbox_data($f) ; //number 
	*/
	
	// tagged places info from tagged_places.php
	//$tagged_places_info = get_tagged_places_data($f) ; // array of place and time 
	
	//friends getting from friends.php  // ***ONLY GET FRIENDS WHICH ARE USING THIS APP
	//$friends_name_id = get_facebook_friends($f); 
	
    $friends_name_id = $g ['friends'] ['data'] ; 
    function cmp($a, $b)   // assend by name
    {
    	return strcmp($a["name"], $b["name"]);
    }
    usort($friends_name_id, "cmp");
    //echo print_r($friends_name_id)."<br/>";
	foreach ($friends_name_id as $one_friend)
	{
		echo $one_friend['name']."<br/><img src='https://graph.facebook.com/".$one_friend['id']."/picture' width='50' height='50'  /><br/>";
		
	}
	// ***************************
	// PASSING DATE TO DATABASE 
	// ***************************
	// $datetoputinmysql = date('Y-m-d', strtotime('02/28/1994')); // "Year-month-date" mysql compatible
	// ***************************
	
    $one_user_all_values_array = get_userinfo_from_mysql($f['id']);
    
    $user_id_for_app = $one_user_all_values_array['user_id'];
    $nameofuser = $one_user_all_values_array['name'];
    $user_avg_like_status  =  $one_user_all_values_array['avg_no_of_like_per_status'] ; 
    $user_avg_like_photo  =  $one_user_all_values_array['avg_no_of_like_per_photo'] ;
    $user_no_of_place = $one_user_all_values_array['no_of_place'] ;
    $user_avg_status_per_week =  $one_user_all_values_array['avg_status_per_week'] ;
    $user_total_status   =$one_user_all_values_array['total_no_of_status'] ;
    $user_avg_photo   =$one_user_all_values_array['avg_photo_per_week'] ;
    $user_total_photo   =$one_user_all_values_array['total_no_of_photo'] ;
    $user_frnd_cnt   =$one_user_all_values_array['friendcount'];
    $user_prfile_pic_cnt   =$one_user_all_values_array['profile_pic_count'];
    $user_cvr_pic_cnt   =$one_user_all_values_array['cover_pic_count'];
    
    
    ?>
	
	
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>

<!--Put the following in the <head>-->
<script type="text/javascript">
$("document").ready(function(){
  $(".js-ajax-php-json").submit(function(){
    var data = {
      "action": "getuserinfo"
    };
    data = $(this).serialize() + "&" + $.param(data);
    $.ajax({
      type: "POST",
      dataType: "json",
      url: "database/getdata.php", //Relative or absolute path to response.php file
      data: data,
      success: function(data) {
        $(".the-return").html(
          "JSON: " + data["json"]
        );
 
        //alert("Form submitted successfully.\nReturned json: " + data["json"]);
      }
    });
    return false;
  });
});
</script>
	
	
<!--Put the following in the <body>-->
<form action="database/getdata.php" class="js-ajax-php-json" method="post" accept-charset="utf-8">
  <input type="text" name="user_id_value" value="" placeholder="User id" />  
  <input type="submit" name="submit" value="Submit form"  />
</form>
 
<div class="the-return">
  [HTML is replaced when successful.]
</div>	
	
	<script src="http://connect.facebook.net/en_US/all.js"></script>
	  <script>
      FB.init({
        appId  : '759326554108395',
        status : true,
        cookie : true,
		xfbml : true
      });

		function invitefriends(){
			FB.ui({
				method: 'apprequests',
		        message: 'Compare Facebook Usage With Your Friends',		
			});				
		}

		function publicresults(){
			msg = 'Compare your results with me.' ; 
			
			FB.ui({
				method: 'feed',
		        name : 'Compare Facebook Usage With Your Friends',
		        link : 'https://apps.facebook.com/759326554108395/'	,	
		       /* picture : '' ,*/
			    caption : 'Can you beat it ? ' ,  
			    description : msg ,   
			});		

			}
	  </script>
      
      <input type="button"
        onclick="invitefriends(); return false;"
        value="Send Request to Friends"
      /><br/><br/><br/>
      
      <input type="button"
        onclick="publicresults(); return false;"
        value="Post Results"
      />
  </body>
</html>
