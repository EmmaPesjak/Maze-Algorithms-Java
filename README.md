
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
`CustomLabel`, `CustomCalcLabel`, and `CustomPanel` were created. 

The `MainModel` class was responsible for managing the application's data, performing calculations, and communicating 
with the controller. It handled the logic for processing the image, generating the maze, and initiating the calculations 
for the pathfinding algorithms.

Initially, the `MainModel` class processed the image using the `ImageIO` tool. The resulting buffered image was then 
passed to the helper method `generateMaze(BufferedImage image)`. The maze was represented as a two-dimensional boolean 
array, where 'false' values indicated coordinates that could not be traversed, such as walls and areas outside the 
boundaries of the maze. Conversely, 'true' values represented open paths. The maze generation process involved analyzing 
the distribution of black and white pixels. To enhance efficiency and avoid processing individual pixels, the maze was 
adjusted to a cell size of five. This approach ensured that white pixels within the walls of the image were accounted 
for while streamlining the maze generation. A cell size of five and a threshold value of 85% were chosen to strike a 
balance between wall thickness and maze efficiency.

To prevent pathfinding coordinates from extending beyond the maze boundaries, a helper method named `identifyBoundaries()`
was employed. This method iterated through the newly created maze, identifying the minimum and maximum x- and y-values 
where walls were present. Another helper method, `isWithingBoundaries(x, y)`, utilized this information to determine 
whether false-values should be added to coordinates outside the walls.

- I DISCUSSION ELLER HÄR?
A two-dimensional boolean array was chosen as the data structure for the maze representation due to its simplicity and 
intuitive nature. Since the weights between cells were constant and equal to 1 in this project, a graph-based 
representation was deemed unnecessary. Graphs become more suitable when the weights between nodes are non-uniform, which 
was not the case here.

Once valid start and end coordinates were selected and verified using the `checkIfValid(Point start, Point end)` method,
the `displayPath(Point start, Point end, String algo)` method was invoked. This method's purpose was to generate a new 
JPanel displaying the paths on the image-maze. It determined which algorithm to use and triggered the corresponding path
finding calculation method. The implemented algorithms included two variations of Dijkstra's algorithm—one utilizing a 
heap with a PriorityQueue and the other employing a Deque—as well as the A* algorithm. These algorithms were implemented 
as separate classes derived from the abstract class `BaseAlgorithm`. Each algorithm's constructor received the 2D 
boolean maze array, start and end point coordinates as arguments.

The Dijkstra's algorithm class encompassed two pathfinding calculation methods: `solveHeap()` and `solveDeque()`. Both 
methods converted the coordinates to instances of `MazePointDijkstra`, a subclass of `BaseMazePoint`, which stored 
distance, point, and previous point values. They also utilized a 2D boolean array to track visited points and 
initialized the distance to 0. The primary distinction between the two methods lay in the chosen data structures for 
storing points to be visited. Both methods iterated through the `openSet` until either the end point was reached or the 
set became empty. They marked the current point as visited, obtained neighboring cells using the `getNeighbors()` 
method in `BaseAlgorithm`, calculated the new distance, and added each neighbor to the `openSet` to visit the next 
neighbor.

Upon finding the final path, the `generateFinalPath()` helper method in `BaseAlgorithm` generated the path by starting 
from the end point and tracing each previous point, ultimately returning a list of points.

The PriorityQueue stored unvisited points based on priority, with the priority determined by the distance from the 
source point. The smallest distance was dequeued first. Conversely, the Deque serves as a flexible data structure, 
functioning as a first-in, first-out (FIFO) or last-in, first-out (LIFO) structure. In this project, the Deque operated 
as a FIFO structure.

# MANNEN jag fattar typ inte heuristic grejerna. kan du lägga till om det behövs mer?
The A* algorithm shared the logic for generating the final path and finding neighbors with the other algorithms, as 
they all derived from the `BaseAlgorithm` abstract class. However, the A* algorithm differed in how it compared 
distances between points. It incorporated a heuristic value to estimate the distance from the current point to the 
endpoint. Apart from this distinction, the A* algorithm shared similarities with the other algorithms.

Once the algorithms completed their calculations, they returned the list representing the shortest path to the 
`MainModel`. The `MainModel` then utilized the `drawPath(Graphics g, List<Point> path)` method to draw the path on the 
JPanel. This method iterated through the points in the shortest path list, drawing lines between each point. 
Subsequently, the controller retrieved this JPanel, along with the execution time for each algorithm, and presented 
them to the view.

### Project plan
Figure 1 depicts a Gantt chart roughly describing the project plan that has been followed for this project.

![](projectplan.png)<br>
Figure 1. Gantt chart of the project plan.

## Discussion

DISKUSSION HÄR OBV

by letting the user themselves pick start and finish point the interactivity of the program is increased, and it becomes
more flexible as different paths can be found in the same maze...

Priority queue vs deque:

PriorityQueue:

PriorityQueue is a data structure that orders its elements based on their priorities.
It provides efficient retrieval of the element with the highest priority (lowest value) using a heap-based implementation.
In Dijkstra's algorithm, a PriorityQueue can be used to store the unvisited nodes, where the priority is determined by the distance from the source node.
The PriorityQueue ensures that the node with the smallest distance is always dequeued first, making it suitable for Dijkstra's algorithm where the shortest distance needs to be determined.
The time complexity for enqueueing an element into a PriorityQueue is O(log N), and the time complexity for dequeuing the element with the highest priority is O(1).
Deque (Double-ended Queue):

Deque is a data structure that allows insertion and removal of elements at both ends (front and rear).
It can function as both a queue (first-in, first-out) and a stack (last-in, first-out).
In Dijkstra's algorithm, a Deque can be used to store the unvisited nodes, where nodes are inserted at one end and dequeued from the other end.
The Deque allows flexibility in terms of the order of node processing. For example, you can implement Dijkstra's algorithm in a breadth-first manner by enqueueing nodes at the rear and dequeuing them from the front, or you can implement it in a depth-first manner by enqueueing nodes at the front and dequeuing them from the rear.
The time complexity for enqueueing and dequeuing an element from a Deque is O(1).


### Run time complexity
Calculate the run time complexity of all solutions and compare them. Snacka om bigO och 
- Verkar som att 2d boolean array är mest efficient i vårt allt, eftersom alla pixels har samma weights. en graph är
  bättre om man har olika graph strukturer och mer flexibel, men i vårt fall är det mög. dessutom så fick vi ner tiden
  när vi bara genererar maze 1 gång.

Deque är O(1) och priority queue är O(n)

### Personal reflections
This project has taught us a lot about different pathfinding algorithms, image processing and GUIs. It has been 
challenging but rewarding. The learning modules of the course has provided us with background knowledge of different
datastructures and algorithms to complete this project.
