var host='www.frinser.com';
var user = '';
var query = '';
var links = [];
var stat = 2;
var used = 0;
var totalFriends = [];
var upVoteFlag = 0;
var twitterName = '';
var storeLinks = [];
var siteUrl = '';
var receiveMessage = "";
var fbFriends = '';
var twitterFriends = '';
var gapiName = '';
var gapiFriends = '';
var _wow = 'wow';
var host = 'www.frinser.com';
var isFbAvailable;
var isGapiAvailable;
var isTwAvailable;
var isFbApplied;
var isGapiApplied;
var isTwApplied;
var isNonFriendApplied;
var currentLinks='';
var GAPI_CONSTANT_FRIENDS = 'ExGapiFriends';
var FB_CONSTANT_FRIENDS = 'ExFbFriends';
var TW_CONSTANT_FRIENDS = 'ExTwFriends';
var FB_CONSTANT_NAME = 'ExFbName';
var TW_CONSTANT_NAME = 'ExTwName';
var GAPI_CONSTANT_NAME = 'ExGapiName';
var SL='sl'
var GAPI = 'Gapi';
var FB = 'Fb';
var TW = 'Tw';
var upVoteQuery = '';
var isGapiLocalStoreAvailable = false;
var isFbLocalStoreAvailable = false;
var isTwLocalStoreAvailable = false;
var isGapiSelfStoreAvailable = false;
var isFbSelfStoreAvailable = false;
var isTwSelfStoreAvailable = false;

var foo = document.getElementById('search');
var isScriptCalled = false;
var fbUserName = '';
var gapiUserName = '';
var sessionMap;
var CONSTANT_STOREMAP = 'storeMap';
var twUserName = '';
var isIdentityDivCreated = false;
var isSearchElementCreated = false;
var mutationObserverTimer;
var gotNonFriendLinkUpVotes = false;
var _all = "all";
var uuid;
//addiFrames()
function getTimeStamp()
{
	var now = new Date();
	var utc_now = new Date(now.getUTCFullYear(), now.getUTCMonth(), now.getUTCDate(),  now.getUTCHours(), now.getUTCMinutes(), now.getUTCSeconds(), now.getUTCMilliseconds());
	uuid=utc_now.getTime()%10000000000;
}
var allowStorage=false;
function addiFrames()
	{ // checkLoginState();
	var x = document.getElementsByTagName("body");
		if ((document.getElementById("frame") == undefined)
				|| (document.getElementById("frame") == null)) {
			/* do nothing as iframe does not exist yet */
		} else {
			x[0].removeChild(document.getElementById("frame"));
		}
		var s = document.getElementsByTagName('body')[0];
		var g = document.createElement('iframe');
		g.src = "https://www.frinser.com/public/popup.html";
		g.id = 'frame';
		g.height = '0px';
		g.width = '0px';
		g.style.display = 'none';
		document.body.appendChild(g);
		document.getElementById('frame').hidden = true;
		if ((document.getElementById("frame2") == undefined)
				|| (document.getElementById("frame2") == null)) {
			/* do nothing as iframe does not exist yet */
		} else {
			x[0].removeChild(document.getElementById("frame2"));
		}
		var g = document.createElement('iframe');
		g.src = "https://" + host + "/twitteroauth-master/index.php";
		g.height = '0px';
		g.width = '0px';
		g.id = 'frame2';
		g.style.display = 'none';
		g.target = '_top';
		document.body.appendChild(g);
		document.getElementById('frame2').hidden = true;
		if ((document.getElementById("frame3") == undefined)
				|| (document.getElementById("frame3") == null)) {
			/* do nothing as iframe does not exist yet */
		} else {
			x[0].removeChild(document.getElementById("frame3"));
		}
		var g = document.createElement('iframe');
		g.src = "https://www.frinser.com/public/gapi.html";
		g.height = '0px';
		g.width = '0px';
		g.id = 'frame3';
		g.style.display = 'none';
		g.target = '_top';
		document.body.appendChild(g);
		document.getElementById('frame3').hidden = true;
		}
		window.addEventListener('message', function(e) {
			var str = e.data + "";
			if (str.indexOf('twitt-name') > -1) {
				isTwAvailable = true;
				twitterName = str.substring(str.indexOf('twitt-name') + 10,
						str.length);
				var name = twitterName;
				if(allowStorage)
					{
					var json='{"uuid":"'+uuid+'","user":"'+twitterName+'"}';
//					document.getElementById('twSent').innerHTML='Twitter Sent';
					sendStoreUser(json,'twSent');
					}
			}
			if (str.indexOf('gapi-name') > -1) {
				isGapiAvailable = true;
				gapiName = str.substring(str.indexOf('gapi-name') + 9, str.length);
				if(allowStorage)
				{
				var json='{"uuid":"'+uuid+'","user":"'+gapiName+'"}';
//				document.getElementById('gmSent').innerHTML='GMail Sent';
				sendStoreUser(json,'gmSent');
				}
			}
			if (str.indexOf('fbName') > -1) {
				isFbAvailable = true;
				fbUserName = str.substring(str.indexOf('fbName') + 6, str.length);

				
			}
			if (str.indexOf('status') > -1) {
				console.log(str);
				if (str.search(-1) > -1) {
					stat = -1
					console.log('Please log in');
				}
				if (str.search(0) > -1) {
					stat = 0;
					console.log('Please allow the app');
				}

			}
			if (str.indexOf('gapi-frnds') > -1) {
				gapiFriends = str.substring(str.indexOf('gapi-frnds^') + 11,
						str.length);
			} else if (str.indexOf('twitt-frnds^') > -1) {
				twitterFriends = str.substring(str.indexOf('twitt-frnds^') + 12,
						str.length);
			} else if (str.search('friends') > -1) {
				fbFriends = e.data;
			}
			if (str.search('user') > -1) {
				var userJSON = str.split('=');
				user = userJSON[1];
				if(allowStorage)
				{
				var json='{"uuid":"'+uuid+'","user":"'+user+'"}';
//				document.getElementById('fbSent').innerHTML='Facebook Sent';
				sendStoreUser(json,'fbSent');
				}
			}

		});
function  sendStoreUser(payload,id)
{

	var url = 'http://localhost:8080/extension/rest/extension/storeUser';
	var req = createRequest(); // defined above
	// Create the callback:
	req.onreadystatechange = function() {
		if (req.readyState != 4)
			return; // Not there yet
		if (req.status != 201) {
			return;
		}
		var resp = req.responseText;
		if(resp.indexOf('Merge Conflict')!=-1)
			{
//			document.getElementById(id).style.color='Red';
//			if(id=='gmSent')
//				document.getElementById(id).innerHTML='Gmail Merge Conflict';
//			if(id=='twSent')
//				document.getElementById(id).innerHTML='Twitter Merge Conflict';
//			if(id=='fbSent')
//				document.getElementById(id).innerHTML='Facebook Merge Conflict';
			}
		if(resp.indexOf('Data saved')==-1)
			return;
//		document.getElementById(id).style.color='green';
//		if(id=='gmSent')
//			document.getElementById(id).innerHTML='Gmail Recieved';
//		if(id=='twSent')
//			document.getElementById(id).innerHTML='Twitter Recieved';
//		if(id=='fbSent')
//			document.getElementById(id).innerHTML='Facebook Recieved';
		
			 
		
	}
	req.open("POST", url, true);
	req.setRequestHeader("Content-Type", "application/json");
	req.send(payload);

}
function createRequest() {
	var result = null;
	if (window.XMLHttpRequest) {
		// FireFox, Safari, etc.
		result = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		// MSIE
		result = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
	}
	
	return result;
}

function storeUser()
{
	getTimeStamp();
	allowStorage=true;
	addiFrames();
//	document.getElementById('gmSent').style.color='red';
//	document.getElementById('twSent').style.color='red';
//	document.getElementById('fbSent').style.color='red';
//	document.getElementById('gmSent').innerHTML='Gmail Not Sent';
//	document.getElementById('twSent').innerHTML='Twitter Not Sent';
//	document.getElementById('fbSent').innerHTML='Facebook Not Sent';
	var jsonStr='{';	
	if(twitterName!='')
	{
		jsonStr += '\"tw\":{\"user\":\"'+twitterName+'\"},';
	}
	else
	{
	jsonStr += '\"tw\":{},';
	}
	if(user!='')
	{
		jsonStr += '\"fb\":{\"user\":\"'+user+'\"},';
	}
	else
	{
	jsonStr += '\"fb\":{},';
	}
	if(gapiName!='')
	{
		jsonStr += '\"gm\":{\"user\":\"'+gapiName+'\"}';
	}
	else
	{
	jsonStr += '\"gm\":{}';
	}
	jsonStr += '}';
	console.log(jsonStr);
}