<?php
function get_inbox_data($facebook_user)
{
	$count_no_messages =  count($facebook_user['inbox']['data']) ;
	return $count_no_messages ;
}
?>