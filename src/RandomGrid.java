
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomGrid implements Runnable{
    
//    SudokuLibrary sudoku = new SudokuLibrary();
    static List<Integer> previous = new ArrayList<>();
    static List<int[][]> sudokus = new ArrayList<>();
//    static List<int[][]> iterative = new ArrayList<>();
    static List<Integer> size = new ArrayList<>();
    static int numgrids, numfilled, count, r, delay = 0;
    static boolean scanned = false;
    
    public void getRandom(int difficulty){
        while(true){
            if (scanned){
                r = (int)((sudokus.size()-2)*Math.random()+2);
//                r = 709;
                if(r<sudokus.size() && !previous.contains(r) && ((size.get(r)==(32-difficulty))|| (difficulty <=2 && size.get(r)>=30))){
                    int count = 0;
                    for (int a = 0; a<9; a++){
                        for (int b = 0; b<9; b++){
                            Main.g.generated[a][b] = sudokus.get(r)[a][b];
                            if (sudokus.get(r)[a][b]!=0){
                                count++;
                            }
                        }
                    }
                    if (previous.size()>=25){
                        previous.clear();
                    }
                    previous.add(r);
                    if (count <= 30){
                        break;
                    }
                    else{
//                        System.out.println(sudokus.size());
//                        print(sudokus.get(r));
                        sudokus.remove(r);
                    }
                }
            }
        }
    }
    
    public void getSpecific(int x){
        Main.g.solved = false;
        while(true){
            if (scanned){
                for (int a = 0; a<9; a++){
                    for (int b = 0; b<9; b++){
                        Main.g.Rows[a][b] = sudokus.get(x)[a][b];
                        Main.g.orig[a][b] = sudokus.get(x)[a][b];
                    }
                }
                break;
            }
        }
    }
    
    public void retrieve(){
        BufferedReader filereader;
//        BufferedInputStream filereader = new BufferedInputStream(getClass().getResourceAsStream("Sudoku Library.txt"));
//        BufferedReader filereader2;
        try {
            filereader = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Sudoku Library.txt")));
//            filereader = new BufferedInputStream(getClass().getResourceAsStream("Sudoku Library.txt"));
//            filereader2 = new BufferedReader (new FileReader ("Iterative.txt"));
            numfilled = Integer.parseInt(filereader.readLine());
            numgrids = Integer.parseInt(filereader.readLine());
            for (int a = 0;a <numgrids; a++){
                int[][] newarray = new int[9][9];
                int claim = Integer.parseInt(filereader.readLine());
                int count = 0;
                for (int b = 0; b<newarray.length; b++){
                    for (int c = 0; c<newarray[b].length;c++){
                        newarray[b][c] = Integer.parseInt(filereader.readLine());
                        if (newarray[b][c]!=0){
                            count++;
                        }
                    }
                }
                if (count<=30){
                    size.add(claim);
                    sudokus.add(newarray);
                }
            }
            numgrids = sudokus.size();
            scanned = true;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {        }
    }
    
    @Override
    public void run(){
        int numtested = 0;
//        System.out.println("running");
        while(sudokus.size()<2500){
            Main.t.d.text.setText("Generated "+(sudokus.size()-numgrids)+" solvable grids out of "+numtested+" test grids\n\n"+sudokus.size()+" grids total\n\nGenerating grid with "+numfilled+" numbers inside");
            int[][]array = new int[9][9];
            int[][]copy = new int[9][9];
            int[][] filled = new int[9][9];
            for (int c = 0; c<9; c++){
                for (int d = 0; d<9; d++){
                    filled[c][d] = 0;
                    array[c][d] = 0;
                    copy[c][d] = 0;
                }
            }
//            int numfilled = (int)((30-17+1)*Math.random()+17);
//            numfilled = (int)((30-30+1)*Math.random()+30);
            count = 0;
            long start2 = System.currentTimeMillis();
            while (count<numfilled){
                long start = System.currentTimeMillis();
                while(true){
                    int r1 = (int)(9*Math.random());
                    int r2 = (int)(9*Math.random());
                    int r3 = (int)(9*Math.random()+1);
                    if(!Main.s.check(r1, r2, r3, array)){
                        filled[r1][r2] = r3;
                        array[r1][r2] = r3;
                        copy[r1][r2] = r3;
                        count++;
                        break;
                    }
                    else if ((System.currentTimeMillis()-start) > 10){
                        count = 100;
                        System.out.println("broken");
                        break;
                    }
                }
                if ((System.currentTimeMillis()-start2) > 1000 || count>numfilled){
                    count = 100;
                    System.out.println("broken");
                    break;
                }
            }
            numtested++;
//            count = 0;
//            for (int b = 0; b<array.length; b++){
//                for (int c = 0; c<array[b].length;c++){
//                    if(array[b][c] !=0){
//                        count++;
//                    }
//                }
//            }
            //count == numfilled &&
            if (count<=30){
                if (Main.s.solvable(copy, filled)){
                    size.add(count);
                    sudokus.add(array);
//                    try {
//                        save();
//                    } catch (IOException ex) {  }
                    if (numfilled < 30){
                        numfilled++;
                    }
                    else{
                        numfilled = 17;
                    }
//                    Thread.yield();
                }
            }
            else{
                System.out.println(count + "       "+numfilled);
            }
        }
    }
    
    
    public void print(int[][]array){
        for (int a = 0; a<array.length; a++){
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]+"  ");
            }
            System.out.println("");
        }
    }
    
    public void save() throws IOException{
        PrintWriter printwriter = new PrintWriter (new FileWriter (new File("./src/Library/Sudoku Library.txt")));
        printwriter.println(numfilled);
        printwriter.println(sudokus.size());
        for (int a = 0;a <sudokus.size(); a++){
            printwriter.println(size.get(a));
            for (int b = 0; b<sudokus.get(a).length; b++){
                for (int c = 0; c<sudokus.get(a)[b].length;c++){
                    printwriter.println(sudokus.get(a)[b][c]);
                }
            }
        }
        printwriter.close();
        numgrids = sudokus.size();
    }
}
