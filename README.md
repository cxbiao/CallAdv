# Calladv

此App支持在来电和去电时显示广告。
- 来电：响铃即触发屏显广告，挂断就消失
- 去电：打去时触发屏显广告，挂断就消失
- 广告图可任意移动
- 按下返回键，home键，recent键，屏显广告自动消失

#效果图
![pic.png](./pic.png "")

#原理

原理比较简单，监听来去电广播，再启动service显示悬浮窗,
此悬浮窗在API>=19 不需要额外权限，API<19需要SYSTEM_ALERT_WINDOW权限

#使用方法

```
//声明service和broadcastreceiver即可
        <service
            android:name=".core.PhoneService"
            android:process=":ui"
            />
        <receiver android:name=".core.PhoneBroadcast">
            <intent-filter android:priority="1000">
                <!-- 系统启动完成后会调用 -->
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <!-- 解锁完成后会调用 -->
                <action android:name="android.intent.action.NEW_OUTGOING_CALL"/>
                <action android:name="android.intent.action.PHONE_STATE"/>
            </intent-filter>
        </receiver>
```

#致谢
- liaohuqiu https://github.com/liaohuqiu/android-UCToast



