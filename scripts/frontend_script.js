		
		/*
		*	Created By: Shane Elliott
		*	Comments: Made for Code Foo 2017!
		*/
		
		//
		//=======================================================
		// Instance Variables
		//=======================================================
		//
		
		//Video and Article List References
		var videoList = document.getElementById('video-list');
		var articleList = document.getElementById('article-list');
		
		//Arays containing new items to add to video/article lists
		var a_add_array = [];
		var v_add_array = [];
		var arequiredSize = 10; //Required number of new entries to search for
		var vrequiredSize = 10;
		
		var alistSize = 0; //Current Size of the article-list including items to be added
		var vlistSize = 0; //Current Size of the video-list including items to be added
		
		//List toggle refrence. So we know if we are searching for videos or articles
		var listtoggle = 0; //(0 = Display Video List) (0 = Display Article List)
		
		var tags = [""]; //Current tags to search for
		var loading_state = true; //Loading state. Are we currently loading data or not.
		
		//URL loading data
		var url = "http://ign-apis.herokuapp.com";
		var contenttype = '/videos';
		var startIndex = '?startIndex=0';
		var count = "&count=10";
		var callback = '&callback=handleVideoList';
		
		//Index of current lists to continue searching new data
		var a_index = 0;
		var v_index = 0;
		
		//Canvas used to draw basic texts and boxes ontop of images
		var canvas = document.createElement('canvas');
		var ctx = canvas.getContext('2d');
		canvas.image_rendering = 'pixelated';
		
		//
		//=============================================================================================
		// Onclick functions
		//=============================================================================================
		//
		
		//Tabs
		all_tab = document.getElementById("tab_all");
		ps4_tab = document.getElementById("tab_ps4");
		xbox_tab = document.getElementById("tab_xbox");
		nintendo_tab = document.getElementById("tab_nintendo");
		pc_tab = document.getElementById("tab_pc");
		media_tab = document.getElementById("tab_media");
		
		//Click tab, Resets Video/Article lists, loads new data
		//Clicking each tab sets new array of string tags
		all_tab.onclick = function(){
			tags = [""];
			tabtoggle(10);
		}
		
		ps4_tab.onclick = function(){
			tags = ["ps4", "playstation", "sony"];
			tabtoggle(20);
		}
		
		xbox_tab.onclick = function(){
			tags = ["xbox", "halo", "microsoft"];
			tabtoggle(20);
		}
		
		nintendo_tab.onclick = function(){
			tags = ["nintendo", "mario", "zelda", "switch", "pokemon"];
			tabtoggle(20);
		}
		
		pc_tab.onclick = function(){
			tags = ["pc"];
			tabtoggle(20);
		}
		media_tab.onclick = function(){
			tags = ["tv", "movie"];
			tabtoggle(20);
		}
		
		function tabtoggle(numb){
			//Reset all data related to the article and video lists
			videoList.innerHTML = "";
			articleList.innerHTML = "";
			v_index = 0;
			a_index = 0;
			a_add_array = [];
			v_add_array = [];
			alistSize = 0;
			vlistSize = 0;
		
			arequiredSize = 10;
			vrequiredSize = 10;
			//Load content
			loadMoreContentEvent(numb, 0);
			loadMoreContentEvent(numb, 1);
			
			//Create and add progress spinners
			vloader = createLoader();
			aloader = createLoader();
				
			articleList.appendChild(aloader);
			videoList.appendChild(vloader);
				
			loading_state = true;
			
			
		}
		
		//Clicking 'VIDEOS' and 'ARTICLES' menus switches between lists to view
		document.getElementById('div-header-left').onclick = function (){
				videoList.style.display = 'block';
				document.getElementById('div-header-left').className = 'div-header-b-on';
				articleList.style.display = 'none';
				document.getElementById('div-header-right').className = 'div-header-b-off';
				document.getElementById('content-footer').innerHTML = '<h4>SEE MORE VIDEOS...</h4>';
				//Toggle List to videoList
				listtoggle = 0;
			}
			
		document.getElementById('div-header-right').onclick = function (){
				videoList.style.display = 'none';
				document.getElementById('div-header-left').className = 'div-header-b-off';
				articleList.style.display = 'block';
				document.getElementById('div-header-right').className = 'div-header-b-on';
				document.getElementById('content-footer').innerHTML = '<h4>SEE MORE ARTICLES...</h4>';
				//Toggle List to articleList
				listtoggle = 1;
			}
		
		//Click 'see more videos/articles'. Loads a required 5 more entries to list.
		function clickLoadMore(){
			if (!loading_state){
				//If videoList: reset new entries array (v_add_array) to empty and set required new entries to 5. Then load new data. 
				if (listtoggle == 0){
						vrequiredSize = 5;
						v_add_array = [];
						vloader = createLoader();
						videoList.appendChild(vloader);
						loadMoreContentEvent(5,0);
					}
				//If articleList: reset new entries array (a_add_array) to empty and set required new entries to 5. Then load new data. 
				else if (listtoggle == 1){
						arequiredSize = 5;
						a_add_array = [];
						aloader = createLoader();
						articleList.appendChild(aloader);
						loadMoreContentEvent(5,1);
					}

				document.getElementById('content-footer').scrollIntoView({behavior:'smooth'});
			}
		}
		
		//
		//=======================================================
		// LOADING AND DISPLAYING DATA
		//=======================================================
		//
		
		/*loadMoreContentEvent - This function makes a new request to load data.
		* Creates a new URL to query the webapi to return conent of content type (video/article),
		* callback function (handleVideoList, handleArticleList), current index (a_index, v_index), 
		* and number of items to return. Next, requests the data as a new script element.
		*@param {count} the number of new entries to load
		*@param (list_type) which list are to load the entries to (videoList/articleList)
		*/
		
		function loadMoreContentEvent(count, list_type){
			
			//Creates URL string acording to type, callback method, starting index and count
			if (list_type == 0){
				contenttype = '/videos';
				callback = '&callback=handleVideoList';
				count = "&count=" + count;
				startIndex = '?startIndex=' + v_index;
				}
			else {
				contenttype = '/articles';
				callback = '&callback=handleArticleList';
				count = "&count=" + count;
				startIndex = '?startIndex=' + a_index;
				}
			
			loading_state = true; //set loading state to TRUE !Important! So multiple load functions arent called in rappid succsesion 
			
			var script_src = url + contenttype + startIndex + count + callback;		
			var script_callback = callback;
			loadNewScript(script_src); //Loads new data from URL
			
		}
		
		/*loadNewScript - Loads a new script with error handling. New script returns data using JSONP method rather than making a HTTPrequest.
		* Upon loading the new script the callback function will be executed.
		*@param {s_src} The webapi URL used load script
		*/
		function loadNewScript(s_src){
			var new_s = document.createElement("script");
			
			new_s.src = s_src;
			new_s.onerror = function() {
				//if error occurs alert users
				if (s_src.includes('ArticleList')){articleList.removeChild(articleList.lastElementChild);}
				else {videoList.removeChild(videoList.lastElementChild);}
				alert("cannot load script");
				loading_state = false;
			}
			
			document.body.appendChild(new_s);
		}
		
		/* 	handleVideoList - callback that handles new data to be parsed and added to videoList useing addNewEntriesFromData function
		*	@param {obj} JSON object provided by webapi
		*/
		function handleVideoList(obj) {
			addNewEntriesFromData(obj, videoList, 0);
		}
		
		/* 	handleVideoList - callback that handles new data to be parsed and added to articleList useing addNewEntriesFromData function
		*	@param {obj} JSON object provided by webapi
		*/
		function handleArticleList(obj) {
			addNewEntriesFromData(obj, articleList, 1);
		}
		
		/* 	addNewEntriesFromData - Upon loading new entries, this function handles the JSON object
		*	that has been returned by the webapi query. This function loops through the obj.data array
		* 	and creates a new list item to be added to the new entries array (either a_add_array or v_add_array). When 
		*	the loop ends, if the length of a_add_array/v_add_array is at least == to requiredSize then all 
		*	entries from a_add_array/v_add_array are appended to articleList or videoList. If the size of the new 
		*	entry array is not large enough the function recursivly calls loadMoreContentEvent.
		*
		*	@param {obj} JSON object provided by webapi
		*	@param {obj_list} The list that new entries will be added to (articleList or videoList)
		* 	@param (type) the of new entries to be added (0 = video and 1 = article)
		*/
		function addNewEntriesFromData(obj, obj_list, type){
			
			//If index > 300 reset index to 0
			if (type==0 && v_index > 300){v_index = 0;}
			if (type==1 && a_index > 300){a_index = 0;}
			
			var item_array = [];
			var i = (type == 0 ? v_index : a_index);
			var ilength = i;
			for (i; i < obj.data.length + ilength; i++){
				
				//Creats new HTML elements to be appended to videoList/articleList
				var li = document.createElement('li');
				var div = document.createElement('div');
				var div_content = document.createElement('div');
				var div_thumbnail = document.createElement('div');
				
				var img_topper = document.createElement('img');
				var img = document.createElement('img');
				var link = document.createElement('a');
				
				var index = document.createElement('h4');
				
				var title = document.createElement('p');			
				var time = document.createElement('h3');
				
				title.className = "title";
				
				if (type == 0){
					v_index += 1;
					t = obj.data[i - ilength].metadata.name;
					st = obj.data[i - ilength].metadata.description;
					duraction = obj.data[i - ilength].metadata.duration;
					link.href = obj.data[i - ilength].metadata.url;
					
					var index_string = "" + (vlistSize  + 1);
					if ((vlistSize + 1) < 10) {index_string = "0" + (vlistSize + 1);}
					}
					
				if (type == 1){
					a_index += 1;
					t = obj.data[i - ilength].metadata.headline;
					st = obj.data[i - ilength].metadata.subHeadline;
					
					slug = obj.data[i - ilength].metadata.slug;
					year = obj.data[i - ilength].metadata.publishDate.substring(0,4);
					month = obj.data[i - ilength].metadata.publishDate.substring(5,7);
					day = obj.data[i - ilength].metadata.publishDate.substring(8,10);
					
					link.href = "http://ign.com/articles/" + year + "/" + month + "/" + day + "/" + slug;
					
					if (st === null){st = ""};
					duraction = "";
					
					var index_string = "" + (alistSize  + 1);
					if ((alistSize + 1) < 10) {index_string = "0" + (alistSize + 1);}
					}
				
				//taghit - used to determine if the current items title or tags[] contains the required tags for new entries
				//If so taghit = true
				taghit = false;
				
				for (tag in tags){
					if (t.toLowerCase().includes(tags[tag])){
						taghit = true;
					}
				}
				
				itemtags = obj.data[i-ilength].tags;
				for (itms in itemtags){
					for (tag in tags){
						if (itemtags[itms].toLowerCase().includes(tags[tag])){
							taghit = true;
						}
					}	
				}
				
				//If taghit == true then add this entry to the new entry array (v_add_array or a_add_array)
				if (taghit){
					
					index.innerHTML = index_string;
					
					img_topper.src = createTopper(t);
					
					img.src = obj.data[i - ilength].thumbnails[2].url;
					img.width='150';
					
					title.innerHTML = t + "<br/> <span>" + st + "<span>";
					time.innerHTML = (duraction == "" ? "" : secondsToHms(duraction));
					
					li.className = "list-li";
					div.className = "sidediv";
					div_content.className = "item-content";
					div_thumbnail.className = "thumbnail-div";
					
					if (t.length > 36){t = t.substring(0, 36) + "...";}
					img_topper.src = createTopper(t);
					img_topper.className = 'img-topper';
					
					div_content.appendChild(index);
					div_content.appendChild(title);
					div_content.appendChild(time);
					
					//Create link to the article/video on IGN
					link.appendChild(img);
					link.appendChild(img_topper);
					div_thumbnail.appendChild(link);
					
					li.appendChild(div);
					li.appendChild(div_content);
					li.appendChild(div_thumbnail);

					if (type == 0){v_add_array[v_add_array.length] = li; vlistSize += 1}
					else {a_add_array[a_add_array.length] = li; alistSize += 1}
					
					item_array[item_array.length] = li;
					
				}
			}
		
			//Add onclick listener for each new list item.
			for (j in item_array){
				(function (j){
					item_array[j].onclick = function (){
					
						var hidden_array = document.querySelectorAll(".list-li-hidden");
						for (k in hidden_array){
							hidden_array[k].className = "list-li";
						}
					
						var viddiv = item_array[j].querySelector(".thumbnail-div");
						
						if (viddiv.style.height != "300px"){
							item_array[j].className = "list-li-hidden";
							}
						else {
							item_array[j].className = "list-li";
							sidediv.style.height = item_array[j].style.height;
							}
							
						}

				})(j);
			}
			
			/*	In the code below, the function checks if the required number of new entries has been added to the new entries array.
			*	IF so then deletes the progrss spinner and the obj_list (articleList/videoList) appends all the new entry elements. 
			*	ELSE load more entries.
			*/
			if (type == 0){
				if (v_add_array.length < vrequiredSize){loadMoreContentEvent(20, 0);}
				else {
					videoList.removeChild(videoList.lastElementChild);
					for (listitems in v_add_array){obj_list.appendChild(v_add_array[listitems]);}
					loading_state = false;
				}
			}
			
			else if (type == 1){
				if (a_add_array.length < arequiredSize){loadMoreContentEvent(20, 1);}
				else {
					articleList.removeChild(articleList.lastElementChild);
					for (listitems in a_add_array){obj_list.appendChild(a_add_array[listitems]);}
					loading_state = false;
				}
			}
			

		}
		
		//
		//=======================================================
		// HELPFULL FUNCTIONS - These functions provide extra functionality
		//=======================================================
		//
		
		//Draws title and 'GO TO IGN' on top of article/video image thumbnails.
		function createTopper(title){
			ctx.clearRect(0,0,352,626);

			canvas.height = 352;
			canvas.width = 626;
			w = 626;
			h = 352;
			
			var ignbox = {};
			ignbox.x = Math.round(w * (4.6/9));
			ignbox.y = Math.round(h * (1/9));
			ignbox.w = Math.round(w * (4/9));
			ignbox.h = Math.round(h * (1.65/9));
			
			ctx.fillStyle = '#c42323';
			ctx.fillRect(ignbox.x, ignbox.y, ignbox.w, ignbox.h);
			
			arc_radius = 5;
			ctx.clearRect(ignbox.x - arc_radius, ignbox.y - arc_radius, arc_radius * 2, arc_radius * 2);
			ctx.clearRect(ignbox.x - arc_radius, ignbox.y + ignbox.h - arc_radius, arc_radius * 2, arc_radius * 2);
			ctx.clearRect(ignbox.x + ignbox.w - arc_radius, ignbox.y + ignbox.h - arc_radius, arc_radius * 2, arc_radius * 2);
			ctx.clearRect(ignbox.x + ignbox.w - arc_radius, ignbox.y - arc_radius, arc_radius * 2, arc_radius * 2);
						
			ctx.beginPath();
			ctx.arc(ignbox.x + arc_radius, ignbox.y + arc_radius, arc_radius, 0, 2 * Math.PI);
			ctx.arc(ignbox.x + arc_radius, ignbox.y + ignbox.h - arc_radius, arc_radius, 0, 2 * Math.PI);
			ctx.arc(ignbox.x + ignbox.w - arc_radius, ignbox.y + ignbox.h - arc_radius, arc_radius, 0, 2 * Math.PI);
			ctx.arc(ignbox.x + ignbox.w - arc_radius, ignbox.y + arc_radius, arc_radius, 0, 2 * Math.PI);
			ctx.fill();
			
			ctx.fillStyle = 'white';
			ctx.font= " normal small-caps bolder 38px arial";
			ctx.fillText( 'GO TO IGN', Math.round(w * (5.65/10)), Math.round(h * (2.4/10)));
			
			ctx.font= " 30px arial";
			ctx.lineWidth = 4;
			ctx.strokeStyle = "black";
			ctx.strokeText(title, Math.round(w * (0.2/9)), Math.round(h * (8/9)));
			ctx.fillText(title, Math.round(w * (0.2/9)), Math.round(h * (8/9)));
			return canvas.toDataURL("image/png");				 
		
		}
		
		//Creats a cirle progress spinner element
		function createLoader(){
			var li_loader = document.createElement('li');
			var div_loader = document.createElement('div');
			li_loader.className = "loader-container";
			div_loader.className = "loader";
			li_loader.appendChild(div_loader);
			return li_loader;
		}
		
		//secondsToHms function provided by source: http://stackoverflow.com/questions/5539028/converting-seconds-into-hhmmss
		function secondsToHms(d) {
			d = Number(d);
			var h = Math.floor(d / 3600);
			var m = Math.floor(d % 3600 / 60);
			var s = Math.floor(d % 3600 % 60);
			return ((h > 0 ? h + ":" + (m < 10 ? "0" : "") : "") + m + ":" + (s < 10 ? "0" : "") + s); 
		}
		
		//ONLOAD FUNCTION
		window.onload = function(){
			loadMoreContentEvent(10, 0);
			loadMoreContentEvent(10, 1);
		}
		