package com.cynthiar.dancingday;

import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixElement;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixRow;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixStatusCode;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixValueText;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceResultTests {

    @Test
    public void parse_Response() throws Exception {
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

        DistanceResult distanceResult = new DistanceResult(jsonResponse);
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
