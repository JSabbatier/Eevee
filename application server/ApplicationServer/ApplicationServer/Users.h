#include <map>
#include <string>
#include "User.h"
#include "md5.h"
#define _USE_MATH_DEFINES
#include <math.h>
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
	void moveClient(string, Point);

	bool clientIsAt(utility::string_t,Point);

	string getStats();

private:
	int distanceBetweenTwoPointsInMeter(Point, Point);
	ListOfClients list;
	string GenerateToken(Point);
};

