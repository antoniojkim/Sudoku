
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

public class Sudoku extends JPanel implements Runnable{
    
    static final int x = p.convertX(35), y = p.convertY(10), width = p.convertX(594), divisions = 9;
    static int mx = 0, my = 0, clickR = 0, clickC = 0, clickX = 0, clickY = 0;
    static int numsteps = 0, speed = 0, difficulty = 8;
    static double time = 0;
    static boolean solved = false, showsteps = false, gMode = false, executing = false, textselected = false, empty = true, first = true, editted = false;
    static boolean autofill = false, newpuzzle = false, clear = false, generate = false, stepspressed = false, helppressed = false;
    public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    
    static JTextArea text = new JTextArea();
    static JScrollPane pane = new JScrollPane();
    static JSlider slider = new JSlider();
    static JSlider Difficulty = new JSlider();
    
    static int[][]Rows = new int[divisions][divisions];
    static int[][]orig = new int[divisions][divisions];
    static int[][]filled = new int[divisions][divisions];
    static int[][]Solution = new int[divisions][divisions];
    static int[][]generated = new int[divisions][divisions];
    
    private class Mouse implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent me) {}
        @Override
        public void mousePressed(MouseEvent me) {
            mx = me.getX();
            my = me.getY();
            setFocusable(true);
            requestFocusInWindow();
            textselected = TextSelected();
            repaint();
        }
        @Override
        public void mouseReleased(MouseEvent me) {
            mx = 0;
            my = 0;
            if (stepspressed){
                steps();
            }
            else if (helppressed){
                helppressed = false;
                Main.thread.interrupt();
                Main.w.Switch(Main.g, Main.i);
                Main.i.key();
            }
            if(!executing){
                if (autofill){
                    solve();
                    autofill = false;
                    repaint();
                }
                else if (newpuzzle){
                    newpuzzle = false;
                    generate = true;
                    if (empty == false){
                        int regenerate = JOptionPane.showConfirmDialog (null, "Do you wish to generate a new grid?\n\nIf you select yes, all progress on the current Grid will be Erased.", "Generate?", JOptionPane.YES_NO_OPTION);
                        if (regenerate == JOptionPane.YES_OPTION){
                            randomGrid();
                        }
                    }
                    else{
                        randomGrid();
                    }
                    generate = false;
                    repaint();
                    
                }
                else if (generate){
                    randomGrid();
                    generate = false;
                    repaint();
                }
                else if (clear){
                    clear();
                }
            }
        }
        @Override
        public void mouseEntered(MouseEvent me) {}
        @Override
        public void mouseExited(MouseEvent me) {}
    }
    
    @Override
    public void run() {
        try{
            setLayout(null);
            add(pane);      pane.setLocation(p.convertX(695), p.convertY(110));      pane.setSize(p.convertX(350), p.convertX(230));   pane.setViewportView(text);     pane.setBorder(createEmptyBorder());
            text.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(40)));      text.setBackground(defaultcolor);
            text.setLineWrap(true);   text.setWrapStyleWord(true);     text.setEditable(false);     text.setText("");       text.setVisible(true);
            emptyGrid();
            Difficulty();
//            randomGrid();
addMouseListener(new Mouse());
addKeyListener(new KeyBoard());
while(true){
    while(!textselected){
        setFocusable(true);
        requestFocusInWindow();
        repaint();
        Thread.sleep(speed);
    }
}
        }catch(Exception e){}
    }
    
    Thread solver;
    public void solve(){
        if (autofill && !solved){
            text.setText("\n\nSolving...");
            empty = false;
            executing = true;
            solution();
            repaint();
            for (int a = 0; a<Rows.length; a++){
                for (int b = 0; b< Rows[a].length; b++){
                    Rows[a][b] = Solution[a][b];
                }
            }
            if (text.getText().equals("\n\nSolving...") && !executing){
                if (Main.s.solvable){
                    text.setText("Sudoku has been solved!\n\n"+numsteps+" iterations were used\n\nIt took "+time+" Milliseconds to solve");
                }
                else if(!editted){
                    text.setText("\n\nSudoku could not be solved.\n\nOne or more of your inputs is invalid");
                }
                else if(editted){
                    text.setText("\n\nSudoku could not be solved.\n\nUnsolvable grid will be resolved in the future");
                    solved = false;
                }
            }
            repaint();
        }
    }
    
    public void solution(){
        for (int a = 0; a<Rows.length; a++){
            for (int b = 0; b< Rows[a].length; b++){
                filled[a][b] = Rows[a][b];
                Solution[a][b] = filled[a][b];
            }
        }
        numsteps = 0;
        solver = new Thread(new Solve());
        solver.start();
    }
    
    public void randomGrid(){
        solved = false;
        editted = false;
        if (generate){
            Main.rg.getRandom(difficulty);
            empty = false;
            for (int a = 0; a<Rows.length; a++){
                for (int b = 0; b<Rows.length; b++){
                    Rows[a][b] = generated[a][b];
                    orig[a][b] = generated[a][b];
                }
            }
            repaint();
            text.setText("\n\nGenerated");
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(70)));
        g.drawString ("Sudoku", x+width+p.convertX(130), y+p.convertY(50));
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(20)));
        g.drawString ("by Antonio Kim", x+width+p.convertX(180), y+p.convertY(80));
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(p.getFontSize(3)));
        g.drawLine(x, y, x, y+width);
        g.drawLine(x, y, x+width, y);
        g.drawLine(x+width, y, x+width, y+width);
        g.drawLine(x, y+width, x+width, y+width);
        g2.setStroke(new BasicStroke(p.getFontSize(1)));
        drawGrid(g2);
        drawValues(g2);
        addButton(g2, x+width+p.convertX(65), y+width-p.convertY(40), p.convertX(165), p.convertY(65), "Auto-Fill", p.getFontSize(40));
        addButton(g2, x+width+p.convertX(250), y+width-p.convertY(40), p.convertX(165), p.convertY(65), "Generate", p.getFontSize(40));
        addButton(g2, x+width+p.convertX(65), y+width-p.convertY(178), p.convertX(350), p.convertY(65), "New Puzzle", p.getFontSize(45));
        addButton(g2, x+width+p.convertX(65), y+width-p.convertY(255), p.convertX(350), p.convertY(65), "Clear", p.getFontSize(45));
        if (showsteps && gMode){
            g.setColor(Color.GRAY);
            g.fillRect (x+width-p.convertX(122),y+width+p.convertY(12), p.convertX(122), p.convertY(39));
            g.setColor(Color.BLACK);
        }
        addButton (g2, x+width-p.convertX(122),y+width+p.convertY(12), p.convertX(122), p.convertY(39), "Show Steps", p.getFontSize(20));
        addButton (g2, p.convertX(1055), p.convertY(10), p.convertX(30), p.convertY(45), "?", p.getFontSize(50));
    }
    public void addButton(Graphics2D g, int a, int b, int c, int d, String string, int size){
        if ((string.equals("Auto-Fill") || string.equals("Generate") || string.equals("Show Steps")) && !gMode){
            g.setColor(new Color(0,0,0, 50));
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.setStroke(new BasicStroke(p.getFontSize(3)));
        g.drawRect (a, b, c, d);
        if (mx >= a && mx <= a+c && my>=b && my<=b+d && (gMode || (string.equals("New Puzzle") || (string.equals("Clear")  || (string.equals("?")))))) {
            g.setColor(Color.GRAY);
            g.fillRect (a+p.convertX(2), b+p.convertY(2), c-p.convertX(3), d-p.convertY(3));
            g.setColor(Color.BLACK);
            setFocusable(true);
            requestFocusInWindow();
            switch (string) {
                case "Auto-Fill":
                    autofill = true;
                    break;
                case "New Puzzle":
                    newpuzzle = true;
                    break;
                case "Generate":
                    generate = true;
                    break;
                case "Clear":
                    clear = true;
                    break;
                case "Show Steps":
                    stepspressed = true;
                    break;
                case "?":
                    helppressed = true;
                    break;
            }
            repaint();
        }
        g.setFont(new Font("Calibri", Font.PLAIN, size));
        g.drawString(string, a+c/2-p.stringWidth(string, g.getFont())/2, b+d/2+p.stringHeight(string, g.getFont())/3);
    }
    public void drawGrid(Graphics2D g){
        for (int a = 1;a<divisions;a++){
            if (a%3 == 0){
                g.setStroke(new BasicStroke(p.getFontSize(3)));
            }
            g.drawLine(x, y+(width/divisions*a), x+width,y+(width/divisions*a));
            g.drawLine(x+(width/divisions*a), y, x+(width/divisions*a), y+width);
            g.setStroke(new BasicStroke(p.getFontSize(1)));
        }
    }
    public void drawValues(Graphics2D g){
        for (int a = 0; a<divisions; a++){
            for (int b = 0; b<divisions; b++){
                if (Rows[a][b]!=0){
                    empty = false;
                    if (orig[a][b] != 0){
                        g.setFont(new Font("Calibri", Font.BOLD, p.getFontSize(30)));
                    }
                    else{
                        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(30)));
                    }
                    g.drawString(String.valueOf(Rows[a][b]), x+p.convertX(26)+(width/divisions*b), y+p.convertY(41)+(width/divisions*a));
                }
            }
        }
    }
    
    public boolean clear(){
        clear = false;
        solved = false;
        repaint();
        if (gMode){
            for (int c = 0; c<Rows.length; c++){
                for (int d = 0; d<Rows[c].length; d++){
                    Rows[c][d] = orig[c][d];
                }
            }
            repaint();
            return true;
        }
        if (editted){
            int clear = JOptionPane.showConfirmDialog (null, "Do you wish to clear your inputs?\n\nIf you select yes, all progress on the current Grid will be Erased.", "Clear?", JOptionPane.YES_NO_OPTION);
            if (clear == JOptionPane.YES_OPTION){
                for (int c = 0; c<Rows.length; c++){
                    for (int d = 0; d<Rows[c].length; d++){
                        Rows[c][d] = orig[c][d];
                    }
                }
                editted = false;
                repaint();
                return true;
            }
            editted = false;
            return true;
        }
        if (!empty){
            int clear = JOptionPane.showConfirmDialog (null, "Do you wish to clear the grid?\n\nIf you select yes, this generated grid will be Erased.", "Clear?", JOptionPane.YES_NO_OPTION);
            if (clear == JOptionPane.YES_OPTION){
                emptyGrid();
            }
        }
        return true;
    }
    public void emptyGrid(){
        solved = false;
        empty = true;
        generate = false;
        editted = false;
        clear = true;
        for (int a = 0; a<Rows.length; a++){
            for (int b = 0; b<Rows[a].length; b++){
                Rows[a][b] = 0;
                orig[a][b] = 0;
            }
        }
        repaint();
    }
    public void steps(){
        stepspressed = false;
        if (showsteps){
            showsteps = false;
            remove(slider);
            speed = 0;
        }
        else if (!showsteps){
            showsteps = true;
            speed = 50;
            slider = new JSlider(JSlider.HORIZONTAL, 500, 1000, 1001-50);
            add(slider);
            slider.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(20)));
            slider.setBounds(x, y+width+p.convertY(10), width-p.convertX(130), p.convertY(50));
            slider.setMinorTickSpacing(100);
            slider.setMajorTickSpacing(500);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setLabelTable(slider.createStandardLabels(100));
            slider.setVisible(true);
            slider.setToolTipText("This will change how quickly the steps are being made");
            slider.addChangeListener((ChangeEvent ce) -> {
                slider = (JSlider)ce.getSource();
                if (!slider.getValueIsAdjusting()) {
                    speed = 1000-(int)slider.getValue();
                }
            });
        }
        repaint();
    }
    public void Difficulty(){
        Difficulty = new JSlider(JSlider.HORIZONTAL, 0, 15, 8);
        add(Difficulty);
        Difficulty.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(20)));
        Difficulty.setBounds(x+width+p.convertX(70), y+width-p.convertY(100), p.convertX(340), p.convertY(50));
        Difficulty.setMinorTickSpacing(1);
        Difficulty.setMajorTickSpacing(5);
        Difficulty.setPaintTicks(true);
        Difficulty.setPaintLabels(true);
        Difficulty.setVisible(true);
        Difficulty.setToolTipText("This will change the difficulty of the generated puzzles. The higher the number the more difficult");
        Difficulty.addChangeListener((ChangeEvent ce) -> {
            Difficulty = (JSlider)ce.getSource();
            if (!Difficulty.getValueIsAdjusting()) {
                difficulty = (int)Difficulty.getValue();
                randomGrid();
            }
        });
    }
    public boolean TextSelected(){
        for (int a = 0; a<9; a++){
            for (int b = 0; b<9; b++){
                if ((mx>(x+p.convertX(66)*b)&&mx<(x+p.convertX(66)*(b+1))&&my>(y+p.convertY(66)*a)&&my<(y+p.convertY(66)*(a+1)))){
                    clickR = x+p.convertX(66)*b;   clickC = y+p.convertY(66)*a;       clickX = a;     clickY = b;
                    return true;
                }
            }
        }
        return false;
    }
    private class KeyBoard implements KeyListener{
        
        @Override
        public void keyTyped(KeyEvent ke) {        }
        
        @Override
        public void keyPressed(KeyEvent ke) {
            if(!executing){
                int keyCode = ke.getKeyCode();
                if (textselected == true){
                    for (int a = 0; a<=9; a++){
                        if ((keyCode == a+48 || keyCode == a+96) && orig[clickX][clickY] == 0 && (a== 0 || !Main.s.check(clickX, clickY, a, Rows))){
                            editted = true;
                            Rows[clickX][clickY] = a;
                            repaint();
                            break;
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_G){
                    if (gMode){
                        gMode = false;
                        showsteps = false;
                        remove(slider);
                        speed = 0;
                    }
                    else{
                        gMode = true;
                        if (Main.rg.sudokus.size()<1000){
                            Main.t.initiate();
                        }
                    }
                    repaint();
                }
                else if(keyCode == KeyEvent.VK_SPACE && gMode){
                    generate = true;
                    mx = p.convertX(950);
                    my = p.convertY(600);
                    if (editted && !gMode){
                        int regenerate = JOptionPane.showConfirmDialog (null, "Do you wish to generate a new grid?\n\nIf you select yes, all progress on the current Grid will be Erased.", "Generate?", JOptionPane.YES_NO_OPTION);
                        if (regenerate == JOptionPane.YES_OPTION){
                            randomGrid();
                        }
                    }
                    else{
                        randomGrid();
                    }
                    repaint();
                }
                else if(keyCode == KeyEvent.VK_ENTER && gMode){
                    autofill = true;
                    mx = p.convertX(805);
                    my = p.convertY(600);
                    repaint();
                    solve();
                }
                else if(keyCode == KeyEvent.VK_C){
                    clear = true;
                    mx = p.convertX(880);
                    my = p.convertY(380);
                    repaint();
                }
                else if(keyCode == KeyEvent.VK_H && gMode){
                    generate = true;
                    repaint();
                    Main.rg.getSpecific(0);
                    text.setText("\n\nGenerated");
                    repaint();
                }
                else if(keyCode == KeyEvent.VK_I && gMode){
                    generate = true;
                    repaint();
                    Main.rg.getSpecific(1);
                    text.setText("\n\nGenerated");
                    repaint();
                }
            }
        }
        
        @Override
        public void keyReleased(KeyEvent ke) {
            mx = 0;
            my = 0;
            repaint();
            if (autofill){
                autofill = false;
                repaint();
            }
            else if (generate){
                generate = false;
                repaint();
            }
            else if (clear){
                clear();
            }
            repaint();
        }
        
    }
}
