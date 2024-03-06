# Bughound

This project aims to be a simple, local, free, and open source bug tracking application. It can maintain a list of projects, associated tickets, and associated comments for each ticket. Bughound uses a local SQL database to store all the aforementioned data.


## Requirements
→ Java 8 JDK, which contains JavaFX support.  
[Here's](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html) the link for Oracle official Java 8 JDK.
## Compile Instructions
1. Clone the repo locally.
2. Open the project in a Java IDE, preferably Eclipse.
3. Export the project into a Runnable JAR. Make sure to package required libraries into the JAR itself.

## Running the Application

1. Open the directory where you exported the Runnable JAR to.
2. Create the following structure in the directory: data/projects.sqlite (if on Mac or Linux), or data\projects.sqlite (if on Windows).
3. Assuming that Java is already in your computer's PATH variable, run:
	a. java -jar filename\_you\_gave.jar (on Mac. On Linux if running atop a Xorg session.)
	b. export GDK\_BACKEND=x11; java -jar filename\_you\_gave.jar (on Linux if running in a Wayland session)
	c. java.exe -jar filename\_you\_gave.jar (on Windows)

## Known Issues

1. Outside the IDE, Bughound's logo doesn't show up.
2. The directory structure and the database file is hard-coded and needs to be created manually with the current implementation.
3. On Linux running atop Wayland, since OpenJDK doesn't yet have a Wayland implementation, Bughound must be run through Xwayland as indicated above, unfortunately.
