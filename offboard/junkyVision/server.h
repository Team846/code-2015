#ifndef _SERVER
#define _SERVER

#include <ctime>
#include <iostream>
#include <string>
#include <mutex>

#include <boost/array.hpp>
#include <boost/bind.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/asio.hpp>

using boost::asio::ip::udp;

namespace Server
{

void updateThrottle(double newThrottle);

std::string createResponse();

class udp_server
{
public:
	udp_server(boost::asio::io_service& io_service);

private:
	void start_receive();

	void handle_receive(const boost::system::error_code& error,
			std::size_t /*bytes_transferred*/);

	void handle_send(boost::shared_ptr<std::string> /*message*/,
			const boost::system::error_code& /*error*/,
			std::size_t /*bytes_transferred*/);

	udp::socket socket_;
	udp::endpoint remote_endpoint_;
	boost::array<char, 1> recv_buffer_;
};

void start();

}

#endif
