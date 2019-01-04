package z.android.communication.datasharingandcommunication.clients;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;

public class ClientOutputThread extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private boolean isRunning = true;
    private TranObject msg;

    public ClientOutputThread(Socket socket){
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void setStart(boolean isRunning){
        this.isRunning = isRunning;
    }

    public void setMsg(TranObject tranObject){
        this.msg = tranObject;
        synchronized (this){
            notify();
        }
    }

    @Override
    public void run() {
        try{
            while (isRunning){
                if (msg!=null){
                    objectOutputStream.writeObject(msg);
                    objectOutputStream.flush();
                    if (msg.getType()== TranObjectType.LOGOUT)
                        break;
                }
                synchronized (this){
                    wait();
                }
            }
//            objectOutputStream.close();
//            if (socket!=null){
//                socket.close();
//                Log.d("clientOutputThread", "run: socket is closed");
//            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
