#include <map>
#include <string>
#include "client.h"
#include "md5.h"
using namespace std;
#pragma once
class clients
{
public:
	clients();
	~clients();
	client * createClient();
	client * createClient(Point);

	client * getClient(string);
	client * moveClient(string, Point);

	bool clientIsAt(Point);


private:
	
	string GenerateToken(Point);



};

