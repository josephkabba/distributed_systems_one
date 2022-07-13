package sequencer;

import java.io.*;
import java.rmi.*;

// Referenced classes of package sequencer:
//            SequencerImpl

public final class SequencerImpl_Skel
    implements Skeleton
{

    public Operation[] getOperations()
    {
        return operations;
    }

    public void dispatch(Remote remote, RemoteCall remotecall, int i, long l)
        throws RemoteException, Exception
    {
        if(l != 0x46fe5f6f059e0674L)
            throw new SkeletonMismatchException("Hash mismatch");
        SequencerImpl sequencerimpl = (SequencerImpl)remote;
        switch(i)
        {
        case 0: // '\0'
            long l1;
            try
            {
                ObjectInput objectinput2 = remotecall.getInputStream();
                l1 = objectinput2.readLong();
            }
            catch(IOException ioexception6)
            {
                throw new UnmarshalException("Error unmarshaling arguments", ioexception6);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            byte abyte0[] = sequencerimpl.getMissing(l1);
            try
            {
                ObjectOutput objectoutput1 = remotecall.getResultStream(true);
                objectoutput1.writeObject(abyte0);
                return;
            }
            catch(IOException ioexception2)
            {
                throw new MarshalException("Error marshaling return", ioexception2);
            }

        case 1: // '\001'
            String s;
            long l2;
            try
            {
                ObjectInput objectinput3 = remotecall.getInputStream();
                s = (String)objectinput3.readObject();
                l2 = objectinput3.readLong();
            }
            catch(IOException ioexception7)
            {
                throw new UnmarshalException("Error unmarshaling arguments", ioexception7);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            sequencerimpl.heartbeat(s, l2);
            try
            {
                remotecall.getResultStream(true);
                return;
            }
            catch(IOException ioexception3)
            {
                throw new MarshalException("Error marshaling return", ioexception3);
            }

        case 2: // '\002'
            String s1;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                s1 = (String)objectinput.readObject();
            }
            catch(IOException ioexception4)
            {
                throw new UnmarshalException("Error unmarshaling arguments", ioexception4);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            SequencerJoinInfo sequencerjoininfo = sequencerimpl.join(s1);
            try
            {
                ObjectOutput objectoutput = remotecall.getResultStream(true);
                objectoutput.writeObject(sequencerjoininfo);
                return;
            }
            catch(IOException ioexception1)
            {
                throw new MarshalException("Error marshaling return", ioexception1);
            }

        case 3: // '\003'
            String s2;
            try
            {
                ObjectInput objectinput1 = remotecall.getInputStream();
                s2 = (String)objectinput1.readObject();
            }
            catch(IOException ioexception5)
            {
                throw new UnmarshalException("Error unmarshaling arguments", ioexception5);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            sequencerimpl.leave(s2);
            try
            {
                remotecall.getResultStream(true);
                return;
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("Error marshaling return", ioexception);
            }

        case 4: // '\004'
            String s3;
            long l3;
            long l4;
            byte abyte1[];
            try
            {
                ObjectInput objectinput4 = remotecall.getInputStream();
                s3 = (String)objectinput4.readObject();
                l3 = objectinput4.readLong();
                l4 = objectinput4.readLong();
                abyte1 = (byte[])objectinput4.readObject();
            }
            catch(IOException ioexception9)
            {
                throw new UnmarshalException("Error unmarshaling arguments", ioexception9);
            }
            finally
            {
                remotecall.releaseInputStream();
            }
            sequencerimpl.send(s3, l3, l4, abyte1);
            try
            {
                remotecall.getResultStream(true);
                return;
            }
            catch(IOException ioexception8)
            {
                throw new MarshalException("Error marshaling return", ioexception8);
            }
        }
        throw new RemoteException("Method number out of range");
    }

    public SequencerImpl_Skel()
    {
    }

    private static Operation operations[] = {
        new Operation("byte getMissing(long)[]"), new Operation("void heartbeat(java.lang.String, long)"), new Operation("sequencer.SequencerJoinInfo join(java.lang.String)"), new Operation("void leave(java.lang.String)"), new Operation("void send(java.lang.String, long, long, byte[])")
    };
    private static final long interfaceHash = 0x46fe5f6f059e0674L;

}
