supervisorctl stop all
pm2 stop iceland_socket
git pull https://ozanyildirim:ozzy1234@bitbucket.org/icelandplatform/betradar_solution.git master
cd betradar_client/
mvn clean compile assembly:single
rm -f /home/iceland/betradar-jar/betradar-client-1.0-jar-with-dependencies.jar
rm -f /home/iceland/betradar-jar/betradar-supervisord.log
mv /home/iceland/betradar_solution/betradar_client/target/betradar-client-1.0-jar-with-dependencies.jar /home/iceland/betradar-jar/betradar-client-1.0-jar-with-dependencies.jar
pm2 start iceland_socket
supervisorctl start all