// JavaScript Document


/*var jsonHard = '{ "users": { "u1": { "token": "0001", "statistics": { "lastKnownLocation": { "latitude": 42.25351, "longitude": -12.55567 }, "travelledDistance": "0250", "rank": 5 } },          "u2": { "token": "0002",  "statistics": { "lastKnownLocation": {  "latitude": 2.25351, "longitude": 39.55567 },"travelledDistance": "0000", "rank": 6}}}}';

 "{\"users\": \"1\":{\"token\":\"0001\",\"statistics\": {\"lastKnownLocation\": {\"latitude\": 42.25351,\"longitude\": -12.55567},\"travelledDistance\": \"0250\",\"rank\": 5}},\"2\":{\"token\": \"0002\",\"statistics\": {\"lastKnownLocation\": {\"latitude\": 2.25351,\"longitude\": 39.55567},\"travelledDistance\": \"0000\",\"rank\": 6}}}}";*/ 
    

/*obj = JSON.parse(jsonHard);
document.getElementById("jsonTest").innerHTML = obj.users.u1.token + " " +obj.users.u1.statistics.lastKnownLocation.latitude + " " + obj.users.u1.statistics.lastKnownLocation.longitude + " " + obj.users.u1.statistics.travelledDistance + " " + obj.users.u1.statistics.rank;


var data = eval( "(" + jsonHard + ")");  
  console.log(data); 
 $("#jsonTest").html('<table class="table, table table-hover"><tr.table-condensed><td>Token</td><td>Latitude</td><td>Longitude</td><td>Travelled Distance</td><td>Rank</td></tr></table>');
for(var key in data)
{  
	for(var tok in data[key])
	{
		var row = $('<tr>');
		
		row.append('<td>' + data[key][tok].token  + '</td>');
		row.append('<td>' + data[key][tok].statistics.lastKnownLocation.latitude + '</td>');
		row.append('<td>' + data[key][tok].statistics.lastKnownLocation.longitude + '</td>');
		row.append('<td>' + data[key][tok].statistics.travelledDistance + ' km </td>');
		row.append('<td>' + data[key][tok].statistics.rank + '</td>');
		  
		$("#jsonTest>table").append(row);
	}
}
 */ 
   /************************************************************ CONNEXION SERVOR ************************************************************/
 
function loadJSON()
{
	//http://perso.imerir.com/jloeve/jsontest.php http://172.31.1.150:34568/supervisor
   var data_file = "http://perso.imerir.com/jloeve/jsontest.php";
   var http_request = new XMLHttpRequest();
   try{
      // Opera 8.0+, Firefox, Chrome, Safari
      http_request = new XMLHttpRequest();
   }catch (e){
      // Internet Explorer Browsers
      try{
         http_request = new ActiveXObject("Msxml2.XMLHTTP");
      }catch (e) {
         try{
            http_request = new ActiveXObject("Microsoft.XMLHTTP");
         }catch (e){
            // Something went wrong
            alert("Your browser broke!");
            return false;
         }
      }
   }
   
   http_request.onreadystatechange  = function(){
      if (http_request.readyState == 4  )
      {	
	  	  
		var data = eval( "(" + http_request.responseText + ")");  
        $("#jsonTest").html('<table class="table, table table-hover"><tr.table-condensed><td>Token</td><td>Latitude</td><td>Longitude</td><td>Travelled Distance</td><td>Rank</td></tr></table>');
		
		for(var key in data)
		{  
			if(key == "users")
			{
					for(var tok in data[key])
					{
							var row = $('<tr>');
			
						row.append('<td>' + data[key][tok].token  + '</td>');
						row.append('<td>' + data[key][tok].statistics.lastKnownLocation.latitude + '</td>');
						row.append('<td>' + data[key][tok].statistics.lastKnownLocation.longitude + '</td>');
						row.append('<td>' + data[key][tok].statistics.travelledDistance + ' km </td>');
						row.append('<td>' + data[key][tok].statistics.rank + '</td>');
						  
						$("#jsonTest>table").append(row);
					}
			}
			else if(key == "connected_users")
			{
				$("#nbconnected").html('Number of players logged: ' + data[key]);
			}
		}
      }
   }
   http_request.open("GET", data_file, true);
   http_request.send();
}
