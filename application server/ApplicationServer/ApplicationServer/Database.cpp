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

web::json::value Database::GetStats()
{
	web::json::value jsondb = web::json::value::parse(U("{\"test\":\"126345\"}"));
	return (jsondb);
}

// Generate an unique token using lat, lon and current timestamp
std::string Database::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
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
