#include "Server.h"
#include "Database.h"

Database db;

Server::Server(const http::uri& url) : m_listener(http_listener(url))
{
	db = Database::Database();
	uri_builder uri(url);
	uri.append_path(U("/"));
	
	m_listener.support(methods::OPTIONS,
		std::tr1::bind(&Server::handle_options,
		this,
		std::tr1::placeholders::_1));
	m_listener.support(methods::GET,
		std::tr1::bind(&Server::handle_get,
		this,
		std::tr1::placeholders::_1));
	m_listener.support(methods::POST,
		std::tr1::bind(&Server::handle_post,
		this,
		std::tr1::placeholders::_1));
/*	m_listener.support(methods::PUT,
		std::tr1::bind(&Server::handle_put,
		this,
		std::tr1::placeholders::_1));
	m_listener.support(methods::DEL,
		std::tr1::bind(&Server::handle_delete,
		this,
		std::tr1::placeholders::_1));*/
}

void Server::handle_get(http_request message)
{
	printf("Handling GET request... ");
	printf("PATH : %S \n", message.absolute_uri().path().c_str());
	if (message.absolute_uri().path() == U("/supervisor"))
	{
		printf("Handling supervisor request\n");
		//message.reply(status_codes::OK, U("Hello, Supervisor ! - From the AppServer "));
		db.GetStats(message);
	}
	else if (message.absolute_uri().path() == U("/client"))
	{
		printf("Handling default client request\n");
		message.reply(status_codes::OK, U("Hello, Client ! - From the AppServer "));
	}
	else if (message.absolute_uri().path() == U("/client/init"))
	{
		printf("Handling init client request\n");
		message.reply(status_codes::OK, U("Hello, Client ! - From the AppServer "));
	}
	else if (message.absolute_uri().path() == U("/favicon.ico"))
	{
		printf("Handling favicon request\n");
		message.reply(status_codes::NotFound, U("No favicon, please refer to the API documentation."));
	}
	else
	{
		printf("Unhandled path for the GET request\n");
		message.reply(status_codes::BadRequest, U("Unhandled GET request, please refer to the API documentation."));
	}
};

void Server::handle_post(http_request message)
{
	printf("Handling POST request... ");
	printf("PATH : %S \n", message.absolute_uri().path().c_str());
	if (message.absolute_uri().path() == U("/client/shot"))
	{
		printf("Handling client/shot request\n");
		//message.reply(status_codes::OK, U("Hello, Supervisor ! - From the AppServer "));
		message.extract_json().then([=](pplx::task<json::value> jsonFromClient)
		{	
			try
			{
				const json::value& jsonValue = jsonFromClient.get();
				// Perform actions here to process the JSON value...
				Point shotPosition = Point::Point(jsonValue.at(U("ClubLat")).as_double(), jsonValue.at(U("ClubLng")).as_double(), 0);
				Point tokenPosition = Point::Point(41.12345, 1.65432, 0);
				//Point newPosition = tokenPosition.newPosition();
				Point newPosition = Point::Point(tokenPosition.x() + (tokenPosition.x() - shotPosition.x()), tokenPosition.y()+(tokenPosition.x() - shotPosition.x()), 0);

				json::value jsonToClient;
				jsonToClient[L"newLocation"][L"lat"] = json::value::number(newPosition.x());
				jsonToClient[L"newLocation"][L"lon"] = json::value::number(newPosition.y());

				http_response serverresponse = http_response();
				serverresponse.set_status_code(status_codes::OK);
				serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*"));
				serverresponse.headers().add(U("Content-Type"), U("application/json"));
				utility::string_t contenType = U("application/json");
				serverresponse.set_body(jsonToClient);
				message.reply(serverresponse);

			}
			catch (const http_exception& e)
			{
				// Print error.
				printf("Error parsing JSON in client/shot POST handler : %s", e.what());
			}
		});

	}
	else if (message.absolute_uri().path() == U("/client/shottest"))
	{
		json::value jsonToClient;
		jsonToClient[L"newLocation"][L"lat"] = json::value::number(43.45674);
		jsonToClient[L"newLocation"][L"lon"] = json::value::number(2.01233);

		http_response serverresponse = http_response();
		serverresponse.set_status_code(status_codes::OK);
		serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*"));
		serverresponse.headers().add(U("Content-Type"), U("application/json"));
		utility::string_t contenType = U("application/json");
		serverresponse.set_body(jsonToClient);
		message.reply(serverresponse);

	}
	else
	{
		printf("Unhandled path for the POST request\n");
		message.reply(status_codes::BadRequest, U("Unhandled POST request, please refer to the API documentation."));
	}
};

void Server::handle_options(http_request message)
{
	printf("Handling OPTIONS request...\n");
	message.reply(status_codes::BadRequest, U("Unhandled OPTIONS request, please refer to the API documentation."));
};