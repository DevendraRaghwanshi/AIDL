package com.orientation;

import android.os.Parcel;
import android.os.Parcelable;

public class IMUModel implements Parcelable {
    public float pitch;
    public float roll;
    public float asd;


    public IMUModel() {
    }

    protected IMUModel(Parcel in) {
        pitch = in.readFloat();
        roll = in.readFloat();
        asd = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(pitch);
        dest.writeFloat(roll);
        dest.writeFloat(asd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IMUModel> CREATOR = new Creator<IMUModel>() {
        @Override
        public IMUModel createFromParcel(Parcel in) {
            return new IMUModel(in);
        }

        @Override
        public IMUModel[] newArray(int size) {
            return new IMUModel[size];
        }
    };
}
