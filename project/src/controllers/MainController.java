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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 */
public class MainController {

    private final MainModel mainModel = new MainModel();
    private final MainView mainView = new MainView(new SelectButtonListener(), new SolveButtonListener(), new RestartButtonListener());

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

            // Run the algorithms and calculate the paths
            mainModel.showPoints(); // Make sure that the start and finish points are showing.
            Point start = mainView.getStartCoords();
            Point end = mainView.getFinishCoords();

            // Verify that the points are within maze or on an open path.
            if (!mainModel.checkIfValid(start, end)){
                mainView.displayErrorMsg(Constants.ERR_COORD);

                // Initiate view, allowing the user to choose new coordinates and new image.
                mainModel.clearPoints();
                mainView.init();
                return;
            }

            // #TODO fixa så att panelen inte är så jäkla ful.
            mainView.displayLoadingPanel();

            long startTime, endTime;
            JPanel path1, path2, path3;

            startTime = System.currentTimeMillis();
            path1 = mainModel.displayPath(start, end, Constants.DIJK_HEAP);
            endTime = System.currentTimeMillis();
            long algorithm1Time = endTime - startTime;
            System.out.println("1: " + algorithm1Time);

            // Measure the execution time of the second algorithm
            startTime = System.currentTimeMillis();
            path2 = mainModel.displayPath(start, end, Constants.DIJK_DEQ);
            endTime = System.currentTimeMillis();
            long algorithm2Time = endTime - startTime;
            System.out.println("2: " + algorithm2Time);

            // Measure the execution time of the third algorithm
            startTime = System.currentTimeMillis();
            path3 = mainModel.displayPath(start, end, Constants.ASTAR);
            endTime = System.currentTimeMillis();
            long algorithm3Time = endTime - startTime;
            System.out.println("3: " + algorithm3Time);

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

                mainView.closeLoadingPanel();

                mainView.displayResults(path1, path2, path3, algorithm1Time, algorithm2Time, algorithm3Time);

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
                }

                // HÄR VISAR DEN ANTAL (när man klickat OK på dialogrutan
                System.out.println("4List-size: " +mainModel.getListSize());*/
            });
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
            mainView.init();
        }
    }
}
