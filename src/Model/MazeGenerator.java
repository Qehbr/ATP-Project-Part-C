package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Helper class for MyModel, that contains maze logic
 */
public class MazeGenerator {

    private Maze maze = null; //curr maze
    private Solution solution = null; //solution for the maze

    /**
     * Starts a server which generates a new maze, and client gets it
     *
     * @param rows rows of the maze
     * @param cols columns of the maze
     * @return
     */
    public void generateMaze(int rows, int cols) {
        //update solution
        solution = null;

        Server serverMazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze()
        );
        serverMazeGenerator.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //ask server for maze
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        //get maze
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream inputStream = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 24];
                        inputStream.read(decompressedMaze);
                        //save the maze
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverMazeGenerator.stop();
    }

    /**
     * Starts a server which solves current maze, and client gets the solution
     *
     * @param newStartPosition Position of the player, from which we should find the solution
     */
    public void solveMaze(Position newStartPosition) {
        Server serverMazeGenerator = new Server(5400, 1000, new ServerStrategySolveSearchProblem()
        );
        serverMazeGenerator.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //make start position of the maze - player position
                        maze.setStartPosition(newStartPosition);
                        //send maze to the server
                        toServer.writeObject(maze);
                        toServer.flush();
                        //get the solution
                        solution = (Solution) fromServer.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverMazeGenerator.stop();
    }

    /**
     * Save generated maze by chosen path, and update its start position by player position
     *
     * @param chosen
     * @param player_row
     * @param player_col
     */
    public void saveMaze(String chosen, int player_row, int player_col) {
        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(chosen));
            //update position of the player
            maze.setStartPosition(new Position(player_row, player_col, "START"));
            //write to the file
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load maze from chosen path
     *
     * @param chosen path to load maze from
     */
    public void loadMaze(String chosen) {
        //get number of rows and columns of the maze
        solution = null;
        byte[] rowsAndCols = new byte[24];
        try {
            // read maze from file
            InputStream in = new FileInputStream(chosen);
            rowsAndCols = in.readNBytes(24);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rows = ByteBuffer.wrap(rowsAndCols, 16, 4).getInt();
        int cols = ByteBuffer.wrap(rowsAndCols, 20, 4).getInt();


        //read maze from the file
        byte[] savedMazeBytes = new byte[0];
        try {
            InputStream in = new MyDecompressorInputStream(new FileInputStream(chosen));
            //rows and cols used here for proper reading of all bytes
            savedMazeBytes = new byte[rows * cols + 24];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //update the maze
        maze = new Maze(savedMazeBytes);
    }

    /**
     * Getters
     */
    public Maze getMaze() {
        return maze;
    }

    public Solution getSolution() {
        return solution;
    }
}
