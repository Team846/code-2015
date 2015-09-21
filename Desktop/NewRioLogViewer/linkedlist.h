//
// Created by BowtieMod on 9/20/2015.
//

#ifndef NEWRIOLOGVIEWER_LINKEDLIST_H
#define NEWRIOLOGVIEWER_LINKEDLIST_H

#include <stdlib.h>

struct linkedlist_node {
    struct linkedlist_node *prev;
    struct linkedlist_node *next;

    int start;
    int end;

    char *message;
    int messageLength;
    int messageType;
    int linesNeeded;
};

struct linkedlist_node *get_specific_entry(struct linkedlist_node *start, int number);

struct linkedlist_node **get_entries_from_end(struct linkedlist_node *end, int number);

void insert_between(struct linkedlist_node *first, struct linkedlist_node *between, struct linkedlist_node *second);

void insert_at_end(struct linkedlist_node *node, struct linkedlist_node *end);

void insert_at_start(struct linkedlist_node *node, struct linkedlist_node *start);

struct linkedlist_node *create_node();

void remove_node(struct linkedlist_node *node);

void free_linkedlist_entries(struct linkedlist_node **entries);

void free_linkedlist(struct linkedlist_node *start);

#endif //NEWRIOLOGVIEWER_LINKEDLIST_H
