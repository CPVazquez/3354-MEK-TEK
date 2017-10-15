#Functional Requirements
System 1: PolyCraft App User interface
1. The Adjust view functionality will allow users to interact with the graphically generated tree view
        1. The Adjust view functionality will have the features panning and zooming.
        2. Panning will allow the user to move the view accross the pre-generated tree, revealing unseen portions and refeshing the screen.
        3. Zooming will allow the user to enlarge or minimize the current view of the tree and refreshing the screen by changing the number of tree levels shown.
2. The Get Node Details functionality will allow the user to view node image, name, and Id for a node on the graphically generated tree view.
        1. The user tapping on a node will trigger an interal query that will return the requested information.
3. The Search function will allow users to search for a polymer that is not naturally occuring in-game. 
    1. The DatabaseHandler will initiate an asynchronous database query for the polymer using the input search string

