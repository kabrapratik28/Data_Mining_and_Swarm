// ==ClosureCompiler==
// @compilation_level SIMPLE_OPTIMIZATIONS
// @output_file_name default.js
// @code_url http://connect.facebook.net/en_US/all.js
// ==/ClosureCompiler==

      FB.init({
        appId  : '759326554108395',
        status : true,
        cookie : true,
		xfbml : true
      });

		function invitefriends(){
			FB.ui({
				method: 'apprequests',
		        message: 'Compare Facebook Usage With Your Friends'		
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
			    description : msg    
			});		

			}
