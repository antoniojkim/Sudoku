
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class GUI extends JPanel implements Runnable{
    
    static boolean showsteps = false;
    static boolean textselected = false;
    static boolean executing = false;
    static boolean solvable = false;
    static boolean solved = false;
    static boolean gMode = false;
    static JTextArea text, results = new JTextArea();
    static JSlider slider;
    public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    static int x = 35, y = 10, width = 594, clickR = 0, clickC = 0, clickX = 0, clickY = 0, numsteps = 0, speed = 50;
    static int mxp, myp;
    static long start, end;
    
    SudokuLibrary sudoku = new SudokuLibrary();
    ArrayList<Integer> previous = new ArrayList<>();
    
    static int[][] Rows = new int[9][9];
    static int[][] filled = new int[9][9];
    static int[][] orig = new int[9][9];
    static int[][]Solution = new int[9][9];
    
    RandomGrid rg = new RandomGrid();
//    Help help;
    
    public void run(){
        try{
            setLayout(null);
            add(results);       results.setLocation(700, 30);      results.setSize(350, 250);      results.setFont(new Font("Calibri", Font.PLAIN, 50));       results.setBackground(defaultcolor);
            results.setLineWrap(true);   results.setWrapStyleWord(true);     results.setEditable(false);     results.setText("Welcome to Sudoku!!");       results.setVisible(true);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent me) {                }
                @Override
                public void mousePressed(MouseEvent me) {
                    int mx = me.getX();
                    int my = me.getY();
                    mxp = mx;
                    myp = my;
                    if( mx>(x+width-120) && mx<(x+width+5) && my>(y+width+2) && my<(y+width+52)) {
                        if(showsteps){
                            remove(slider);
                            showsteps = false;
                        }
                        else{
                            showsteps = true;
                            slider = new JSlider(JSlider.HORIZONTAL, 500, 1000, 1001-50);
                            add(slider);
                            slider.setBounds(x, y+width+10, width-130,50);
                            slider.setMinorTickSpacing(100);
                            slider.setMajorTickSpacing(500);
                            slider.setPaintTicks(true);
                            slider.setPaintLabels(true);
                            slider.setLabelTable(slider.createStandardLabels(100));
                            slider.setVisible(true);
                            slider.setToolTipText("This will change the speed of the steps");
                            slider.addChangeListener(new ChangeListener() {
                                @Override
                                public void stateChanged(ChangeEvent ce) {
                                    slider = (JSlider)ce.getSource();
                                    if (!slider.getValueIsAdjusting()) {
                                        speed = 1000-(int)slider.getValue();
                                    }
                                }
                            });
                        }
                    }
                    else if( mx>1055 && mx<1055+30 && my>10 && my<30+45 ){
//                        help = new Help();
//                        help.setVisible(true);
                    }
                    if (!executing){
                        if (mx>720&&mx<1020&&my>430&&my<530 && gMode){
                            randomGrid();
                            results.setText("Generated");
                            repaint();
                        }
                        else if (mx>720&&mx<1020&&my>310&&my<410){
                            emptyGrid();
                            results.setFont(new Font("Calibri", Font.PLAIN, 50));
                            results.setText("Welcome to Sudoku!!");
                            repaint();
                        }
                        else if (mx>720&&mx<1020&&my>550&&my<650){
                            solve();
                        }
                        textselected = TextSelected();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent me) {
                    mxp = 0; myp = 0;
                    if (!textselected){
                        repaint();
                    }
                }
                @Override
                public void mouseEntered(MouseEvent me) {                }
                @Override
                public void mouseExited(MouseEvent me) {                }
            });
            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent ke) {                }
                @Override
                public void keyPressed(KeyEvent ke) {
                    int keyCode = ke.getKeyCode();
                    if(!executing){
                        if (textselected){
                            for (int a = 48; a<=57; a++){
                                if (keyCode == a && orig[clickX][clickY] == 0 && !Main.s.check(clickX, clickY, a-48, Rows)){
                                    Rows[clickX][clickY] = a-48;
                                    repaint();
                                    break;
                                }
                            }
                        }
                        else if (gMode){
                            if (keyCode == KeyEvent.VK_SPACE){
                                randomGrid();
                                results.setText("Generated");
                                mxp = 730; myp = 440;
                                repaint();
                            }
                            else if (keyCode == KeyEvent.VK_ENTER){
                                mxp = 730; myp = 560;
                                repaint();
                                solve();
                            }
                            else if (keyCode == KeyEvent.VK_C){
                                emptyGrid();
                                results.setFont(new Font("Calibri", Font.PLAIN, 50));
                                results.setText("Welcome to Sudoku!!");
                                mxp = 730; myp = 320;
                                repaint();
                            }
                            else if (keyCode == KeyEvent.VK_H){
                                solved = false;
                                for (int a = 0; a<sudoku.sudoku[0].length; a++){
                                    for (int b = 0; b<sudoku.sudoku[0][a].length; b++){
                                        Rows[a][b] = sudoku.sudoku[0][a][b];
                                        orig[a][b] = sudoku.sudoku[0][a][b];
                                    }
                                }
                                repaint();
                            }
                            else if (keyCode == KeyEvent.VK_P){
                                solved = false;
                                while(true){
                                    int r = (int)((sudoku.sudoku.length-2)*Math.random()+2);
                                    if(!previous.contains(r)){
                                        for (int a = 0; a<sudoku.sudoku[r].length; a++){
                                            for (int b = 0; b<sudoku.sudoku[r][a].length; b++){
                                                Rows[a][b] = sudoku.sudoku[r][a][b];
                                                orig[a][b] = sudoku.sudoku[r][a][b];
                                            }
                                        }
                                        if (previous.size()>=7){
                                            previous.clear();
                                            
                                        }
                                        previous.add(r);
                                        break;
                                    }
                                }
                                repaint();
                            }
                            else if (keyCode == KeyEvent.VK_I){
                                solved = false;
                                for (int a = 0; a<sudoku.sudoku[1].length; a++){
                                    for (int b = 0; b<sudoku.sudoku[1][a].length; b++){
                                        Rows[a][b] = sudoku.sudoku[1][a][b];
                                        orig[a][b] = sudoku.sudoku[1][a][b];
                                    }
                                }
                                repaint();
                            }
                        }
                        if (keyCode == KeyEvent.VK_G){
                            if (gMode){
                                gMode = false;
                            }
                            else{
                                gMode = true;
                            }
                        }
                    }
                }
                @Override
                public void keyReleased(KeyEvent ke) {
                    mxp = 0; myp = 0;
                    repaint();
                }
            });
            while(true){
                while(!textselected){
                    setFocusable(true);
                    requestFocusInWindow();
                    repaint();
//                System.out.println("repainted");
                    Thread.sleep(speed);
                }
            }
        }catch(Exception e){}
    }
    
    Thread solve;
    public void solve(){
        if (!solved){
            executing = true;
            solution();
            try {
                solve.join();
            } catch (InterruptedException ex) {        }
            executing = false;
            for (int a = 0;a < Rows.length;a++){
                System.arraycopy(Solution[a], 0, Rows[a], 0, Rows[a].length);
            }
            repaint();
        }
    }
    
    public void solution(){
        for (int a = 0;a < Rows.length;a++){
            System.arraycopy(Rows[a], 0, filled[a], 0, Rows[a].length);
        }
        numsteps = 0;
        solve = new Thread(new Solve());
        solve.start();
    }
    
    public void emptyGrid(){
        solved = false;
        for (int a = 0; a<Rows.length; a++){
            for (int b = 0; b<Rows[a].length; b++){
                Rows[a][b] = 0;
                orig[a][b] = 0;
            }
        }
    }
    public boolean randomGrid(){
        solved = false;
        results.setText("Generating...");
        if(solvable){
            for (int c = 0; c<9; c++){
                for (int d = 0; d<9; d++){
//                    Rows[c][d] = rg.test[c][d];
//                    orig[c][d] = rg.test[c][d];
                }
            }
        }
        else if (!solvable){
            while(true){
                int r = (int)((sudoku.sudoku.length-2)*Math.random()+2);
                if(!previous.contains(r)){
                    for (int a = 0; a<sudoku.sudoku[r].length; a++){
                        for (int b = 0; b<sudoku.sudoku[r][a].length; b++){
                            Rows[a][b] = sudoku.sudoku[r][a][b];
                            orig[a][b] = sudoku.sudoku[r][a][b];
                        }
                    }
                    if (previous.size()>=7){
                        previous.clear();
                        
                    }
                    previous.add(r);
                    break;
                }
            }
        }
        solvable = false;
        Thread t1 = new Thread(rg);
        t1.start();
        return true;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g.drawLine(x, y, x, y+width);
        g.drawLine(x, y, x+width, y);
        g.drawLine(x+width, y, x+width, y+width);
        g.drawLine(x, y+width, x+width, y+width);
        DrawButton (g2, 720, 550, 300, 100, "Solve", 818, 615, 50);
        DrawButton (g2, 720, 430, 300, 100, "Generate", 775, 495, 50);
        DrawButton (g2, 720, 310, 300, 100, "Clear", 818, 375, 50);
        DrawButton (g2, x+width-120,y+width+10, 120, 40, "Show Steps", x+width-108,y+width+35, 20);
        g2.setStroke(new BasicStroke(2));
        if (showsteps && gMode){
            g.setColor(Color.GRAY);
            g.fillRect (x+width-119,y+width+11, 118, 38);
            g.setColor(Color.BLACK);
        }
        g2.setStroke(new BasicStroke(1));
        g.drawRect (1055, 10, 30, 45);
        GridRow(g, g2, y);
        GridColumn(g, g2, x);
        g.setFont(new Font("Calibri", Font.PLAIN, 30));
        DrawValues(g, g2, x+26, y+41, 0, 0);
        g.setFont(new Font("Calibri", Font.PLAIN, 50));
        g.drawString("?", 1060, 50);
    }
    public void DrawValues(Graphics g, Graphics2D g2, int x, int y, int a, int b){
        if (b>8){
            DrawValues(g,g2, 61, y+66, a+1, 0);
        }
        else if ( a<=8 && b<=8 &&Rows[a][b]!=0 ){
            if (orig[a][b] != 0){
                g.setFont(new Font("Calibri", Font.BOLD, 30));
            }
            else{
                g.setFont(new Font("Calibri", Font.PLAIN, 30));
            }
            g.drawString(String.valueOf(Rows[a][b]), x, y);
            if (a<=8 && b<=8){
                DrawValues(g,g2, x+66, y, a, b+1);
            }
        }
        else if ( a<=8 && b<=8 &&Rows[a][b]==0){
            DrawValues(g,g2, x+66, y, a, b+1);
        }
    }
    public void GridRow(Graphics g, Graphics2D g2, int n){
        g2.setStroke(new BasicStroke(1));
        if (n >= y+width){
            g.drawLine(x, n, x+width,n);
        }
        else if (n==y+(width/9*3)||n==y+(width/9*6)){
            g2.setStroke(new BasicStroke(3));
            g.drawLine(x, n, x+width,n);
            GridRow(g,g2, n+(width/9));
        }
        else{
            g.drawLine(x, n, x+width,n);
            GridRow(g,g2, n+(width/9));
        }
    }
    public void GridColumn(Graphics g, Graphics2D g2, int n){
        g2.setStroke(new BasicStroke(1));
        if (n >= x+width){
            g.drawLine(n, y, n, y+width);
        }
        else if (n==x+(width/9*3)||n==x+(width/9*6)){
            g2.setStroke(new BasicStroke(3));
            g.drawLine(n, y, n, y+width);
            GridColumn(g,g2, n+(width/9));
        }
        else{
            g.drawLine(n, y, n, y+width);
            GridColumn(g,g2, n+(width/9));
        }
    }
    public void DrawButton(Graphics2D g, int a, int b, int c, int d, String string, int x, int y, int size){
        g.drawRect (a, b, c, d);
        if (mxp >= a && mxp <= a+c && myp>=b && myp<=b+d) {
            g.drawRect (a, b, c, d);
            g.setColor(Color.GRAY);
            g.fillRect (a+2, b+2, c-3, d-3);
            g.setColor(Color.BLACK);
        }
        g.setFont(new Font("Calibri", Font.PLAIN, size));
        g.drawString(string, x, y);
    }
    public boolean TextSelected(){
        for (int a = 0; a<9; a++){
            for (int b = 0; b<9; b++){
                if ((mxp>(x+66*b)&&mxp<(x+66*(b+1))&&myp>(y+66*a)&&myp<(y+66*(a+1)))){
//                                    System.out.println("Row: "+a+"\nColumn: "+b);
                    clickR = x+66*b;   clickC = y+66*a;       clickX = a;     clickY = b;
                    return true;
                }
            }
        }
        return false;
    }
    
}
