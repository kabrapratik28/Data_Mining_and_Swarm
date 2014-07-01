<html>

 <body>
<?php


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
                'scope'         => 'email,publish_stream,status_update,user_birthday,user_location,user_work_history,user_location,user_activities,user_about_me,user_education_history,user_events,user_groups,user_interests,user_likes,user_photos,user_relationship_details,user_status,user_tagged_places'
            )
        );

    if ($user)
    {
      try
      {
        $f=$facebook->api('/me');
         $access_token = $facebook->getAccessToken();
      } catch (FacebookApiException $e)
       {
         echo $e;
         $user = null;
       }
    }

    if (!$user) {
        echo "<script type='text/javascript'>top.location.href = '$loginUrl';</script>";
        exit;
    }
echo $f['name']."<br/><img src='https://graph.facebook.com/".$f['id']."/picture' width='50' height='50'  /><br/>";

echo $f['birthday']."<br/><br/><br/>" ; 
echo print_r($f)."<br/>";

?>
</body>
</html>