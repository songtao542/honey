package jiguang.chat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.model.Message;

public class Constants {
    private Constants() {
    }

    public static final String TARGET_ID = "targetId";
    public static final String TARGET_APP_KEY = "targetAppKey";

    public static final String GROUP_ID = "groupId";

    public static int EMOTICON_CLICK_TEXT = 1;
    public static int EMOTICON_CLICK_BIGIMAGE = 2;
    public static final String MsgIDs = "msgIDs";
    public static final int IMAGE_MESSAGE = 1;
    public static final int TAKE_PHOTO_MESSAGE = 2;
    public static final int TAKE_LOCATION = 3;
    public static final int FILE_MESSAGE = 4;
    public static final int TACK_VIDEO = 5;
    public static final int TACK_VOICE = 6;
    public static final int BUSINESS_CARD = 7;
    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";
    public static final String POSITION = "position";
    public final static int CAPTURE_VIDEO = 1;// 拍摄视频
    public final static int GET_LOCAL_VIDEO = 2;// 选择视频
    public final static int GET_LOCAL_FILE = 3; // 选择文件
    public final static int PICK_IMAGE = 4;
    public final static int PICKER_IMAGE_PREVIEW = 5;
    public final static int PREVIEW_IMAGE_FROM_CAMERA = 6;
    public final static int GET_LOCAL_IMAGE = 7;// 相册
    public static final String NAME = "name";
    public static final int TAKE_PHOTO = 99;
    public static final int TAKE_VIDEO = 88;
    public static final String ATALL = "atall";
    public static final String CONV_TITLE = "conv_title";
    public static String FILE_DIR = "sdcard/JChatDemo/recvFiles/";
    public static final int RESULT_CODE_AT_MEMBER = 31;
    public static final int RESULT_CODE_AT_ALL = 32;
    public static final int REQUEST_CODE_CHAT_DETAIL = 14;
    public static final int RESULT_CODE_CHAT_DETAIL = 15;
    public static final int RESULT_CODE_SELECT_PICTURE = 8;
    public static final int RESULT_CODE_BROWSER_PICTURE = 13;
    public static final int RESULT_CODE_SEND_LOCATION = 25;
    public static final int RESULT_CODE_SEND_FILE = 27;
    public static final int REQUEST_CODE_SEND_LOCATION = 24;
    public static final int REQUEST_CODE_SEND_FILE = 26;
    public static final int REQUEST_CODE_FRIEND_INFO = 16;

    public static final String DRAFT = "draft";

    public static List<Message> ids = new ArrayList<>();
    public static List<Message> forwardMsg = new ArrayList<>();
    public static Map<Long, Boolean> isAtMe = new HashMap<>();
    public static Map<Long, Boolean> isAtall = new HashMap<>();
}
