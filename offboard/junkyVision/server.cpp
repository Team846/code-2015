/*
 * server.cpp
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */
#include "server.h"

namespace Server
{

std::mutex throttleMutex;
double throttle = 0.0f;

std::mutex forwardMutex;
double forward = 0.0f;

void updateThrottle(double newThrottle)
{
	throttleMutex.lock();
	throttle = newThrottle;
	throttleMutex.unlock();
}

void updateForward(double newForward)
{
	forwardMutex.lock();
	forward = newForward;
	forwardMutex.unlock();
}

std::string createResponse()
{
	std::string response;

	throttleMutex.lock();
	forwardMutex.lock();

	response = std::to_string(throttle) + " " + std::to_string(forward);

	throttleMutex.unlock();
	forwardMutex.unlock();

	return response;
}

udp_server::udp_server(boost::asio::io_service& io_service) :
		socket_(io_service, udp::endpoint(udp::v4(), 4201))
{
	start_receive();
}

void udp_server::start_receive()
{
	socket_.async_receive_from(boost::asio::buffer(recv_buffer_),
			remote_endpoint_,
			boost::bind(&udp_server::handle_receive, this,
					boost::asio::placeholders::error,
					boost::asio::placeholders::bytes_transferred));
}

void udp_server::handle_receive(const boost::system::error_code& error,
		std::size_t /*bytes_transferred*/)
{
	if (!error || error == boost::asio::error::message_size)
	{
		boost::shared_ptr<std::string> message(
				new std::string(createResponse())); //funky stuff with pointers happening here

		socket_.async_send_to(boost::asio::buffer(*message), remote_endpoint_,
				boost::bind(&udp_server::handle_send, this, message,
						boost::asio::placeholders::error,
						boost::asio::placeholders::bytes_transferred));

		start_receive();
	}
}

void udp_server::handle_send(boost::shared_ptr<std::string> /*message*/,
		const boost::system::error_code& /*error*/,
		std::size_t /*bytes_transferred*/)
{
}

void start()
{
	try
	{
		boost::asio::io_service io_service;
		udp_server server(io_service);
		io_service.run();
	} catch (std::exception& e)
	{
		std::cerr << e.what() << std::endl;
	}
}

}
