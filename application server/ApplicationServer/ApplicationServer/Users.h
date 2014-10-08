#include <map>
#include <string>
#include "User.h"
#include "md5.h"
using namespace std;
typedef map<utility::string_t, User*> ListOfClients;

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

	web::json::value getStats();

private:
	ListOfClients list;
	string GenerateToken(Point);
};

