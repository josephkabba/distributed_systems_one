package sequencer;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

// Referenced classes of package sequencer:
//            SequencerJoinInfo, SequencerException, Sequencer, SequencerImpl

public class Group
    implements Runnable
{
    public class HeartBeater extends Thread
    {

        public void run()
        {
            do
                try
                {
                    do
                        Thread.sleep(period * 1000);
                    while((new Date()).getTime() - lastSendTime < (long)(period * 1000));
                    sequencer.heartbeat(myName, lastSequenceRecd);
                }
                catch(Exception _ex) { }
            while(true);
        }

        int period;

        public HeartBeater(int period)
        {
            this.period = period;
        }
    }

    public interface MsgHandler
    {

        public abstract void handle(int i, byte abyte0[]);
    }

    public class GroupException extends Exception
    {

        public GroupException(String s)
        {
            super(s);
        }
    }


    public Group(String host, MsgHandler handler, String name)
        throws GroupException
    {
        lastSequenceRecd = -1L;
        lastSequenceSent = -1L;
        try
        {
            String fred[] = Naming.list("//mpc2/");
            for(int i = 0; i < fred.length; i++)
                System.out.println(String.valueOf(fred[i]));

            myAddr = InetAddress.getLocalHost();
            sequencer = (Sequencer)Naming.lookup("//" + host + "/TKSequencer");
            myName = name + myAddr;
            SequencerJoinInfo joinInfo = sequencer.join(myName);
            groupAddr = joinInfo.addr;
            lastSequenceRecd = joinInfo.sequence;
            System.out.println("ip of group: " + groupAddr);
            socket = new MulticastSocket(10000);
            socket.joinGroup(groupAddr);
            this.handler = handler;
            t = new Thread(this);
            t.start();
            heartBeater = new HeartBeater(5);
            heartBeater.start();
        }
        catch(SequencerException ex)
        {
            System.out.println("Couldn't create group " + ex);
            throw new GroupException(String.valueOf(ex));
        }
        catch(Exception ex)
        {
            System.out.println("Couldn't create group " + ex);
            throw new GroupException("Couldn't join to sequencer");
        }
    }

    public void send(byte msg[])
        throws GroupException
    {
        if(socket != null)
            try
            {
                sequencer.send(myName, ++lastSequenceSent, lastSequenceRecd, msg);
                lastSendTime = (new Date()).getTime();
            }
            catch(Exception ex)
            {
                System.out.println("couldn't contact sequencer " + ex);
                throw new GroupException("Couldn't send to sequencer");
            }
        else
            throw new GroupException("Group not joined");
    }

    public void leave()
    {
        if(socket != null)
            try
            {
                socket.leaveGroup(groupAddr);
                sequencer.leave(myName);
            }
            catch(Exception ex)
            {
                System.out.println("couldn't leave group " + ex);
            }
    }

    public void run()
    {
        try
        {
            while(true) 
            {
                byte buf[] = new byte[10240];
                DatagramPacket dgram = new DatagramPacket(buf, buf.length);
                socket.receive(dgram);
                ByteArrayInputStream bstream = new ByteArrayInputStream(buf, 0, dgram.getLength());
                DataInputStream dstream = new DataInputStream(bstream);
                long gotSequence = dstream.readLong();
                int count = dstream.read(buf);
                long wantSeq = lastSequenceRecd + 1L;
                if(lastSequenceRecd >= 0L && wantSeq < gotSequence)
                {
                    for(long getSeq = wantSeq; getSeq < gotSequence; getSeq++)
                    {
                        byte bufExtra[] = sequencer.getMissing(getSeq);
                        int countExtra = bufExtra.length;
                        System.out.println("Group: fetch missing " + getSeq);
                        handler.handle(countExtra, bufExtra);
                    }

                }
                lastSequenceRecd = gotSequence;
                handler.handle(count, buf);
            }
        }
        catch(Exception ex)
        {
            System.out.println("bad in run " + ex);
        }
    }

    Thread t;
    Thread heartBeater;
    Sequencer sequencer;
    MulticastSocket socket;
    MsgHandler handler;
    long lastSequenceRecd;
    long lastSequenceSent;
    InetAddress groupAddr;
    InetAddress myAddr;
    String myName;
    long lastSendTime;
}
