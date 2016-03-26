package com.bryan.calladv.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryan.calladv.R;
import com.bryan.calladv.utils.ContactUtils;
import com.bryan.calladv.utils.PhoneLocationUtils;
import com.bryan.calladv.utils.ScreenUtils;

import java.util.Random;

public class PhoneTipController implements View.OnClickListener, View.OnTouchListener, ViewContainer.KeyEventHandler {

    private static final String TAG = "PhoneTipController";
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewContainer mWholeView;
    private ViewDismissHandler mViewDismissHandler;
    private String mNumber;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_location;
    private ImageView iv_bg;
    private WindowManager.LayoutParams layoutParams;
    private HomeKeyBroadcastReceiver mHomeKeyBroadcastReceiver;
    private boolean isUnRegister;
    private ContactUtils contactUtils;


    private int[] imgs=new int[]{R.mipmap.a1,R.mipmap.a2,R.mipmap.a3,R.mipmap.a4,R.mipmap.a5};

    public PhoneTipController(Context application, String number) {
        mContext = application;
        mNumber = number;
        contactUtils=new ContactUtils(application);
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }


    public void show() {


        mHomeKeyBroadcastReceiver=new HomeKeyBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mContext.registerReceiver(mHomeKeyBroadcastReceiver, intentFilter);
        mWholeView = (ViewContainer) View.inflate(mContext, R.layout.widget_pop_view, null);

        tv_name= (TextView) mWholeView.findViewById(R.id.tv_name);
        tv_phone= (TextView) mWholeView.findViewById(R.id.tv_phone);
        tv_location= (TextView) mWholeView.findViewById(R.id.tv_location);
        iv_bg= (ImageView) mWholeView.findViewById(R.id.iv_bg);

        String[] ret=getTips();
        tv_name.setText(ret[0]);
        tv_phone.setText(ret[1]);
        tv_location.setText(ret[2]);
        iv_bg.setImageResource(imgs[new Random().nextInt(5)]);

        mWholeView.setOnTouchListener(this);
        mWholeView.setKeyEventHandler(this);
        int w = WindowManager.LayoutParams.WRAP_CONTENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags =WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.y=200;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        mWindowManager.addView(mWholeView, layoutParams);
    }


    public void hide(){
        if(!isUnRegister){
            mContext.unregisterReceiver(mHomeKeyBroadcastReceiver);
            isUnRegister=true;
        }
        removePoppedViewAndClear();

    }

    private void removePoppedViewAndClear() {

        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }
        mWholeView=null;

    }

    private int rawX;
    private int rawY;
    private float mTouchStartX;
    private float mTouchStartY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int statusHeight= ScreenUtils.getStatusHeight(mContext);
        rawX = (int) event.getRawX();
        rawY = (int) event.getRawY()-statusHeight;
       // Log.e(TAG, "currX"+rawX+"====currY"+rawY);
        //获取相对View的坐标，即以此View左上角为原点
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX =  event.getX();
                mTouchStartY =  event.getY();
               // Log.e(TAG, "startX" + mTouchStartX+"====startY"+mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX=mTouchStartY=0;
                break;
        }
        return false;
    }

    private void updateViewPosition(){
        //更新浮动窗口位置参数
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x=(int)( rawX-mTouchStartX);
        layoutParams.y=(int) (rawY-mTouchStartY);
        mWindowManager.updateViewLayout(mWholeView, layoutParams);  //刷新显示
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            hide();
        }
    }


    public String[] getTips(){
        String[] ret=new String[3];
        Contact contact=contactUtils.getContactByNumber(mNumber);
        String[] phonelocation= PhoneLocationUtils.getPhoneLocation(mContext, mNumber);
        if(contact==null){
            ret[0]=mNumber;
            ret[1]="陌生号码";
            ret[2]=phonelocation[0]+" "+phonelocation[1];
        }else {
            ret[0]=contact.getDisplay_name();
            ret[1]=mNumber;
            ret[2]=phonelocation[0]+" "+phonelocation[1];
        }
        return ret;
    }

    @Override
    public void onClick(View v) {

    }


    public interface ViewDismissHandler {
        void onViewDismiss();
    }



    class HomeKeyBroadcastReceiver extends BroadcastReceiver {
        private final String SYSTEM_REASON = "reason";
        //Home键
        private final String SYSTEM_HOME_KEY = "homekey";
        //最近使用的应用键
        private final String SYSTEM_RECENT_APPS = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String systemReason = intent.getStringExtra(SYSTEM_REASON);
                if (systemReason != null) {
                    if (systemReason.equals(SYSTEM_HOME_KEY)) {
                        Log.e(TAG, "按下HOME键");
                        hide();
                    } else if (systemReason.equals(SYSTEM_RECENT_APPS)) {
                        Log.e(TAG, "按下RECENT_APPS键");
                        hide();
                    }
                }
            }

        }

    }
}
