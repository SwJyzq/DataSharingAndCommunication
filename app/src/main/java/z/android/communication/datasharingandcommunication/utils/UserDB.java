package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.interfaces.User;

public class UserDB {
    private DatabaseHelper databaseHelper;

    public UserDB(Context context){
        databaseHelper = new DatabaseHelper(context, Constants.USR_DB);
    }

    public User selectInfo(int id){
        User user =  new User();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from user where id=?",new String[]{""+id});
        if (cursor.moveToFirst()){
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
        }
        return user;
    }

    public void addUser(List<User> list){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        for (User user : list){
            database.execSQL("insert into user (id,name,isOnline) values(?,?,?)",
                    new Object[] { user.getId(), user.getName(),user.getIsOnline() });
        }
        database.close();
    }

    public void delete(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from user");
        databaseHelper.close();
    }

    public List<User> getUser(){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        List<User> userList = new ArrayList<User>();
        Cursor cursor = database.rawQuery("select * from user",null);
        while (cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setIsOnline(cursor.getInt(cursor.getColumnIndex("inOnline")));
            userList.add(user);
        }
        cursor.close();
        database.close();
        return  userList;
    }
}
