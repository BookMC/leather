# leather
MIT mappings for Minecraft with no exceptions

## Tannery
Tannery is our custom gradle plugin to create the perfect mapping workspace

## Tasks
### `build`
Any task in the project that uses `Jar` will automatically run the `export` task.

### `export`
The export task converts the `mappings` directory into a tiny file. 

### `downloadMinecraftLibraries`
This task shouldn't really be run by itself but reads the version manifest and downloads all the libraries + client/server jar from it.

### `downloadMinecraftManifest`
Another task that shouldn't really be run by itself but 
fetches the specified version's manifest from
the Mojang CDN and downloads it onto the system.

### `genRunTask[VERSION]`
The genRunTask[VERSION] task creates a perfect mapping workspace with all the game libraries + the game itself
on the classpath so that mapping is easier. It also automatically loads the jar and mappings making it easier for you!


## Questions
### How do I map server-side stuff?
Simple! Just head to the gradle.properties and switch the side to `SERVER`.
Don't worry it's not case-sensitive :)