package sequencer;

import java.io.*;
import java.util.*;

public class History extends Hashtable
{

    public History()
    {
        historyCleanedTo = -1L;
        senders = new Hashtable();
    }

    public synchronized void noteReceived(String sender, long recvd)
    {
        senders.put(sender, new Long(recvd));
    }

    public synchronized void addMsg(String sender, long sequence, byte msg[])
    {
        if(msg != null)
            put(new Long(sequence), msg);
        if(size() > 1024)
        {
            System.out.println("CLEAN HISTORY; size: " + size());
            long min = 0x7fffffffffffffffL;
            for(Enumeration enum1 = senders.keys(); enum1.hasMoreElements();)
            {
                String sent = (String)enum1.nextElement();
                Long got = (Long)senders.get(sent);
                long have = got.longValue();
                System.out.println(sent + " has received " + have);
                if(have < min)
                    min = have;
            }

            System.out.println("clean from " + (historyCleanedTo + 1L) + " to " + min);
            for(long s = historyCleanedTo + 1L; s <= min; s++)
            {
                remove(new Long(s));
                historyCleanedTo = s;
            }

            System.out.println("CLEANED HISTORY; size is now " + size());
        }
    }

    public byte[] getMsg(long sequence)
    {
        return (byte[])get(new Long(sequence));
    }

    public synchronized void eraseSender(String sender)
    {
        senders.remove(sender);
    }

    private Hashtable senders;
    public static final int MAX_HISTORY = 1024;
    long historyCleanedTo;
}
