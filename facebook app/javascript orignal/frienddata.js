// ==ClosureCompiler==
// @compilation_level SIMPLE_OPTIMIZATIONS
// @output_file_name default.js
// @code_url http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js
// ==/ClosureCompiler==



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

    	  var sec_user_name = (data['name' ]).toString() ;
    	  var sec_avg_like_of_stat = data['avg_no_of_like_per_status'];
 		  var sec_avg_like_of_photo  = data['avg_no_of_like_per_photo'] ;
     	  var sec_no_of_place = data['no_of_place'] ;
     	  var sec_total_status = data['total_no_of_status'] ;
     	  var sec_total_no_of_photo = data['total_no_of_photo']  ;
     	  var sec_friendcount = data['friendcount'] ;
     	  var sec_profile_pic_count = data['profile_pic_count'] ;
     	  var sec_cover_pic_count = data['cover_pic_count'];

     		var total_avg_like_of_stat = ( parseFloat(fir_avg_like_of_stat)   + parseFloat( sec_avg_like_of_stat) ).toString();
    		var total_avg_like_of_photo = ( parseFloat(fir_avg_like_of_photo) + parseFloat( sec_avg_like_of_photo)).toString();
    		var total_no_of_place =( parseFloat(fir_no_of_place) + parseFloat(sec_no_of_place )).toString();
    		var total_total_status = (parseFloat(fir_total_status)  + parseFloat(sec_total_status)).toString();
    		var total_total_no_of_photo = (parseFloat(fir_total_no_of_photo) + parseFloat(sec_total_no_of_photo)).toString()  ;
    		var total_friendcount = (parseFloat(fir_friendcount) + parseFloat(sec_friendcount) ).toString();
    		var total_profile_pic_count =(parseFloat(fir_profile_pic_count) + parseFloat(sec_profile_pic_count)).toString() ;
    		var total_cover_pic_count =( parseFloat(fir_cover_pic_count) +  parseFloat(sec_cover_pic_count)).toString() ;


          $(".the-return").html(

        		    "<div class=\"container_size\">\
\
        		      <div class=\"compatibility_name\" >\
        			<h1>COMPATIBILITY FINDER</h1>\
        		      </div>\
\
        		      <div class=\"left_user_details\">\
\
        			<div id=\"page-wrapper\">\
\
        			  <h2>"+fir_user_name+"</h2>\
\
\
        			  <p class=\"styled\">\
        			    <span style=\"float : right;\">"+fir_avg_like_of_stat+"</span>\
        			    <progress dir=rtl value=\""+fir_avg_like_of_stat+"\" max=\""+total_avg_like_of_stat+"\" ></progress> <!--dir=rtl for right to left -->\
        			  </p>\
        			  <p class=\"styled\">\
      			   	 	<span style=\"float : right;\">"+fir_avg_like_of_photo+"</span>\
      			    	<progress dir=rtl value=\""+fir_avg_like_of_photo+"\" max=\""+total_avg_like_of_photo+"\" ></progress> <!--dir=rtl for right to left -->\
      			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_no_of_place+"</span>\
    			    	<progress dir=rtl value=\""+fir_no_of_place+"\" max=\""+total_no_of_place+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_total_status+"</span>\
    			    	<progress dir=rtl value=\""+fir_total_status+"\" max=\""+total_total_status+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_total_no_of_photo+"</span>\
    			    	<progress dir=rtl value=\""+fir_total_no_of_photo+"\" max=\""+total_total_no_of_photo+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_friendcount+"</span>\
    			    	<progress dir=rtl value=\""+fir_friendcount+"\" max=\""+total_friendcount+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_profile_pic_count+"</span>\
    			    	<progress dir=rtl value=\""+fir_profile_pic_count+"\" max=\""+total_profile_pic_count+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
        			  <p class=\"styled\">\
    			   	 	<span style=\"float : right;\">"+fir_cover_pic_count+"</span>\
    			    	<progress dir=rtl value=\""+fir_cover_pic_count+"\" max=\""+total_cover_pic_count+"\" ></progress> <!--dir=rtl for right to left -->\
    			  	  </p>\
\
        			</div><!-- page wrapper ends  -->\
        		      </div><!-- user left ends  -->\
\
        		      <div class=\"middle_comparision_name\">\
        			<div id=\"page-wrapper\">\
\
        			  <h2>No of Pramameter</h2>\
        			  \
\
        			  <p>Average No of likes per Status<p>\
        			    <br>\
        			  <p>Average No of likes per Photo<p> \
        			    <br>   \
        			  <p>No of Places Went to<p> \
        			    <br>  \
        			  <p>Total No of Status<p> \
        			    <br> \
        			  <p>Total No of Photo<p> \
        			    <br>\
        			  <p>No of Friend<p> \
        			    <br>\
        			  <p>Profile Picture Count<p> \
        			    <br>\
        			  <p>Cover Picture Count<p> \
        			    <br>\
        			    \
        			</div>\
        		      </div>\
\
        		      <div class=\"right_user_details\">\
        			<div id=\"page-wrapper\">\
\
        			  <h2>"+sec_user_name+"</h2>\
\
\
<p class=\"styledd\">\
  <span style=\"float : left;\">"+sec_avg_like_of_stat+"</span>\
  <progress value=\""+sec_avg_like_of_stat+"\" max=\""+total_avg_like_of_stat+"\"></progress>\
</p>\
<p class=\"styledd\">\
	<span style=\"float : left;\">"+sec_avg_like_of_photo+"</span>\
	<progress value=\""+sec_avg_like_of_photo+"\" max=\""+total_avg_like_of_photo+"\"></progress>\
	  </p>\
<p class=\"styledd\">\
	<span style=\"float : left;\">"+sec_no_of_place+"</span>\
	<progress value=\""+sec_no_of_place+"\" max=\""+total_no_of_place+"\"></progress>\
  </p>\
<p class=\"styledd\">\
<span style=\"float : left;\">"+sec_total_status+"</span>\
<progress value=\""+sec_total_status+"\" max=\""+total_total_status+"\"></progress>\
</p>\
<p class=\"styledd\">\
<span style=\"float : left;\">"+sec_total_no_of_photo+"</span>\
<progress value=\""+sec_total_no_of_photo+"\" max=\""+total_total_no_of_photo+"\"></progress>\
</p>\
<p class=\"styledd\">\
	<span style=\"float : left;\">"+sec_friendcount+"</span>\
	<progress value=\""+sec_friendcount+"\" max=\""+total_friendcount+"\"></progress>\
  </p>\
<p class=\"styledd\">\
<span style=\"float : left;\">"+sec_profile_pic_count+"</span>\
<progress value=\""+sec_profile_pic_count+"\" max=\""+total_profile_pic_count+"\"></progress>\
</p>\
<p class=\"styledd\">\
<span style=\"float : left;\">"+sec_cover_pic_count+"</span>\
<progress value=\""+sec_cover_pic_count+"\" max=\""+total_cover_pic_count+"\"></progress>\
</p>\
</div><!-- page wrapper ends  -->  \
</div><!-- user right ends  -->  \
</div>"
        );

        //alert("Form submitted successfully.\nReturned json: " + data["json"]);
      }
    });
    return false;
  });
});
