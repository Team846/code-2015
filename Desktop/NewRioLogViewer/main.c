//
// Created by BowtieMod on 9/19/2015.
//

#include <stdlib.h>
#include <stdio.h>
#include <curses.h>
#include <signal.h>

#include "network.h"
#include "draw.h"
#include "linkedlist.h"

#define MAX_LENGTH 2048

static void cleanup();

static void signal_exit(int signal);

static void signal_window_size(int signal);

WINDOW *mainWindow;
struct socket_metadata *socket_m;
struct terminal_info *term_info;

struct linkedlist_node *start;
struct linkedlist_node *end;

int main() {
    // Initialize ncurses
    mainWindow = initscr();

    if (mainWindow == NULL) {
        fprintf(stderr, "Error initializing ncurses");
        exit(EXIT_FAILURE);
    }

    start_color();
    curs_set(0); // hide cursor

    init_pair(1, COLOR_YELLOW, COLOR_BLUE);
    init_pair(2, COLOR_WHITE, COLOR_BLACK);
    init_pair(3, COLOR_RED, COLOR_BLACK);
    init_pair(4, COLOR_CYAN, COLOR_BLACK);
    init_pair(5, COLOR_YELLOW, COLOR_BLACK);
    init_pair(6, COLOR_BLACK, COLOR_WHITE);
    init_pair(7, COLOR_GREEN, COLOR_WHITE);

    // init terminal metadata
    term_info = malloc(sizeof(struct terminal_info));
    term_info->window = mainWindow;
    term_info->dirty = true;
    term_info->scrollPosition = -1;

    // Set up signals
    signal(SIGINT, signal_exit);
    signal(SIGWINCH, signal_window_size);

    // Set up UDP socket
    socket_m = init_log_socket();

    // Set up linked list
    int linkedlist_len = 0;

    start = create_node();
    start->start = true;

    end = create_node();
    end->end = true;

    start->next = end;
    end->prev = start;


    // Set up main loop
    char currentMessage[BUFFER_SIZE * 6] = ""; // FIXME arbitrary buffer size
    char currentMessageLength = 0;

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wmissing-noreturn"
    while (true) {
        update_terminal_info(term_info);

        if (receive_packets(socket_m)) {
            strncat(currentMessage, socket_m->recv_buffer, (BUFFER_SIZE - currentMessageLength + 1));
            currentMessageLength += socket_m->recv_len;

            if (socket_m->recv_buffer[socket_m->recv_len - 1] == '\n') {
                // it's a complete log message!

                socket_m->recv_buffer[socket_m->recv_len - 1] = '\0'; // get rid of newline

                // add log message to linked list, increment counter, mark layer as dirty
                struct linkedlist_node *newLog = create_node();
                newLog->message = malloc(
                        sizeof(char) * currentMessageLength); //we don't want buffer-long allocations for everything
                newLog->messageLength = currentMessageLength;
                strncpy(newLog->message, currentMessage, currentMessageLength);
                insert_at_end(newLog, end);

                linkedlist_len++;
                term_info->dirty = true;

                // reset concat message
                currentMessage[0] = 0;
                currentMessageLength = 0;
            }
        }

        if (linkedlist_len > MAX_LENGTH) {
            remove_node(start->next);
            linkedlist_len--;
            term_info->dirty = true;
        }

        draw(term_info, end);
        usleep(10000); // TODO move to define
    }
#pragma clang diagnostic pop

    cleanup();

    assert(0); // execution should never reach here
    return NULL;
}

static void cleanup() {
    refresh();
    delwin(mainWindow);
    endwin();

    free_linkedlist(start);
    free(term_info);
    free_log_socket(socket_m);
}

static void signal_exit(int signal) {
    cleanup();
    exit(signal);
}

static void signal_window_size(int signal) {
    update_terminal_info(term_info);
    term_info->dirty = true;
}