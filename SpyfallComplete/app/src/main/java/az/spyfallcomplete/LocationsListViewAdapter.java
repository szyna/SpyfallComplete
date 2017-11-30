package az.spyfallcomplete;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class LocationsListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<List<String>> data;

    LocationsListViewAdapter(Activity activity, List<String> locations){
        this.mInflater = activity.getLayoutInflater();
        data = new LinkedList<>();
        LinkedList<String> list = new LinkedList<>();
        int i = 0;
        for (String location : locations){
            if ( i % 2 == 0 && i > 0 ){
                data.add(list);
                list = new LinkedList<>();
            }
            list.add(location);
            i++;
        }

        if ( i % 2 != 0){
            data.add(list);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = mInflater.inflate(R.layout.locations_list_view_item, null);
            int[] ids = {R.id.location1, R.id.location2};
            TextView tv;
            for (int id = 0; id < data.get(i).size(); id++){
                tv = view.findViewById(ids[id]);
                tv.setText(data.get(i).get(id));
                tv.setTextSize(18);
            }
        }
        return view;
    }
}
