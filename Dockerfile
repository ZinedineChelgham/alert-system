# Use the official MariaDB image as the base image
FROM mariadb:10.3

# Set environment variables
ENV MARIADB_ROOT_PASSWORD=admin
ENV MARIADB_USER=root
ENV MARIADB_PASSWORD=admin

# Expose port 3306
EXPOSE 3306
