#include "Users.h"
#include <codecvt>

using namespace web;

std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter; // Converter from std::string to wchar_t

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


utility::string_t Users::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(converter.from_bytes(retour.hexdigest()));
}
int Users::distanceBetweenTwoPointsInMeter(Point coordinate1, Point coordinate2)
{
	int distance;
	distance = round((acos(sin(coordinate1.x * M_PI / 180) * sin(coordinate2.x * M_PI / 180) + cos(coordinate1.x * M_PI / 180) * cos(coordinate2.x * M_PI / 180) * cos((coordinate1.y - coordinate2.y) * M_PI / 180)) * 180 / M_PI) * 60 * 1852);
	return distance;
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


