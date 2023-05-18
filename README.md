
1. A maze generator from the given maze image. The maze images can be downloaded from here . The suggestion is to use a medium rectangle maze such as Maze 7.
2. Find the shortest path with Dijkstra's algorithm. The solution shall be implemented in two versions, i.e., using two different data structures. For example, one implementation uses a heap as a data structure.
3. Find the shortest path with the A* algorithm.
4. Display the maze and the shortest path found for all implementations.
5. Calculate the run time complexity of all solutions and compare them.


- Download maze-image and render a maze using e.g. BufferedImage or Tesseract. Should be generic to be able
  render any type of maze.
- Find the shortest path using Dijkstra's
  - Two implementations with different datastructures 
    - Heap
    - ???
- Shortest path using A*
- Display maze with the shortest paths for all algorithms. 
- Time complexity??? 



Ska vi typ skriva ut hur lång shortest path blev i GUIt?

Ska vi ta tiden på varje algo och printa ut i GUIt?

# TODO'S:

- Verkar som att 2d boolean array är mest efficient i vårt allt, eftersom alla pixels har samma weights. en graph är
  bättre om man har olika graph strukturer och mer flexibel, men i vårt fall är det mög. dessutom så fick vi ner tiden
  när vi bara genererar maze 1 gång. 


xxx Printa ut tiden i gui
xxx Se till så att användaren dirigeras om till start om koordinaterna är fel 

- Kirra storleken på framen och bilderna så pack() blir fint
- Kirra load grejen
- Kirra exekveringen av programmet så att errors på null path visas och sånt mög
- Kolla time complexity
- Städa upp kod + kommentarer
- Kirra MVG grejer om vi pallar: välja om koordinater istället för att dirigeras om till att välja fil
