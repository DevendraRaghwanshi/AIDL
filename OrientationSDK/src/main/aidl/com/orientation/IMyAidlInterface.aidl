// IMyAidlInterface.aidl
package com.orientation;

// Declare any non-default types here with import statements
import com.orientation.ICallback;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void start();
     void registerCallback(ICallback cb);
     void unregisterCallback(ICallback cb);
}