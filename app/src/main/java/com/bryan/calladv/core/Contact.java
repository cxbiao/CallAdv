package com.bryan.calladv.core;

import android.graphics.Bitmap;

/**
 * Author：Cxb on 2016/3/15 10:17
 */
public class Contact  {
    /**联系人显示名称**/
    private String display_name;
    /**电话号码**/
    private String number;
    /**头像**/
    private Bitmap photo;
    /**联系人的ID**/
    private long contact_id;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public long getContact_id() {
        return contact_id;
    }

    public void setContact_id(long contact_id) {
        this.contact_id = contact_id;
    }
}
