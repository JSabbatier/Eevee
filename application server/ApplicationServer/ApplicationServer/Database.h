#pragma once
#include <string>
#include "Point.h"
#include "Users.h"

#include <cpprest/http_client.h>
#include <cpprest/filestream.h>

using namespace web;                        // Common features like URIs.
using namespace web::http;                  // Common HTTP functionality
using namespace web::http::client;          // HTTP client features
using namespace concurrency::streams;       // Asynchronous streams

class Database
{
public:
	Database();
	~Database();
	void SendPois(http_request, Point);
private:
};

