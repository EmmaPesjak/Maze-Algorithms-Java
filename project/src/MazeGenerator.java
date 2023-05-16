
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.imageio.ImageIO;



// RADERA DENNA??? mvh Emma

public class MazeGenerator {

    private BufferedImage mazeImage = null;
    private boolean maze[][];

    public MazeGenerator() throws Exception {
        maze = generateMaze("project/src/mazeImages/mazetest.png");
        for (boolean[] row : maze) {
            System.out.println(Arrays.toString(row));
        }
        printMaze();
    }

    private boolean[][] generateMaze(String url) throws IOException {

        // Get image
        BufferedImage image = ImageIO.read(new File(url));

        // Get sizes
        int width = image.getWidth();
        int height = image.getHeight();

        boolean[][] maze = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                // if the pixel is black, it is a wall, otherwise it is an open path
                maze[y][x] = (pixel == 0xFF000000);
            }
        }
        return maze;
    }

    private void printMaze() throws Exception {
        try (PrintWriter out = new PrintWriter("project/src/output.txt")) {
            for (boolean[] row : maze) {
                for (boolean cell : row) {
                    out.print(cell ? "#" : " "); // print a space for an open cell and # for a wall
                }
                out.println();
            }
        } catch (FileNotFoundException e) {
            throw new Exception("Could not create output file");
        }
    }


}
