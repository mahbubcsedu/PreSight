cmake_minimum_required(VERSION 2.8)
project( wheelnavcore-test )
find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS} )
add_executable( wheelnavcore-test wheelnavcore-test.cpp )
target_link_libraries( wheelnavcore-test ${OpenCV_LIBS} )
