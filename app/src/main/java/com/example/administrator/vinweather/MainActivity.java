package com.example.administrator.vinweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDescriptionTextView;
    private TextView mTemperatureTextView;
    private TextView mPressureTextView;
    private TextView mHumidityTextView;
    private TextView mWindSpeedTextView;
    private TextView mCloudsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDescriptionTextView = (TextView) findViewById(R.id.description);
        mTemperatureTextView = (TextView) findViewById(R.id.temperature);
        mPressureTextView = (TextView) findViewById(R.id.pressure);
        mHumidityTextView = (TextView) findViewById(R.id.humidity);
        mWindSpeedTextView = (TextView) findViewById(R.id.wind_speed);
        mCloudsTextView = (TextView) findViewById(R.id.clouds);
        new MyAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAsyncTask extends AsyncTask<String, Void, JSONObject>  {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObj = null;
            try {
                InputStream stream = new URL("http://api.openweathermap.org/data/2.5/weather?q=Vinnytsya").openStream();
                String str = streamToString(stream);
                jsonObj = new JSONObject(str);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.print(jsonObj);
            return jsonObj;
        }

        private String streamToString (InputStream inputStream) throws IOException {
            InputStreamReader is = new InputStreamReader(inputStream);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(is);
            String read = br.readLine();
            while(read != null) {
                sb.append(read);
                read = br.readLine();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response == null) return;
            try {
                mDescriptionTextView.setText(response.getJSONArray("weather").getJSONObject(0).getString("main") +
                        " (" + response.getJSONArray("weather").getJSONObject(0).getString("description") + ")");
                mTemperatureTextView.setText("Temperature: " +
                        String.valueOf(response.getJSONObject("main").getInt("temp")) + " \u2103");
                mPressureTextView.setText("Pressure: " +
                        String.valueOf(response.getJSONObject("main").getInt("pressure")) + " hPa");
                mHumidityTextView.setText("Humidity: " +
                        String.valueOf(response.getJSONObject("main").getInt("humidity")) + " %");
                mWindSpeedTextView.setText("Wind: " +
                        String.valueOf(response.getJSONObject("wind").getInt("speed")) + " meter/sec");
                mCloudsTextView.setText("Clouds: " +
                        String.valueOf(response.getJSONObject("clouds").getInt("all")) + " %");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}


