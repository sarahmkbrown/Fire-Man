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
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FireMan extends JComponent implements KeyListener, ActionListener
{
    private static final int MAX_WATER = 200;
    private static final int MAX_FIREMAN = 20;

    private boolean[] fireManAwake = new boolean[MAX_FIREMAN];
    private int[] fireManX = new int[MAX_FIREMAN];
    private int[] waterX = new int[MAX_WATER];
    private int[] waterY = new int[MAX_WATER];
    private Image sleepingFireman;
    private Image awakeFireman;
    private Image bucket;
    private Image water;
    private boolean[] waterVisible = new boolean[MAX_WATER];

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

        fireManAwake[0] = true;
        fireManAwake[5] = true;
        for (int i = 0; i < MAX_WATER; i++)
        {
            waterVisible[i] = false;

        }

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
                g.drawImage(awakeFireman, fireManX[i], 0, 30, 30, null);
            } else
            {
                g.drawImage(sleepingFireman, fireManX[i], 600 - 30, 30, 30, null);
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
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
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
            for (int i = 0; i < MAX_FIREMAN; i++)
            {
                if (fireManAwake[i])
                {
                    waterVisible[i] = true;
                    waterX[i] = fireManX[i];
                    waterY[i] = 40;
                }

            }
        }
        
        if(ke.getKeyCode() == KeyEvent.VK_LEFT)
        {
            for( int i =0; i <MAX_FIREMAN; i++)
            {
                if(fireManAwake[i])
                {
                    fireManX[i] -= 2;
                }
            }
        }
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
        {
             for( int i =0; i <MAX_FIREMAN; i++)
            {
                if(fireManAwake[i])
                {
                    fireManX[i] += 2;
                }
            }
        }
            
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        for(int i = 0; i < MAX_WATER; i++)
        {
            if(waterVisible[i])
            {
                waterY[i] += 5;
            }
        }
        repaint();
    }
}
