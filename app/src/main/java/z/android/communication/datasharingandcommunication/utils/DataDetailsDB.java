package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;

import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class DataDetailsDB {
    private DatabaseHelper databaseHelper;

    public DataDetailsDB(Context context){
        databaseHelper = new DatabaseHelper(context,Constants.DATA_DETAILS_DB);
    }
}
