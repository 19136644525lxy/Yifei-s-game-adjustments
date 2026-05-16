package com.yifei.ygd.imblocker;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class IMManagerMac implements IMManager.PlatformIMManager {
    private static boolean state = false;
    static private final Pointer viewClass = getViewClass();
    static private Pointer view = null;
    static private final InterpretKeyEventsCallback Imp;
    static private final InterpretKeyEventsCallback NewImp;

    static {
        // see https://github.com/glfw/glfw/blob/b4c3ef9d0fdf46845f3e81e5d989dab06e71e6c1/src/cocoa_window.m#L571
        // Replacing the method dynamically to determine whether to send text based on state
        // see reference for objc_runtime's dynamic manipulation at https://developer.apple.com/documentation/objectivec/objective-c_runtime
        var selector = sel("interpretKeyEvents:");
        var method = ObjC.INSTANCE.class_getInstanceMethod(viewClass, selector);
        Imp = ObjC.INSTANCE.method_getImplementation(method);
        NewImp = (self, sel, eventArray) -> {
            if (view == null) view = self;
            if (!state) {
                var textInputContext = cls("NSTextInputContext");
                var current = msgPointer(textInputContext, "currentInputContext");
                msg(current, "discardMarkedText");
                return;
            }
            Imp.invoke(self, sel, eventArray);
        };
        ObjC.INSTANCE.class_replaceMethod(viewClass, selector, NewImp, "v@:@");
    }

    private static Pointer getViewClass() {
        try {
            // Try to load the Objective-C runtime
            return ObjC.INSTANCE.objc_getClass("GLFWContentView");
        } catch (Exception e) {
            return null;
        }
    }

    private static Pointer sel(String name) {
        try {
            return ObjC.INSTANCE.sel_registerName(name);
        } catch (Exception e) {
            return null;
        }
    }

    private static Pointer cls(String name) {
        try {
            return ObjC.INSTANCE.objc_getClass(name);
        } catch (Exception e) {
            return null;
        }
    }

    private static Pointer msgPointer(Pointer obj, String selector) {
        try {
            return ObjC.INSTANCE.objc_msgSend(obj, sel(selector));
        } catch (Exception e) {
            return null;
        }
    }

    private static void msg(Pointer obj, String selector) {
        try {
            ObjC.INSTANCE.objc_msgSend(obj, sel(selector));
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * @see <a href="https://developer.apple.com/documentation/objectivec/objective-c_runtime">Apple Developer Documentation for objc_runtime:</a>
     */
    private interface ObjC extends Library {
        ObjC INSTANCE = Native.load("objc", ObjC.class);

        void class_replaceMethod(Pointer cls, Pointer selector, InterpretKeyEventsCallback imp, String types);

        InterpretKeyEventsCallback method_getImplementation(Pointer selector);

        Pointer class_getInstanceMethod(Pointer cls, Pointer selector);

        Pointer objc_getClass(String name);

        Pointer sel_registerName(String name);

        Pointer objc_msgSend(Pointer obj, Pointer selector);
    }

    /**
     * The underlying native type is IMP, which should be a function pointer to the implementation of interpretKeyEvents:
     * @see <a href="https://developer.apple.com/documentation/objectivec/objective-c_runtime/imp">Documentation for IMP</a>
     * @see <a href="https://developer.apple.com/documentation/appkit/nsresponder/1531599-interpretkeyevents?language=objc">Documentation for interpretKeyEvents:</a>
     */
    private interface InterpretKeyEventsCallback extends Callback {
        /**
         * @param self       "this" pointer for NSObject
         * @param selector   selector for interpretKeyEvents:
         * @param eventArray an array of NSEvent objects
         */
        void invoke(Pointer self, Pointer selector, Pointer eventArray);
    }

    @Override
    public void makeOn() {
        setState(true);
    }

    @Override
    public void makeOff() {
        setState(false);
    }

    @Override
    public void setState(boolean on) {
        if (state == on) return;
        state = on;
    }

    @Override
    public void syncState() {

    }

    @Override
    public boolean getState() {
        return state;
    }
}
