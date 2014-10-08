#include "Users.h"

using namespace web;
typedef map<utility::string_t, User*> ListOfClients;

Users::Users()
{
	ListOfClients list;
}


Users::~Users()
{
}

User * Users::createClient(Point coordinates)
{
	User * cl = new User;

	cl->setToken(GenerateToken(coordinates));
	//List
	return cl;
	//return nullptr;
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
utility::string_t Users::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}

json::value Users::getStats()
{
	json::value jsonResult;
	jsonResult[U("connected_users")] = json::value::number((int)list.size()); // Casting int because the return of size() match 2 overloaded instances of number()
	int cpt = 0;
	for (auto iter = list.cbegin(); iter != list.cend(); ++iter)
	{		
		jsonResult[U("users")][wprintf(L"u%d", iter)][U("token")] = json::value::string(iter->first); // First is the token
		jsonResult[U("users")][wprintf(L"u%d", iter)][U("statistics")][U("lastKnownLocation")][U("latitude")] = json::value::number(iter->second->getLstPositionKnown().x()); // Second is the user ptr
		jsonResult[U("users")][wprintf(L"u%d", iter)][U("statistics")][U("lastKnownLocation")][U("longitude")] = json::value::number(iter->second->getLstPositionKnown().y());
		jsonResult[U("users")][wprintf(L"u%d", iter)][U("statistics")][U("travelledDistance")] = json::value::number(iter->second->getDistance());
		jsonResult[U("users")][wprintf(L"u%d", iter)][U("statistics")][U("rank")] = json::value::number(0);
		cpt++;
	}
	return jsonResult;
}

