package z.android.communication.datasharingandcommunication.interfaces;

import android.app.Application;
import android.app.NotificationManager;
import android.widget.Toast;

import java.util.LinkedList;

import z.android.communication.datasharingandcommunication.adapters.RecentChatAdapter;
import z.android.communication.datasharingandcommunication.clients.Client;
import z.android.communication.datasharingandcommunication.server.Server;
import z.android.communication.datasharingandcommunication.utils.SharedPreferenceUtil;

public class MyApplication extends Application {
    private Client client;// 客户端
    private Server server;
    private boolean isClientStart;// 客户端连接是否启动
    private NotificationManager mNotificationManager;
    private int newMsgNum = 0;// 后台运行的消息
    private LinkedList<RecentChatEntity> mRecentList;
    private RecentChatAdapter mRecentAdapter;
    private int recentNum = 0;

    @Override
    public void onCreate() {
        SharedPreferenceUtil util = new SharedPreferenceUtil(this,
                Constants.SAVE_USER);
        server = new Server(MyApplication.this);
        server.start();
        client = new Client(util.getIp(), util.getPort());// 从配置文件中读ip和地址
        mRecentList = new LinkedList<RecentChatEntity>();
        mRecentAdapter = new RecentChatAdapter(getApplicationContext(),
                mRecentList);
        super.onCreate();
    }

    public Client getClient() {
        return client;
    }

//    public Server getServer() {
//        return server;
//    }

    public boolean isClientStart() {
        return isClientStart;
    }

    public void setClientStart(boolean isClientStart) {
        this.isClientStart = isClientStart;
    }

    public NotificationManager getmNotificationManager() {
        return mNotificationManager;
    }

    public void setNotificationManager(NotificationManager mNotificationManager) {
        this.mNotificationManager = mNotificationManager;
    }

    public int getNewMsgNum() {
        return newMsgNum;
    }

    public void setNewMsgNum(int newMsgNum) {
        this.newMsgNum = newMsgNum;
    }

    public LinkedList<RecentChatEntity> getmRecentList() {
        return mRecentList;
    }

    public void setmRecentList(LinkedList<RecentChatEntity> mRecentList) {
        this.mRecentList = mRecentList;
    }

    public RecentChatAdapter getmRecentAdapter() {
        return mRecentAdapter;
    }

    public void setmRecentAdapter(RecentChatAdapter mRecentAdapter) {
        this.mRecentAdapter = mRecentAdapter;
    }

    public int getRecentNum() {
        return recentNum;
    }

    public void setRecentNum(int recentNum) {
        this.recentNum = recentNum;
    }
}
