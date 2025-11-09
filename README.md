# video-game
Repository for developing video game. Cohort 3, Team 8.

-----------------------------------------------------------------------
Note you must use **Adoptium's Temurin® Java 17**:
- This is the version the modules technical requirements state we must use, check **JAVASETUP.md** for setup instructions
- To test you are using the right version, run the file: **CheckJDKVersion.java**
    - Expect results like this:
        - Java Version: 17.0.16
        - Java Vendor: Eclipse Adoptium

-----------------------------------------------------------------------
Commit Standards:
- **Type: Message**
    - Type:
        - **feat** - changes that add a feature or modify one
        - **fix**  - changes that fix bugs or issues
        - **doc**  - changes to documentation
        - **conf**  - changes to file structure
        - **test** - changes to code tests
        - **dep** - changes to dependencies

    - Message: short explaining change
- Example:
    - *fix: data validation bug corrected*

-----------------------------------------------------------------------

Names:
- Henry G
- Lenny S
- Isaac M
- Andri K
- Rishi T
- Ida K
- Viktor B

-----------------------------------------------------------------------

# Maze Game

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

## Where to start

the controller class of the game is MazeGame which inherits from applicationAdapter to access the libgdx framework
this then calls every other class and controls them as the game goes on in render
some helper functions are also found within gameController
most complex methods are class methods within their respective classes 

## Notes about improvements

the base code should be very good starter
made to be as transferable as possible to any new tasks
potential areas that assesment 2 may take and what you need to do:
    - new level - this is easy, just make a new Map through Tiled and pass it to Maze class
    - difficult - not as simple but still easy, just change the time given, speed of bob, harshness of events or the map level that title screen moves to when the player starts
    - replay - this is something that you will have to implement yourself, start with making some of the insides of create() a new function that is called to reset the game then call this from the lose and win screens
    - scoreboard - this is one we thought might be coming up, you'll probably need to start by adding the stuff from above but its quite trivial after that, just take the event counters or time and write a method to calculate a score and add it to a new screen
    - new entities - this should be very easy to do. use whichever class describes the entity the best and either make a subclass or just instance of that class depending on what you want to do
    - if anything else comes up and we're still in the handover week come and talk to us, we'll be happy to give a rundown of the existing code and/or what you need to do to implement the new feature
