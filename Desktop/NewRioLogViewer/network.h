//
// Created by BowtieMod on 9/20/2015.
//

#ifndef NEWRIOLOGVIEW_NETWORK_H
#define NEWRIOLOGVIEW_NETWORK_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/unistd.h>
#include <cygwin/in.h>
#include <assert.h>
#include <fcntl.h>

#define ROBORIO_LOG_RECV_PORT 6666
#define BUFFER_SIZE 512

struct socket_metadata {
    int socket; // socket
    struct sockaddr_in *myaddr; // server's address;

    unsigned char *recv_buffer; // buffer to read messages into
    int recv_len; // length of message received
};

struct socket_metadata *init_log_socket();

void free_log_socket(struct socket_metadata *socket_m);

int receive_packets(struct socket_metadata *socket_m);

#endif //NEWRIOLOGVIEW_NETWORK_H
