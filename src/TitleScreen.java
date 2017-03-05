
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;


public class TitleScreen extends JPanel{
    
    static int mxp = 0, myp = 0;
    static boolean beginpressed = false;
    
    Developers d;
    
    public void run(){
        Main.rg.retrieve();
        addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) {}
            
            @Override
            public void mousePressed(MouseEvent me) {
                int mx = me.getX();
                int my = me.getY();
                mxp = mx;
                myp = my;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                mxp = 0; myp = 0;
                repaint();
                if (beginpressed){
                    Main.w.dispose();
                    p.delay(100);
                    Main.w = new Window();
                    Main.w.SetSize(p.convertX(1100), p.convertY(700));
                    Main.w.setVisible(true);
                    Main.w.add(Main.i);
                    Main.i.key();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent me) {}
            
            @Override
            public void mouseExited(MouseEvent me) { }
        });
        addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent ke) {
                
            }
            
            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                if(keyCode == KeyEvent.VK_SPACE){
                    beginpressed = true;
                    mxp = p.convertX(275);
                    myp = p.convertY(290);
                    repaint();
                    
                }
                else if(keyCode == KeyEvent.VK_BACK_QUOTE){
                    JPasswordField code = new JPasswordField();
                    Object[] message = {
                        "Please enter the developers code: ", code
                    };
                    int option = JOptionPane.showConfirmDialog(null, message, "Developers Mode", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION && code.getText().equalsIgnoreCase("5167")){
                        d = new Developers();
                        d.setVisible(true);
                    }
                    else if (option == JOptionPane.OK_OPTION && !code.getText().equalsIgnoreCase("5167")){
                        JOptionPane.showMessageDialog (null, "Incorrect. Unable to access Developers Mode", "Developers Mode", JOptionPane.YES_NO_OPTION);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent ke) {
                if(beginpressed){
                    beginpressed = false;
                    mxp = 0;
                    myp = 0;
                    repaint();
                    Main.w.Switch(Main.t, Main.i);
                    Main.w.SetSize(p.convertX(1100), p.convertY(700));
                    Main.i.key();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Main.w.SetSize(p.convertX(700), p.convertY(550));
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(110)));
        String str = "Sudoku";
        int strWidth = p.stringWidth(str, g.getFont());
        int x = (Main.w.getWidth()/2-strWidth/2), y = p.convertY(90);
        g.drawString ("Sudoku", x, y);
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(30)));
        str = "by Antonio Kim";
        strWidth = p.stringWidth(str, g.getFont());
        g.drawString ("by Antonio Kim", (Main.w.getWidth()/2-strWidth/2), y+p.convertY(40));
        g2.setStroke(new BasicStroke(p.getFontSize(3)));
        g.setColor(Color.BLACK);
        y = p.convertY(270);
        addButton(g2, (int)(Main.w.getWidth()/5.0), y, (int)((3.0/5)*Main.w.getWidth()), p.convertY(100), "Begin", p.getFontSize(80));
//        addButton(g2, (int)(Main.w.getWidth()/5.0), y+120, (int)((3.0/5)*Main.w.getWidth()), 100, "Instructions", (Main.w.getWidth()/2-190), y+195, 80);
    }
    public void addButton(Graphics2D g, int a, int b, int c, int d, String string, int size){
        g.drawRect (a, b, c, d);
        if (mxp >= a && mxp <= a+c && myp>=b && myp<=b+d) {
            g.drawRect (a, b, c, d);
            g.setColor(Color.GRAY);
            g.fillRect (a+p.convertX(2), b+p.convertY(2), c-p.convertX(3), d-p.convertY(3));
            g.setColor(Color.BLACK);
            beginpressed = true;
        }
        g.setFont(new Font("Calibri", Font.PLAIN, size));
        g.drawString(string, a+c/2-p.stringWidth(string, g.getFont())/2, b+d/2+p.stringHeight(string, g.getFont())/3);
    }
    
    public void initiate(){
        Main.random = new Thread(Main.rg);
        Main.random.start();
    }
    
    public class Developers extends JFrame{
        
        JTextArea text = new JTextArea();
        
        public Developers(){
            super("Sudoku");
            setLayout(new BorderLayout());
            Main.w.SetSize(p.convertX(700), p.convertY(550));
            setLocationRelativeTo(null);
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    try {
                        Main.rg.save();
                    } catch (IOException ex) {                }
                    try{
                        Main.random.interrupt();
                    } catch(NullPointerException e){}
                    System.exit(0);
                }
            });
            setResizable(false);
            setLayout(null);
            add(text);
            text.setLocation(p.convertX(100), p.convertY(100));      text.setSize(p.convertX(550), p.convertY(430));
            text.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(40)));      text.setBackground(Main.g.defaultcolor);
            text.setLineWrap(true);   text.setWrapStyleWord(true);     text.setEditable(false);     text.setText("");       text.setVisible(true);
            initiate();
        }
    }
    
}
