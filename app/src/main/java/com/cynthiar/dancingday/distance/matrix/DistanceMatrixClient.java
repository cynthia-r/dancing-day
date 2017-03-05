package com.cynthiar.dancingday.distance.matrix;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceMatrixClient {
    private static final String DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";

    private String mApiKey;

    public DistanceMatrixClient(String apiKey){
        mApiKey = apiKey;
    }

    public DistanceResult getDistance(DistanceQuery distanceQuery){

        String jsonResponse = "{\n" +
                "   \"destination_addresses\" : [ \"New York, NY, USA\" ],\n" +
                "   \"origin_addresses\" : [ \"Washington, DC, USA\" ],\n" +
                "   \"rows\" : [\n" +
                "      {\n" +
                "         \"elements\" : [\n" +
                "            {\n" +
                "               \"distance\" : {\n" +
                "                  \"text\" : \"225 mi\",\n" +
                "                  \"value\" : 361722\n" +
                "               },\n" +
                "               \"duration\" : {\n" +
                "                  \"text\" : \"3 hours 48 mins\",\n" +
                "                  \"value\" : 13672\n" +
                "               },\n" +
                "               \"duration_in_traffic\" : {\n" +
                "                  \"text\" : \"4 hours 12 mins\",\n" +
                "                  \"value\" : 15672\n" +
                "               },\n" +
                "               \"status\" : \"MAX_ELEMENTS_EXCEEDED\"\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"status\" : \"OK\"\n" +
                "}";

        return new DistanceResult(jsonResponse);
    }
}
