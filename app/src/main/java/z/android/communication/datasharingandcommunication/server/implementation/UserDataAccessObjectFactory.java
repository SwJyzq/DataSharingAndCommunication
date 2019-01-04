package z.android.communication.datasharingandcommunication.server.implementation;

import android.content.Context;

import z.android.communication.datasharingandcommunication.server.UserDataAccessObject;

public class UserDataAccessObjectFactory {
    private static UserDataAccessObject dataAccessObject;

    public static UserDataAccessObject getInstance(Context db_context){
        if(dataAccessObject==null){
            dataAccessObject = new UserDataAccessObjectImplementation(db_context);
        }
        return dataAccessObject;
    }
}
