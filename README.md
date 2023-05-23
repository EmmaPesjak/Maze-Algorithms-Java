
# TODO'S:
 
Features & annat:
- Finslipa time-complexity.
- Städa upp kod + kommentarer.
- MVG-grejer ???

  


KVAR I README:
* method
* discussion
* Run time complexity



# Project

## Group Members details
Emma Pesjak, empe2105.
Ebba Nimér, ebni2100.
Link to repository:
https://bitbucket.org/EmmaPesjak/dt183g_project/src/master/

## Environment & Tools
Emma: This assignment was performed on a Windows 10 PC with IntelliJ version 2022.2.3, Java version openjdk 19.0.1,
and Git version 2.33.0.windows.2.

Ebba: Microsoft Windows 10 Home 64-bit, IntelliJ IDEA 2021.3.3, Command Prompt, openjdk 17.0.2 2022-01-18, 
OpenJDK Runtime Environment (build 17.0.2+8-86), OpenJDK 64-Bit Server VM (build 17.0.2+8-86, mixed mode, sharing),
Apache Maven 3.8.5, and Git version 2.33.0.windows.2

## Purpose
The purpose of this project was to create a maze-solving application in Java. Specifically, create a graphical user
interface (GUI) with swing adhering to the Model-View-Controller (MVC) design pattern. Also, to implement two
different varieties of Dijkstra's algorithm (using different data structures), and the A* algorithm for the pathfinding
in the maze.

## Introduction
Dijkstra's algorithm and the A* search algorithm are commonly used for pathfinding. They are both used for finding the
shortest path in a weighted graph such as a maze, a map etc. The A* algorithm is slightly smarter (in some cases, 
not always in mazes since the shortest path may actually lead away from the finish point) than Dijkstra's algorithm 
since it takes a heuristic value into consideration, going towards the goal. The heuristic value is an estimation 
of the remaining distance from the current point to the goal point, and in this program, not taking the wall obstacles 
of the maze into consideration.

### About the project
When the user starts the application, the main screen is displayed, prompting the user to enter the name of a file
depicting a maze image. A `Select Maze` button lets the user proceed if a proper file name has been entered, otherwise,
a message dialog is shown describing any errors. The user is then prompted to select start and finish coordinates by
clicking on the screen. At any time, the user can go back to the main screen with a `Go back` button. When the start
and finish coordinates have been selected, a `Solve Maze` button appears, which launches the algorithm calculations. If
the user has selected invalid coordinates (e.g. outside the maze, too close to or in a wall), or if no path has been 
found, the user will be prompted to re-pick the start and finish points. If paths have been found, these and their 
calculation times will be displayed on the screen. The user can then restart the program and run a new maze by pressing 
the `Run new maze` button. The program is assumed to run on standard to large screens and has not been adapted to fit 
smaller screens, since this course is focused on algorithms and not design, this should not be an issue for the purpose
of the program.

## Methodology
The MVC pattern was the base structure and architecture used for this project. Different packages for models, views,
and controllers contained almost all the classes and interfaces. A `Constants` interface was created in the support
package. This interface contained defined constant values used throughout the program by the different classes.
The `Main` class was the program's main starting point and initiated the `MainController` in an AWT event dispatch 
thread for the GUI to update properly.

The `MainController` was responsible for communication with the `MainModel` and the `MainView`, accessing data from 
the view, telling the model to calculate the data, and then telling the view to update. Action listeners for the View's 
buttons were implemented as inner classes. Three different action listeners were implemented; the 
`SelectButtonListener`, the `SolveButtonListener`, and the `RestartButtonListener`. The`SelectButtonListener` simply
ensured that the user had entered a valid file name, displaying error messages in alert dialogs if not, then 
communicating the file name to the `MainModel` which produced the JPanel of the image to be displayed in the `MainView`.
The `SolveButtonListener` got the start and finish coordinates from the `MainView`, checked if they were valid through 
the `MainModel`, letting the user re-pick if not, then ran the different algorithms through the `MainModel`, 
calculating the times taken, and lastly displaying the results to the user through the `MainView`. The 
`RestartButtonListener` simply cleared the values of the user's chosen coordinates in the `MainModel` and initiated 
the main screen of the `MainView`. A private `rePick()` method was also implemented in the `MainController`. This 
method was responsible for redirecting the user in the GUI to change coordinates if no valid path was found. The method 
was used by the `MainModel` through the `RePickWhenNoPath` interface for communicating between `MainModel` and 
`MainController` without the `MainModel` being aware of the `MainController`, ensuring that the MVC pattern was 
followed.

The `MainView` was responsible for creating the GUI for the application. The class initiated the JFrame and the 
constructor set the action listeners used by the buttons. To make the code more readable and since all the buttons of 
the application and almost all the panels and labels shared the same base design, the generic classes `CustomButton`,
`CustomLabel`, `CustomCalcLabel`, and `CustomPanel` were created. BEHÖVER VI FÖRKLARA MER SWING???

The `MainModel` class was responsible for handling the application's data, performing calculations, and
communicating with the controller. The `MainModel` class was completely in charge of the logic for

EBBA SKRIVER DU OM MAINMODEL/mazegenerating och pathdrawing?

Emma skriver om algoritmerna??? ELLER SKRIVER EBBA OM ARV OCH SÅNT SHIT??

### Project plan
Figure 1 depicts a Gantt chart roughly describing the project plan that has been followed for this project.

![](projectplan.png)<br>
Figure 1. Gantt chart of the project plan.

## Discussion

DISKUSSION HÄR OBV

by letting the user themselves pick start and finish point the interactivity of the program is increased, and it becomes
more flexible as different paths can be found in the same maze...

### Run time complexity
Calculate the run time complexity of all solutions and compare them. Snacka om bigO och 
- Verkar som att 2d boolean array är mest efficient i vårt allt, eftersom alla pixels har samma weights. en graph är
  bättre om man har olika graph strukturer och mer flexibel, men i vårt fall är det mög. dessutom så fick vi ner tiden
  när vi bara genererar maze 1 gång.


### Personal reflections
This project has taught us a lot about different pathfinding algorithms, image processing and GUIs. It has been 
challenging but rewarding. The learning modules of the course has provided us with background knowledge of different
datastructures and algorithms to complete this project.
