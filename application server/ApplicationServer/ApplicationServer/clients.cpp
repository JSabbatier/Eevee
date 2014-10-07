#include "clients.h"

typedef map<string, user*> ListOfClients;

clients::clients()
{
	ListOfClients list;
}


clients::~clients()
{
}

user * clients::createClient(Point coordinates)
{
	/*user * cl = new user;

	cl->setToken(GenerateToken(coordinates));
	//List
	return cl;*/
	return nullptr;
}
user * clients::getClient(utility::string_t)
{
	return nullptr;
}
user * clients::moveClient(string, Point)
{
	return nullptr;
}

bool clients::clientIsAt(Point)
{
	return true;
}
string clients::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}

string clients::getStats()
{
	return "{ \"users\": { \"u1\": {  \"token\": \"0001\",\"statistics\": {\"lastKnownLocation\": {   \"latitude\": 42.25351,\"longitude\": -12.55567  },\"travelledDistance\": \"0250\",\"rank\": 5  }},  \"u2\": {   \"token\": \"0002\",  \"statistics\": {\"lastKnownLocation\": {\"latitude\": 2.25351,  \"longitude\": 39.55567 },\"travelledDistance\": \"0000\",   \"rank\": 6}}}}";
}

