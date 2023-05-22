package controllers;

import models.MainModel;
import support.Constants;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 */
public class MainController {

    private final MainModel mainModel = new MainModel(this::rePick);
    private final MainView mainView = new MainView(new SelectButtonListener(), new SolveButtonListener(), new RestartButtonListener());

    // för ifall algosarna inte hade en path så får man välja om
    private void rePick() throws IOException {
        mainView.init();  // fråga mig inte varför men init måste köras annars blir det ingen bild.
        String file = mainView.getFileName();
        System.out.println("WOHOOOOO!");
        JPanel img = mainModel.getMaze(file);
        mainView.showMaze(img);
        mainView.displayErrorMsg(Constants.ERR_NO_PATH);
    }

    /**
     *  Inner class responsible for listening to the select maze button.
     */
    class SelectButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Objects.equals(mainView.getFileName(), "")) { // Ensure that the user has entered a file name.
                try {
                    // Get values before calling show-maze.
                    String file = mainView.getFileName();

                    JPanel img = mainModel.getMaze(file);
                    mainView.showMaze(img);

                } catch (IOException ex) {
                    mainView.displayErrorMsg(Constants.ERR_NO_VALID_FILE);
                }
            } else {
                mainView.displayErrorMsg(Constants.ERR_NO_FILE_NAME);
            }
        }
    }

    /**
     * Inner class responsible for listening to the solve button.
     */
    class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Point start = mainView.getStartCoords();
            Point end = mainView.getFinishCoords();
            // Run the algorithms and calculate the paths
            mainModel.showPoints(); // Make sure that the start and finish points are showing.
            System.out.println("Start x: " + start.x);

            // Verify that the points are within maze or on an open path.
            if (!mainModel.checkIfValid(start, end)){
                mainView.displayErrorMsg(Constants.ERR_COORD);

                // Allow the user to change coordinates.
                mainModel.clearPoints();
                mainView.clearCoords();
                return;
            }

            long startTime, endTime;
            JPanel path1, path2, path3;

            startTime = System.nanoTime();
            path1 = mainModel.displayPath(start, end, Constants.DIJK_HEAP);
            endTime = System.nanoTime();
            long algorithm1Time = endTime - startTime;

            // Measure the execution time of the second algorithm
            startTime = System.nanoTime();
            path2 = mainModel.displayPath(start, end, Constants.DIJK_DEQ);
            endTime = System.nanoTime();
            long algorithm2Time = endTime - startTime;

            // Measure the execution time of the third algorithm
            startTime = System.nanoTime();
            path3 = mainModel.displayPath(start, end, Constants.ASTAR);
            endTime = System.nanoTime();
            long algorithm3Time = endTime - startTime;



            // HÄR VISAR DEN 0
            /*System.out.println("1List-size: " +mainModel.getListSize());
            boolean heapPath = mainModel.checkIfPath(Constants.DIJK_HEAP);
            boolean dequePath = mainModel.checkIfPath(Constants.DIJK_DEQ);
            boolean astarPath = mainModel.checkIfPath(Constants.ASTAR);*/

            // Varför returnar den false fastän algoritmerna har körts???????
            //System.out.println("H: " + heapPath + ", D:" + dequePath +", A: "+ astarPath);

            // Display the results
            SwingUtilities.invokeLater(() -> {

                // HÄR VISAR DEN 0
                //System.out.println("2List-size: " +mainModel.getListSize());

                mainView.displayResults(path1, path2, path3, algorithm1Time, algorithm2Time, algorithm3Time);
                System.out.println(mainModel.hasPath());
                // HUR KAN DE VISA 0 OM PANELEN VISAR PATHSEN???
                // HÄR VISAR DEN 0
                /*System.out.println("3List-size: " +mainModel.getListSize());

                if (!heapPath && !dequePath && !astarPath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH);
                } else if (!heapPath && !dequePath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_HEAP_DEQUE);
                } else if (!dequePath && !astarPath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_DEQUE_ASTAR);
                } else if (!heapPath && !astarPath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_HEAP_ASTAR);
                } else if (!dequePath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_DEQUE);
                } else if (!heapPath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_HEAP);
                } else if (!astarPath){
                    mainView.displayErrorMsg(Constants.ERR_NO_PATH_ASTAR);
                }*/

                // Varför 0??? Antagligen JPanel hej.
                System.out.println("4List-size: " +mainModel.getListSize());
            });

            // Printas bara när man klickar på "restart"
            System.out.println("5List-size: " +mainModel.getListSize());
        }
    }

    /**
     * Inner class responsible for listening to the restart button.
     */
    class RestartButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            mainModel.clearPoints();
            mainModel.clearMazeData();
            mainView.init();
        }
    }
}
