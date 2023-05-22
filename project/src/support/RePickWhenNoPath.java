package support;

import java.io.IOException;

/**
 * Interface for communicating between MainModel and MainController
 * without the MainModels being aware of the MainController.
 */
public interface RePickWhenNoPath {

    void rePick() throws IOException;
}
