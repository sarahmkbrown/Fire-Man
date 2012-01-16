package fireman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FireMan extends JComponent implements KeyListener, ActionListener
{
    public static final int AWAKE_FIREMAN_Y = 0;
    public static final int SLEEPING_FIREMAN_Y = 600 - 30;
    private static final int MAX_WATER = 200;
    private static final int MAX_FIREMAN = 30;
    private static final int WATER_SIZE = 30;
    private static final int FIREMAN_SIZE = 30;
    private static final int HEIGHT = 600;
    private static final int WIDTH = MAX_FIREMAN * FIREMAN_SIZE;
    private static final int MAX_BIRDS = 5;

    private Random r = new Random();
    private boolean[] fireManAwake = new boolean[MAX_FIREMAN];
    private int[] fireManX = new int[MAX_FIREMAN];
    private int[] waterX = new int[MAX_WATER];
    private int[] waterY = new int[MAX_WATER];
    private Image sleepingFireman;
    private Image awakeFireman;
    private Image bucket;
    private Image water;
    private boolean[] waterVisible = new boolean[MAX_WATER];
    private int[] birdX = new int[MAX_BIRDS];
    private int[] birdY = new int[MAX_BIRDS];
    private int[] birdSize = new int[MAX_BIRDS];
    private boolean[] birdVisible = new boolean[MAX_BIRDS];

    public static void main(String[] args) throws IOException
    {
        JFrame window = new JFrame();
        FireMan game = new FireMan();
        window.add(game);
        window.addKeyListener(game);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Timer t = new Timer(100, game);
        t.start();

    }

    public FireMan() throws IOException
    {
        for (int i = 0; i < MAX_FIREMAN; i++)
        {
            fireManAwake[i] = false;
            fireManX[i] = i * 30;
        }

        fireManAwake[MAX_FIREMAN/2] = true;
        for (int i = 0; i < MAX_WATER; i++)
        {
            waterVisible[i] = false;

        }
        for (int i = 0; i < MAX_BIRDS; i++)
        {
           birdVisible[i] = false;
        }
        birdVisible[0] = true;
        birdX[0] = 0;
        birdY[0] = HEIGHT /2;
        birdSize[0] = 25; 

        sleepingFireman = ImageIO.read(getClass().getResource("sleeping.jpg"));
        awakeFireman = ImageIO.read(getClass().getResource("head.jpg"));
        bucket = ImageIO.read(getClass().getResource("bucket.jpg"));


    }

    @Override
    protected void paintComponent(Graphics g)
    {
        for (int i = 0; i < MAX_FIREMAN; i++)
        {
            if (fireManAwake[i])
            {
                g.drawImage(awakeFireman, fireManX[i], AWAKE_FIREMAN_Y, 30, 30, null);
            } else
            {
                g.drawImage(sleepingFireman, fireManX[i], SLEEPING_FIREMAN_Y, 30, 30, null);
            }
        }

        for (int i = 0; i < MAX_WATER; i++)
        {
            if (waterVisible[i])
            {
                g.setColor(Color.BLUE.darker());
                g.fillRect(waterX[i], waterY[i], 30, 30);
            }

        }
        
        for(int i = 0; i < MAX_BIRDS; i++)
        {
            if(birdVisible[i])
            {
                g.setColor(Color.MAGENTA);
                g.fillRect(birdX[i], birdY[i], birdSize[i], birdSize[i]);
  
                    
            }
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH , HEIGHT);
    }

    @Override
    public void keyTyped(KeyEvent ke)
    {
    }

    @Override
    public void keyPressed(KeyEvent ke)
    {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE)
        {
            pourWater();
        }
        
        if(ke.getKeyCode() == KeyEvent.VK_LEFT)
        {
            moveFiremenLeft();
        }
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            moveFiremenRight();
        }
            
        repaint();
    }

    private void moveFiremenRight()
    {
        for( int i =0; i <MAX_FIREMAN; i++)
       {
           if(fireManAwake[i])
           {
               fireManX[i] += 2;
           }
       }
    }

    private void moveFiremenLeft()
    {
        for( int i =0; i <MAX_FIREMAN; i++)
        {
            if(fireManAwake[i])
            {
                fireManX[i] -= 2;
            }
        }
    }

    private void pourWater()
    {
        for (int i = 0; i < MAX_FIREMAN; i++)
        {
            if (fireManAwake[i])
            {
                for( int j= 0; j <MAX_WATER; j++)
                {
                    if(waterVisible[j] == false)
                    {
                        waterVisible[j] = true;
                        waterX[j] = fireManX[i];
                        waterY[j] = 40;
                        break;
                    }
                }
               
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        updateWater();
        updateBirds();
        makeNewBirds();
        
        repaint();
    }

    private void updateBirds()
    {
        for (int i = 0; i < MAX_BIRDS; i++)
        {
            if(birdVisible[i])
            {
                birdX[i] += 20;
                for(int j = 0; j < MAX_WATER; j++) {
                    if (birdX[i] < waterX[j] + WATER_SIZE
                            &&birdX[i] + birdSize[i] > waterX[j]
                            && birdY[i] < waterY[j] + WATER_SIZE
                            && birdY[i] + birdSize[i] > waterY[j]
                            && waterVisible[j])
                    {
                        waterVisible[j] = false;
                        birdSize[i] += 15;
                    }
                }
                if (birdX[i] > WIDTH) 
                {
                    birdVisible[i] = false;
                }
            }
            
            
        }
    }

    private void updateWater()
    {
        for(int i = 0; i < MAX_WATER; i++)
        {
            if(waterVisible[i])
            {
                waterY[i] += 5;
                for (int j = 0; j < MAX_FIREMAN; j++)
                {
                   if(fireManX[j] < waterX[i] + WATER_SIZE 
                           && fireManX[j] + FIREMAN_SIZE >waterX[i]
                           && SLEEPING_FIREMAN_Y < waterY[i] + WATER_SIZE
                           && SLEEPING_FIREMAN_Y + FIREMAN_SIZE> waterY[i]
                     )
                   {
                       fireManAwake[j] = true;
                   }
                    
                }
                
                if(waterY[i] > HEIGHT)
                {
                    waterVisible[i] = false;
                }
            }
        }
    }

    private void makeNewBirds()
    {
        int p = r.nextInt(100);
        if(p < 20)
        {
            for( int i = 0; i< MAX_BIRDS; i++)
            {
                if(birdVisible[i] == false)
                {
                    birdVisible[i] = true;
                    birdSize[i] = r.nextInt(45)+ 5;
                    birdX[i] = -birdSize[i];
                    birdY[i] = r.nextInt(HEIGHT - FIREMAN_SIZE - birdSize[i] - FIREMAN_SIZE  ) + FIREMAN_SIZE;
                    break;
                }
            }

        }
    }
}
