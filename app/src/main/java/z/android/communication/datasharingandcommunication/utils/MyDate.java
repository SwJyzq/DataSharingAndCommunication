package z.android.communication.datasharingandcommunication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

    public static String getDate(){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

}
