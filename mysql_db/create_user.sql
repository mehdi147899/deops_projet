CREATE USER 'docker_user'@'%' IDENTIFIED BY 'Docker@1234!';
GRANT ALL PRIVILEGES ON stationSki.* TO 'docker_user'@'%';
FLUSH PRIVILEGES;
