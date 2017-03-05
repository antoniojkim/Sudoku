
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame{
    
    int width = (int)(p.getScreenWidth()*3.0/4.0), height = (int)(p.getScreenHeight()*7.0/9.0);
    
    public Window(){
        super("Sudoku by Antonio Kim");
        setLayout(new BorderLayout());
        setSize(width, height);
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
    }
    
    public void Switch(JPanel panel1, JPanel panel2){
        panel1.setVisible(false);
        remove(panel1);
        add(panel2, BorderLayout.CENTER);
        panel2.setVisible(true);
    }
    
    public void SetSize(int x, int y){
        setSize(x, y);
        setLocationRelativeTo(null);
    }
    
    public double getWindowDiagonal(){
        return Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
    }
}
