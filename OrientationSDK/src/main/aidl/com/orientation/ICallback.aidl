// ICallback.aidl
package com.orientation;

// Declare any non-default types here with import statements

interface ICallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void showResult(float pitch, float roll);
}