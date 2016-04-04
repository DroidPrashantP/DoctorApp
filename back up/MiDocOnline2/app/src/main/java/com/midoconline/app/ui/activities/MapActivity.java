package com.midoconline.app.ui.activities;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.midoconline.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity {

    private static final String GOOGLE_API_KEY = "AIzaSyCjwoMjm_Nt87taeGjSKQ7k39fLbqz_2hY";
   // GoogleMap googleMap;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
//        if (!isGooglePlayServicesAvailable()) {
//            finish();
//        }
//        setContentView(R.layout.activity_map);
//
//        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
//        googleMap = fragment.getMap();
//        googleMap.setMyLocationEnabled(true);
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, true);
//        Location location = locationManager.getLastKnownLocation(bestProvider);
//        if (location != null) {
//            onLocationChanged(location);
//        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
//
//        executePlaceRequest();
    }

//    private void executePlaceRequest() {
//        String type = "atm";
//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        googlePlacesUrl.append("location=" + latitude + "," + longitude);
//        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
//        googlePlacesUrl.append("&types=" + type);
//        googlePlacesUrl.append("&sensor=true");
//        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
//
//        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
//        Object[] toPass = new Object[2];
//        toPass[0] = googleMap;
//        toPass[1] = googlePlacesUrl.toString();
//        googlePlacesReadTask.execute(toPass);
//    }
//
//    private boolean isGooglePlayServicesAvailable() {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (ConnectionResult.SUCCESS == status) {
//            return true;
//        } else {
//            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
//            return false;
//        }
//    }
//
//
//    @Override
//    public void onLocationChanged(Location location) {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        LatLng latLng = new LatLng(latitude, longitude);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_map, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
//        String googlePlacesData = null;
//        GoogleMap googleMap;
//
//        @Override
//        protected String doInBackground(Object... inputObj) {
//            try {
//                googleMap = (GoogleMap) inputObj[0];
//                String googlePlacesUrl = (String) inputObj[1];
//                googlePlacesData = read(googlePlacesUrl);
//            } catch (Exception e) {
//                Log.d("Google Place Read Task", e.toString());
//            }
//            return googlePlacesData;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
//            Object[] toPass = new Object[2];
//            toPass[0] = googleMap;
//            toPass[1] = result;
//            placesDisplayTask.execute(toPass);
//        }
//    }
//
//    public String read(String httpUrl) throws IOException {
//        String httpData = "";
//        InputStream inputStream = null;
//        HttpURLConnection httpURLConnection = null;
//        try {
//            URL url = new URL(httpUrl);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.connect();
//            inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//            httpData = stringBuffer.toString();
//            bufferedReader.close();
//        } catch (Exception e) {
//            Log.d("reading Http url", e.toString());
//        } finally {
//            inputStream.close();
//            httpURLConnection.disconnect();
//        }
//        return httpData;
//    }
//
//    public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
//
//        JSONObject googlePlacesJson;
//        GoogleMap googleMap;
//
//        @Override
//        protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
//
//            List<HashMap<String, String>> googlePlacesList = null;
//            Places placeJsonParser = new Places();
//
//            try {
//                googleMap = (GoogleMap) inputObj[0];
//                googlePlacesJson = new JSONObject((String) inputObj[1]);
//                googlePlacesList = placeJsonParser.parse(googlePlacesJson);
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            }
//            return googlePlacesList;
//        }
//
//        @Override
//        protected void onPostExecute(List<HashMap<String, String>> list) {
//            googleMap.clear();
//            for (int i = 0; i < list.size(); i++) {
//                MarkerOptions markerOptions = new MarkerOptions();
//                HashMap<String, String> googlePlace = list.get(i);
//                double lat = Double.parseDouble(googlePlace.get("lat"));
//                double lng = Double.parseDouble(googlePlace.get("lng"));
//                String placeName = googlePlace.get("place_name");
//                String vicinity = googlePlace.get("vicinity");
//                LatLng latLng = new LatLng(lat, lng);
//                markerOptions.position(latLng);
//                markerOptions.title(placeName + " : " + vicinity);
//                googleMap.addMarker(markerOptions);
//            }
//        }
//    }
//
//
//    public class Places {
//
//        public List<HashMap<String, String>> parse(JSONObject jsonObject) {
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = jsonObject.getJSONArray("results");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return getPlaces(jsonArray);
//        }
//
//        private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
//            int placesCount = jsonArray.length();
//            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
//            HashMap<String, String> placeMap = null;
//
//            for (int i = 0; i < placesCount; i++) {
//                try {
//                    placeMap = getPlace((JSONObject) jsonArray.get(i));
//                    placesList.add(placeMap);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return placesList;
//        }
//
//        private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
//            HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
//            String placeName = "-NA-";
//            String vicinity = "-NA-";
//            String latitude = "";
//            String longitude = "";
//            String reference = "";
//
//            try {
//                if (!googlePlaceJson.isNull("name")) {
//                    placeName = googlePlaceJson.getString("name");
//                }
//                if (!googlePlaceJson.isNull("vicinity")) {
//                    vicinity = googlePlaceJson.getString("vicinity");
//                }
//                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
//                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
//                reference = googlePlaceJson.getString("reference");
//                googlePlaceMap.put("place_name", placeName);
//                googlePlaceMap.put("vicinity", vicinity);
//                googlePlaceMap.put("lat", latitude);
//                googlePlaceMap.put("lng", longitude);
//                googlePlaceMap.put("reference", reference);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return googlePlaceMap;
//        }
//    }
}
