#include <map>
#include <string>
#include "user.h"
#include "md5.h"
using namespace std;

#pragma once
class clients
{
public:
	clients();
	~clients();
	//user * createClient();
	user * createClient(Point);

	user * getClient(utility::string_t);
	user * moveClient(string, Point);

	bool clientIsAt(Point);

	string getStats();

private:
	
	string GenerateToken(Point);
};

