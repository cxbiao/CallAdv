package com.bryan.calladv.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Author：Cxb on 2016/3/17 09:58
 */
public class PhoneLocationUtils {

    private static final String TAG = "PhoneLocationUtils";
    public final static String DB_NAME = "mobileLocation.db";
    public final static String TABLE_NAME = "Dm_Mobile";
    public final static String FIELD_NUMBER="MobileNumber";
    public final static String FIELD_TYPE="MobileType";
    public final static String FIELD_AREA="MobileArea";
    public final static String FIELD_AREA_CODE="AreaCode";
    public static boolean isCopyFinished=false;

    public static String[] getPhoneLocation(Context context,String number) {

        File filePath=context.getExternalFilesDir(null);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        File file = new File(filePath, DB_NAME);
        String[] result=new String[2];
        result[0]="未知归属地";
        result[1]="";
        if (isCopyFinished) {

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);

            if (number.length() == 11 && !number.startsWith("0")) {
                Cursor cursor = db.query(TABLE_NAME, null, FIELD_NUMBER+"=?", new String[]{number.substring(0, 7)}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    result[0] = cursor.getString(cursor.getColumnIndex(FIELD_AREA));
                    result[1] = cursor.getString(cursor.getColumnIndex(FIELD_TYPE));
                    cursor.close();
                }
            } else {
                if(number.startsWith("0") && number.length()>4){
                    Cursor cursor = db.query(TABLE_NAME, null, FIELD_AREA_CODE+"=? or "+FIELD_AREA_CODE+"=?", new String[]{number.substring(0, 3),number.substring(0, 4)}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        result[0] = cursor.getString(cursor.getColumnIndex(FIELD_AREA));
                        cursor.close();
                    }
                }
            }
            db.close();
        }
        return result;
    }

    public static boolean copyDB(Context context) {
        try {
            File filePath=context.getExternalFilesDir(null);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            File file = new File(filePath, DB_NAME);
            if (!file.exists()) {
                InputStream is = context.getAssets().open(DB_NAME);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len=0;
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();
            }
            isCopyFinished=true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            isCopyFinished=false;
            return false;
        }
    }
}
