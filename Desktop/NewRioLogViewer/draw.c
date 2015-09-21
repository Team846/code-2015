//
// Created by BowtieMod on 9/20/2015.
//

#include "draw.h"

void update_terminal_info(struct terminal_info *info) {
    int y;
    int x;
    getmaxyx(info->window, y, x); // update width and height

    info->width = x;
    info->height = y;
    info->usableWidth = info->width; // TODO for now
    info->usableHeight = info->height - MENU_HEIGHT;
}

static void draw_menu_bg(struct terminal_info *info) {
    // color background
    attron(COLOR_PAIR(1));

    int i;
    for (i = 1; i <= MENU_HEIGHT; i++) {
        int j;
        for (j = 0; j < info->width; j++) {
            mvwaddch(info->window, info->height - i, j, ' ');
        }
    }

    attroff(COLOR_PAIR(1));
}

static int determine_color_of_message(char *message) { // FIXME regular expressions
    char *copy = malloc(sizeof(char) * strlen(message));
    strcpy(copy, message);

    // truncate
    int i = 0;
    for (i = 0; i < strlen(message); i++) {
        if (copy[i] = ']') {
            copy[i] = '\0';
            break;
        }
    }

    int color;

    if (strstr(copy, "ERROR")) {
        color = 3;
    } else if (strstr(copy, "INFO")) {
        color = 4;
    } else if (strstr(copy, "WARNING")) {
        color = 5;
    } else if (strstr(copy, "LOG")) {
        color = 6;
    } else if (strstr(copy, "DEBUG")) {
        color = 6;
    } else {
        color = 2;
    }

    free(copy);

    return color;
}

static void draw_text(struct terminal_info *info, struct linkedlist_node *end) {
    // determine how many messages can fit on the current configuration
    int messsagesCanFit = info->usableHeight;

    struct linkedlist_node **entries = get_entries_from_end(end, info->usableHeight);

    int i;
    for (i = 0; i < info->usableHeight; i++) {
        entries[i]->linesNeeded = (int) ceil((double) entries[i]->messageLength / (double) info->usableWidth);

        if (entries[i]->start) {
            break;
        }

        // FIXME handle cases where linesNeeded > messagesCanFit
    }

    // draw dos suckers
    int j;
    for (j = 0; messsagesCanFit > 0 && !entries[j]->start; j++) {
        // while we are not out of drawing space and we are not at the start of the logs

        messsagesCanFit -= entries[j]->linesNeeded;

        int color = determine_color_of_message(entries[j]->message);

        attron(COLOR_PAIR(color));
        mvwprintw(info->window, messsagesCanFit - 1, 0, entries[j]->message);
        attroff(COLOR_PAIR(color));
    }
}

void draw(struct terminal_info *info, struct linkedlist_node *end) {
    if (info->dirty) {
        clear();

        draw_menu_bg(info);
        draw_text(info, end);

        touchwin(info->window);
        refresh();
        info->dirty = false; // TODO figure out how to mark dirty after resize
    }
}