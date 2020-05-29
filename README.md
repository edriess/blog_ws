# blog\_web\_service

# Instructions
- Create a public GitHub repo. 
- Create a small application in the framework and language of your choice. 
- The application should serve two endpoints: 
    - a POST endpoint where blog posts can be added or updated to an in-memory store. 
    - And a GET endpoint where blog posts can be retrieved by id. 
- A blog post supports the following fields: id, author, title, content, date.

The project utilizes the following frameworks and libraries:

- Spring ([https://spring.io/](https://spring.io/))
- RestEasy ([http://resteasy.jboss.org/](http://resteasy.jboss.org/))
- JUnit ([https://junit.org/junit4/](https://junit.org/junit4/))
- Mockito ([http://site.mockito.org/](http://site.mockito.org/))
- Postman ([https://www.getpostman.com/](https://www.getpostman.com/))


`/src/test/postman/Blogs.postman_collection.json` is a Postman collection to verify these endpoints against a running server.

# Git

You can clone the project with:

	git clone https://github.com/edriess/blog_ws.git


# Prerequisites

* Java 8
		
# Build

	mvn clean package

# Run

The app can be run from terminal using the Maven command:

	mvn jetty:run



