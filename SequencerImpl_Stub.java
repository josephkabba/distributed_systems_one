package sequencer;

import java.io.*;
import java.rmi.*;

// Referenced classes of package sequencer:
//            Sequencer, SequencerException, SequencerJoinInfo

public final class SequencerImpl_Stub extends RemoteStub
    implements Sequencer, Remote
{

    public SequencerImpl_Stub()
    {
    }

    public SequencerImpl_Stub(RemoteRef remoteref)
    {
        super(remoteref);
    }

    public byte[] getMissing(long l)
        throws RemoteException, SequencerException
    {
        int i = 0;
        RemoteRef remoteref = super.ref;
        RemoteCall remotecall = remoteref.newCall(this, operations, i, 0x46fe5f6f059e0674L);
        try
        {
            ObjectOutput objectoutput = remotecall.getOutputStream();
            objectoutput.writeLong(l);
        }
        catch(IOException ioexception)
        {
            throw new MarshalException("Error marshaling arguments", ioexception);
        }
        try
        {
            remoteref.invoke(remotecall);
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(SequencerException sequencerexception)
        {
            throw sequencerexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("Unexpected exception", exception);
        }
        byte abyte0[];
        try
        {
            ObjectInput objectinput = remotecall.getInputStream();
            abyte0 = (byte[])objectinput.readObject();
        }
        catch(IOException ioexception1)
        {
            throw new UnmarshalException("Error unmarshaling return", ioexception1);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new UnmarshalException("Return value class not found", classnotfoundexception);
        }
        catch(Exception exception2)
        {
            throw new UnexpectedException("Unexpected exception", exception2);
        }
        finally
        {
            remoteref.done(remotecall);
        }
        return abyte0;
    }

    public void heartbeat(String s, long l)
        throws RemoteException
    {
        int i = 1;
        RemoteRef remoteref = super.ref;
        RemoteCall remotecall = remoteref.newCall(this, operations, i, 0x46fe5f6f059e0674L);
        try
        {
            ObjectOutput objectoutput = remotecall.getOutputStream();
            objectoutput.writeObject(s);
            objectoutput.writeLong(l);
        }
        catch(IOException ioexception)
        {
            throw new MarshalException("Error marshaling arguments", ioexception);
        }
        try
        {
            remoteref.invoke(remotecall);
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("Unexpected exception", exception);
        }
        remoteref.done(remotecall);
    }

    public SequencerJoinInfo join(String s)
        throws RemoteException, SequencerException
    {
        byte byte0 = 2;
        RemoteRef remoteref = super.ref;
        RemoteCall remotecall = remoteref.newCall(this, operations, byte0, 0x46fe5f6f059e0674L);
        try
        {
            ObjectOutput objectoutput = remotecall.getOutputStream();
            objectoutput.writeObject(s);
        }
        catch(IOException ioexception)
        {
            throw new MarshalException("Error marshaling arguments", ioexception);
        }
        try
        {
            remoteref.invoke(remotecall);
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(SequencerException sequencerexception)
        {
            throw sequencerexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("Unexpected exception", exception);
        }
        SequencerJoinInfo sequencerjoininfo;
        try
        {
            ObjectInput objectinput = remotecall.getInputStream();
            sequencerjoininfo = (SequencerJoinInfo)objectinput.readObject();
        }
        catch(IOException ioexception1)
        {
            throw new UnmarshalException("Error unmarshaling return", ioexception1);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new UnmarshalException("Return value class not found", classnotfoundexception);
        }
        catch(Exception exception2)
        {
            throw new UnexpectedException("Unexpected exception", exception2);
        }
        finally
        {
            remoteref.done(remotecall);
        }
        return sequencerjoininfo;
    }

    public void leave(String s)
        throws RemoteException
    {
        byte byte0 = 3;
        RemoteRef remoteref = super.ref;
        RemoteCall remotecall = remoteref.newCall(this, operations, byte0, 0x46fe5f6f059e0674L);
        try
        {
            ObjectOutput objectoutput = remotecall.getOutputStream();
            objectoutput.writeObject(s);
        }
        catch(IOException ioexception)
        {
            throw new MarshalException("Error marshaling arguments", ioexception);
        }
        try
        {
            remoteref.invoke(remotecall);
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("Unexpected exception", exception);
        }
        remoteref.done(remotecall);
    }

    public void send(String s, long l, long l1, byte abyte0[])
        throws RemoteException
    {
        byte byte0 = 4;
        RemoteRef remoteref = super.ref;
        RemoteCall remotecall = remoteref.newCall(this, operations, byte0, 0x46fe5f6f059e0674L);
        try
        {
            ObjectOutput objectoutput = remotecall.getOutputStream();
            objectoutput.writeObject(s);
            objectoutput.writeLong(l);
            objectoutput.writeLong(l1);
            objectoutput.writeObject(abyte0);
        }
        catch(IOException ioexception)
        {
            throw new MarshalException("Error marshaling arguments", ioexception);
        }
        try
        {
            remoteref.invoke(remotecall);
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("Unexpected exception", exception);
        }
        remoteref.done(remotecall);
    }

    private static Operation operations[] = {
        new Operation("byte getMissing(long)[]"), new Operation("void heartbeat(java.lang.String, long)"), new Operation("sequencer.SequencerJoinInfo join(java.lang.String)"), new Operation("void leave(java.lang.String)"), new Operation("void send(java.lang.String, long, long, byte[])")
    };
    private static final long interfaceHash = 0x46fe5f6f059e0674L;

}
