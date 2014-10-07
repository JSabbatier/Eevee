#include <map>
#include <string>
#include "User.h"
#include "md5.h"
using namespace std;

#pragma once
class Users
{
public:
	Users();
	~Users();
	//user * createClient();
	User * createClient(Point);

	User * getClient(utility::string_t);
	User * moveClient(string, Point);

	bool clientIsAt(Point);

	string getStats();

private:
	
	string GenerateToken(Point);
};

