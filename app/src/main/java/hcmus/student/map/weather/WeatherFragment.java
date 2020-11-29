package hcmus.student.map.weather;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;
import hcmus.student.map.weather.utilities.DetailWeather;
import hcmus.student.map.weather.utilities.GetWeather;
import hcmus.student.map.weather.utilities.GetWeatherDetailTask;
import hcmus.student.map.weather.utilities.OnDetailWeatherResponse;

public class WeatherFragment extends Fragment implements OnAddressLineResponse, OnDetailWeatherResponse {
    private MainActivity activity;
    private TextView txtPlaceName;
    private TextView txtDescription;
    private TextView txtTemperature;

    public static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null, false);
        txtPlaceName = view.findViewById(R.id.txtPlaceName);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtDescription = view.findViewById(R.id.txtDescription);

        txtPlaceName.setText(R.string.txtLoadingAddressLine);
        AddressLine addressLine = new AddressLine(new Geocoder(getContext()), this);
        Location location = activity.getLocation();
        addressLine.execute(new LatLng(location.getLatitude(), location.getLongitude()));

        GetWeatherDetailTask getWeatherDetailTask = new GetWeatherDetailTask(this);
        String url = GetWeather.getUrl(getContext(), new LatLng(location.getLatitude(), location.getLongitude()));
        getWeatherDetailTask.execute(url);
        return view;
    }

    @Override
    public void onAddressLineResponse(String addressLine) {
        if (addressLine != null)
            txtPlaceName.setText(addressLine);
        else
            txtPlaceName.setText(R.string.txtNullLocation);
    }

    @Override
    public void onDetailWeatherResponse(DetailWeather detailWeather) {
        if (detailWeather != null) {
            txtTemperature.setText(String.format("%s°", detailWeather.getTemperature()));
            txtDescription.setText(detailWeather.getDescription());
        }
    }
}