package z.android.communication.datasharingandcommunication.fragments;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.clients.Client;
import z.android.communication.datasharingandcommunication.clients.ClientOutputThread;
import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.interfaces.MyApplication;
import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;
import z.android.communication.datasharingandcommunication.utils.SharedPreferenceUtil;

public class DataSharingFragment extends MyFragment {
    private SharedPreferenceUtil sharedPreferenceUtil;
    private View view;
    private String[] type;
    private String[][] detail;
    private Client client;
    private MyApplication myApplication;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getResources().getStringArray(R.array.data_type);
        try {
            sharedPreferenceUtil = new SharedPreferenceUtil(getActivity().getApplicationContext(),Constants.SAVE_USER);
            if (sharedPreferenceUtil.getIsRunning()){
                myApplication = (MyApplication) getActivity().getApplication();
                client = myApplication.getClient();
                ClientOutputThread clientOutputThread =client.getClientOutputThread();
                TranObject tranObject = new TranObject(TranObjectType.DATA_DETAIL);
                clientOutputThread.setMsg(tranObject);
            }
        }catch (Exception e){
            Log.d("create util failed", "onCreate: dataSharingFragment util"+ e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.data_sharing,container,false);
        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter() {
            private TextView getTextView(){
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,64);
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CLIP_VERTICAL | Gravity.START);
                textView.setPadding(36,0,0,0);
                textView.setTextSize(20);
                return textView;
            }
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return type.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return detail[groupPosition].length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return type[groupPosition];
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return detail[groupPosition][childPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = getTextView();
                textView.setText(getGroup(groupPosition).toString());
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = getTextView();
                textView.setText(getChild(groupPosition,childPosition).toString());
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        };
        ExpandableListView expandableListView = view.findViewById(R.id.data_sharing_fragment_list);
        expandableListView.setAdapter(expandableListAdapter);
        return view;
    }

    @Override
    public void getMessage(TranObject msg) {

    }
}
