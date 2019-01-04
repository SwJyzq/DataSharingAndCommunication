package z.android.communication.datasharingandcommunication.server;

import java.util.ArrayList;

import z.android.communication.datasharingandcommunication.interfaces.DataDetails;
import z.android.communication.datasharingandcommunication.interfaces.User;

public interface UserDataAccessObject {
    public int register(User user);

    public ArrayList<User> login(User user);

    public ArrayList<User> refresh(int id);

    public void logout(int id);

    public ArrayList<DataDetails> getDataDetails();
}
