#include "Database.h"
#include "Server.h"

int main(int argc, char* argv[])
{
	utility::string_t port = U("34568");
	utility::string_t address = U("http://192.168.0.22:"); // Listening on all URIs (localhost, 127.0.0.1, externa IP...)
	address.append(port); // Adding the port to the URI
	Server playerAppListener = Server::Server(address);

	try
	{
		playerAppListener
			.open()
			.then([&playerAppListener](){printf("\nstarting to listen\n"); })
			.wait();

		while (true);
	}
	catch (const std::exception &e)
	{
		printf ("Exception while opening the listener : %s", e.what());
	}

/*	Database db = Database::Database();
	Point test = Point::Point(1.46877f, (float)42.32894, (float)0);
	db.RegisterToken(test);*/


	char temp = 'r';
	scanf_s("%c", temp);

	playerAppListener.close().wait();

	return 0;
}