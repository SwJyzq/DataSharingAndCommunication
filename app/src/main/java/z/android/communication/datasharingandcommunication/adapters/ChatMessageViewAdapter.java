package z.android.communication.datasharingandcommunication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.interfaces.ChatMessageEntity;
import z.android.communication.datasharingandcommunication.interfaces.MsgViewType;

public class ChatMessageViewAdapter extends BaseAdapter {
    private int[] images;
    private static final int ITEM_COUNT = 2;
    private List<ChatMessageEntity> coll;
    private LayoutInflater layoutInflater;

    static class ViewHolder {
        TextView sendTimeTextView;
        TextView userNameTextView;
        TextView contentTextView;
        public ImageView icon;
        boolean isComeMsg = true;
    }

    public ChatMessageViewAdapter(Context context, List<ChatMessageEntity> coll){
        this.coll = coll;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coll.size();
    }

    @Override
    public Object getItem(int position) {
        return coll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessageEntity chatMessageEntity = coll.get(position);
        boolean isComeMsg = chatMessageEntity.getMessageType();
        ViewHolder viewHolder =new ViewHolder();
        if (convertView==null){
            if (isComeMsg){
                convertView = layoutInflater.inflate(R.layout.chatting_item_msg_text_left,null);
            }else {
                convertView = layoutInflater.inflate(R.layout.chatting_item_msg_text_right,null);
            }
            viewHolder.sendTimeTextView = convertView.findViewById(R.id.send_time_text);
            viewHolder.userNameTextView = convertView.findViewById(R.id.user_name);
            viewHolder.contentTextView = convertView.findViewById(R.id.chat_content_text);
            viewHolder.icon = convertView.findViewById(R.id.user_header);
            viewHolder.isComeMsg = isComeMsg;
            convertView.setTag(viewHolder);
        }else {
            viewHolder.sendTimeTextView.setText(chatMessageEntity.getDate());
            viewHolder.userNameTextView.setText(chatMessageEntity.getName());
            viewHolder.contentTextView.setText(chatMessageEntity.getMessage());
            viewHolder.icon.setImageResource(images[chatMessageEntity.getImage()]);
            return convertView;
        }
        return null;
    }

    public int getViewTypeCount(){
        return ITEM_COUNT;
    }

    public int getItemViewType(int position){
        ChatMessageEntity chatMessageEntity = coll.get(position);
        if (chatMessageEntity.getMessageType()){
            return MsgViewType.COME_MSG;
        }else {
            return MsgViewType.TO_MSG;
        }
    }
}
