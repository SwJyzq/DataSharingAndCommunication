package z.android.communication.datasharingandcommunication.server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class Server extends Thread{
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private boolean isRunning = true;
    private ExecutorService executorService;
    private Context DB_Context;

    public Server(Context db_context){
        try {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
            serverSocket = new ServerSocket(Constants.PORT);
            DB_Context = db_context;
        }catch (Exception e){
            Toast.makeText(db_context,e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("serverStart", "Server: startFailed");
            quit();
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning){
                socket = serverSocket.accept();
                if (socket.isConnected()){
                    executorService.execute(new SocketTask(socket));
                }
//                if (socket!=null)
//                    socket.close();
//                if (serverSocket!=null)
//                    serverSocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private final class SocketTask implements Runnable{
        private Socket socket ;
        private ServerInputThread inputThread;
        private ServerOutputThread outputThread;
        private ServerOutputThreadMap outputThreadMap;

        public SocketTask(Socket socket){
            this.socket = socket;
            outputThreadMap = ServerOutputThreadMap.getInstance();
        }
        @Override
        public void run() {
            outputThread = new ServerOutputThread(socket,outputThreadMap);
            inputThread = new ServerInputThread(socket,outputThread,outputThreadMap,DB_Context);
            outputThread.setStart(isRunning);
            inputThread.setStart(isRunning);
            inputThread.start();
            outputThread.start();
        }
    }

    public void quit(){
        try{
            this.isRunning = false;
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
