<?php
function get_groups_data($facebook_user)
{
	$count_no_groups =  count($facebook_user['groups']['data']) ; 
	return $count_no_groups ; 
}
?>