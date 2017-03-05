
import java.awt.Font;


public class Solve implements Runnable{
    
    static boolean solvable = true;
    
    public void run(){
        try{
            Main.g.solved = false;
            solvable = true;
            long start = System.nanoTime();
            solvable = solvable(Main.g.Solution, Main.g.filled);
            long end = System.nanoTime();
            Main.g.text.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(30)));
            Main.g.time = (double)((end-start)/1000000);
            if (Main.g.text.getText().equals("\n\nSolving...")){
                Main.g.executing = false;
                for (int a = 0; a< Main.g.Rows.length; a++){
                    for (int b = 0; b<Main.g.Rows[a].length; b++){
                        Main.g.Rows[a][b] = Main.g.Solution[a][b];
                    }
                }
                if (Main.s.solvable){
                    Main.g.text.setText("Sudoku has been solved!\n\n"+Main.g.numsteps+" iterations were used\n\nIt took "+Main.g.time+" Milliseconds to solve");
                    Main.g.solved = true;
                }
                else if(Main.g.editted){
                    Main.g.text.setText("\n\nSudoku could not be solved.\n\nOne or more of your inputs is invalid");
                }
                else if(Main.g.editted){
                    Main.g.text.setText("\n\nSudoku could not be solved.\n\nUnsolvable grid will be resolved in the future");
                    Main.rg.sudokus.remove(Main.rg.r);
                }
                Main.g.repaint();
            }
        }catch(Exception e){}
    }
    
    public boolean solvable(int[][]array, int[][]filled){
        for (int a = 0; a<9; a++){
            for (int b = 0; b<9; b++){
                if (array[a][b] == 0){
                    solve(array, filled, 0,0);
                    if (array[a][b] == 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean solve(int[][]array, int[][]filled, int row, int column){
        if (row<=8 && column<=8 && (filled[row][column]==0)){
            for (int b = 1; b<=9;b++){
                if (check(row, column, b, array)==false && array[row][column]!=b){
                    array[row][column]=b;
                    if(Main.g.executing){
                        Main.g.numsteps++;
                        Main.g.Rows[row][column]=b;
                        if (Main.g.showsteps){
                            try{
                                Thread.sleep(Main.g.speed);
                            }catch(InterruptedException e){}
                        }
                    }
                    if((column>=8 && solve(array, filled, row+1, 0) == true)||(column<8 && solve(array, filled, row, column+1)==true)){
                        return true;
                    }
                }
            }
            array[row][column] = 0;
            if(Main.g.executing){
                Main.g.Rows[row][column]=0;
            }
            if (Main.g.showsteps){
                try{
                    Thread.sleep(Main.g.speed);
                }catch(InterruptedException e){}
            }
            return false;
        }
        else if (row<=8 && column<=8 && filled[row][column]!=0){
            if (column>=8){
                return solve(array, filled, row+1, 0);
            }
            else if(column<8){
                return solve(array, filled, row, column+1);
            }
        }
        return true;
    }
    
    public void print(int[][]array){
        for (int a = 0; a<array.length; a++){
            for (int b = 0; b<array[a].length; b++){
                System.out.println(array[a][b]+"  ");
            }
            System.out.println("");
        }
    }
    
    
    
//boolean checked = true, done1 = false, done2 = false, done3 = false;
    public boolean check(int row, int column, int num, int[][]array){
        for(int e = 0;e<array[row].length;e++){
            if(array[row][e]==num){return true;}
            else if(array[e][column]==num){return true;}
        }
        int a = 0, b = 0, c = 0, d = 0;
        int[][] s = {{1, 2},{1, -1},{-2, -1}};
        a = s[row%3][0];
        b = s[row%3][1];
        c = s[column%3][0];
        d = s[column%3][1];
        /*
        if (row == 0 || row == 3 || row == 6){a = 1; b = 2;}
        else if (row == 1 || row == 4 || row == 7){a = 1; b = -1;}
        else if (row == 2 || row == 5 || row == 8){a = -2; b = -1;}
        if (column == 0 || column == 3 || column == 6){c = 1; d = 2;}
        else if (column == 1 || column == 4 || column == 7){c = 1; d = -1;}
        else if (column == 2 || column == 5 || column == 8){c = -2; d = -1;}
        */
        int[]checkrow = {0, a, b};
        int[]checkcolumn = {0, c, d};
        for (int x = 0; x<checkrow.length; x++){
            for (int y = 0; y<checkcolumn.length; y++){
                if (array[row+checkrow[x]][column+checkcolumn[y]]==num){
                    return true;
                }
            }
        }
//        if (array[row][column]==num||array[row][column+c]==num||array[row][column+d]==num||array[row+a][column]==num||array[row+a][column+c]==num||array[row+a][column+d]==num||array[row+b][column]==num||array[row+b][column+c]==num||array[row+b][column+d]==num){
//            return true;
//        }
        return false;
        
//        checked = true;
//       done1 = false;  done2 = false;  done3 = false;
//        System.out.println("checking");
//        Thread t1 = new Thread(new Check(1,row,column, num));
//        Thread t2 = new Thread(new Check(2,row,column, num));
//        Thread t3 = new Thread(new Check(3,row,column, num));
//        while(true){
//            if (done1 == true && done2 == true && done3 == true){
//                return checked;
//            }
//            else if (1>3){break;}
//        }
//        return true;
    }
}
