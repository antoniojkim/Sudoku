
import java.awt.BorderLayout;
import java.io.IOException;

public class Main {
    
//    static GUI g = new GUI();
    static Solve s = new Solve();
    static Window w = new Window();
    static TitleScreen t = new TitleScreen();
    static Sudoku g;
    static RandomGrid rg = new RandomGrid();
    static Instructions i = new Instructions();
    static Thread thread;
    static Thread random;
    static boolean running = true;
//    static SudokuLibrary sudoku = new SudokuLibrary();
    
    public static void main(String[] args) throws IOException {
//        sudoku.run();
        w.add(t, BorderLayout.CENTER);
        w.setVisible(true);
        t.run();
//        thread = new Thread(g);
//        thread.start();
//        g.emptyGrid();
    }
    
}
