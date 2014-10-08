#include "Database.h"
#include "md5.h"

#include <iostream>
#include <cstring>
#include <ctime>
#include <codecvt>

std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter; // Converter std::string to wchar_t (utility::string_t)

Database::Database()
{	
}

void Database::SendPois(http_request message, Point location)
{
	printf(" - Entering in the SendPois function\n");

	//auto jsondb = web::json::value::parse(U("{'test':'126345'}"));

	json::value jsonToDB;
	auto clientQuery = web::uri::split_query(message.absolute_uri().query());
	utility::string_t clientToken = clientQuery.cbegin()->second;
	//Point location = Point(clientsDb->getClient(clientToken)->getLstPositionKnown());
	jsonToDB[L"coordinates"][L"lat"] = json::value::number(location.x());
	jsonToDB[L"coordinates"][L"lon"] = json::value::number(location.y());
	jsonToDB[L"options"][L"nb"] = json::value::number(1);


	int a = 10;
	// Open stream to output file.
	//pplx::task<http_response> resp = client.request(methods::GET, U("/getstats.php"), U(""))
	// Create http_client to send the request.
	http_client client(U("http://perso.imerir.com/mdacosta/projetEevee/"));
	pplx::task<void> requestTask = client.request(methods::POST, U("getpois.php"), jsonToDB)
		// Handle response headers arriving.
		.then([=](http_response response)->pplx::task<json::value>
	{
		printf("	Status code : %d\n", response.status_code());
		if (response.status_code() == status_codes::OK)
		{
			//printf("response.extract_string() : %S\n", response.extract_string().get().c_str()); // Raw response body from the db server
			return response.extract_json();
		}
		return pplx::task_from_result(json::value());
	})
		.then([=](pplx::task<json::value> previousTask)
	{
		try
		{
			printf("	Parsing DB JSON \n");
			const json::value& jsonValue = previousTask.get();
			// Perform actions here to process the JSON value...
			if (jsonValue.has_field(U("pois")))
			{
				http_response serverresponse = http_response();
				serverresponse.set_status_code(status_codes::OK);
				serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*"));
				serverresponse.headers().add(U("Content-Type"), U("application/json"));
				utility::string_t contenType = U("application/json");
				serverresponse.set_body(jsonValue);
				//message.reply(status_codes::OK, jsonValue.serialize(), U("application/json")); // Can't add many headers with this form
				message.reply(serverresponse);
			}
			else
			{
				message.reply(status_codes::ExpectationFailed, U("Incorrect JSON parameters, expected a \"pois\" array."));
			}
		}
		catch (const http_exception& e)
		{
			// Print error.
			printf("	Exeption getting JSON : %s\n", e.what());
		}
	});
}

Database::~Database()
{
}
