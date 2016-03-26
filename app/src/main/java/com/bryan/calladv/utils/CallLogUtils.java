package com.bryan.calladv.utils;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.bryan.calladv.core.CallLogBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author：Cxb on 2016/3/21 17:30
 */
public class CallLogUtils {


    private static final String[] projection = { CallLog.Calls.DATE, // 日期
            CallLog.Calls.NUMBER, // 号码
            CallLog.Calls.TYPE, // 类型
            CallLog.Calls.CACHED_NAME, // 名字
            CallLog.Calls._ID, // id
    };
    private ContentResolver mResolver;
    private List<CallLogBean> callLogs=new ArrayList<>();

    public CallLogUtils(Context context){
        mResolver=context.getContentResolver();
    }


    public  void getCallLogs(){
        MyAsyncQueryHandler asyncQueryHandler=new MyAsyncQueryHandler(mResolver);
        asyncQueryHandler.startQuery(0, null, CallLog.Calls.CONTENT_URI, projection, null, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);


    }


    private class MyAsyncQueryHandler extends AsyncQueryHandler{

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (cursor != null && cursor.getCount() > 0) {
                callLogs = new ArrayList<CallLogBean>();
                SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
                Date date;
                while (cursor.moveToNext()){
                    date = new Date(cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DATE)));
                    String number = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.NUMBER));
                    int type = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls.TYPE));
                    String cachedName = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称与电话号码，如果它的存在
                    int id = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls._ID));

                    CallLogBean callLogBean = new CallLogBean();
                    callLogBean.setId(id);
                    callLogBean.setNumber(number);
                    callLogBean.setName(cachedName);
                    if (null == cachedName || "".equals(cachedName)) {
                        callLogBean.setName(number);
                    }
                    callLogBean.setType(type);
                    callLogBean.setDate(sfd.format(date));
                    callLogs.add(callLogBean);
                }
            }
        }


    }
}
