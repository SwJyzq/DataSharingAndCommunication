package z.android.communication.datasharingandcommunication.server.implementation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.interfaces.DataDetails;
import z.android.communication.datasharingandcommunication.interfaces.User;
import z.android.communication.datasharingandcommunication.server.UserDataAccessObject;
import z.android.communication.datasharingandcommunication.server.util.ServerDatabaseUtil;
import z.android.communication.datasharingandcommunication.utils.DataDetailsDB;
import z.android.communication.datasharingandcommunication.utils.MyDate;

public class UserDataAccessObjectImplementation implements UserDataAccessObject {
    private ServerDatabaseUtil serverDatabaseUtil;
    private DataDetailsDB dataDetailsDB;


    public UserDataAccessObjectImplementation(Context db_context){
        serverDatabaseUtil = new ServerDatabaseUtil(db_context);
        dataDetailsDB = new DataDetailsDB(db_context);
    }

    @Override
    public int register(User user) {
        String sql = "insert into usr (name,password,email,time) values(?,?,?,?)";
        String[] column_select = new String[]{"_id"};
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.getName());
        contentValues.put("password",user.getPassword());
        contentValues.put("email",user.getEmail());
        contentValues.put("time", MyDate.getDate());
        contentValues.put("isOnline",0);
        if (serverDatabaseUtil.execSqlInsert("usr",null,contentValues)>0) {
            Cursor result = serverDatabaseUtil.getSelectCursor(false, Constants.SERVER_DB_USR_TABLE, column_select,null,null,null,null,null,null);
            if (result.moveToLast()){
                int id = result.getInt(result.getColumnIndexOrThrow("_id"));
                createFriendTable(id);
                return id;
            }
        }
        return 0;
    }

    @Override
    public ArrayList<User> refresh(int id) {
        ArrayList<User> list = new ArrayList<User>();
        list.add(findMe(id));
        String sql = "select * from _"+id;
        Cursor result = serverDatabaseUtil.getRawQuery(sql,null);
        if (result==null){
            Log.d("handle refresh", "refresh: list is empty");
            return null;
        }else {
            if (result.moveToFirst()){
                do {
                    User friend = new User();
                    friend.setId(result.getInt(result.getColumnIndex("_id")));
                    friend.setName(result.getString(result.getColumnIndex("name")));
                    list.add(friend);
                }while (result.moveToNext());
                return list;
            }else {
                return list;
            }
        }
    }

    @Override
    public ArrayList<User> login(User user) {
        String sql = "select * from usr where _id=? and password=?";
        Cursor result = serverDatabaseUtil.getRawQuery(sql,new String[]{user.getId()+"",user.getPassword()});
        if (result.moveToFirst()){
            setOnline(user.getId());
            Log.d("login", "login: select success");
            return refresh(user.getId());
        }
        Log.d("login", "login: select failed");
        return null;
    }

    @Override
    public void logout(int id) {
        String sql = "select * from user where _id=? and password=?";

    }

    @Override
    public ArrayList<DataDetails> getDataDetails() {
        File dataDetailDir = new File(Environment.getExternalStorageDirectory(),Constants.DATA_DETAILS_DIR);
        File[] files = dataDetailDir.listFiles();
        if (files!=null){

        }
        return null;
    }


    public void setOnline(int id){
        String sql = "update usr set isOnline=1 where _id=?";
        ContentValues up_data = new ContentValues();
        up_data.put("isOnline",1);
        serverDatabaseUtil.getWritableSqLiteDatabase().update(Constants.SERVER_DB_USR_TABLE,up_data,"_id = ?",new String[]{id+""});
    }

    public void createFriendTable(int id){
        String sql = "create table if not exists _" + id
                + " (_id integer primary key autoincrement,"
                + "name text,"
                + "isOnline text)";
        serverDatabaseUtil.getWritableSqLiteDatabase().execSQL(sql);
    }

    public User findMe(int id){
        User me = new User();
        String sql = "select * from usr where _id = ?";
        Cursor result = serverDatabaseUtil.getRawQuery(sql,new String[]{""+id});
        if (result.moveToFirst()){
            me.setId(result.getInt(result.getColumnIndex("_id")));
            me.setEmail(result.getString(result.getColumnIndex("email")));
            me.setName(result.getString(result.getColumnIndex("name")));
            Log.d("handle findMe", "findMe: success");
            return me;
        }else {
            Log.d("handle findMe", "findMe: failed");
            return null;
        }
    }
}
