package com.awesomeproject;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import com.shield.android.Shield;
import com.shield.android.ShieldCallback;
import org.json.JSONObject;
import com.shield.android.ShieldException;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import androidx.annotation.Nullable;
import android.util.Log;

public class ShieldModule extends ReactContextBaseJavaModule implements ShieldCallback<JSONObject>{
    private ReactApplicationContext reactContext;
    ShieldModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

     @Override
    public String getName() {
     return "ShieldModule";
    }


    @ReactMethod
    public void initShield(String siteID, String key) {
        Activity currentActivity = reactContext.getCurrentActivity();
        if (currentActivity != null) {
            try {
                Shield shield = new Shield.Builder(currentActivity, siteID, key)
                .registerDeviceShieldCallback(this)
                .build();
                Shield.setSingletonInstance(shield);
                Log.d("shield", "initialization success");
            
            }  
            catch (IllegalStateException e) {
     
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public String getSessionId() {
        return Shield.getInstance().getSessionId();
    }

    @Override
    public void onSuccess(@Nullable JSONObject jsonObject) {
        Log.d("result", jsonObject.toString());
        if (jsonObject != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("success", jsonObject.toString());
        }

    }

    @Override
    public void onFailure(@Nullable ShieldException e) {
        if (e != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("error", e.getLocalizedMessage());
        }

    }
}
