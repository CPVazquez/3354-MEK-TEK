
# Software Requirements Specification#
## Definitions, Acronyms, and Abbreviations ##

## Functional Requirements ##
1. The search function will allow users to search for a specific polymer name
   1.1. The system shall search for final polymer by full scientific name
   1.2. The system shall initiate an asynchronous database query for the polymer
   1.3. The query will retrieve information (ID, Quantity, Inventory) and create a recipe tree from the “start” (the searched object) to the “end” (initial raw materials)

## Use Case Diagram ##