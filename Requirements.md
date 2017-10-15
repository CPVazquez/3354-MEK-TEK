# Software Requirements Specification #
## Definitions, Acronyms, and Abbreviations ##
| Term      |  Definition                                                |
| --------- | ---------------------------------------------------------- |
| ID        | Identification of Element                                  |
| UI        | User Interface (Android App)                               |
| Polymers  | Subset of elements in polycraft. Also known as Plastics    |
| Handler   | Application programming interface                          |
| Inventory | Chemical process (machining mill, distillation column,etc) |
| Craft     | To create a polycraft item by using an inventory           |
| Item      | A polycraft/minecraft object found in the game (a block)   |  
| Recipe | A formula consisting of item inputs, input quantities, and item outputs and quantities. Each Inventory has many such associated Recipes. |
| Naturally found | An Item that is readily available in the game, like a dirt, coal, water, oil, etc. |

## System 1: Polycraft App User interface ##
1. The Adjust view functionality will allow users to interact with the graphically generated tree view
    1. The Adjust view functionality will have the features panning and zooming.
    2. Panning will allow the user to move the view accross the pre-generated tree, revealing unseen portions and refeshing the screen.
    3. Zooming will allow the user to enlarge or minimize the current view of the tree and refreshing the screen by changing the number of tree levels shown.
2. The Get Node Details functionality will allow the user to view node image, name, and Id for a node on the graphically generated tree view.
    1. The user tapping on a node will trigger an interal query that will return the requested information.
3. The Search function will allow users to search for a polymer that is not naturally occuring in-game. 
    1. The DatabaseHandler will initiate an asynchronous database query for the polymer using the input search string

## System 2: Database ## 
1. The Database will be manually built and stored in a SQLite Database on the android device by the developer
2. The DatabaseHandler will interact and retrive information from the Database using SQL queries
	1. The DatabaseHandler will get recipe details, including the inventory used, inventory image, the input and output item names/IDs, and their respective multiplicities.
	2. The DatabaseHandler will get item details, including image, name, and ID.
3. The DatabaseHandler will build a tree that displays the ordered steps required to "craft" or assemble in-game any item that cannot otherwise be found naturally.
	1. The Nodes of the constructed tree will detail all recipes and items needed, including the inventories (processing machines) required, their respective item inputs and outputs, and their input/output multiplicities.
	2. Items considered "baseItems", or items found naturally in-game, will be leaf nodes of this tree
	3. The Root Node of the tree is the item being "crafted"

## Use Case Diagrams ##
[Polycraft App System Use Case Diagram](Diagrams/UseCasePolycraftApp.png)
[Database System Use Case Diagram](Diagrams/UseCaseDatabaseSystem.png)
