#include "User.h"


User::User()
{
}

void User::setToken(utility::string_t tok)
{
	this->token = tok;
}
utility::string_t User::getToken() const
{
	return this->token;
}
void User::setLstConnection(std::time_t tm)
{
	this->lastConnection = tm;
}
std::time_t User::getLstConnection() const
{
	return this->lastConnection;
}
void User::setStates(int state)
{
	this->state = state;
}
int User::getStates() const
{
	return this->state;
}
void User::setLstPositionKnown(Point coordinates)
{
	this->lastPositionKnown = coordinates;
}
Point User::getLstPositionKnown() const
{ 
	//return Point::Point(12.2f, 3.5f, 0.f); // For tests purpose only
	return this->lastPositionKnown;
}

User::~User()
{
}
