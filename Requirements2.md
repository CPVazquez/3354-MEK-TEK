#Functional Requirements
System 1: PolyCraft App

System 2: Database 
1. The Database will be manually built and stored in a SQLite Database on the android device by the developer
2. The DatabaseHandler will interact and retrive information from the Database using SQL queries
	1. The DatabaseHandler will get recipe details, including the inventory used, inventory image, the input and output item names/IDs, and their respective multiplicities.
	2. The DatabaseHandler will get item details, including image, name, and ID.
3. The DatabaseHandler will build a tree that displays the ordered steps required to "craft" or assemble in-game any item that cannot otherwise be found naturally.
	1. The Nodes of the constructed tree will detail all recipes and items needed, including the inventories (processing machines) required, their respective item inputs and outputs, and their input/output multiplicities.
	2. Items considered "baseItems", or items found naturally in-game, will be leaf nodes of this tree
	3. The Root Node of the tree is the item being "crafted"
