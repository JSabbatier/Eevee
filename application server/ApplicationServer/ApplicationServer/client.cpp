#include "client.h"


client::client()
{
}

void client::setToken(std::string tok)
{
	this->token = tok;
}
std::string client::getToken() const
{
	return this->token;
}
void client::setLstConnection(std::time_t tm)
{
	this->lastConnection = tm;
}
std::time_t client::getLstConnection() const
{
	return this->lastConnection;
}
void client::setStates(int state)
{
	this->state = state;
}
int client::getStates() const
{
	return this->state;
}
void client::setLstPositionKnown(Point coordinates)
{
	this->lastPositionKnown = coordinates;
}
Point client::getLstPositionKnown() const
{
	return this->lastPositionKnown;
}

client::~client()
{
}
