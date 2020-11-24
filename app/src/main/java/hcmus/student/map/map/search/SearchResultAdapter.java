package hcmus.student.map.map.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.R;
import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;
import hcmus.student.map.map.utilities.place.GetPlaces;
import hcmus.student.map.map.utilities.place.PlaceRespondCallback;
import hcmus.student.map.map.utilities.place.PlaceSearch;

public class SearchResultAdapter extends BaseAdapter implements PlaceRespondCallback {
    Context context;
    List<Place> places;
    Database db;
    String currentRequest;

    public SearchResultAdapter(Context context) {
        this.context = context;
        this.places = new ArrayList<>();
        this.db = new Database(context);
        currentRequest = null;
    }

    public void search(String query) {
        places = new ArrayList<>();
        if (query.length() > 0) {
            places.addAll(db.searchForPlaces(query));
            GetPlaces getPlaces = new GetPlaces(this);
            currentRequest = PlaceSearch.getUrl(query, (Activity) context);
            getPlaces.execute(currentRequest);
        }
        else {
            currentRequest = null;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_search_result, null, false);
        }
        ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
        TextView txtPlaceName = convertView.findViewById(R.id.txtPlaceName);

        Place place = places.get(position);
        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
            ivAvatar.setImageBitmap(bmp);
        }
        txtPlaceName.setText(place.getName());
        return convertView;
    }

    @Override
    public void onRespond(String url, List<Place> placeList) {
        if (!currentRequest.equals(url))
            return;
        if (placeList == null)
            return;
        places.addAll(placeList);
        notifyDataSetChanged();
    }
}
