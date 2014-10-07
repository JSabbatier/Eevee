#pragma once
#include <string>
#include <ctime>
#include "Point.h"
#include <cpprest/http_client.h>
class User
{
public:
	User();
	~User();
	void setToken(std::string);
	utility::string_t getToken() const{ return(U("slfdghjfe")); };
	void setLstConnection(std::time_t);
	std::time_t getLstConnection() const;
	void setStates(int);
	int getStates() const;
	void setLstPositionKnown(Point);
	Point getLstPositionKnown() const { return Point::Point(12.2f, 3.5f, 0.f); };
	int getDistance() const { return 12; };
	void setDistance(int);
	bool isAt(Point){ return true; };



private:
	int distance;
	std::string token;
	std::time_t lastConnection;
	int state;
	Point lastPositionKnown;

	// a déplacer



};

