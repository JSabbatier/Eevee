#include <map>
#include <string>
#include "User.h"
#include "md5.h"
#define _USE_MATH_DEFINES
#include <math.h>
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
	void moveClient(utility::string_t, Point);

	bool clientIsAt(utility::string_t, Point);
	void clear();
	int Users::size();

	web::json::value  getStats();

private:
	int distanceBetweenTwoPointsInMeter(Point, Point);
	ListOfClients list;
	utility::string_t GenerateToken(Point);
};

