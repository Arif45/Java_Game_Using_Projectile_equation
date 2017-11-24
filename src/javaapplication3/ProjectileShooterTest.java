
package javaapplication3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
/*class basket extends JPanel 
{
   private Image image= new ImageIcon("b.png").getImage();
  
   public void paint(Graphics g)
   {
       g.drawImage(image, 100, 100, null);
   }

    
    
 }*/

public class ProjectileShooterTest
{
    
    
    
    public static void main(String[] args)
    {
       
       
        //basket bas= new basket();
       
        
       
        
        
        
       // SwingUtilities.invokeLater(new Runnable()
        //{
           // @Override
           //public void run()
            //{
                // JLabel scoreLabel = new JLabel("Score: " +scoreclass.getscore());
            //Home h1 = new Home();
            //h1.setVisible(true);
            //this.dispose();
            
              
              
                createAndShowGUI();
           //}
       //});
    }

    static void createAndShowGUI()
    {
       
        JPanel panel = new JPanel();
        JButton button = new JButton("button1");


           panel.setLayout(null);
            button.setBounds(100,100,20,20);
             panel.add(button);
       
         
        JFrame f = new JFrame();
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(550,500);
        f.setTitle("GRAB THE OVAL");
      

        final ProjectileShooter projectileShooter = 
            new ProjectileShooter();
        ProjectileShooterPanel projectileShooterPanel = 
            new ProjectileShooterPanel(projectileShooter);
        projectileShooter.setPaintingComponent(projectileShooterPanel);
       

        JPanel controlPanel = new JPanel(new GridLayout(1,0));
        JPanel j= new JPanel();

        controlPanel.add(new JLabel("Angle"));
        final JSlider angleSlider = new JSlider(0, 90, 45);
         //angleSlider.setBackground(Color.green);
        controlPanel.add(angleSlider);

        controlPanel.add(new JLabel("Power"));
        final JSlider powerSlider = new JSlider(0, 100, 50);
       // powerSlider.setBackground(Color.green);
        controlPanel.add(powerSlider);
   
        
       
         JButton shootButton = new JButton("GRAB");
         shootButton.setBackground(Color.green);
          controlPanel.add(shootButton);
      
       
        shootButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int angleDeg = angleSlider.getValue();
                int power = powerSlider.getValue();
                projectileShooter.setAngle(Math.toRadians(angleDeg));
                projectileShooter.setPower(power);
                projectileShooter.shoot();
            }
        });
        controlPanel.add(shootButton);
      
         

        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(controlPanel, BorderLayout.NORTH);
        f.getContentPane().add(projectileShooterPanel, BorderLayout.CENTER);
         f.getContentPane().add(panel, BorderLayout.SOUTH);
       
        f.setVisible(true);
       
        
        
        
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
/*class ImagePanel extends JPanel {

  private Image img;

  public ImagePanel(String img) {
    this(new ImageIcon(img).getImage());
  }

  public ImagePanel(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(img, 0,0, null);
  }

}*/

class ProjectileShooter
{
    private double angleRad = Math.toRadians(45);
    private double power = 50;
    private Projectile projectile;
    private JComponent paintingComponent;

    void setPaintingComponent(JComponent paintingComponent)
    {
        this.paintingComponent = paintingComponent;
    }

    void setAngle(double angleRad)
    {
        this.angleRad = angleRad;
    }

    void setPower(double power)
    {
        this.power = power;
    }

    void shoot()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                executeShot();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void executeShot()
    {
        if (projectile != null)
        {
            return;
        }
        projectile = new Projectile();

        Point2D velocity = 
            AffineTransform.getRotateInstance(angleRad).
                transform(new Point2D.Double(1,0), null);
        velocity.setLocation(
            velocity.getX() * power * 0.5, 
            velocity.getY() * power * 0.5);
        projectile.setVelocity(velocity);

        //System.out.println("Initial "+velocity);

        long prevTime = System.nanoTime();
        while (projectile.getPosition().getY() >= 0)
        {
            long currentTime = System.nanoTime();
            double dt = 3 * (currentTime - prevTime) / 1e8;
            projectile.performTimeStep(dt);

            prevTime = currentTime;
            paintingComponent.repaint();
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }

        projectile = null;
        paintingComponent.repaint();
    }

    Projectile getProjectile()
    {
        return projectile;
    }
}

class Projectile
{
    private final Point2D ACCELERATION = new Point2D.Double(0, -9.81 * 0.1);

    private final Point2D position = new Point2D.Double();
    private final Point2D velocity = new Point2D.Double();

    public Point2D getPosition()
    {
        return new Point2D.Double(position.getX(), position.getY());
    }
    public void setPosition(Point2D point)
    {
        position.setLocation(point);
    }

    public void setVelocity(Point2D point)
    {
        velocity.setLocation(point);
    }

    void performTimeStep(double dt)
    {
        scaleAddAssign(velocity, dt, ACCELERATION);
        scaleAddAssign(position, dt, velocity);

        //System.out.println("Now at "+position+" with "+velocity);
    }

    private static void scaleAddAssign(
        Point2D result, double factor, Point2D addend)
    {
        double x = result.getX() + factor * addend.getX();
        double y = result.getY() + factor * addend.getY();
        result.setLocation(x, y);
    }

}
/*class Score
 {
    int score=0;
   
   public void someoneScored()
    {
      score++;
      
    }
  public  int getscore()
   {
       return score ;
   }
 }*/
 

class ProjectileShooterPanel extends JPanel implements Runnable
{
    //private Image backgroundImage;
   // backgroundImage = ImageIO.read(new File(back.png));
  // JLabel background = new JLabel(new ImageIcon(back.png));
 ImageIcon img = new ImageIcon("pic/bird.png");
 ImageIcon img1 = new ImageIcon("pic/back.png");
 ImageIcon img2 = new ImageIcon("pic/over.png");
 ImageIcon img3 = new ImageIcon("pic/back1.jpg");
    
    int score=0,k;
    public static int i=0;
  // scoreclass s2= new scoreclass(); 
    //scoreclass s1= new scoreclass();
  
   Circle v1,v2;
    int p=500;
    private Thread t ; 
    
     //private Image image= new ImageIcon("b.png").getImage();
    private final ProjectileShooter projectileShooter;
    boolean collision,m=true,n=true;

    public ProjectileShooterPanel(ProjectileShooter projectileShooter)
    {
        this.projectileShooter = projectileShooter;
            
             v1= new Circle(400,p);
             
              
             JLabel scoreLabel = new JLabel();
             add(scoreLabel);
               t=new Thread(this);
              t.start();
    }
 
    

    @Override
    protected void paintComponent(Graphics gr)
    {
   
     
        super.paintComponent(gr);
        //this.setBackground(Color.BLACK);
        Graphics2D g = (Graphics2D)gr;
        Graphics2D g1 = (Graphics2D)gr;
        Graphics2D g2 = (Graphics2D)gr;
        Graphics2D g3 = (Graphics2D)gr;
        Graphics2D g5 = (Graphics2D)gr;
            Graphics2D g4 = (Graphics2D)gr;
            // g5.drawString("GRAB WHEN YOU CAN",0, 100);
        img3.paintIcon(this,g3,0,0);
        if(i==1)
        {
              img2.paintIcon(this,g4,0,0);
        }
       
     
        
        g.drawString(scoreclass.getscore(), 220, 100);
         g1.setColor(Color.RED);
           //g1.fillOval(300,p,20,20);
        // img1.drawImage(g3, 0,0,null);
        if(collision==false && m==true){
          
          //m=false;
          
         g1.fillOval(400,p,20,20);
         
         }
         else
         {
             m=false;
         
             
           
            
         }
         // gr.drawImage(image, 100, 100, null);
         // String fileName = "b.png";
      //Image img = getToolkit().getImage(fileName);
      ///g1.drawImage(img,300,p,20,20)
      
     //img.paintIcon( this, g2,5,5 );
     
   
         
        

        Projectile projectile = projectileShooter.getProjectile();
        if (projectile != null)
        {
            g.setColor(Color.BLUE);
             
            Point2D position = projectile.getPosition();
            int i= (int)position.getX();
            int j = getHeight() - (int)position.getY();
            v2= new Circle(i,j);
            v2.move();
            collision();
           
            img.paintIcon(this,g2,v2.x, v2.y);
            
            
          
         
            
        }
       /*if( i==1)
        {
            img2.paintIcon(this,g4,0,0);
            //score+=5;
            //JOptionPane.showMessageDialog(null, "Your score : " + Score);
            //scoreLabel.setText("Score: " + Score);
         
          //g1.drawString("Collide",300,300);
          
          //g.dispose();
          //g1.dispose();;
          //score++;
        
          //s2.getscore();
         //repaint();
         //scoreclass.someoneScored();
        // System.out.println(scoreclass.getscore());
//         JLabel scoreLabel = new JLabel("Score: " +scoreclass.getscore());
//         add(scoreLabel);
          
          
       }*/
       
        
        
        
    }

    

    @Override
    public void run() {
        while(true)
       {
            v1.move();
           // collision();
      
            if(p<250)
            {
                p=550;
                if(!m)
                {
                    scoreclass.someoneScored();
                }
                else
                {
                     i=1; 
                   
                    JOptionPane.showMessageDialog(null, "YOUR  " + scoreclass.getscore());
                    // scoreLabel.setText("Score: " + scoreclass.getscore());
                    //Graphics g4 = null;
                   // img2.paintIcon(this,g4,0,0);
                    System.exit(0);
       
                   
                    //new Home().setVisible(true);
                  
         
                  
                }
                m=true;
            }
            p-=5;
            v1= new Circle(400,p);
            repaint();
           try{
                t.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectileShooterPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void collision()
    {
        Rectangle rect1=v1.bounds();
        Rectangle rect2=v2.bounds();
        if(rect1.intersects(rect2))
        {
            collision=true;
              //scoreclass.someoneScored();
            
        }
        else{
            collision=false;
            boolean n=false;
            
        }
    }

}

