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

## Functional Requirements ##
1. The search function will allow users to search for a specific polymer name
    1. The system will search for final polymer by full scientific name
    2. The system will initiate an asynchronous database query for the polymer
    3. The query will retrieve information (ID, Quantity, Inventory) and create a recipe tree from the “start” (the searched object) to the “end” (initial raw materials)
2. The user interface handler will allow users to view the recipe tree in a graphical format
    1. The interface will construct a graphical node displaying the icon of each ingredient
    2. The interface will display an item ID (name) and amount for each node
    3. The interface will connect each node to its predecessors and children with the appropriate in:ventory
    4. Tapping a node on the recipe tree will display information (quantity needed, frequency of occurrence, typical locations, in game icon, and item ID) about the specific item
    5. Dragging on the recipe tree will allow traversal along the sequential inventory steps for the desired element 
3. The Database Handler will :make queries to the Database
[comment]: <> (The Database will be constructed from a subset of tabular data from Polycraft. The Database will reflect actual items from Polycraft. The Database will contain recipes for crafting items using the different inventories)
    1. 
    2. 
    3. The Handler will pass item ID, item quantities, and inventories used to derive child or parent nodes
    4. The Handler, given an item ID, can return inventory and the item IDs of the items it was crafted from  
    5. The Handler, given an item ID, can return an icon representing the item in-game.
    6. The Handler, given an item ID, can return item details (frequency of occurrence, typical locations)
    7. The Database Handler can be given a new tabular data file to update itself
## Use Case Diagram ##
[Diagram can be found here](UseCase.png)
