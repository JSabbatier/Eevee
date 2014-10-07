#pragma once
#include <string>
#include <ctime>
#include "Point.h"

class client
{
public:
	client();
	~client();
	void setToken(std::string);
	std::string getToken() const;
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
	std::string token;
	std::time_t lastConnection;
	int state;
	Point lastPositionKnown;

	// a déplacer



};

