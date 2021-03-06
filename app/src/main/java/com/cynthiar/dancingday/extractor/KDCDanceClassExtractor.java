package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Page;
import com.snowtide.pdf.layout.Block;
import com.snowtide.pdf.layout.BlockParent;
import com.snowtide.pdf.layout.Line;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Robert on 18/02/2017.
 */

public class KDCDanceClassExtractor extends DanceClassExtractor<String> {
    private Pattern timePattern = Pattern.compile("(\\d+\\D*((am)|(pm))?)+");

    public KDCDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "KDC";
    }

    @Override
    public String getUrl() {
        return "http://www.kirklanddance.org/uploads/4/8/4/0/48406365/adult_teen_schedule__1_.pdf";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add(this.getKey());
        return schoolList;
    }

    /*public File processDownload(String downloadStream, String baseUri) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(downloadStream.getBytes());
        Document pdf = PDF.open(inputStream, "adult_teen_schedule__1_.pdf");
        //return pdf;
        return null;
    }


    public Document processDownload(InputStream downloadStream, String baseUri) throws IOException {
        File file = new File(mContext.getCacheDir(), "kirklandSchedule.pdf");
        //Document pdf = PDF.open(downloadStream, "adult_teen_schedule__1_.pdf");
        Document pdf = PDF.open(file);
        return pdf;
    }*/

    @Override
    public String processDownload(InputStream downloadStream, String baseUri) {
        return "okay";
    }

    @Override
    public ExtractorResults extract(String okay) {
        List<DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("6-7 pm"), Schools.KDC_SCHOOL, "Jerri", DanceClassLevel.BeginnerIntermediate));
        dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("6-7 pm"), Schools.KDC_SCHOOL, "Beinna", DanceClassLevel.BeginnerIntermediate));
        dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("7-8 pm"), Schools.KDC_SCHOOL, "Elbert/Cody", DanceClassLevel.OpenLevel));
        dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("6-7 pm"), Schools.KDC_SCHOOL, "Lindsay", DanceClassLevel.Beginner));
        dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("7-8 pm"), Schools.KDC_SCHOOL, "Mustafa", DanceClassLevel.OpenLevel));
        dummyItemList.add(new DummyItem("Wednesday", DanceClassTime.create("7-8 pm"), Schools.KDC_SCHOOL, "Mari", DanceClassLevel.BeginnerIntermediate));
        dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("6-7 pm"), Schools.KDC_SCHOOL, "Jerri", DanceClassLevel.Beginner));
        dummyItemList.add(new DummyItem("Saturday", DanceClassTime.create("10-11:30 am"), Schools.KDC_SCHOOL, "Jerri", DanceClassLevel.BeginnerIntermediate));
        dummyItemList.add(new DummyItem("Saturday", DanceClassTime.create("11:30 am-12:30 pm"), Schools.KDC_SCHOOL, "Jerri", DanceClassLevel.Pointe));
        return new ExtractorResults(dummyItemList);
    }

    public List<DummyItem> Extract(File pdfFile) throws IOException {
        File file = new File(mContext.getCacheDir(), "kirklandSchedule.pdf");
        //Document pdf = PDF.open(downloadStream, "adult_teen_schedule__1_.pdf");
        Document pdf = PDF.open(file);

        //ByteBuffer buffer = ByteBuffer.wrap(htmlContent.getBytes());

        Page page = pdf.getPage(0);
        BlockParent blockParent = page.getTextContent();
        List<DummyItem> dummyItemList = new ArrayList<>();

        // Front studio table
        Block firstTableBlock = blockParent.getChild(2);
        parseBlock(firstTableBlock, dummyItemList);

        // Second studio table
        Block secondTableBlock = blockParent.getChild(4);
        parseBlock(secondTableBlock, dummyItemList);


        pdf.close();

        return dummyItemList;
    }


    private void parseBlock(Block block, List<DummyItem> dummyItemList) {

        // Return if empty block
        if (0 == block.getChildCnt() && 0 == block.getLineCnt())
            return;

        // Parse block lines
        DummyItem dummyItem = null;
        int lineCount = block.getLineCnt();
        if (0 < lineCount) {
            DanceClassLevel level = DanceClassLevel.Unknown;
            String teacher = "";
            boolean isBalletOrHiphop = false;
            List<String> timeParts = new ArrayList<>();
            for (int i=0; i < lineCount; i++) {
                Line line = block.getLine(i);
                StringBuilder textLine = new StringBuilder(1024);
                line.pipe(new OutputTarget(textLine));
                String lineString = textLine.toString();

                // Try to parse category
                if (lineString.equals("Ballet") || lineString.equals("Hip Hop")) {
                    isBalletOrHiphop = true;
                    continue;
                }

                // Try to parse the level
                String levelString = parseLevel(lineString);
                if (!levelString.isEmpty()) {
                    level = DanceClassLevel.Unknown; // TODO
                    continue;
                }

                // Try to parse the time
                Matcher matcher = timePattern.matcher(lineString);
                if (matcher.matches()) {
                    timeParts.add(lineString);
                    continue;
                }

                // Otherwise it's the teacher name
                // Only store it if we already confirm that this is a ballet or hip hop class
                if (isBalletOrHiphop)
                    teacher = lineString;
            }

            // Create the dummy item
            if (isBalletOrHiphop) {
                String time = "";
                for (String timePart:timeParts
                     ) {
                    time = time.concat(timePart);
                }
                dummyItem = new DummyItem("day", DanceClassTime.create(time), Schools.KDC_SCHOOL, teacher, level);
            }
        }

        // Add the dummy item to the list
        if (null != dummyItem)
            dummyItemList.add(dummyItem);

        // Parse child blocks
        for (int i=0; i < block.getChildCnt(); i++) {
            Block childBlock = block.getChild(i);
            parseBlock(childBlock, dummyItemList);
        }
    }

    private String parseLevel(String levelText) {
        if (levelText.equals("Beg/Int"))
            return "Beg/Int";
        if (levelText.equals("Int/Adv"))
            return "Int/Adv";
        if (levelText.equals("Intermediate"))
            return "Intermediate";
        if (levelText.equals("Beginner"))
            return "Beginner";
        if (levelText.equals("Open Level"))
            return "Open Level"; // TODO this should be an enum
        return "";
    }
}
