package com.cynthiar.dancingday;

import com.cynthiar.dancingday.extractor.ADIDanceClassExtractor;
import com.cynthiar.dancingday.extractor.DanceClassExtractor;
import com.cynthiar.dancingday.extractor.ExtractorResults;
import com.cynthiar.dancingday.extractor.NewPNBDanceClassExtractor;
import com.cynthiar.dancingday.extractor.PNBBellevueDanceClassExtractor;
import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.comparer.SingleDayDummyItemComparer;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for the dance class extractors.
 */

@RunWith(MockitoJUnitRunner.class)
public class ExtractorTests {
    @Mock
    JSONArray mockJsonArray;
    @Mock
    JSONObject mockMorningJson;
    @Mock
    JSONObject mockAfternoonJson;

    @Test
    public void TestPNBExtraction() throws IOException, URISyntaxException, JSONException {
        // Mock morning classes
        when(mockMorningJson.getString("monday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Coello");
        when(mockMorningJson.getString("tuesday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Carrie Imler");
        when(mockMorningJson.getString("wednesday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Nancy Crowley");
        when(mockMorningJson.getString("thursday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Naomi Glass");
        when(mockMorningJson.getString("friday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Miriam Landis");
        when(mockMorningJson.getString("saturday")).thenReturn("10 - 11:30 AM<br \\/>Intermediate<br \\/>Coello");

        // Mock afternoon classes
        when(mockAfternoonJson.getString("monday")).thenReturn("6 - 7:30 PM<br \\/>Intermediate<br \\/>Carrie Imler");
        when(mockAfternoonJson.getString("tuesday")).thenReturn("6 - 7:30 PM<br \\/>Beginner<br \\/>Deborah Kenner");
        when(mockAfternoonJson.getString("wednesday")).thenReturn("6 - 7:30 PM<br \\/>Intermediate<br \\/>Susan Gorter");
        when(mockAfternoonJson.getString("thursday")).thenReturn("6 - 7:30 PM<br \\/>Intermediate<br \\/>Kiyon Ross");
        when(mockAfternoonJson.getString("friday")).thenReturn("");
        when(mockAfternoonJson.getString("saturday")).thenReturn("");

        // Mock json response
        when(mockJsonArray.length()).thenReturn(2);
        when(mockJsonArray.getJSONObject(0)).thenReturn(mockMorningJson);
        when(mockJsonArray.getJSONObject(1)).thenReturn(mockAfternoonJson);

        // Test the extraction
        PNBBellevueDanceClassExtractor pnbExtraction = new PNBBellevueDanceClassExtractor();
        List<DummyItem> classItemList = pnbExtraction.extract(Schools.PNB_BELLEVUE_SCHOOL, mockJsonArray);
        this.TestExtraction(new ExtractorResults(classItemList));

        // Check the classes details
        assertEquals(10, classItemList.size());

        // Check the Wednesday morning class
        DummyItem expectedWednesdayMorningClass = new DummyItem("Wednesday", new DanceClassTime(10, 0, 11, 30),
                Schools.PNB_BELLEVUE_SCHOOL, "Nancy Crowley", DanceClassLevel.Intermediate);
        DummyItem wednesdayMorningClass = classItemList.get(2);
        assertEquals(0, new SingleDayDummyItemComparer().compare(expectedWednesdayMorningClass, wednesdayMorningClass));

        // Check the Tuesday afternoon class
        DummyItem expectedTuesdayAfternoonClass = new DummyItem("Tuesday", new DanceClassTime(18, 0, 19, 30),
                Schools.PNB_BELLEVUE_SCHOOL, "Deborah Kenner", DanceClassLevel.Beginner);
        DummyItem tuesdayAfternoonClass = classItemList.get(7);
        assertEquals(0, new SingleDayDummyItemComparer().compare(expectedTuesdayAfternoonClass, tuesdayAfternoonClass));
    }

    @Test
    public void TestADIExtraction() throws IOException, URISyntaxException {
        ADIDanceClassExtractor adiExtractor = new ADIDanceClassExtractor(null);
        Document content = this.LoadContent("adiTable.html", adiExtractor);
        ExtractorResults extractorResults = adiExtractor.extractItems(content);
        this.TestExtraction(extractorResults);
    }

    private <T> T LoadContent(String htmlFileName, DanceClassExtractor<T> danceClassExtractor) throws URISyntaxException, IOException {
        // Load the test HTML file
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(htmlFileName);
        FileInputStream fileInputStream = new FileInputStream(resource.getPath());

        // Process the download
        return danceClassExtractor.processDownload(fileInputStream, resource.toURI().toString());
    }

    private void TestExtraction(ExtractorResults extractorResults) {
        // Check that the items were successfully extracted
        assertNotNull(extractorResults);
        assertTrue(extractorResults.isSuccess());
        assertNotNull(extractorResults.getClassList());
        assertFalse(extractorResults.getClassList().isEmpty());
    }
}
