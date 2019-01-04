package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class SharedPreferenceUtil {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtil(Context context, String file){
        this.sharedPreferences =  context.getSharedPreferences(file,Context.MODE_PRIVATE);
    }

    public boolean getIsFirst(){
        return sharedPreferences.getBoolean("isFirst",true);
    }

    public void setIsFirst(boolean isFirst){
        this.editor = sharedPreferences.edit();
        editor.putBoolean("isFirst",isFirst);
        editor.apply();
    }

    public void setIsRunning(boolean isRunning){
        this.editor = sharedPreferences.edit();
        editor.putBoolean("isRunning",isRunning);
        editor.apply();
    }

    public boolean getIsRunning(){
        return sharedPreferences.getBoolean("isRunning",false);
    }

    public String getPassword(){
        return  sharedPreferences.getString("Password",null);
    }

    public void setPassword(String password){
        this.editor = sharedPreferences.edit();
        editor.putString("Password",password);
        editor.apply();
    }

    public void setIp(String ip){
        this.editor = sharedPreferences.edit();
        editor.putString("ip",ip);
        editor.apply();
    }

    public void setPort(int port){
        this.editor = sharedPreferences.edit();
        editor.putInt("port",port);
        editor.apply();
    }

    public String getName(){
        return sharedPreferences.getString("name",null);
    }

    public String getIp(){
        return sharedPreferences.getString("ip", Constants.LOCAL_HOST);
        //默认使用localhost
    }

    public void setId(String id){
        this.editor = sharedPreferences.edit();
        editor.putString("id",id);
        editor.apply();
    }

    public void setEmail(String email){
        this.editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.apply();
    }
    public void  setName(String name){
        this.editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.apply();
    }
    public int getPort(){
        return sharedPreferences.getInt("port",Constants.PORT);
    }

    public String getId(){
        return sharedPreferences.getString("id","1");
    }

    public int getImage(){return sharedPreferences.getInt("image",0);}

}
