package z.android.communication.datasharingandcommunication.clients;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import z.android.communication.datasharingandcommunication.interfaces.MessageListener;
import z.android.communication.datasharingandcommunication.transport.TranObject;

public class ClientInputThread extends Thread {
    private Socket socket;
    private TranObject msg;
    private boolean isRunning;
    private ObjectInputStream objectInputStream;
    private MessageListener messageListener;

    public ClientInputThread(Socket socket){
        this.socket = socket;
        try {
            if (socket.getInputStream()==null)
                Log.d("createClientInputFailed", "ClientInputThread: inputStream is null");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setMessageListener(MessageListener messageListener){
        this.messageListener = messageListener;
    }

    public void setStart(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        try {
            while (isRunning){
                msg = (TranObject)objectInputStream.readObject();
                messageListener.Message(msg);
            }
            objectInputStream.close();
//            if (socket!=null){
//                socket.close();
//                Log.d("clientInputThread", "run: socket is closed");
//            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
