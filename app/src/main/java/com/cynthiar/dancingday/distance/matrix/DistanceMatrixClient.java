package com.cynthiar.dancingday.distance.matrix;

import android.net.Uri;

import com.cynthiar.dancingday.download.HttpClient;
import com.cynthiar.dancingday.download.IHttpUser;
import com.cynthiar.dancingday.dummy.DummyUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceMatrixClient {
    private static final String PROTOCOL = "https";
    private static final String DISTANCE_MATRIX_HOST = "maps.googleapis.com";
    private static final String DISTANCE_MATRIX_PATH = "maps/api/distancematrix";
    private static final String OUTPUT_FORMAT = "json";

    private String mApiKey;

    private class DistanceHttpComponent implements IHttpUser {

        @Override
        public void onProgress(int... progressCode) {
        }

        @Override
        public Object processStream(InputStream inputStream, URL url) {
            try {
                return DummyUtils.readAllStream(inputStream);
            }
            catch(Exception e) {
                return "";
            }
        }
    }

    public DistanceMatrixClient(String apiKey){
        mApiKey = apiKey;
    }

    public DistanceResult getDistance(DistanceQuery distanceQuery){
        // Build url
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(DistanceMatrixClient.PROTOCOL);
        builder.authority(DistanceMatrixClient.DISTANCE_MATRIX_HOST);
        builder.appendEncodedPath(DistanceMatrixClient.DISTANCE_MATRIX_PATH);
        builder.appendEncodedPath(DistanceMatrixClient.OUTPUT_FORMAT);

        String[] originAddresses = distanceQuery.getOrigins();
        if (null != originAddresses && originAddresses.length > 0) {
            String originsParam = StringUtils.join(originAddresses, "|");
            builder.appendQueryParameter("origins", originsParam);
        }

        String[] destinationAddresses = distanceQuery.getDestinations();
        if (null != destinationAddresses && destinationAddresses.length > 0) {
            String destinationsParam = StringUtils.join(destinationAddresses, "|");
            builder.appendQueryParameter("destinations", destinationsParam);
        }

        this.appendQueryParam(builder, "mode", distanceQuery.getMode());
        this.appendQueryParam(builder, "language", distanceQuery.getLanguage());
        this.appendQueryParam(builder, "units", distanceQuery.getUnit());
        this.appendQueryParam(builder, "departure_time", distanceQuery.getDepartureTime());
        this.appendQueryParam(builder, "traffic_model", distanceQuery.getTrafficModel());
        builder.appendQueryParameter("key", mApiKey);

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Send the request
        DistanceHttpComponent distanceHttpComponent = new DistanceHttpComponent();
        HttpClient httpClient = new HttpClient(distanceHttpComponent);
        String jsonResponse = null;
        try {
            jsonResponse = (String)httpClient.getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the result
        return new DistanceResult(jsonResponse);
    }

    private void appendQueryParam(Uri.Builder builder, String paramName, String paramValue) {
        if (null != paramValue && !paramValue.isEmpty())
            builder.appendQueryParameter(paramName, paramValue);
    }
}
