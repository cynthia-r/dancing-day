package com.cynthiar.dancingday.distance.matrix;

import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixElement;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixRow;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixStatusCode;
import com.cynthiar.dancingday.distance.matrix.model.DistanceMatrixValueText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceResult {
    private DistanceMatrixStatusCode mStatus;
    private String[] mOriginAddresses;
    private String[] mDestinationAddresses;
    private DistanceMatrixRow[] mRows;

    public DistanceResult() {
    }

    public DistanceMatrixStatusCode getStatus() {
        return mStatus;
    }

    public String[] getOriginAddresses() {
        return mOriginAddresses;
    }

    public String[] getDestinationAddresses() {
        return mDestinationAddresses;
    }

    public DistanceMatrixRow[] getRows() {
        return mRows;
    }

    public void initialize(JSONObject jsonObject) {
        if (null == jsonObject)
            return;
        try {
            // Status
            String statusString = jsonObject.getString("status");
            mStatus = this.parseStatusCode(statusString);

            // Origin and destination addresses
            JSONArray origins = jsonObject.getJSONArray("origin_addresses");
            mOriginAddresses = this.parseAddresses(origins);
            JSONArray destinations = jsonObject.getJSONArray("destination_addresses");
            mDestinationAddresses = this.parseAddresses(destinations);

            // Rows
            JSONArray rowArray = jsonObject.getJSONArray("rows");
            mRows = this.parseRows(rowArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEstimatedTime(){
        if (mStatus != DistanceMatrixStatusCode.OK)
            return "Error: " + mStatus.toString();

        if (null == mRows || mRows.length <= 0)
            return "Unknown";

        DistanceMatrixRow firstRow = mRows[0];
        DistanceMatrixElement[] elements = firstRow.getElements();
        if (null == elements || elements.length <= 0)
            return "Unknown";

        DistanceMatrixElement firstElement = elements[0];
        DistanceMatrixValueText durationInTraffic = firstElement.getDurationInTraffic();
        if (null == durationInTraffic)
            return "Unknown";

        return durationInTraffic.getText();
    }

    private String[] parseAddresses(JSONArray jsonArray){
        if (null == jsonArray || jsonArray.length() <= 0)
            return new String[0];

        String[] addresses = new String[jsonArray.length()];
        for (int i=0; i < jsonArray.length(); i++
                ) {
            try{
                addresses[i] =  jsonArray.getString(i);
            }
            catch(Exception e){
                addresses[i] = "";
            }
        }
        return addresses;
    }

    private DistanceMatrixRow[] parseRows(JSONArray rowArray){
        if (null == rowArray || rowArray.length() <= 0)
            return new DistanceMatrixRow[0];

        int rowLength = rowArray.length();
        DistanceMatrixRow[] rows = new DistanceMatrixRow[rowLength];
        for (int i=0; i < rowLength; i++){
            try{
                JSONObject row = rowArray.getJSONObject(i);
                JSONArray elementArray = row.getJSONArray("elements");
                if (null == elementArray || elementArray.length() <= 0) {
                    rows[i] = new DistanceMatrixRow();
                    continue;
                }

                int elementCount = elementArray.length();
                DistanceMatrixElement[] elements = new DistanceMatrixElement[rowLength];
                for (int j=0; j < elementCount; j++){
                    JSONObject elementObject = elementArray.getJSONObject(j);
                    if (null == elementObject) {
                        elements[j] = new DistanceMatrixElement();
                        continue;
                    }

                    String statusString = elementObject.getString("status");
                    DistanceMatrixStatusCode statusCode = this.parseStatusCode(statusString);

                    JSONObject durationObject = elementObject.getJSONObject("duration");
                    DistanceMatrixValueText duration = this.parseValueText(durationObject);

                    JSONObject durationInTrafficObject = elementObject.getJSONObject("duration_in_traffic");
                    DistanceMatrixValueText durationInTraffic = this.parseValueText(durationInTrafficObject);

                    JSONObject distanceObject = elementObject.getJSONObject("distance");
                    DistanceMatrixValueText distance = this.parseValueText(distanceObject);

                    elements[j] = new DistanceMatrixElement(statusCode, duration, durationInTraffic, distance);
                }
                rows[i] = new DistanceMatrixRow(elements);
            }
            catch (Exception e){
                rows[i] = null;
            }
        }
        return rows;
    }

    private DistanceMatrixValueText parseValueText(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject)
            return new DistanceMatrixValueText();

        int value = jsonObject.getInt("value");
        String text = jsonObject.getString("text");
        return new DistanceMatrixValueText(value, text);
    }

    private DistanceMatrixStatusCode parseStatusCode(String statusString){
        if (null == statusString || statusString.isEmpty())
            return DistanceMatrixStatusCode.UNKNOWN_STATUS;

        for (DistanceMatrixStatusCode statusCode:DistanceMatrixStatusCode.values()
             ) {
            if (statusCode == DistanceMatrixStatusCode.valueOf(statusString))
                return statusCode;
        }
        return DistanceMatrixStatusCode.UNKNOWN_STATUS;
    }
}
