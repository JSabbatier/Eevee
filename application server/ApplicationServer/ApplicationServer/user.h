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
	void setToken(utility::string_t);
	utility::string_t getToken() const;
	void setLstConnection(std::time_t);
	std::time_t getLstConnection() const;
	void setStates(int);
	int getStates() const;
	void setLstPositionKnown(Point);
	Point getLstPositionKnown() const;
	int getDistance() const;
	void setDistance(int);


private:
	int distance;
	utility::string_t token;
	std::time_t lastConnection;
	int state;
	Point lastPositionKnown;

	// a déplacer



};

