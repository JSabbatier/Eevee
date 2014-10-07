#include "Users.h"

typedef map<string, User*> ListOfClients;

Users::Users()
{
	ListOfClients list;
}


Users::~Users()
{
}

User * Users::createClient(Point coordinates)
{
	/*user * cl = new user;

	cl->setToken(GenerateToken(coordinates));
	//List
	return cl;*/
	return nullptr;
}
User * Users::getClient(utility::string_t)
{
	return nullptr;
}
User * Users::moveClient(string, Point)
{
	return nullptr;
}

bool Users::clientIsAt(Point)
{
	return true;
}
string Users::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}

string Users::getStats()
{
	return "{ \"users\": { \"u1\": {  \"token\": \"0001\",\"statistics\": {\"lastKnownLocation\": {   \"latitude\": 42.25351,\"longitude\": -12.55567  },\"travelledDistance\": \"0250\",\"rank\": 5  }},  \"u2\": {   \"token\": \"0002\",  \"statistics\": {\"lastKnownLocation\": {\"latitude\": 2.25351,  \"longitude\": 39.55567 },\"travelledDistance\": \"0000\",   \"rank\": 6}}}}";
}

