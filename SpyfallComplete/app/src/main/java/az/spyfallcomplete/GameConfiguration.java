package az.spyfallcomplete;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GameConfiguration implements Parcelable{

    List<String> playersList;
    long timeLeft;
    String json;

    GameConfiguration(Activity activity, List<String> playersList, long timeLeft, String locationsFileName){
        this.playersList = playersList;
        this.timeLeft = timeLeft;
        this.json = this.loadJSONFromAsset(activity, locationsFileName);
        Log.d("XD", json);
    }

    private String loadJSONFromAsset(Activity activity, String jsonName) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(jsonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    protected GameConfiguration(Parcel in) {
        playersList = in.createStringArrayList();
        timeLeft = in.readLong();
    }

    public static final Creator<GameConfiguration> CREATOR = new Creator<GameConfiguration>() {
        @Override
        public GameConfiguration createFromParcel(Parcel in) {
            return new GameConfiguration(in);
        }

        @Override
        public GameConfiguration[] newArray(int size) {
            return new GameConfiguration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(playersList);
        parcel.writeLong(timeLeft);
    }
}
