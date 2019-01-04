package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String sql_client = "CREATE table IF NOT EXISTS user"
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, name TEXT, img TEXT, isOnline TEXT, _group TEXT)";
    private final static String sql_server = "CREATE table IF NOT EXISTS usr"+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, email TEXT, time TEXT,isOnline TEXT)";
    private String database;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context,String database){
        super(context,database,null,1);
        this.database = database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (Constants.USR_DB.equals(database)){
            db.execSQL(sql_client);
            sqLiteDatabase = db;
        }else {
            db.execSQL(sql_server);
            sqLiteDatabase  = db;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
