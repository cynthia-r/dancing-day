package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyContent;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 8/4/2018.
 */

public abstract class NewPNBDanceClassExtractor extends DanceClassExtractor<String> {

    protected NewPNBDanceClassExtractor(Context context) { super(context); }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add(this.getKey());
        return schoolList;
    }

    @Override
    public Certificate getCertificate() throws IOException, CertificateException {
        InputStream certificateInput = mContext.getResources().openRawResource(R.raw.wwwpnborg);
        Certificate certificate = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            certificate = cf.generateCertificate(certificateInput);
        }
        finally {
            certificateInput.close();
        }
        return certificate;
    }

    @Override
    public String processDownload(InputStream downloadStream, String baseUri) throws IOException {
        String danceClassResponse = DummyUtils.readAllStream(downloadStream);
        return danceClassResponse;
    }

    @Override
    protected ExtractorResults extract(String htmlContent) throws IOException {

        List<DummyItem> classItemList = new ArrayList<>();
        try {
            // Get the array of class categories (one for the morning and one for the evening)
            JSONArray classCategories = new JSONArray(htmlContent);
            Schools.DanceSchool school = Schools.DanceSchool.fromString(this.getKey());
            classItemList = this.extract(school, classCategories);

        } catch (JSONException e) {
            return new ExtractorResults(classItemList, "Could not parse class element: " + e.getMessage());
        }
        return new ExtractorResults(classItemList);
    }

    public List<DummyItem> extract(Schools.DanceSchool danceSchool, JSONArray classCategories) throws JSONException {
        if (null == classCategories || classCategories.length() <= 0)
            return new ArrayList<>();

        List<DummyItem> classItemList = new ArrayList<>();
        for (int i=0; i < classCategories.length(); i++) {
            JSONObject danceClass = classCategories.getJSONObject(i);
            for (int j=0; j < DummyContent.DAYS_OF_THE_WEEK.length - 1; j++) {
                String day = DummyContent.DAYS_OF_THE_WEEK[j];
                String dayKey = day.toLowerCase();

                // Retrieve the class text
                String classText = danceClass.getString(dayKey);
                if (null == classText || classText.isEmpty())
                    continue;

                // Parse the class text
                String[] classTextElements = classText.split("<br \\/>");
                if (null == classTextElements || 0 == classTextElements.length)
                    continue;

                // First element is the time
                DanceClassTime danceClassTime = DanceClassTime.create(classTextElements[0]);

                // Second element is the level
                DanceClassLevel danceClassLevel = DanceClassLevel.Unknown;
                if (2 <= classTextElements.length)
                    danceClassLevel = this.parseLevel(classTextElements[1]);

                // Third element is the teacher
                String teacher = "";
                if (3 <= classTextElements.length)
                    teacher = classTextElements[2];

                // Add the class to the list
                classItemList.add(new DummyItem(day, danceClassTime, danceSchool, teacher, danceClassLevel));
            }
        }
        return classItemList;
    }

    private DanceClassLevel parseLevel(String levelText) {
        // Try parsing the level from the text directly
        DanceClassLevel danceClassLevel = DanceClassLevel.Unknown;
        try {
            danceClassLevel = DanceClassLevel.valueOf(levelText);
        }
        catch (Exception e){
            // Swallow the exception and try parsing manually
            if (levelText.contains("Beginner/Intermediate"))
                return DanceClassLevel.BeginnerIntermediate;
            if (levelText.contains("Beginner"))
                return DanceClassLevel.Beginner;
            if (levelText.contains("Intermediate"))
                return DanceClassLevel.Intermediate;
        }

        // Return the dance class level
        return danceClassLevel;
    }
}
