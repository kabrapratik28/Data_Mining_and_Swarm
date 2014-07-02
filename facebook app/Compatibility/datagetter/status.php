<?php 
function get_status_data($facebook_user)
       {
           $array_of_status = array() ; 
           $array_of_no_of_comments = array() ; 
           $array_of_no_of_likes= array() ;

        // status // likes // no of data

       foreach( $facebook_user['statuses']['data'] as $status) {
           array_push($array_of_status , $status['message']) ; 
           array_push($array_of_no_of_likes ,count($status['likes']['data'])) ;
           array_push( $array_of_no_of_comments , count($status['comments']['data']));
       }
        //test 
        //recursive print statues
        //echo print_r($f[statuses])."<br/>";
       
          $array_of_return = array() ; 
          array_push($array_of_return,$array_of_status,$array_of_no_of_likes, $array_of_no_of_comments) ; 
          
          //return array of status array, no of like array, no of comments array 
          return  $array_of_return ; 
       }
?>       