// IVoiceCallService.aidl
package com.snt.phoney;

// Declare any non-default types here with import statements

import com.snt.phoney.ICallStateListener;

interface IVoiceCallService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

        void hangup();

        void refuse();

        void accept();

        void enableSpeaker( boolean enable);

        void registerICallStateListener(ICallStateListener listener);

        void unregisterICallStateListener(ICallStateListener listener);
}
