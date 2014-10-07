#include "clients.h"

typedef map<string, client*> ListOfClients;

clients::clients()
{
	ListOfClients list;
}


clients::~clients()
{
}

client * clients::createClient(Point coordinates)
{
	client * cl = new client ;

	cl->setToken(GenerateToken(coordinates));
	List
	return cl;
}
client * clients::getClient(string)
{

}
client * clients::moveClient(string, Point)
{

}

bool clients::clientIsAt(Point)
{

}
string clients::GenerateToken(Point coordinates)
{
	char buffer[50];
	std::time_t t = std::time(0);
	sprintf_s(buffer, "%f %f %u", coordinates.x(), coordinates.y(), t);
	MD5 retour = MD5(buffer);
	return(retour.hexdigest());
}

