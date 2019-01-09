package com.snt.phoney.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


/**
 * @Parcelize CREATOR 返回的 Object 无法在 Binder 中使用，所以这个类是 ImUser for Binder
 */
public class JMUser implements Parcelable {
    public Long id;
    public String uuid;
    public String password;
    public String username;
    public String nickname;
    public String avatar;
    public String token;

    public JMUser() {
    }

    public JMUser(Long id,
                  String uuid,
                  String password,
                  String username,
                  String nickname,
                  String avatar,
                  String token) {
        this.id = id;
        this.uuid = uuid;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
        this.token = token;
    }

    protected JMUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        uuid = in.readString();
        password = in.readString();
        username = in.readString();
        nickname = in.readString();
        avatar = in.readString();
        token = in.readString();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("{")
                .append("id:").append(id)
                .append(" ,password:").append(password)
                .append(" ,username:").append(username)
                .append(" ,nickname:").append(nickname)
                .append(" ,avatar:").append(avatar)
                .append(" ,token:").append(token)
                .append("}");
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
        dest.writeString(uuid);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeString(token);
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
        return new JMUser(id, null, null, username, nickname, avatar, null);
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
        String uuid = user.getUuid();
        String token = user.getToken();
        return new JMUser(null, uuid, password, username, nickname, avatar, token);
    }

    public static JMUser from(com.snt.phoney.domain.model.ImUser user) {
        Long id = user.getId();
        String nickname = user.getNickname();
        String username = user.getUsername();
        String avatar = user.getAvatar();
        String password = user.getPassword();
        return new JMUser(id, null, password, username, nickname, avatar, null);
    }
}
