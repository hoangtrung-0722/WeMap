package hcmus.student.map.map.utilities.place;

import java.util.List;

import hcmus.student.map.database.Place;

public interface PlaceRespondCallback {
    void onRespond(String url, List<Place> placeList);
}
