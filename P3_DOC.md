1. Design pattern
	<br />DatabaseHandler uses the singleton design pattern.<br />It is located at 3354-MEK-TEK/PolycraftApp/app/src/main/java/edu/utdallas/mektek/polycraftapp/DatabaseHandler.java <br />It is instantiated in 3354-MEK-TEK/PolycraftApp/app/src/main/java/edu/utdallas/mektek/polycraftapp/MainActivity.java

2. Test Classes<br />test classes are located in the following folder<br />
	3354-MEK-TEK/PolycraftApp/app/src/test/java/edu/utdallas/mektek/polycraftapp/
	Please note: Both AndroidTest (for UI Tests) and Test (JUnit Tests) were implemented

3. Installation Instructions

	- Clone the repo (download as Zip)
	- Unzip the file and place the directory in an easily accessible location
	- Open the folder using Android Studio
	- Update local.properties with your Android sdk location (you may be prompted for this)
	- build the project.
	- run the project using AVD or a connected Android Phone.
	
4. How to Use the App:
This app uses the distillRecipe.csv and itemInfo.csv tables that are preloaded onto a SQLite Database handled within the app. Valid element searches are as follows (please search the exact string, or you will not see any results and receive instead an "invalid item" error).

	- Drum (Kerosene)
	- Drum (Light Olefins)
	- Drum (Light Parrafins)
	- Drum (Heavy Naphtha)
	
Searching any one of these will draw a visual graph composed of Item nodes (names underneath image)  and Recipe Nodes (recipeID, an integer, underneath image). You can pinch to zoom and drag your finger to navigate around the canvas. 
You can double-tap on a node to see the DetailView launch with Node Details. You can double tap on a recipe to see the RecipeDetail view launch with Recipe details (a list of inputs/outputs and their respective quantities). 
If you scroll to the root node (bottom-most node), you will see that it is Drum (Crude Oil) for all of these elements. Double-tapping this will bring up Crude Oil's details - please note that it's naturally occuring? value is Yes. 
	
Because of the incompleteness of the table, there are possible "trees" that can be built with a root node that is not "naturally occuring". 
This incompleteness requires the user to utilize multiple Polycraft Inventories that contain more recipes bridging the gap. 
An example of this "incompleteness" can be seen when searching.

	- Beaker (Heavy Naphtha)
	- Flask (Nitrogen)
	- Flask (Ethane) *This is cool!! Would recommend trying this one out*
	- Cartridge (Ethylene)
	
All of these systems "end" but their root nodes are not "Naturally Occuring". This is a defect of the database given to us by the development team at PolycraftWorld - we simply built a system that can handle these problems and not "break". 

Please note - while a lot of these items have similar names (e.g. Beaker (Heavy Naphtha), Drum (Heavy Naphtha)), they are very different items and are not interchangeable in game. 
To convert, the user needs to use a different "inventory" that is not a distillation column. Since the scope of our project only includes implementation of one such inventory (namely distillation column), this 'type' conversion is not accounted for.
Otherwise, yes, "Beaker (Light Naphtha)" Should be able to follow a similar path as "Drum (Heavy Naphtha)", if it were converted using a different inventory. 
