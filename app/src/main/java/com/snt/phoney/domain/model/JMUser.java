package com.snt.phoney.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


/**
 * @Parcelize CREATOR 返回的 Object 无法在 Binder 中使用，所以这个类是 ImUser for Binder
 */
public class JMUser implements Parcelable {
    public Long id;
    public String password;
    public String username;
    public String nickname;
    public String avatar;

    public JMUser() {
    }

    public JMUser(Long id,
                  String password,
                  String username,
                  String nickname,
                  String avatar) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    protected JMUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        password = in.readString();
        username = in.readString();
        nickname = in.readString();
        avatar = in.readString();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("id:" + id);
        builder.append(" ,password:" + password);
        builder.append(" ,username:" + username);
        builder.append(" ,nickname:" + nickname);
        builder.append(" ,avatar:" + avatar);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(nickname);
        dest.writeString(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JMUser> CREATOR = new Creator<JMUser>() {
        @Override
        public JMUser createFromParcel(Parcel in) {
            return new JMUser(in);
        }

        @Override
        public JMUser[] newArray(int size) {
            return new JMUser[size];
        }
    };

    public static JMUser from(cn.jpush.im.android.api.model.UserInfo user) {
        Long id = user.getUserID();
        String nickname = user.getNickname();
        String username = user.getUserName();
        String avatar = user.getAvatar();
        return new JMUser(id, null, username, nickname, avatar);
    }

    public static JMUser from(com.snt.phoney.domain.model.User user) {
        String nickname = user.getNickname();
        String username = user.getUsername();
        String avatar = user.getAvatar();
        ImUser im = user.getIm();
        String password = null;
        if (im != null) {
            password = im.getPassword();
            nickname = im.getNickname();
            username = im.getUsername();
        }
        return new JMUser(null, password, username, nickname, avatar);
    }

    public static JMUser from(com.snt.phoney.domain.model.ImUser user) {
        Long id = user.getId();
        String nickname = user.getNickname();
        String username = user.getUsername();
        String avatar = user.getAvatar();
        String password = user.getPassword();
        return new JMUser(id, password, username, nickname, avatar);
    }
}
