package models;

// Detta blir ju typ vår MazeGenerator/solver

/**
 * Main model class, responsible for handling the application's data and performing calculations.
 * @author Emma Pesjak
 */
public class MainModel {

    public MainModel() {

    }

    // Kirra mazarna
    public boolean calculate(String fileName) {

        

        // Returnera true om det gick att lösa.
        return (dijkstraOne() && dijkstraTwo() && aStar());
    }

    // Heap
    private boolean dijkstraOne() {
        // Returnera true om det gick att lösa.
        return true;
    }

    // Other data structure
    private boolean dijkstraTwo() {
        // Returnera true om det gick att lösa.
        return true;
    }

    private boolean aStar() {
        // Returnera true om det gick att lösa.
        return true;
    }
}
