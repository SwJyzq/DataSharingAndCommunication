package z.android.communication.datasharingandcommunication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.activities.HomePageActivity;
import z.android.communication.datasharingandcommunication.clients.Client;
import z.android.communication.datasharingandcommunication.clients.ClientInputThread;
import z.android.communication.datasharingandcommunication.clients.ClientOutputThread;
import z.android.communication.datasharingandcommunication.interfaces.ChatMessageEntity;
import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.interfaces.MessageListener;
import z.android.communication.datasharingandcommunication.interfaces.MyApplication;
import z.android.communication.datasharingandcommunication.interfaces.TextMessage;
import z.android.communication.datasharingandcommunication.interfaces.User;
import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;
import z.android.communication.datasharingandcommunication.utils.MessageDatabase;
import z.android.communication.datasharingandcommunication.utils.MyDate;
import z.android.communication.datasharingandcommunication.utils.SharedPreferenceUtil;

public class GetMsgService extends Service {
    private Looper serviceLooper;
    private static final int MSG = 0x11;
    private MyApplication mApplication;
    private Client client;
    private NotificationManager notificationManager;
    private boolean isRunning = false;
    private Context mContext = this;
    private Notification.Builder notificationBuilder;
    private Notification notification;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private MessageDatabase messageDatabase;
    private Thread networkThread;

//    FutureTask<Boolean> task = new FutureTask<Boolean>((Callable<Boolean>)() ->{
//        return client.start();
//    });

    private final  class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG :
                    int newMsgNum = mApplication.getNewMsgNum();
                    newMsgNum++;
                    mApplication.setNewMsgNum(newMsgNum);
                    TranObject<TextMessage> textMessageTranObject = (TranObject<TextMessage>) msg.getData().getSerializable("msg");
                    if (textMessageTranObject!=null){
                        int from = textMessageTranObject.getFromUser();
                        String message = textMessageTranObject.getObject().getMessage();
                        ChatMessageEntity chatMessageEntity = new ChatMessageEntity("", MyDate.getDate(),message,-1,true);
                        messageDatabase.saveMessage(from,chatMessageEntity);
                        int icon = R.drawable.notify_icon;
                        CharSequence tickerText = from + ":" + message ;
                        long when = System.currentTimeMillis();
                        notification = notificationBuilder.setTicker(tickerText)
                                .setSmallIcon(icon)
                                .setWhen(when)
                                .setContentText("("+newMsgNum + "条新消息")
                                .build();
                        notification.flags = Notification.FLAG_NO_CLEAR;
                        Intent intent= new Intent(mContext, HomePageActivity.class);
                        notification.contentIntent = PendingIntent.getActivity(mContext,0,intent,0);
                        notificationManager.notify(Constants.NOTIFY_ID,notification);
                    }
                    break;
                    default:    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
       startCommand();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (messageDatabase!=null){
            messageDatabase.close();
            unregisterReceiver(backKeyBroadcastReceiver);
            notificationManager.cancel(Constants.NOTIFY_ID);
            if (isRunning){
                if (client!=null){
                    ClientOutputThread clientOutputThread = client.getClientOutputThread();
                    TranObject<User> userTranObject = new TranObject<User>(TranObjectType.LOGOUT);
                    User user = new User();
                    user.setId(Integer.parseInt(sharedPreferenceUtil.getId()));
                    userTranObject.setObject(user);
                    clientOutputThread.setMsg(userTranObject);
                    clientOutputThread.setStart(false);
                    client.getClientOutputThread().setStart(false);
                }else{
                    Log.d("destroy Service", "onDestroy: client is null");
                }
            }
        }
    }

    private BroadcastReceiver backKeyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(mContext,"后台运行",Toast.LENGTH_SHORT).show();
            setMsgNotification();
        }
    };

    private void setMsgNotification(){
        int icon = R.drawable.notify;
        CharSequence tickerText = "";
        long when = System.currentTimeMillis();
        notification = notificationBuilder.setSmallIcon(icon)
                .setTicker(tickerText)
                .setWhen(when)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.notify_layout);
        contentView.setTextViewText(R.id.notify_name,sharedPreferenceUtil.getName());
        contentView.setTextViewText(R.id.notify_msg,"正在后台运行");
        contentView.setTextViewText(R.id.notify_time, MyDate.getDate());
        notification.contentView = contentView;
        Intent intent = new Intent();
        notification.contentIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager.notify(Constants.NOTIFY_ID,notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void  create(){
        messageDatabase = new MessageDatabase(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BACK_KEY_ACTION);
        registerReceiver(backKeyBroadcastReceiver,intentFilter);
        notificationManager = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);
        mApplication = (MyApplication)mContext.getApplicationContext();
        client = mApplication.getClient();
        mApplication.setNotificationManager(notificationManager);
    }

    public void startCommand(){
        Toast.makeText(mContext,"启动服务",Toast.LENGTH_SHORT).show();
        notificationBuilder = new Notification.Builder(mContext);
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext(),Constants.SAVE_USER);
        try {
           if (client!=null){
               Toast.makeText(mContext,"client is not null",Toast.LENGTH_SHORT).show();
//              networkThread = new Thread(task,"networkThread");
//              networkThread.start();
//              isRunning = task.get();
               client.start();
               Thread.sleep(3000);
               isRunning = client.getIsRunning();
               mApplication.setClientStart(isRunning);
               if (isRunning){
                   Toast.makeText(this,"socket连接成功",Toast.LENGTH_SHORT).show();
                   if (client.getClientInputThread()==null){
                       Log.d("clientThreadException", "startCommand: clientInputThread is null");
                       Toast.makeText(this,"clientInputThread is not null",Toast.LENGTH_SHORT).show();
                   }
                   ClientInputThread clientInputThread = client.getClientInputThread();
                   clientInputThread.setMessageListener(new MessageListener() {
                       @Override
                       public void Message(TranObject msg) {
                           if (sharedPreferenceUtil.getIsRunning()){
                               if (msg.getType()== TranObjectType.MESSAGE){
                                   HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
                                   thread.start();
                                   serviceLooper = thread.getLooper();
                                   ServiceHandler handler = new ServiceHandler(serviceLooper);
                                   Message message = handler.obtainMessage();
                                   message.what = MSG;
                                   message.getData().putSerializable("msg",msg);
                                   handler.sendMessage(message);
                               }
                           }else {
                               Intent broadCast = new Intent();
                               broadCast.setAction(Constants.ACTION);
                               broadCast.putExtra(Constants.MESSAGE_KEY,msg);
                               if (msg==null){
                                   Log.d("sendBroadCast", "Message: sent a broadCast  : null Object");
                               }else {
                                   if (msg.getObject()==null){
                                       Log.d("sendBroadCast", "Message: sent a broadCast  "+msg.getType()+" : null Object");
                                   }else {
                                       Log.d("sendBroadCast", "Message: sent a broadCast  "+msg.getType()+" :" +msg.getObject().toString());
                                   }
                               }
                               sendBroadcast(broadCast);
                           }
                       }
                   });
               }else{
                   Toast.makeText(this,"socket连接失败",Toast.LENGTH_SHORT).show();
               }
           }else {
               Toast.makeText(mContext,"client is null",Toast.LENGTH_SHORT).show();
           }
        }catch (Exception e){
            Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("SocketFailed",e.toString());
        }

    }

}
