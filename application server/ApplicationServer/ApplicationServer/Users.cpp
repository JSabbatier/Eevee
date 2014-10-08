#include "Users.h"
#include <codecvt>
#include <cstring>

using namespace web;

std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> strconverter; // Converter from std::string to wchar_t

Users::Users()
{
	list.clear();
}

void Users::clear()
{
	list.clear();
}

int Users::size()
{
	return list.size();
}


Users::~Users()
{
}

User * Users::createClient(Point coordinates)
{
	User * cl = new User;
	utility::string_t token;
	//generate a new token if there is an already existing one it generate an another one
	do
	{ 
		 token = GenerateToken(coordinates);
	} 
	while (list.count(token)!=0);

	cl->setToken(token);
	cl->setDistance(0);
	cl->setLstConnection(std::time(0));
	cl->setLstPositionKnown(coordinates);
	cl->setStates(1); // 1 = connected

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
	cl->setDistance(cl->getDistance() + (this->distanceBetweenTwoPointsInMeter(cl->getLstPositionKnown(), coordinates)));
	cl->setLstPositionKnown(coordinates);
}

bool Users::clientIsAt(utility::string_t token, Point coordonates)
{
	User * cl = list.at(token);
	return((cl->getLstPositionKnown().x() == coordonates.x() && cl->getLstPositionKnown().y() == coordonates.y() && cl->getLstPositionKnown().z() == coordonates.z()) ? true : false);
}


utility::string_t Users::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(strconverter.from_bytes(retour.hexdigest()));
}
int Users::distanceBetweenTwoPointsInMeter(Point coordinate1, Point coordinate2)
{
	int distance;
	distance = round((acos(sin(coordinate1.x() * M_PI / 180) * sin(coordinate2.x() * M_PI / 180) + cos(coordinate1.x() * M_PI / 180) * cos(coordinate2.x() * M_PI / 180) * cos((coordinate1.y() - coordinate2.y()) * M_PI / 180)) * 180 / M_PI) * 60 * 1852);
	return distance;
}

json::value Users::getStats()
{
	json::value jsonResult;
	jsonResult[U("connected_users")] = json::value::number((int)list.size()); // Casting int because the return of size() match 2 overloaded instances of number()
	int cpt = 0;
	for (auto iter = list.cbegin(); iter != list.cend(); ++iter)
	{
		wchar_t userId[25];
		swprintf_s(userId, L"u%d", cpt);
		jsonResult[U("users")][userId][U("token")] = json::value::string(iter->first); // First is the token
		jsonResult[U("users")][userId][U("statistics")][U("lastKnownLocation")][U("latitude")] = json::value::number(iter->second->getLstPositionKnown().x()); // Second is the user ptr
		jsonResult[U("users")][userId][U("statistics")][U("lastKnownLocation")][U("longitude")] = json::value::number(iter->second->getLstPositionKnown().y());
		jsonResult[U("users")][userId][U("statistics")][U("travelledDistance")] = json::value::number(iter->second->getDistance());
		jsonResult[U("users")][userId][U("statistics")][U("rank")] = json::value::number(0);
		cpt++;
	}
	return jsonResult;
}


