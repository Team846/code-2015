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

namespace Server {

using boost::asio::ip::udp;

std::mutex throttleMutex;
double throttle = 0.0f;

void updateThrottle(double newThrottle) {
	throttleMutex.lock();
	throttle = newThrottle;
	throttleMutex.unlock();
}

std::string createResponse()
{
	std::string response;

	throttleMutex.lock();
	response = std::to_string(throttle);
	throttleMutex.unlock();

	return response;
}

class udp_server {
public:
	udp_server(boost::asio::io_service& io_service) :
			socket_(io_service, udp::endpoint(udp::v4(), 4201)) {
		start_receive();
	}

private:
	void start_receive() {
		socket_.async_receive_from(boost::asio::buffer(recv_buffer_),
				remote_endpoint_,
				boost::bind(&udp_server::handle_receive, this,
						boost::asio::placeholders::error,
						boost::asio::placeholders::bytes_transferred));
	}

	void handle_receive(const boost::system::error_code& error,
			std::size_t /*bytes_transferred*/) {
		if (!error || error == boost::asio::error::message_size) {
			boost::shared_ptr<std::string> message(
					new std::string(createResponse()));

			socket_.async_send_to(boost::asio::buffer(*message),
					remote_endpoint_,
					boost::bind(&udp_server::handle_send, this, message,
							boost::asio::placeholders::error,
							boost::asio::placeholders::bytes_transferred));

			start_receive();
		}
	}

	void handle_send(boost::shared_ptr<std::string> /*message*/,
			const boost::system::error_code& /*error*/,
			std::size_t /*bytes_transferred*/) {
	}

	udp::socket socket_;
	udp::endpoint remote_endpoint_;
	boost::array<char, 1> recv_buffer_;
};

void start() {
	try {
		boost::asio::io_service io_service;
		udp_server server(io_service);
		io_service.run();
	} catch (std::exception& e) {
		std::cerr << e.what() << std::endl;
	}
}

}

#endif
