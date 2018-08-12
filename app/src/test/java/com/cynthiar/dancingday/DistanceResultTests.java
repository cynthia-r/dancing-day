package com.cynthiar.dancingday;

import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixElement;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixRow;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixStatusCode;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixValueText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by CynthiaR on 3/4/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class DistanceResultTests {

    @Mock
    JSONObject mockJsonObject;
    @Mock
    JSONArray mockOriginAddressesJson;
    @Mock
    JSONArray mockDestinationAddressesJson;
    @Mock
    JSONArray mockRowsJson;
    @Mock
    JSONObject mockRowJson;
    @Mock
    JSONArray mockElementsJson;
    @Mock
    JSONObject mockElementJson;
    @Mock
    JSONObject mockDurationJson;
    @Mock
    JSONObject mockDurationInTrafficJson;
    @Mock
    JSONObject mockDistanceJson;

    @Test
    public void parse_Response() throws Exception {
        /*String jsonResponse = "{\n" +
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
                "}";*/

        // Mock the origin and destination addresses
        when(mockOriginAddressesJson.length()).thenReturn(1);
        when(mockOriginAddressesJson.getString(0)).thenReturn("Washington, DC, USA");
        when(mockDestinationAddressesJson.length()).thenReturn(1);
        when(mockDestinationAddressesJson.getString(0)).thenReturn("New York, NY, USA");

        // Mock the values
        when(mockDurationJson.getInt("value")).thenReturn(13672);
        when(mockDurationJson.getString("text")).thenReturn("3 hours 48 mins");
        when(mockDurationInTrafficJson.getInt("value")).thenReturn(15672);
        when(mockDurationInTrafficJson.getString("text")).thenReturn("4 hours 12 mins");
        when(mockDistanceJson.getInt("value")).thenReturn(361722);
        when(mockDistanceJson.getString("text")).thenReturn("225 mi");

        // Mock the element
        when(mockElementJson.getString("status")).thenReturn("MAX_ELEMENTS_EXCEEDED");
        when(mockElementJson.getJSONObject("duration")).thenReturn(mockDurationJson);
        when(mockElementJson.getJSONObject("duration_in_traffic")).thenReturn(mockDurationInTrafficJson);
        when(mockElementJson.getJSONObject("distance")).thenReturn(mockDistanceJson);

        // Mock the rows and elements
        when(mockElementsJson.length()).thenReturn(1);
        when(mockElementsJson.getJSONObject(0)).thenReturn(mockElementJson);
        when(mockRowJson.getJSONArray("elements")).thenReturn(mockElementsJson);
        when(mockRowsJson.length()).thenReturn(1);
        when(mockRowsJson.getJSONObject(0)).thenReturn(mockRowJson);

        // Mock the top-level JSON object
        when(mockJsonObject.getString("status"))
                .thenReturn("OK");
        when(mockJsonObject.getJSONArray("origin_addresses"))
                .thenReturn(mockOriginAddressesJson);
        when(mockJsonObject.getJSONArray("destination_addresses"))
                .thenReturn(mockDestinationAddressesJson);
        when(mockJsonObject.getJSONArray("rows"))
                .thenReturn(mockRowsJson);

        DistanceResult distanceResult = new DistanceResult();
        distanceResult.initialize(mockJsonObject);

        assertEquals(DistanceMatrixStatusCode.OK, distanceResult.getStatus());

        String[] originAddresses = distanceResult.getOriginAddresses();
        assertArrayEquals(originAddresses, new String[] {"Washington, DC, USA"});
        String[] destinationAddresses = distanceResult.getDestinationAddresses();
        assertArrayEquals(destinationAddresses, new String[] {"New York, NY, USA"});

        DistanceMatrixRow[] rows = distanceResult.getRows();
        assertTrue(1 == rows.length);

        DistanceMatrixRow row = rows[0];
        DistanceMatrixElement[] elements = row.getElements();
        assertTrue(1 == elements.length);

        DistanceMatrixElement element = elements[0];
        assertEquals(DistanceMatrixStatusCode.MAX_ELEMENTS_EXCEEDED, element.getStatus());
        DistanceMatrixValueText duration = element.getDuration();
        assertEquals("3 hours 48 mins", duration.getText());
        assertEquals(13672, duration.getValue());

        DistanceMatrixValueText durationInTraffic = element.getDurationInTraffic();
        assertEquals("4 hours 12 mins", durationInTraffic.getText());
        assertEquals(15672, durationInTraffic.getValue());

        DistanceMatrixValueText distance = element.getDistance();
        assertEquals("225 mi", distance.getText());
        assertEquals(361722, distance.getValue());
    }
}
