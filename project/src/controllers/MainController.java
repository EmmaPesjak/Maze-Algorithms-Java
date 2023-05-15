package controllers;

import models.MainModel;
import views.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 * @author Emma Pesjak
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
                    mainView.showMaze(mainModel.createImage(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords())); // här får man ju slänga in en bild istället
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
            try {
                if (true) {
                    mainModel.showPoints(); // Make sure that the start and finish points are showing.
                    // Skicka  in solvade mazar här från model. Här får vi ju fixa så vi inte anropar tre gånger!
                    mainView.displayResults(mainModel.createImage(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords()), mainModel.createImage(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords()), mainModel.createImage(mainView.getFileName(), mainView.getStartCoords(), mainView.getFinishCoords()));
                } else {
                    mainView.displayErrorMsg("Did not work.");
                }
            } catch (IOException exception) {
                mainView.displayErrorMsg("Something went wrong, please restart the program and try again.");
            }
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
