
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Instructions extends JPanel {
    
    static JTextArea hotkeys = new JTextArea();
    static JTextArea guide = new JTextArea();
    static JScrollPane pane = new JScrollPane();
    static int mx = 0, my = 0;
    static boolean pressed = false;
    
    public Instructions(){
        setLayout(null);
        add(pane);      pane.setLocation(p.convertX(530), p.convertY(10));      pane.setSize(p.convertX(550), p.convertY(650));   pane.setViewportView(guide);     pane.setBorder(createEmptyBorder());
        guide.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(25)));      guide.setBackground(Main.g.defaultcolor);
        guide.setLineWrap(true);   guide.setWrapStyleWord(true);     guide.setEditable(false);     guide.setVisible(true);
        add(hotkeys);
        hotkeys.setLocation(p.convertX(10), p.convertY(10));      hotkeys.setSize(p.convertX(470), p.convertY(470));
        hotkeys.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(25)));      hotkeys.setBackground(Main.g.defaultcolor);
        hotkeys.setLineWrap(true);   hotkeys.setWrapStyleWord(true);     hotkeys.setEditable(false);     hotkeys.setVisible(true);
        hotkeys.setText("Sudoku Hotkeys:\n\n"
                + "      \"Space\"              Generates a random grid\n\n"
                + "      \"Enter\"               Solves the Current Grid\n\n"
                + "      \"C\"                      Clears the Grid \n\n"
                + "      \"H\"                     Generates World's \n                                  Hardest Sudoku\n\n"
                + "      \"I\"                       Generates \"68 Million\" \n                                  Grid\n\n"
                + "      \"G\"                      Enables G-Mode");
        guide.setText("Sudoku Rules and Instructions:\n\n"
                + "     Sudoku is a puzzle in which the solver's objective is to fill a 9×9 grid with digits so that each column, each row, and each of the nine 3×3 sub-grids that compose the grid contains all of the digits from 1 to 9.\n\n"
                + "     In this program, you start with a blank grid. You can press \"New Puzzle\" to begin playing. To input a number onto the grid you click on the square and then input a number. If the number happens to be a valid entry,"
                + "then you will see a number pop up. An input of 0 will clear the square\n\n     On the screen you will notice a slider. This slider controls the difficulty of the puzzle, or more so the number of filled squares on the grid. The higher the number, the less "
                + "squares show up filled.\n\n     Should you so choose, you can have the program solve it for you. However, you will notice that the \"AutoFill\" button is faded out and wil not work. To enable this button, you "
                + "must enter G-Mode. To do this, you have to press the \"G\" key as shown in the Hotkey's list. Once you press this, you will notice that the buttons become opaque and now you may click on them.\n\n"
                + "     You can have the program show it's steps once you click \"AutoFill\" by pressing the \"Show Steps\" toggle. If the \"Show Steps\" toggle is toggled on a slider will appear. This slider controls the speed at which the"
                + "program shows it's steps. The higher the number, the faster it shows.\n\n     That's it. Enjoy the Program!");
        addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) {            }
            
            @Override
            public void mousePressed(MouseEvent me) {
                mx = me.getX();
                my = me.getY();
//                System.out.println(mx+", "+my);
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                mx = 0;
                my = 0;
                repaint();
                if (pressed){
                    pressed = false;
                    if (Main.g.empty){
                        Main.g = new Sudoku();
                        Main.thread = new Thread( Main.g);
                        Main.thread.start();
                    }
                    Main.w.Switch(Main.i, Main.g);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent me) {            }
            
            @Override
            public void mouseExited(MouseEvent me) {            }
        });
        addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent ke) {            }
            
            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                if (keyCode == KeyEvent.VK_SPACE){
                    pressed = true;
                    mx = p.convertX(330);
                    my = p.convertY(550);
                    repaint();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent ke) {
                if (pressed){
                    pressed = false;
                    mx = 0;
                    my = 0;
                    repaint();
                    if (Main.g.empty){
                        Main.g = new Sudoku();
                        Main.thread = new Thread( Main.g);
                        Main.thread.start();
                    }
                    Main.w.Switch(Main.i, Main.g);
                }
            }
        });
    }
    
    public void key(){
        setFocusable(true);
        requestFocusInWindow();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        addButton(g2, p.convertX(60), p.convertY(520), p.convertX(400), p.convertY(100), "Take me to the Grid", p.getFontSize(45));
    }
    public void addButton(Graphics2D g, int a, int b, int c, int d, String string, int size){
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(p.getFontSize(3)));
        g.drawRect (a, b, c, d);
        if (mx >= a && mx <= a+c && my>=b && my<=b+d) {
            g.setColor(Color.GRAY);
            g.fillRect (a+p.convertX(2), b+p.convertY(2), c-p.convertX(3), d-p.convertY(3));
            g.setColor(Color.BLACK);
            repaint();
            pressed = true;
        }
        g.setFont(new Font("Calibri", Font.PLAIN, size));
        g.drawString(string, a+c/2-p.stringWidth(string, g.getFont())/2, b+d/2+p.stringHeight(string, g.getFont())/3);
    }
}



