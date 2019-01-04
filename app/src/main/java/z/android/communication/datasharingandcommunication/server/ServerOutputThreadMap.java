package z.android.communication.datasharingandcommunication.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerOutputThreadMap {
    private HashMap<Integer,ServerOutputThread> map;
    private static  ServerOutputThreadMap instance;

    private ServerOutputThreadMap(){
        map = new HashMap<Integer, ServerOutputThread>();
    }

    public synchronized static ServerOutputThreadMap getInstance(){
        if (instance == null){
            instance = new ServerOutputThreadMap();
        }
        return instance;
    }

    public synchronized void add(Integer id,ServerOutputThread serverOutputThread){
        map.put(id,serverOutputThread);
    }

    public synchronized void remove(Integer id){
        map.remove(id);
    }

    public synchronized  ServerOutputThread getServerOutputThreadById(Integer id){
        return map.get(id);
    }

    public synchronized List<ServerOutputThread> getAll(){
        List<ServerOutputThread> serverOutputThreadList = new ArrayList<ServerOutputThread>();
        for (Map.Entry<Integer,ServerOutputThread> entry :
                map.entrySet()) {
            serverOutputThreadList.add(entry.getValue());
        }
        return serverOutputThreadList;
    }
}
