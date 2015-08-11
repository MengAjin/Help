package com.example.Message;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.firstprogram.R;

import java.util.List;

/**
 * Created by Âþ¶¹»­Ñ¿ on 2015/8/11.
 */
public class ChatListAdapter extends BaseAdapter {
    private List<ChatMsgEntity> coll;
    private LayoutInflater mInflater;
    private Context context;

    public ChatListAdapter(Context context, List<ChatMsgEntity> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public int getViewTypeCount() {
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.getMsgType();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.chatting_item_message, null);

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.item_message_tv_time);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.item_message_tv_content);
            viewHolder.nickname =  (TextView) convertView.findViewById(R.id.item_message_tv_nickname);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.nickname.setText(entity.getName());
        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, entity.getText());
        viewHolder.tvContent.setText(spannableString);

        return convertView;
    }

    class ViewHolder {
        public TextView tvSendTime;
        public TextView tvContent;
        public TextView nickname;
        public boolean isComMsg = true;
    }

}
