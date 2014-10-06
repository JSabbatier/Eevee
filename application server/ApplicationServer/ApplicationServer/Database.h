#pragma once
#include <string>
#include "Point.h"

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
	int RegisterToken(Point);
	void GetStats(http_request);
private:
	std::string GenerateToken(Point);
	int PutToken(std::string);
};

