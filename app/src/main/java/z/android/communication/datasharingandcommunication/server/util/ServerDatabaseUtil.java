package z.android.communication.datasharingandcommunication.server.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.utils.DatabaseHelper;

public class ServerDatabaseUtil {
    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;

    public ServerDatabaseUtil(){}

    public ServerDatabaseUtil(Context context){
        sqLiteDatabase = context.openOrCreateDatabase(Constants.SERVER_DB,Context.MODE_PRIVATE,null);
        databaseHelper = new DatabaseHelper(context,Constants.SERVER_DB);
    }

    public long execSqlInsert(String table, String nullColumnHack, ContentValues values){
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return  sqLiteDatabase.insert(table,nullColumnHack,values);
        /**
         * while values is null,you can use  nullColumnHack  to insert null values into tables
         */
    }

    public Cursor getSelectCursor(boolean distinction,String table,String[] columns,String selection,String[] selectionArgs,String groupBy,String having,String orderBy,String limit){
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        return sqLiteDatabase.query(distinction,table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
    }

    public SQLiteDatabase getWritableSqLiteDatabase(){
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this.sqLiteDatabase;
    }

    public SQLiteDatabase getReadableDataBase(){
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        return this.sqLiteDatabase;
    }

    public Cursor getRawQuery(String sql,String[] what){
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,what);
    }
}
