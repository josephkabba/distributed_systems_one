package sequencer;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

// Referenced classes of package sequencer:
//            SequencerJoinInfo, History, SequencerException, Sequencer

public class SequencerImpl extends UnicastRemoteObject
    implements Sequencer
{

    public SequencerImpl(String name)
        throws RemoteException
    {
        this.name = name;
        try
        {
            history = new History();
            mySenders = new Vector();
            socket = new MulticastSocket();
            groupAddr = InetAddress.getByName("228.5.6.7");
           // groupAddr = InetAddress.getByName("172.16.4.124");
        }
        catch(Exception ex)
        {
            System.out.println("Couldn't initialise seq: " + ex);
        }
    }

    public synchronized SequencerJoinInfo join(String senderName)
        throws RemoteException, SequencerException
    {
        if(mySenders.contains(senderName))
        {
            throw new SequencerException(senderName + " not unique");
        } else
        {
            mySenders.addElement(senderName);
            history.noteReceived(senderName, sequence);
            return new SequencerJoinInfo(groupAddr, sequence);
        }
    }

    public synchronized void send(String sender, long id, long lastRecd, byte msg[])
        throws RemoteException
    {
        try
        {
            ByteArrayOutputStream bstream = new ByteArrayOutputStream(10240);
            DataOutputStream dstream = new DataOutputStream(bstream);
            dstream.writeLong(++sequence);
            dstream.write(msg, 0, msg.length);
            socket.send(new DatagramPacket(bstream.toByteArray(), bstream.size(), groupAddr, 10000));
        }
        catch(Exception ex)
        {
            System.out.println("problem sending by sequ " + ex);
        }
        history.noteReceived(sender, lastRecd);
        history.addMsg(sender, sequence, msg);
    }

    public byte[] getMissing(long sequence)
        throws RemoteException, SequencerException
    {
        byte found[] = history.getMsg(sequence);
        if(found != null)
        {
            System.out.println("Sequencer supplies missing " + sequence);
            return found;
        } else
        {
            System.out.println("Sequencer couldn't find missing " + sequence);
            throw new SequencerException("Couldn't find missing " + sequence);
        }
    }

    public synchronized void heartbeat(String sender, long lastRecd)
    {
        System.out.println(sender + " HEARTBEAT: " + lastRecd);
        history.noteReceived(sender, lastRecd);
    }

    public synchronized void leave(String senderName)
        throws RemoteException
    {
        mySenders.removeElement(senderName);
        history.eraseSender(senderName);
    }

    public static void main(String args[])
    {
        System.setSecurityManager(new RMISecurityManager());
        try
        {
            SequencerImpl impl = new SequencerImpl("TKSequencer");
            Naming.rebind("/TKSequencer", impl);
            System.out.println("Bound OK");
        }
        catch(Exception ex)
        {
            System.out.println("Sequencer server failed: " + ex);
        }
    }

    private int sequence;
    private String name;
    MulticastSocket socket;
    public static final int MAX_MSG_LENGTH = 10240;
    private static final String ipAddr = "228.5.6.7";
   // private static final String ipAddr = "172.16.4.124";
    public static final int GROUP_PORT = 10000;
    InetAddress groupAddr;
    History history;
    Vector mySenders;
}
