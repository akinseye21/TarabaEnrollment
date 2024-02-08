// IWindowManager.aidl
package android.view;

import android.graphics.Point;

// Declare any non-default types here with import statements

interface IWindowManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void getInitialDisplaySize(int displayId, out Point size);

        void getBaseDisplaySize(int displayId, out Point size);

        void getRealDisplaySize(out Point paramPoint);

}
