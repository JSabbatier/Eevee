#include "Database.h"
#include "Server.h"

int main(int argc, char* argv[])
{
	utility::string_t port = U("34568");
	utility::string_t address = U("http://172.31.1.64:");
	address.append(port);
	Server playerAppListener = Server::Server(address);

	playerAppListener.open().wait();

/*	Database db = Database::Database();
	Point test = Point::Point(1.46877f, (float)42.32894, (float)0);
	db.RegisterToken(test);*/


	char temp = 'r';
	scanf_s("%c", temp);

	playerAppListener.close().wait();

	return 0;
}