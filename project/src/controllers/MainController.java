package controllers;

import models.MainModel;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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



                    //JPanel maze = mainModel.createMaze(file, start, end, "dijkstraOne");
                    //mainView.showMaze(maze); // här får man ju slänga in en bild istället
                } catch (IOException ex) {
                    mainView.displayErrorMsg("Please enter a valid file name.");
                }
            } else {
                mainView.displayErrorMsg("Enter a file name!");
            }
        }
    }

    /**
     * Inner class responsible for listening to the solve button.
     */
    class SolveButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Här tror jag att vi kan skippa try/catchen när vi väl har fixat hur vi kör algosarna.
            //try {
                mainModel.showPoints(); // Make sure that the start and finish points are showing.
                Point start = mainView.getStartCoords();
                Point end = mainView.getFinishCoords();

                JPanel path1 = mainModel.displayPath(start, end, "dijkstraOne");
                JPanel path2 = mainModel.displayPath(start, end, "dijkstraTwo");
                JPanel path3 = mainModel.displayPath(start, end, "aStar");

                mainView.displayResults(path1, path2, path3);


                // Skicka  in solvade mazar här från model. Här får vi ju fixa så vi inte anropar tre gånger!
                /*mainView.displayResults(mainModel.createMaze(
                        mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords(), "dijkstraOne"),
                        mainModel.createMaze(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords(), "dijkstraTwo"),
                        mainModel.createMaze(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords(), "aStar"));*/
            //} catch (IOException exception) {
                //mainView.displayErrorMsg("Something went wrong, please restart the program and try again.");
                //System.out.println(exception);
            //}
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
