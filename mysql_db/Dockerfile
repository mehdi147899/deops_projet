FROM mysql:5.7

# Root credentials
ENV MYSQL_ROOT_PASSWORD=Root@1234
ENV MYSQL_DATABASE=stationSki

# Add a script to create the docker_user with the required permissions
COPY ./create_user.sql /docker-entrypoint-initdb.d/
