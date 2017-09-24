package com.cynthiar.dancingday;

import android.net.NetworkInfo;
import android.util.Pair;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.download.DownloadTaskProgress;
import com.cynthiar.dancingday.download.IDownloadCallback;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.extractor.DanceClassExtractor;
import com.cynthiar.dancingday.model.extractor.KDCDanceClassExtractor;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.net.ssl.HttpsURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Robert on 18/02/2017.
 */

public class PdfExtractionTest {
    @Test
    public void pdfExtraction() throws Exception {

        String filePath = "E:\\AndroidProjects\\DancingDayFiles\\adult_teen_schedule__1_.pdf";
        FileInputStream fileInputStream = new FileInputStream(filePath);

        URL url = new URL("http://www.kirklanddance.org/uploads/4/8/4/0/48406365/adult_teen_schedule__1_.pdf");

        String toString = DummyUtils.readAllStream(fileInputStream);

        /*PDDocument pdDocument = PDDocument.load(fileInputStream);
        //PDDocument pdDocument = PDDocument.load(url);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(pdDocument);
        pdDocument.close();*/

        testParsePdf(filePath);
        //new KDCDanceClassExtractor().Extract(fileInputStream);

        fileInputStream.close();

    }

    private void testParsePdf(String filePath) {
        File file = new File(filePath);
        Document pdf = PDF.open(file);
    }

    private class TestDownloadCallback implements IDownloadCallback<List<DummyItem>>,
            IConsumerCallback<Pair<String, List<DummyItem>>> {

        @Override
        public void updateFromDownload(List<DummyItem> result) {

        }

        @Override
        public NetworkInfo getActiveNetworkInfo() {
            return null;
        }

        @Override
        public void onProgressUpdate(DownloadTaskProgress progressCode, DownloadTaskProgress percentComplete) {

        }

        @Override
        public void reportError(Exception exception) {

        }

        @Override
        public void finishDownloading() {

        }

        @Override
        public void updateFromResult(Pair<String, List<DummyItem>> result) {

        }
    }

    private void testDownloadAsync(URL url) {
        DanceClassExtractor danceClassExtractor = new KDCDanceClassExtractor(null);
        String key = "KDC";
        TestDownloadCallback testDownloadCallback = new TestDownloadCallback();
        IConsumerCallback testConsumerCallback = new TestDownloadCallback();
        //DownloadTask downloadTask = new DownloadTask(testDownloadCallback, testConsumerCallback, key, danceClassExtractor);
        //downloadTask.execute(danceClassExtractor.getUrl());
    }

    private Object testDownloadPdf(URL url) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        Object processedInput = null;
        try {

        if (url.getProtocol().equals("https"))
            connection = (HttpsURLConnection) url.openConnection();
        else
            connection = (HttpURLConnection) url.openConnection();
        // Timeout for reading InputStream arbitrarily set to 3000ms.
        connection.setReadTimeout(3000);
        // Timeout for connection.connect() arbitrarily set to 3000ms.
        connection.setConnectTimeout(3000);
        // For this use case, set HTTP method to GET.
        connection.setRequestMethod("GET");
        // Already true by default but setting just in case; needs to be true since this request
        // is carrying an input (response) body.
        connection.setDoInput(true);
        // Open communications link (network traffic occurs here).
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        // Retrieve the response body as an InputStream.
        stream = connection.getInputStream();
        if (stream != null) {
            // Converts Stream to String
           // String result = DummyUtils.readAllStream(stream);
            processedInput = new KDCDanceClassExtractor(null).processDownload(stream, url.toString());
        }
    } finally {
        // Close the stream
        if (stream != null)
            stream.close();

        // Disconnect HTTPS connection.
        if (connection != null) {
            connection.disconnect();
        }
    }
    return processedInput;
    }

    @Test
    public void testTimeRegex() {
        Pattern pattern = Pattern.compile("(\\d+\\D*((am)|(pm))?)+");
        Matcher m = pattern.matcher("6-7 pm");
        assertTrue(m.matches());
    }
}
