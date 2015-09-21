//
// Created by BowtieMod on 9/20/2015.
//

#ifndef NEWRIOLOGVIEWER_DRAW_H
#define NEWRIOLOGVIEWER_DRAW_H

#include <curses.h>
#include <math.h>
#include <string.h>

#include "linkedlist.h"

#define MENU_HEIGHT 2

struct terminal_info {
    WINDOW *window;

    int width;
    int height;

    int usableWidth;
    int usableHeight;

    int dirty;

    int currentFilter; // TODO typedef?

    int scrollPosition; // positive number : position, -1 : latest log
};

void update_terminal_info(struct terminal_info *info);

void draw(struct terminal_info *info, struct linkedlist_node *start);

#endif //NEWRIOLOGVIEWER_DRAW_H
