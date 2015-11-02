package boulier.remi.whiskers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Remi BOULIER on 02/11/2015.
 * email: boulier.r@gmail.com
 * project: Whiskers
 */
public class Food implements Parcelable{
    String name;
    @SerializedName("package")
    String foodPackage;

    protected Food(Parcel in) {
        name = in.readString();
        foodPackage = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(foodPackage);
    }

    public boolean isFood(String food) {
        return  name != null && name.equals(food);
    }
}
