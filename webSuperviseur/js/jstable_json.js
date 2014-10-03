function loadingBody(json, mydiv){
	//json ="{'token':	[{ 'rank': '1','name': 'pierre','score': '205','timestamp': '123987234' },{ 'rank': '2','name': 'paul','score': '223','timestamp': '123987234' },{ 'rank': '3','name': 'jacques','score': '344','timestamp':'123987234' }]}";
	
	
	/*[{ 'rank': '1','name': 'pierre','score': '205','timestamp': '123987234' },{ 'rank': '2','name': 'paul','score': '223','timestamp': '123987234' },{ 'rank': '3','name': 'jacques','score': '344','timestamp':'123987234' }]*/
	//document.getElementById('myjson').innerHTML=json; // Affiche le résultat de la requete
	
	var jsondoc = eval('(' + json + ')');

	if(jsondoc.user.length > 0){
		displayData(jsondoc, mydiv);
	}
	else{
		document.getElementById(mydiv).innerHTML='No records found!';
	}
}

function createTableRowContent(rowObject, data, cellType){
	var rowContent = document.createElement(cellType);
	var cell = document.createTextNode(data);
	rowContent.appendChild(cell);
	rowObject.appendChild(rowContent);
}

function createTableData(rowObject, data){
	createTableRowContent(rowObject, data, 'td');
}

function createTableHeader(rowObject, data){
	createTableRowContent(rowObject, data, 'th');
}

function displayData(jsonString, mydiv){
	
	var table = document.createElement('table');
	table.border = "1";
	
	var thead = document.createElement('thead');
	table.appendChild(thead);
	
	var row = document.createElement('tr');
	if (mydiv == 'mytable') // Pour afficher la table des scores
	{
		createTableHeader(row, 'Token');
		createTableHeader(row, 'Last Known Location');
		createTableHeader(row, 'Travelled Distance');
		createTableHeader(row, 'Rank');
	}
	else // Pour afficher le résultat de la tentative
	{
		createTableHeader(row, 'Rang');
		createTableHeader(row, 'Nom');
		createTableHeader(row, 'Date');
	}
	
	thead.appendChild(row);
	
	var tbody = document.createElement('tbody');
	table.appendChild(tbody);
	
	for(i=0; i<jsonString.user.length; i++)
	{
		var row = document.createElement('tr');

		if (mydiv == 'mytable') // Pour afficher la table des scores
		{
			createTableData(row, jsonString.user[i].token);
			createTableData(row, jsonString.user[i].lastKnownLocation);
			createTableData(row, jsonString.user[i].travelledDistance);
			createTableData(row, jsonString.user[i].rank);
		}
		else
		{
			createTableData(row, jsonString.user[i].rank);
			createTableData(row, jsonString.user[i].score);
			createTableData(row, jsonString.user[i].timestamp);
		}
			tbody.appendChild(row);
	}

	document.getElementById(mydiv).innerHTML = '';
	document.getElementById(mydiv).appendChild(table);
}
