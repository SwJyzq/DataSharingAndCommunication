package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import z.android.communication.datasharingandcommunication.interfaces.ChatMessageEntity;
import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class MessageDatabase {

    private SQLiteDatabase sqLiteDatabase;
    private final static String sql_1 = "CREATE table IF NOT EXISTS _usr";
    private final static String sql_2 = "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,image TEXT,date TEXT,isCome TEXT,message TEXT)";
    private final static String sql_3 = "insert into _usr";
    private final static String sql_4 = " (name,img,date,isCome,message) values(?,?,?,?,?)";

    public MessageDatabase(){}

    public MessageDatabase(Context context){
        sqLiteDatabase = context.openOrCreateDatabase(Constants.USR_DB,Context.MODE_PRIVATE,null);
    }

    public void saveMessage(int id, ChatMessageEntity messageEntity){
        sqLiteDatabase.execSQL(sql_1+id+sql_2);
        int isCome = 0;
        if (messageEntity.getMessageType()){
            isCome = 1;
        }
        sqLiteDatabase.execSQL(sql_3 + id + sql_4,new Object[]{messageEntity.getName(),messageEntity.getImage(),messageEntity.getDate(),isCome,messageEntity.getMessage()});
    }

    public List<ChatMessageEntity> getMeesage(int id){
        List<ChatMessageEntity> chatMessageEntityList = new ArrayList<ChatMessageEntity>();
        sqLiteDatabase.execSQL(sql_1+id+sql_2);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM _usr" + id +"ORDER BY _id DESC LIMITS",null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int image = cursor.getInt(cursor.getColumnIndex("image"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int isCome = cursor.getInt(cursor.getColumnIndex("isCome"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            boolean isComeMsg = false;
            if (isCome == 1){
                isComeMsg = true;
            }
            ChatMessageEntity chatMessageEntity = new ChatMessageEntity(name,date,message,image,isComeMsg);
            chatMessageEntityList.add(chatMessageEntity);
        }
        cursor.close();
        return chatMessageEntityList;
    }

    public void close(){
        if (sqLiteDatabase!=null){
            sqLiteDatabase.close();
        }
    }

}
