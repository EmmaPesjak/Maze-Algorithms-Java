package controllers;

import models.MainModel;
import views.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 * @author Emma Pesjak
 */
public class MainController {

    private final MainModel mainModel = new MainModel();
    private final MainView mainView = new MainView(new SolveButtonListener(), new RestartButtonListener());

    public MainController() {

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
            try {
                if (true) {
                    // Skicka  in solvade mazar här från model. Här får vi ju fixa så vi inte anropar tre gånger!
                    mainView.displayResults(mainModel.calculate(mainView.getFileName()), mainModel.calculate(mainView.getFileName()), mainModel.calculate(mainView.getFileName()));

                } else {
                    mainView.displayErrorMsg("Did not work."); // Annat felmeddeleande här?
                }
            } catch (Exception exception) { // vilken sorts exception? IOException? Mer?
                System.out.println(exception);
                mainView.displayErrorMsg("Please enter a valid file name.");
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
            mainView.init();
        }
    }
}
