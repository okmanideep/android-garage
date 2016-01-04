package com.manidesto.androidgarage.helper;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manidesto on 19/10/15.
 */
public class ActivityLog implements Parcelable{
    Class<? extends Activity> activity;
    int timeOpened;

    public ActivityLog(Class<? extends Activity> activity, int timeOpened){
        this.activity = activity;
        this.timeOpened = timeOpened;
    }

    public static final Creator<ActivityLog> CREATOR = new Creator<ActivityLog>() {
        @Override
        public ActivityLog createFromParcel(Parcel in) {
            String name = in.readString();
            Class<? extends Activity> activity = null;
            try {
                activity = (Class<? extends Activity>) Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int timeOpened = in.readInt();
            return new ActivityLog(activity, timeOpened);
        }

        @Override
        public ActivityLog[] newArray(int size) {
            return new ActivityLog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(timeOpened);
    }
}
