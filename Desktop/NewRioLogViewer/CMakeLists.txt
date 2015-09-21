cmake_minimum_required(VERSION 3.3)
project(NewRioLogViewer)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c99 ")

set(SOURCE_FILES main.c network.c network.h draw.c draw.h linkedlist.c linkedlist.h)
add_executable(NewRioLogViewer ${SOURCE_FILES})

target_link_libraries(NewRioLogViewer ncurses)