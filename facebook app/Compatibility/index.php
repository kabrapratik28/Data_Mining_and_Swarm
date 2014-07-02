<html>
  <body>
    <?php
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
                'scope'         => 'public_profile,user_friends,email,user_about_me,user_activities,user_activities,user_birthday,user_education_history,user_events,user_groups,user_hometown,user_interests,user_likes,user_location,user_photos,user_relationships,user_relationship_details,user_relationship_details,user_status,user_tagged_places,user_videos,user_work_history,read_friendlists,read_friendlists'
           )
     );

    if ($user)
    {
      try
      {
        $f=$facebook->api('/me?fields=statuses');
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

    echo count($f[statuses][data])."<br/>" ; 
    
    //recursive print statues
    //echo print_r($f[statuses])."<br/>";

   // status // likes // no of data
    foreach( $f[statuses][data] as $status) {
        echo "Status : ".$status[message]."<br/>" ; 
        echo "Like no. : ".count($status[likes][data])."<br/>" ;
        echo "Comment no. : ".count($status[comments][data])."<br/>" ;
        echo "<br/><br/><br/>";
    
    }

  

    ?>
  </body>
</html>

