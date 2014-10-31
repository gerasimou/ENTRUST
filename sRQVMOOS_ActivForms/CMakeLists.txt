#--------------------------------------------------------
# The CMakeLists.txt for:                        RQVMOOS
# Author(s):                         		 Simos     
#--------------------------------------------------------

# FILE(GLOB SRC *.cpp)

SET(SRC
  sRQVMOOS_AF.cpp
  sRQVMOOS_AF_Info.cpp
  main.cpp
)

SET (CMAKE_VERBOSE_MAKEFILE, 1)

ADD_SUBDIRECTORY(Client)

ADD_EXECUTABLE(sRQVMOOS_ActivForms ${SRC})

TARGET_LINK_LIBRARIES(sRQVMOOS_ActivForms
   ${MOOS_LIBRARIES}
   mbutil
   m
   pthread
   ClientL)

