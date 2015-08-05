project(wheelnavcore2)
cmake_minimum_required(VERSION 2.8)

find_package(OpenCV REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS})

add_executable(demo main.cpp)
target_link_libraries(demo ${OpenCV_LIBS})

