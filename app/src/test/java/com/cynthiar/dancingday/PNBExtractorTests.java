package com.cynthiar.dancingday;

import com.cynthiar.dancingday.extractor.ExtractorResults;
import com.cynthiar.dancingday.extractor.PNBDanceClassExtractor;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PNB dance class extractor.
 */

public class PNBExtractorTests {
    @Test
    public void TestExtraction() throws URISyntaxException, IOException {
        // Load the test HTML file
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("pnbTable.html");
        FileInputStream fileInputStream = new FileInputStream(resource.getPath());

        // Extract the items
        PNBDanceClassExtractor pnbExtractor = new PNBDanceClassExtractor(null);
        Document doc = pnbExtractor.processDownload(fileInputStream, resource.toURI().toString());
        ExtractorResults extractorResults = pnbExtractor.extractItems(doc);

        // Check that the items were successfully extracted
        assertNotNull(extractorResults);
        assertTrue(extractorResults.isSuccess());
        assertNotNull(extractorResults.getClassList());
        assertFalse(extractorResults.getClassList().isEmpty());
    }
}
