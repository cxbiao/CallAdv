package com.bryan.calladv.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.bryan.calladv.R;
import com.bryan.calladv.core.Contact;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Author：Cxb on 2016/3/15 09:46
 *
 * AsyncQueryHandler 可以异步查询，使用了HandlerThread
 */
public class ContactUtils {

    private Context mContext;
    private ContentResolver mResolver;
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /**联系人显示名称**/
    private static  int display_name_index = -1;//0;

    /**电话号码**/
    private static  int number_index = -1;//1;

    /**头像ID**/
    private static  int photo_id_index = -1;//2;

    /**联系人的ID**/
    private static int contact_id_index = -1;//3;


    private ArrayList<Contact> contactList=new ArrayList<>();

    public ContactUtils(Context context){
        this.mContext=context;
        mResolver= mContext.getContentResolver();
    }

    /**得到手机通讯录联系人信息**/
    public void getPhoneContacts(){

        contactList.clear();
        // 获取手机联系人
        Cursor phoneCursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null,null, null);

        if(display_name_index==-1){
            display_name_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        }
        if(number_index ==-1){
            number_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        }
        if(photo_id_index==-1){
            photo_id_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
        }
        if(contact_id_index==-1){
            contact_id_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        }

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(number_index);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(display_name_index);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(contact_id_index);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(photo_id_index);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(mResolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }


                Contact contact=new Contact();
                contact.setContact_id(contactid);
                contact.setDisplay_name(contactName);
                contact.setNumber(phoneNumber);
                contact.setPhoto(contactPhoto);
                contactList.add(contact);
            }

            phoneCursor.close();
        }
    }


    /**得到手机SIM卡联系人人信息**/
    public void getSIMContacts() {
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = mResolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(number_index);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(display_name_index);

                //Sim卡中没有联系人头像
                Contact contact=new Contact();
                contact.setDisplay_name(contactName);
                contact.setNumber(phoneNumber);
                contactList.add(contact);
            }

            phoneCursor.close();
        }
    }


    public Contact getContactByNumber(String number){
        // 获取手机联系人
        StringBuilder sb=new StringBuilder();
        Cursor phoneCursor=null;
        if (number.length()==11){
            sb.append(number.subSequence(0,3));
            sb.append(" ");
            sb.append(number.substring(3,7));
            sb.append(" ");
            sb.append(number.substring(7,11));
            phoneCursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION,
                    ContactsContract.CommonDataKinds.Phone.NUMBER+"=? or "+ ContactsContract.CommonDataKinds.Phone.NUMBER+"=?",
                    new String[]{number,sb.toString()}, null);
        }else {
            phoneCursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION,
                    ContactsContract.CommonDataKinds.Phone.NUMBER+"=?",
                    new String[]{number}, null);
        }


        if(display_name_index==-1){
            display_name_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        }
        if(number_index ==-1){
            number_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        }
        if(photo_id_index==-1){
            photo_id_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
        }
        if(contact_id_index==-1){
            contact_id_index =phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        }

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(number_index);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(display_name_index);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(contact_id_index);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(photo_id_index);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(mResolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }

                phoneCursor.close();
                Contact contact=new Contact();
                contact.setContact_id(contactid);
                contact.setDisplay_name(contactName);
                contact.setNumber(phoneNumber);
                contact.setPhoto(contactPhoto);
                return contact;
            }


        }
        return null;
    }
}
