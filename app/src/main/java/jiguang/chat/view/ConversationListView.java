package jiguang.chat.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snt.phoney.R;

import jiguang.chat.fragment.ConversationListFragment;

/**
 * Created by ${chenyn} on 2017/3/13.
 */

public class ConversationListView {
    private View mConvListFragment;
    private ListView mConvListView = null;
    private TextView mTitle;
    private LinearLayout mSearchHead;
    private LinearLayout mHeader;
    private RelativeLayout mLoadingHeader;
    private ImageView mLoadingIv;
    private LinearLayout mLoadingTv;
    private Context mContext;
    private TextView mNull_conversation;
    private LinearLayout mSearch;
    //private TextView mAllUnReadMsg;
    private ConversationListFragment mFragment;
    private Handler mHandler = new Handler();

    public ConversationListView(View view, Context context, ConversationListFragment fragment) {
        this.mConvListFragment = view;
        this.mContext = context;
        this.mFragment = fragment;
    }

    public void initModule() {
        mConvListView = mConvListFragment.findViewById(R.id.conv_list_view);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeader = (LinearLayout) inflater.inflate(R.layout.conv_list_head_view, mConvListView, false);
        mSearchHead = (LinearLayout) inflater.inflate(R.layout.conversation_head_view, mConvListView, false);
        mLoadingHeader = (RelativeLayout) inflater.inflate(R.layout.jmui_drop_down_list_header, mConvListView, false);
        mLoadingIv = mLoadingHeader.findViewById(R.id.jmui_loading_img);
        mLoadingTv = mLoadingHeader.findViewById(R.id.loading_view);
        mSearch = mSearchHead.findViewById(R.id.search_title);
        mNull_conversation = mConvListFragment.findViewById(R.id.null_conversation);
        //mAllUnReadMsg = mFragment.getActivity().findViewById(R.id.all_unread_number);
        mConvListView.addHeaderView(mLoadingHeader);
        mConvListView.addHeaderView(mSearchHead);
        mConvListView.addHeaderView(mHeader);

        mHeader.setOnClickListener(v -> {
            Log.d("ConversationListView", "no network clicked,but we do nothing");
            if (mContext != null) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                ComponentName cn = intent.resolveActivity(mContext.getPackageManager());
                if (cn != null) {
                    mContext.startActivity(intent);
                }
            }
        });
        mHeader.setOnLongClickListener(v -> {
            Log.d("ConversationListView", "no network long clicked,but we do nothing");
            return true;
        });
    }

    public void setConvListAdapter(ListAdapter adapter) {
        mConvListView.setAdapter(adapter);
    }


    public void setListener(View.OnClickListener onClickListener) {
        mSearch.setOnClickListener(onClickListener);
    }

    public void setItemListeners(AdapterView.OnItemClickListener onClickListener) {
        mConvListView.setOnItemClickListener(onClickListener);
    }

    public void setLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mConvListView.setOnItemLongClickListener(listener);
    }

    public void showHeaderView() {
        mHeader.findViewById(R.id.network_disconnected_iv).setVisibility(View.VISIBLE);
        mHeader.findViewById(R.id.check_network_hit).setVisibility(View.VISIBLE);
    }

    public void dismissHeaderView() {
        mHeader.findViewById(R.id.network_disconnected_iv).setVisibility(View.GONE);
        mHeader.findViewById(R.id.check_network_hit).setVisibility(View.GONE);
    }


    public ListView getConvListView() {
        return mConvListView;
    }

    public void showLoadingHeader() {
        mLoadingIv.setVisibility(View.VISIBLE);
        mLoadingTv.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) mLoadingIv.getDrawable();
        drawable.start();
    }

    public void dismissLoadingHeader() {
        mLoadingIv.setVisibility(View.GONE);
        mLoadingTv.setVisibility(View.GONE);
    }

    public void setNullConversation(boolean isHaveConv) {
        if (isHaveConv) {
            mNull_conversation.setVisibility(View.GONE);
        } else {
            mNull_conversation.setVisibility(View.VISIBLE);
        }
    }


    public void setUnReadMsg(final int count) {
        mHandler.post(() -> {
//                if (mAllUnReadMsg != null) {
//                    if (count > 0) {
//                        mAllUnReadMsg.setVisibility(View.VISIBLE);
//                        if (count < 100) {
//                            mAllUnReadMsg.setText(count + "");
//                        } else {
//                            mAllUnReadMsg.setText("99+");
//                        }
//                    } else {
//                        mAllUnReadMsg.setVisibility(View.GONE);
//                    }
//                }
        });
    }


}
