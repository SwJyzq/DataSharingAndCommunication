package z.android.communication.datasharingandcommunication.clients;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread{
    private Socket client;
    private String ip;
    private int port;
    private ClientThread clientThread;
    private boolean isRunning = false;

    public class ClientThread extends  Thread{
        private ClientInputThread inputThread;
        private ClientOutputThread outputThread;

        public ClientThread(Socket socket){
            inputThread = new ClientInputThread(socket);
            outputThread = new ClientOutputThread(socket);
        }

        @Override
        public void run() {
            inputThread.setStart(true);
            outputThread.setStart(true);
            inputThread.start();
            outputThread.start();
        }

        public ClientInputThread getInputThread() {
            return inputThread;
        }

        public ClientOutputThread getOutputThread() {
            return outputThread;
        }

    }

    @Override
    public void run() {
        super.run();
        isRunning = true;
        Log.d("clientThread", "run: clientThread  is running");
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(ip,port),2000);
            if (client.isConnected()){
                clientThread = new ClientThread(client);
                clientThread.start();
                Log.d("clientThread", "run: clientThread  is running");
            }
        }catch (IOException e){
            e.printStackTrace();
           isRunning = false;
        }catch (Exception e){
            Log.d("clientStart", e.toString());
            isRunning = false;
        }
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    public Client(String ip,int port){
        this.ip = ip;
        this.port = port;
    }

    public ClientOutputThread getClientOutputThread(){
        return clientThread.getOutputThread();
    }

    public ClientInputThread getClientInputThread(){
        return clientThread.getInputThread();
    }

    public void setIsRunning(boolean isRunning){
        clientThread.getInputThread().setStart(isRunning);
        clientThread.getOutputThread().setStart(isRunning);
    }
}
