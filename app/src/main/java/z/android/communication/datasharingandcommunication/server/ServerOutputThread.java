package z.android.communication.datasharingandcommunication.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import z.android.communication.datasharingandcommunication.transport.TranObject;

public class ServerOutputThread extends Thread {
    private ServerOutputThreadMap outputThreadMap;
    private ObjectOutputStream objectOutputStream;
    private TranObject tranObject;
    private boolean isRunning = true;
    private Socket socket;

    public ServerOutputThread(){ }

    public ServerOutputThread(Socket socket,ServerOutputThreadMap serverOutputThreadMap){
        try{
            this.socket = socket;
            this.outputThreadMap = serverOutputThreadMap;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setStart(boolean isRunning){
        this.isRunning = isRunning;
    }

    public void setMessage(TranObject tranObject){
        this.tranObject = tranObject;
        synchronized (this){
            notify();
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning){
                synchronized (this){
                    wait();
                }
                if (tranObject!=null){
                    objectOutputStream.writeObject(tranObject);
                    objectOutputStream.flush();
                }
//            if (objectOutputStream!=null){
//                    objectOutputStream.close();
//            }
//            if (socket!=null){
//                    socket.close();
//            }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
