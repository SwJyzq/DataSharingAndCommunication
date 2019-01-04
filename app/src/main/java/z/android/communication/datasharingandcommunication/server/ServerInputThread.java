package z.android.communication.datasharingandcommunication.server;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import z.android.communication.datasharingandcommunication.interfaces.DataDetails;
import z.android.communication.datasharingandcommunication.interfaces.TextMessage;
import z.android.communication.datasharingandcommunication.interfaces.User;
import z.android.communication.datasharingandcommunication.server.implementation.UserDataAccessObjectFactory;
import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;
import z.android.communication.datasharingandcommunication.utils.DatabaseHelper;

public class ServerInputThread extends Thread {
    private Socket socket;
    private ServerOutputThread serverOutputThread;
    private ServerOutputThreadMap serverOutputThreadMap;
    private ObjectInputStream objectInputStream;
    private boolean isRunning = true;
    private Context DB_Context;

    public ServerInputThread(Socket socket,ServerOutputThread serverOutputThread,ServerOutputThreadMap serverOutputThreadMap,Context db_context){
        this.socket = socket;
        this.serverOutputThread = serverOutputThread;
        this.serverOutputThreadMap = serverOutputThreadMap;
        DB_Context = db_context;
        try {
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        }catch (Exception e){
            Log.d("create failed", "ServerInputThread: "+e.toString());
        }
    }

    public void setStart(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        try {
            while (isRunning){
                readMessage();
            }
        }catch (Exception e){
            Log.d("serverInputThread", "run: "+ e.toString());
        }
        super.run();
    }

    public void readMessage()throws IOException,ClassNotFoundException{
        Object readObject = objectInputStream.readObject();
        UserDataAccessObject dataAccessObject = UserDataAccessObjectFactory.getInstance(DB_Context);
        if (readObject!=null&&readObject instanceof TranObject){
            TranObject read_tranObject = (TranObject)readObject;
            switch (read_tranObject.getType()){
                case REGISTER:{
                    User registerUser = (User)read_tranObject.getObject();
                    int registerResult = dataAccessObject.register(registerUser);
                    TranObject<User> register2TranObject = new TranObject<User>(TranObjectType.REGISTER);
                    User register2user = new User();
                    register2user.setId(registerResult);
                    register2TranObject.setObject(register2user);
                    serverOutputThread.setMessage(register2TranObject);
                    break;
                }
                case LOGIN:{
                    User loginUser = (User)read_tranObject.getObject();
                    ArrayList<User> userArrayList = dataAccessObject.login(loginUser);
                    TranObject<ArrayList<User>> login2Object = new TranObject<ArrayList<User>>(TranObjectType.LOGIN);
                    if (userArrayList!=null){
                        TranObject<User> onObject = new TranObject<User>(TranObjectType.LOGIN);
                        User login2User  = new User();
                        login2User.setId(loginUser.getId());
                        onObject.setObject(login2User);
                        for (ServerOutputThread onOut : serverOutputThreadMap.getAll()){
                            onOut.setMessage(onObject);
                        }
                        serverOutputThreadMap.add(login2User.getId(),serverOutputThread);
                        login2Object.setObject(userArrayList);
                    }else {
                        login2Object.setObject(null);
                        Log.d("handle login", "readMessage: Object is set to be null");
                    }
                    serverOutputThread.setMessage(login2Object);
                    break;
                }
                case LOGOUT:{
                    User logoutUser = (User)read_tranObject.getObject();
                    int offId = logoutUser.getId();
                    dataAccessObject.logout(offId);
                    isRunning = false;
                    serverOutputThreadMap.remove(offId);
                    serverOutputThread.setMessage(null);
                    serverOutputThread.setStart(false);
                    TranObject<User> offObject = new TranObject<User>(TranObjectType.LOGOUT);
                    User logout2User = new User();
                    logout2User.setId(offId);
                    offObject.setObject(logout2User);
                    for(ServerOutputThread offOut : serverOutputThreadMap.getAll()){
                        offOut.setMessage(offObject);
                    }
                    break;
                }
                case MESSAGE:{
                    int id2 = read_tranObject.getToUser();
                    ServerOutputThread toOut = serverOutputThreadMap.getServerOutputThreadById(id2);
                    if (toOut!=null){
                        toOut.setMessage(read_tranObject);
                    }else {
                        TextMessage textMessage = new TextMessage();
                        textMessage.setMessage("亲！对方不在线哦，您的消息将暂时保存在服务器");
                        TranObject<TextMessage> offText = new TranObject<TextMessage>(TranObjectType.MESSAGE);
                        offText.setObject(textMessage);
                        offText.setFromUser(0);
                        serverOutputThread.setMessage(offText);
                    }
                    break;
                }
                case REFRESH:{
                    List<User> refreshList = dataAccessObject.refresh(read_tranObject.getFromUser());
                    TranObject<List<User>> refresh0 = new TranObject<List<User>>(TranObjectType.REFRESH);
                    refresh0.setObject(refreshList);
                    serverOutputThread.setMessage(refresh0);
                    break;
                }
                case DATA_DETAIL:{
//                    List<DataDetails> dataDetailsList =
                    TranObject<List<DataDetails>> tran = new TranObject<List<DataDetails>>(TranObjectType.DATA_DETAIL);
                    break;
                }
                case FILE:{
                    break;
                }
                case FRIEND_LOGIN:{
                    break;
                }
                case FRIEND_LOGOUT:{
                    break;
                }
                default:    break;
            }
        }
    }
}
