// ICallStateListener.aidl
package com.snt.phoney;

// Declare any non-default types here with import statements

interface ICallStateListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,            double aDouble, String aString);

    void onCallOutgoing();

    void onCallInviteReceived();

    void onCallOtherUserInvited();

    void onCallConnected();

    void onCallMemberJoin();

    void onCallMemberOffline();

    void onCallDisconnected();

    void onCallError();
}
