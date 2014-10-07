#include "Users.h"

typedef map<utility::string_t, User*> ListOfClients;

Users::Users()
{
	list.clear();
}


Users::~Users()
{
}

User * Users::createClient(Point coordinates)
{
	User * cl = new User;
	//generate a new token if there is an already existing one it generate an another one
	do { utility::string_t token = GenerateToken(coordinates) } while (list[token].count(token)!=0);

	cl->setToken(token);
	cl->setDistance = 0;
	cl->setLstConnection(std::time(0));
	cl->setLstPositionKnown(coordinates);
	cl->setStates = 1;

	list[token]= cl;
	return cl;
}
User * Users::getClient(utility::string_t token)
{
	return list.at(token);
}
void Users::moveClient(utility::string_t token, Point coordinates)
{
	User * cl = list.at(token);
	cl->setDistance(cl->getDistance + (this->distanceBetweenTwoPointsInMeter(cl->setLstPositionKnown, coordinates)));
	cl->setLstPositionKnown(coordinates);
}

bool Users::clientIsAt(utility::string_t token, Point coordonates)
{
	User * cl = list.at(token);
	return(cl->getLstPositionKnown == coordonates ? true :  false);
}


string Users::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}
int Users::distanceBetweenTwoPointsInMeter(Point coordinate1, Point coordinate2)
{
	int distance;
	distance = round((acos(sin(coordinate1.x * M_PI / 180) * sin(coordinate2.x * M_PI / 180) + cos(coordinate1.x * M_PI / 180) * cos(coordinate2.x * M_PI / 180) * cos((coordinate1.y - coordinate2.y) * M_PI / 180)) * 180 / M_PI) * 60 * 1852);
	return distance;
}

string Users::getStats()
{
	return "{ \"users\": { \"u1\": {  \"token\": \"0001\",\"statistics\": {\"lastKnownLocation\": {   \"latitude\": 42.25351,\"longitude\": -12.55567  },\"travelledDistance\": \"0250\",\"rank\": 5  }},  \"u2\": {   \"token\": \"0002\",  \"statistics\": {\"lastKnownLocation\": {\"latitude\": 2.25351,  \"longitude\": 39.55567 },\"travelledDistance\": \"0000\",   \"rank\": 6}}}}";
}

