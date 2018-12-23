package jiguang.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.snt.phoney.R;

import cn.jpush.im.android.api.model.Conversation;
import jiguang.chat.activity.ChatActivity;
import jiguang.chat.adapter.ChattingListAdapter;

/**
 * Created by ${chenyn} on 2017/3/28.
 */

public class ChatView extends RelativeLayout {
    Context mContext;
    private DropDownListView mChatListView;
    private Conversation mConv;
    private Button mAtMeBtn;

    private Toolbar mToolbar;

    public ChatView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initModule() {
        mToolbar = findViewById(R.id.toolbar);
        mAtMeBtn = findViewById(R.id.jmui_at_me_btn);
        mChatListView = findViewById(R.id.lv_chat);

    }

    public void setToPosition(int position) {
        mChatListView.smoothScrollToPosition(position);
        mAtMeBtn.setVisibility(GONE);
    }

    public void setChatListAdapter(ChattingListAdapter chatAdapter) {
        mChatListView.setAdapter(chatAdapter);
    }

    public DropDownListView getListView() {
        return mChatListView;
    }

    public void setToBottom() {
        mChatListView.clearFocus();
        mChatListView.post(() -> mChatListView.setSelection(mChatListView.getAdapter().getCount() - 1));
    }

    public void setConversation(Conversation conv) {
        this.mConv = conv;
    }

    public void setGroupIcon() {
    }

    public void setListeners(ChatActivity listeners) {
        mAtMeBtn.setOnClickListener(listeners);
    }

    public void dismissRightBtn() {
    }

    public void showRightBtn() {
    }

    public void setChatTitle(int id, int count) {
        mToolbar.setTitle(id);
    }

    public void setChatTitle(int id) {
        mToolbar.setTitle(id);
    }

    public void showAtMeButton() {
        mAtMeBtn.setVisibility(VISIBLE);
    }


    //设置群聊名字
    public void setChatTitle(String name, int count) {
        mToolbar.setTitle(name + "(" + count + ")");
    }

    public void setChatTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void dismissGroupNum() {
    }
}
