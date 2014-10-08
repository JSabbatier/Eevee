#include "Server.h"
#include "Database.h"
// protocol : // server [: port] / path ? query # fragment 
Database POIsDb;
Users clientsDb;
int power;

Server::Server(const http::uri& url) : m_listener(http_listener(url))
{
	POIsDb = Database::Database();
	clientsDb = Users::Users();
	power = 2;
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

// Handle all the GET requests
void Server::handle_get(http_request message)
{
	printf("Handling GET request... ");
	printf("PATH : %S \n", message.absolute_uri().path().c_str());
	if (message.absolute_uri().path() == U("/supervisor/getstats"))
	{
		printf("Handling supervisor request\n");
		//message.reply(status_codes::OK, U("Hello, Supervisor ! - From the AppServer "));

		http_response serverresponse = http_response();
		json::value jsonToClient;
		serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*")); // Necessary for cross-domain requests
		serverresponse.headers().add(U("Content-Type"), U("application/json"));
		serverresponse.set_status_code(status_codes::Created);
		serverresponse.set_body(clientsDb.getStats());
		message.reply(serverresponse);
	}
	else if (message.absolute_uri().path() == U("/client/getpois"))
	{
		// Return the POIs near the client
		printf("Handling client/getpois request\n");

		http_response serverresponse = http_response();
		json::value jsonToClient;
		User* mobClient;
		serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*")); // Necessary for cross-domain requests
		serverresponse.headers().add(U("Content-Type"), U("application/json"));

		// Getting the token parameter from the URI query
		auto clientQuery = web::uri::split_query(message.absolute_uri().query());
		if (!clientQuery.empty())
		{
			if (clientQuery.cbegin()->first == L"token")
			{
				utility::string_t clientToken = clientQuery.cbegin()->second;

			}
			else
			{
				jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
				jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect query parameter. Please refer to the API documentation.");
				serverresponse.set_status_code(status_codes::BadRequest);
			}
		}
		else
		{
			jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
			jsonToClient[L"error"][L"reason"] = json::value::string(L"No query parameter. Please refer to the API documentation.");
			serverresponse.set_status_code(status_codes::BadRequest);
		}
		serverresponse.set_body(jsonToClient);
		message.reply(serverresponse);
		
	}
	else if (message.absolute_uri().path() == U("/client"))
	{
		printf("Handling default client request\n");
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

// Handle all the POST requests
void Server::handle_post(http_request message)
{
	printf("Handling POST request... ");
	printf("PATH : %S \n", message.absolute_uri().path().c_str());

	if (message.absolute_uri().path() == U("/client/init"))
	{
		// Create a new client un the clients map by generating a new token with the GPS coordinates sent by the client, sending the new token to the client.
		printf("Handling client/init request\n");
		message.extract_json().then([=](pplx::task<json::value> jsonFromClient)
		{

			http_response serverresponse = http_response();
			json::value jsonToClient;
			User* mobClient;
			serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*")); // Necessary for cross-domain requests
			serverresponse.headers().add(U("Content-Type"), U("application/json"));
			try
			{
				const json::value& jsonValue = jsonFromClient.get();
				// Perform actions here to process the JSON value...
				if (jsonValue.has_field(L"BallLat") && jsonValue.has_field(L"BallLng"))
				{
					printf("Received JSON from the client BallLat : %f, BallLng : %f\n", jsonValue.at(U("BallLat")).as_double(), jsonValue.at(U("BallLng")).as_double());
					Point clientBallPosition = Point::Point(jsonValue.at(U("BallLat")).as_double(), jsonValue.at(U("BallLng")).as_double(), 0);
					mobClient = clientsDb.createClient(clientBallPosition);
					//jsonToClient[L"token"] = json::value::string(mobClient->getToken());
					jsonToClient[L"token"] = json::value::string(L"thomas_sait_pas_faire_des_requetes"); // For test purpose only
					printf(" * New token created\n");
					serverresponse.set_status_code(status_codes::Created);
				}
				else
				{
					jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
					jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect JSON parameters");
					serverresponse.set_status_code(status_codes::BadRequest);
				}

			}
			catch (const http_exception& e)
			{
				// Print error.
				printf("Error parsing JSON in client/shot POST handler : %s", e.what());
				jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
				jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect JSON parameters (exeption)");
				serverresponse.set_status_code(status_codes::BadRequest);
			}
			serverresponse.set_body(jsonToClient);
			message.reply(serverresponse);
		});
	}
	else if (message.absolute_uri().path() == U("/client/shot"))
	{
		// Moving the client ball depending on the "shot" he did on the map + verification of the initial ball coordinates (anti-cheat)
		printf("Handling client/shot request\n");
		//message.reply(status_codes::OK, U("Hello, Supervisor ! - From the AppServer "));
		message.extract_json().then([=](pplx::task<json::value> jsonFromClient)
		{	

			http_response serverresponse = http_response();
			json::value jsonToClient; 

			User* mobClient;
			// Default response formating, if none of the following functions are processed
			serverresponse.headers().add(U("Access-Control-Allow-Origin"), U("*"));
			serverresponse.headers().add(U("Content-Type"), U("application/json"));
			serverresponse.set_status_code(status_codes::NotModified);
			try
			{
				const json::value& jsonValue = jsonFromClient.get();
				if (jsonValue.has_field(L"BallLat") && jsonValue.has_field(L"BallLng") && jsonValue.has_field(L"ClubLat") && jsonValue.has_field(L"ClubLng") && jsonValue.has_field(L"Token"))
				{
					// Perform actions here to process the JSON value...
					printf("Received JSON from the client ClubLat : %f, ClubLon : %f\n", jsonValue.at(U("ClubLat")).as_double(), jsonValue.at(U("ClubLng")).as_double());
					Point clientShotPosition = Point::Point(jsonValue.at(U("ClubLat")).as_double(), jsonValue.at(U("ClubLng")).as_double(), 0);
					Point clientBallPosition = Point::Point(jsonValue.at(U("BallLat")).as_double(), jsonValue.at(U("BallLng")).as_double(), 0);
					mobClient = clientsDb.getClient(jsonValue.at(U("Token")).as_string());
					if (mobClient != nullptr)
					{
						// Checking if the ball position sent by the client is the same as the token position in the server DB
						if (!mobClient->isAt(clientBallPosition))
						{
							jsonToClient[L"error"][L"code"] = json::value::number(status_codes::Conflict);
							jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect ball position, please reset your position. (Don't cheat please)");
							serverresponse.set_status_code(status_codes::Conflict);
						}
						else
						{
							Point tokenPosition = mobClient->getLstPositionKnown();
							// Calculating the new position of the ball
							Point newPosition = Point::Point(power * (tokenPosition.x() + (tokenPosition.x() - clientShotPosition.x())), power * (tokenPosition.y() + (tokenPosition.x() - clientShotPosition.x())), 0);

							jsonToClient[L"coordinates"][L"lat"] = json::value::number(newPosition.x());
							jsonToClient[L"coordinates"][L"lon"] = json::value::number(newPosition.y());
							jsonToClient[L"distance"] = json::value::number(mobClient->getDistance());
							serverresponse.set_status_code(status_codes::OK);
						}
					}
					else
					{
						jsonToClient[L"error"][L"code"] = json::value::number(status_codes::NoContent);
						jsonToClient[L"error"][L"reason"] = json::value::string(L"This token is not registred, please reset your position.");
					}
				}
				else
				{
					jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
					jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect JSON parameters");
					serverresponse.set_status_code(status_codes::BadRequest);
				}

			}
			catch (const http_exception& e)
			{
				// Print error.
				printf("Error parsing JSON in client/shot POST handler : %s", e.what());
				jsonToClient[L"error"][L"code"] = json::value::number(status_codes::BadRequest);
				jsonToClient[L"error"][L"reason"] = json::value::string(L"Incorrect JSON parameters (exeption)");
				serverresponse.set_status_code(status_codes::BadRequest);
			}
			serverresponse.set_body(jsonToClient);
			message.reply(serverresponse);
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