package z.android.communication.datasharingandcommunication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.fragments.CommunicationFragment;
import z.android.communication.datasharingandcommunication.fragments.DataSharingFragment;
import z.android.communication.datasharingandcommunication.fragments.MyFragmentTabHost;
import z.android.communication.datasharingandcommunication.interfaces.Constants;

public class HomePageActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        MyFragmentTabHost mFragmentTabHost = findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);

        TabHost.TabSpec tabSpecDataSharing = mFragmentTabHost
                .newTabSpec(DataSharingFragment.class.getSimpleName())
                .setIndicator(Constants.DATA_SHARING_INDICATOR);
        mFragmentTabHost.addTab(tabSpecDataSharing,DataSharingFragment.class,null);

        TabHost.TabSpec tabSpecCommunication = mFragmentTabHost
                .newTabSpec(CommunicationFragment.class.getSimpleName())
                .setIndicator(Constants.COMMUNICATION_INDICATOR);
        mFragmentTabHost.addTab(tabSpecCommunication,CommunicationFragment.class,null);
    }
}
