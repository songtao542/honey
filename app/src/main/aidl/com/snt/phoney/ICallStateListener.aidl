// ICallStateListener.aidl
package com.snt.phoney;

// Declare any non-default types here with import statements
import com.snt.phoney.domain.model.JMUser;

interface ICallStateListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,            double aDouble, String aString);

    void onCallOutgoing();

    void onCallInviteReceived(in JMUser user);

    void onCallOtherUserInvited(in JMUser user);

    void onCallConnected();

    void onCallMemberJoin(in JMUser user);

    void onCallMemberOffline(in JMUser user,int reason);

    void onCallDisconnected(int reason);

    void onCallError(int errorCode, String desc);

}
