set OLDDIR=%CD%

cd socket_client_angular_debug_events
call npm install
chdir /d %OLDDIR% &rem restore current directory

cd socket_server
call npm install
chdir /d %OLDDIR% &rem restore current directory

cd socket_server/app
node iceland_socket.js
