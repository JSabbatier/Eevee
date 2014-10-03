#include "Server.h"
#include "Database.h"

Server::Server(const http::uri& url) : m_listener(http_listener(url))
{
	printf("Request recieved\n");
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
/*	m_listener.support(methods::PUT,
		std::tr1::bind(&Server::handle_put,
		this,
		std::tr1::placeholders::_1));
	m_listener.support(methods::POST,
		std::tr1::bind(&Server::handle_post,
		this,
		std::tr1::placeholders::_1));
	m_listener.support(methods::DEL,
		std::tr1::bind(&Server::handle_delete,
		this,
		std::tr1::placeholders::_1));*/
}

void Server::handle_get(http_request message)
{
	printf("Handling GET request...\n");
	printf("PATH : %S \n", message.absolute_uri().path());
	if (message.absolute_uri().path() == U("/supervisor"))
	{
		printf("Handling supervisor request\n");
		Database db = Database::Database();
		web::json::value jsonStats = web::json::value::value();
		jsonStats = db.GetStats();
		printf("JSON : %S \n", jsonStats.serialize());
		message.reply(status_codes::OK, jsonStats.serialize());
	}
	else if (message.absolute_uri().path() == U("/client"))
	{
		printf("Handling client request\n");
		message.reply(status_codes::OK, U("Hello, Client ! - From the AppServer "));
	}
	else
	{
		printf("Unhandled path for the GET request\n");
		message.reply(status_codes::OK, U("Unhandled GET request, please refer to the API documentation"));
	}
};

void Server::handle_options(http_request message)
{
	printf("Handling OPTIONS request...\n");
};