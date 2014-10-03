#pragma once
#include <cpprest\http_listener.h>

using namespace web::http::experimental::listener;
using namespace web::http;
using namespace web;

class Server
{
public:
	Server(const http::uri& url);
	pplx::task<void> open() { return m_listener.open(); }
	pplx::task<void> close() { return m_listener.close(); }

private:
	void handle_options(http_request request);
	void handle_get(http_request request);
	void handle_put(http_request request);
	void handle_post(http_request request);
	void handle_delete(http_request request);

	http_listener m_listener;
};
