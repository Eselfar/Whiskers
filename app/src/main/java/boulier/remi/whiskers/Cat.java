package boulier.remi.whiskers;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Remi BOULIER on 13/10/2015.
 * email: boulier.r@gmail.com
 * project: Whiskers
 */
public class Cat implements Parcelable {
    String breed;
    Double legs;
    Image image;
    @SerializedName("prefered-food")
    String preferredFood;
    @SerializedName("colour")
    String color;
    String size;
    int whiskers;
    String foodPackage;

    Creator<Cat> CREATOR = new Creator<Cat>() {
        @Override
        public Cat createFromParcel(Parcel source) {
            return new Cat(source);
        }

        @Override
        public Cat[] newArray(int size) {
            return new Cat[size];
        }
    };

    public Cat(Parcel in) {
        breed = in.readString();
        legs = in.readDouble();
        image = in.readParcelable(Image.class.getClassLoader());
        preferredFood = in.readString();
        color = in.readString();
        size = in.readString();
        whiskers = in.readInt();
        foodPackage = in.readString();
    }

    public void setFoodPackage(@NonNull List<Food> foods) {
        for (Food food : foods) {
            if (food.isFood(preferredFood)){
                this.foodPackage = food.foodPackage;
                return;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(breed);
        dest.writeDouble(legs);
        dest.writeParcelable(image, 0);
        dest.writeString(preferredFood);
        dest.writeString(color);
        dest.writeString(size);
        dest.writeInt(whiskers);
        dest.writeString(foodPackage);
    }

    public String getFoodPackage(Context context) {
        if (TextUtils.isEmpty(foodPackage))
            return context.getString(R.string.n_a);
        return foodPackage;
    }

    class Image implements Parcelable{
        private String xxhdpi;
        private String xhdpi;
        private String hdpi;
        private String mdpi;
        private String ldpi;

        protected Image(Parcel in) {
            xxhdpi = in.readString();
            xhdpi = in.readString();
            hdpi = in.readString();
            mdpi = in.readString();
            ldpi = in.readString();
        }

        public final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        public String getImageResString() {
            if (!TextUtils.isEmpty(xxhdpi))
                return xxhdpi;
            if (!TextUtils.isEmpty(xhdpi))
                return xhdpi;
            if (!TextUtils.isEmpty(hdpi))
                return hdpi;
            if (!TextUtils.isEmpty(mdpi))
                return mdpi;

            return ldpi;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(xxhdpi);
            dest.writeString(xhdpi);
            dest.writeString(hdpi);
            dest.writeString(mdpi);
            dest.writeString(ldpi);
        }
    }
}

