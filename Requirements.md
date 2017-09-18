# Software Requirements Specification #
## Definitions, Acronyms, and Abbreviations ##

## Functional Requirements ##
1. The search function will allow users to search for a specific polymer name
    1. The system shall search for final polymer by full scientific name
    2. The system shall initiate an asynchronous database query for the polymer
    3. The query will retrieve information (ID, Quantity, Inventory) and create a recipe tree from the “start” (the searched object) to the “end” (initial raw materials)

3. The Database API will be constructed from a subset of tabular data from Polycraft
    1. The Database will reflect actual items from Polycraft
    2. The Database will contain formulas for crafting items using the different inventories
    3. The API can pass item ID, item quantities, and inventories used to derive child or parent nodes
    4. The API, given an item ID, can return inventory and quantity 
    5. The API, given an item ID, can return an icon representing the item in-game.
    6. The API, given an item ID, can return item details (frequency of occurrence, typical locations, etc.)
    7. The Database API can be given a new tabular data file to update itself

## Use Case Diagram ##
