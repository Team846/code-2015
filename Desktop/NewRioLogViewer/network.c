//
// Created by BowtieMod on 9/20/2015.
//

#include "network.h"

struct socket_metadata *init_log_socket() {
    // creates a UDP socket that is bound to listen for RoboRio logs
    // returns socket_metadata structure

    struct socket_metadata *socket_m =
            malloc(sizeof(struct socket_metadata)); // allocate main struct

    socket_m->myaddr = malloc(sizeof(struct sockaddr_in)); // allocate address struct
    socket_m->recv_buffer = malloc(sizeof(char) * BUFFER_SIZE); // allocate buffer

    memset((char *) socket_m->myaddr, 0, sizeof(*socket_m->myaddr)); // init address
    memset(socket_m->recv_buffer, '\0', sizeof(char) * BUFFER_SIZE); // init buffer

    socket_m->socket = socket(AF_INET, SOCK_DGRAM, 0);  // create unix socket

    /* flag meanings
     *
     * AF_INET = over IPv4
     * SOCK_DGRAM = datagram
     * 0 = datagram flag implies UDP */

    int flags = fcntl(socket_m->socket, F_GETFL);
    flags |= O_NONBLOCK;
    fcntl(socket_m->socket, F_SETFL, flags); // set socket to not block

    if (socket_m->socket < 0) {
        fprintf(stderr, "Could not create UDP socket");
        exit(EXIT_FAILURE);
    }

    // bind socket to any IP address, but listen for RoboRio on multicast
    socket_m->myaddr->sin_family = AF_INET;
    socket_m->myaddr->sin_addr.s_addr = htonl(INADDR_ANY);
    socket_m->myaddr->sin_port = htons(ROBORIO_LOG_RECV_PORT);

    int bind_res =
            bind(socket_m->socket, (struct sockaddr *) socket_m->myaddr, sizeof(*socket_m->myaddr));

    if (bind_res < 0) {
        fprintf(stderr, "Error binding UDP socket");
        free_log_socket(socket_m);
        exit(EXIT_FAILURE);
    }

    return socket_m;
}

void free_log_socket(struct socket_metadata *socket_m) {
    close(socket_m->socket);

    free(socket_m->recv_buffer);
    free(socket_m->myaddr);

    free(socket_m);
}

int receive_packets(struct socket_metadata *socket_m) {
    socket_m->recv_len = recvfrom(socket_m->socket, socket_m->recv_buffer, BUFFER_SIZE, 0, 0, 0);

    if (socket_m->recv_len > 0) {
        socket_m->recv_buffer[socket_m->recv_len] = '\0'; // terminate received string
        return 1; // we have a non-empty message
    } else {
        return 0;
    }

    assert(0);
}