#include "Database.h"
#include "md5.h"

#include <iostream>
#include <cstring>
#include <ctime>
#include <codecvt>

std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter; // Converter from std::string to wchar_t

Database::Database()
{	
}

void Database::GetStats(http_request message)
{
	printf(" - Entering in the GetStats function\n");

	//auto jsondb = web::json::value::parse(U("{'test':'126345'}"));

	json::value jsonToDB;
	jsonToDB[L"coordinates"][L"lat"] = json::value::number(11);
	jsonToDB[L"coordinates"][L"lon"] = json::value::number(4);
	jsonToDB[L"options"][L"nb"] = json::value::number(1);


	int a = 10;
	// Open stream to output file.
	//pplx::task<http_response> resp = client.request(methods::GET, U("/getstats.php"), U(""))
		printf("Entering in the GetStats task\n");
		// Create http_client to send the request.
		http_client client(U("http://perso.imerir.com/mdacosta/projetEevee/"));
		pplx::task<void> requestTask = client.request(methods::POST, U("/testgetpois.php"), jsonToDB)
		// Handle response headers arriving.
		.then([=](http_response response)->pplx::task<json::value>
		{
			printf("Status code : %d\n", response.status_code());
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
				printf("Parsing JSON \n");
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
				printf("Error getting JSON : %s\n", e.what());
			}
		});
	/*	int retCode;
		printf("Received response status code:%u\n", response.status_code());
		switch (response.status_code())
		{
		case 200:
			printf("Receiving stats...\n");
			retCode = 1;
			serverresponse.set_status_code(status_codes::OK);
			serverresponse.set_body(jsondb.serialize());*/
			//serverresponse.set_body("Et la ?");
			//serverresponse.set_body(jsondb);
/*			response.extract_json()
				.then([=](web::json::value jsonValue)
			{
				if (true)//jsonValue.has_field(U("users")))
				{
					http_response serverresponse = http_response();
					serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*"));
					serverresponse.headers().add(U("Content-Type"), U("application/json"));
					utility::string_t contenType = U("application/json");
					serverresponse.set_body(jsonValue);
					//message.reply(status_codes::OK, jsonValue.serialize(), U("application/json"));
					message.reply(serverresponse);
				}

			});
			break;
		case 404:
			printf("Not found\n");
			retCode = 0;
			break;
		case 405:
			printf("The server expects GET method\n");
			retCode = 0;
			break;
		default:
			printf("Not handled response code : %u\n", response.status_code());
			retCode = 0;

		}*/
		// Write response body into the fil
		//return response.body().read_to_end(fileStream->streambuf());

		// Close the file stream.
		/*.then([=](size_t)
	{
		return fileStream->close();
	});*/

	// Wait for all the outstanding I/O to complete and handle any exceptions
/*	try
	{
		requestTask.wait();
	}
	catch (const std::exception &e)
	{
		printf("Error exception:%s\n", e.what());
	}*/

	return;
}

// Generate an unique token using lat, lon and current timestamp
std::string Database::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}

int Database::RegisterToken(Point coordinates)
{
	PutToken(GenerateToken(coordinates));
	return 0;
}

int Database::PutToken(std::string token)
{
	auto fileStream = std::make_shared<ostream>();

	// Open stream to output file.
	pplx::task<void> requestTask = fstream::open_ostream(U("results.html")).then([=](ostream outFile)
	{
		*fileStream = outFile;

		// Create http_client to send the request.
		http_client client(U("http://perso.imerir.com/mdacosta/projetEevee/"));

		// Build request URI and start the request.
		//uri_builder builder(U("test.php"));
		//json::value bool_value = json::value("testetete");
		//builder.append_query(U("tk"), converter.from_bytes(token));
		//const utility::string_t url = U("/test.php");
		//const utility::string_t data = U("teeest");
		return client.request(methods::PUT, U("/newtoken.php"), converter.from_bytes(token));
	})

		// Handle response headers arriving.
		.then([=](http_response response)
	{
		int retCode;
		printf("Received response status code:%u\n", response.status_code());
		switch (response.status_code())
		{
		case 200:
			printf("Simple 'ok'.\n");
			retCode = 1;
			break;
		case 201 :
			printf("New token created\n");
			retCode = 1;
			break;
		case 204:
			printf("Token already exists, retrying\n");
			retCode = 0;
			break;
		case 404:
			printf("Not found\n");
			retCode = 0;
			break;
		case 405:
			printf("The server expects PUT method\n");
			retCode = 0;
			break;
		default:
			printf("Not handled response code : %u\n", response.status_code());
			retCode = 0;

		}
		// Write response body into the file.
		return response.body().read_to_end(fileStream->streambuf());
	})

		// Close the file stream.
		.then([=](size_t)
	{
		return fileStream->close();
	});

	// Wait for all the outstanding I/O to complete and handle any exceptions
	try
	{
		requestTask.wait();
	}
	catch (const std::exception &e)
	{
		printf("Error exception:%s\n", e.what());
	}
	return 0;
}


Database::~Database()
{
}
