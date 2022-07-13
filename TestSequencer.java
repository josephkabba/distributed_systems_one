package sequencer;

import java.awt.*;
import java.io.*;
import java.util.*;

// Referenced classes of package sequencer:
//            Group

public class TestSequencer extends Frame
    implements Runnable, Group.MsgHandler, AdjustmentListener, ActionListener
{

    public TestSequencer(String host, String myName)
    {
        super("TestSequencer");
        returned = "Fred";
        paused = false;
        setSize(200, 200);
        this.myName = myName;
        slider = new Scrollbar(0, 0, 10, 0, 100);
        slider.addAdjustmentListener(this);
        rate = slider.getValue();
        stopIt = new Button("Quit");
        stopIt.addActionListener(this);
        pauseIt = new Button("Pause/Continue");
        pauseIt.addActionListener(this);
        text = new TextField(80);
        setLayout(new BorderLayout());
        add("North", text);
        add("East", stopIt);
        add("West", pauseIt);
        add("South", slider);
        try
        {
            group = new Group(host, this, myName);
            t = new Thread(this);
            t.start();
        }
        catch(Exception ex)
        {
            System.out.println("Can't create group: " + ex);
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        rate = slider.getValue();
        slider.setValue(rate);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == stopIt)
        {
            group.leave();
            System.exit(1);
        } else
        if(e.getSource() == pauseIt)
            paused = !paused;
    }

    public void run()
    {
        try
        {
            int i = 0;
            do
            {
                do
                    if(rate <= 90)
                        try
                        {
                            Thread.sleep((90 - rate) * 10);
                        }
                        catch(Exception _ex) { }
                while(paused);
                group.send((new String(myName + ": " + i++)).getBytes());
            } while(true);
        }
        catch(Exception ex)
        {
            System.out.println("Applet exception " + ex);
        }
    }

    public synchronized void handle(int count, byte msg[])
    {
        text.setText(new String(msg, 0, count));
    }

    public static void main(String args[])
    {
        if(args.length < 2)
        {
            System.out.println("Usage: prog host clientName");
        } else
        {
            TestSequencer st = new TestSequencer(args[0], args[1]);
            st.show();
        }
    }

    String returned;
    Group group;
    Thread t;
    Scrollbar slider;
    Button stopIt;
    Button pauseIt;
    TextField text;
    int rate;
    String myName;
    boolean paused;
}
