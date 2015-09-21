//
// Created by BowtieMod on 9/20/2015.
//

#include "linkedlist.h"

struct linkedlist_node *get_specific_entry(struct linkedlist_node *start, int number) {
    int i;
    struct linkedlist_node *pointer = start;

    for (i = 0; i < number; i++) {
        pointer = pointer->next;
    }

    return pointer;
}

struct linkedlist_node **get_entries_from_end(struct linkedlist_node *end, int number) {
    struct linkedlist_node **entries = malloc(sizeof(struct linkedlist_node *) * number);

    struct linkedlist_node *pointer = end;

    int i;
    for (i = 0; i < number; i++) {
        entries[i] = pointer->prev;

        if (pointer->prev->start) {
            break;
        }

        pointer = pointer->prev;
    }

    return entries;
}

void insert_between(struct linkedlist_node *first, struct linkedlist_node *between, struct linkedlist_node *second) {
    first->next = between;
    between->prev = first;
    between->next = second;
    second->prev = between;
}

void insert_at_end(struct linkedlist_node *node, struct linkedlist_node *end) {
    insert_between(end->prev, node, end);
}

void insert_at_start(struct linkedlist_node *node, struct linkedlist_node *start) {
    insert_between(start, node, start->next);
}

struct linkedlist_node *create_node() {
    struct linkedlist_node *newNode = malloc(sizeof(struct linkedlist_node));
    newNode->prev = NULL;
    newNode->next = NULL;

    return newNode;
}

void remove_node(struct linkedlist_node *node) {
    if (node->prev != NULL) {
        node->prev->next = node->next;
    }

    if (node->next != NULL) {
        node->next->prev = node->prev;
    }

    free(node->message);
    free(node);
}

void free_linkedlist_entries(struct linkedlist_node **entries) {
    free(entries);
}

void free_linkedlist(struct linkedlist_node *start) {
    struct linkedlist_node *pointer = start;

    for (; ;) {
        pointer = pointer->next;
        remove_node(pointer->prev);

        if (pointer->end) {
            remove_node(pointer);
            return;
        }
    }
}