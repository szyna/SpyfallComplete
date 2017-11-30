package az.spyfallcomplete;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameConfiguration implements Parcelable{

    Map<String, String> playerRoles;
    List<String> playerList;
    Map<String, Integer> playerPoints;
    Map<String, List<String>> locations;
    long roundTime;
    long timeLeft;
    int roundsLeft = 8;
    String currentLocation;
    String currentSpy;

    GameConfiguration(Activity activity, List<String> playersList, long timeLeft, String locationsFileName){
        this.playerList = playersList;
        this.playerPoints = new HashMap<>();
        this.roundTime = timeLeft;
        this.timeLeft = timeLeft;
        this.locations = loadLocationsFromJSON(activity, locationsFileName);
        this.currentLocation = "";
        this.playerRoles = new HashMap<>();
    }

    private Map<String, List<String>> loadLocationsFromJSON(Activity activity, String locationsFileName){
        Map<String, List<String>> result = new HashMap<>();
        String json = loadJSONFromAsset(activity, locationsFileName);
        try {
            JSONObject obj = (new JSONObject(json)).getJSONObject("Places").getJSONObject(Locale.getDefault().getDisplayLanguage());
            Iterator<String> keys = obj.keys();
            while( keys.hasNext() ){
                String key = keys.next();
                if ( obj.get(key) instanceof JSONArray ) {
                    JSONArray roles = (JSONArray) obj.get(key);
                    LinkedList<String> r = new LinkedList<>();
                    for (int i=0; i<roles.length(); i++){
                        r.add((String) roles.get(i));
                    }
                    result.put(key, r);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String loadJSONFromAsset(Activity activity, String jsonName) {
        String json;
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

    void updatePlayerPoints(){
        for(String player : this.playerList){
            playerPoints.put(player, 0);
        }
    }

    void selectNextGameConfiguration(){
        // clear previous roles and remove previous location from locations map
        playerRoles.clear();
        timeLeft = roundTime;
        roundsLeft -= 1;
        if (locations.containsKey(currentLocation)){
            locations.remove(currentLocation);
        }
        // get shuffle locations and select one
        LinkedList<String> shuffledList = new LinkedList<>(locations.keySet());
        Collections.shuffle(shuffledList);
        currentLocation = shuffledList.get(0);
        // get locations roles, remove spy shuffle rest, add spy, add n -1 items and shuffle again
        List<String> rolesList = new LinkedList<>(locations.get(currentLocation));
        String spyRole = rolesList.get(0);
        rolesList.remove(0);
        Collections.shuffle(rolesList);
        rolesList.add(0, spyRole);
        rolesList = rolesList.subList(0, playerList.size());
        Collections.shuffle(rolesList);
        for (int i=0; i<playerList.size(); i++){
            playerRoles.put(playerList.get(i), rolesList.get(i));
            if (rolesList.get(i).equals(spyRole)){
                currentSpy = playerList.get(i);
            }
        }
    }

    protected GameConfiguration(Parcel in) {
        playerList = new LinkedList<>();
        in.readList(playerList, String.class.getClassLoader());
        locations = new HashMap<>();
        in.readMap(locations, LinkedList.class.getClassLoader());
        playerPoints = new HashMap<>();
        in.readMap(playerPoints, Integer.class.getClassLoader());
        playerRoles = new HashMap<>();
        in.readMap(playerRoles, String.class.getClassLoader());
        timeLeft = in.readLong();
        roundTime = in.readLong();
        roundsLeft = in.readInt();
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
        parcel.writeList(playerList);
        parcel.writeMap(locations);
        parcel.writeMap(playerPoints);
        parcel.writeMap(playerRoles);
        parcel.writeLong(timeLeft);
        parcel.writeLong(roundTime);
        parcel.writeInt(roundsLeft);
    }
}
