package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyContent;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Robert on 13/02/2017.
 */

public class PNBDanceClassExtractor extends HtmlDanceClassExtractor {
    private static final String mainSelector = "table tr";
    private int mSchoolNumber;

    public PNBDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "PNB";
    }

    @Override
    public String getUrl() {
        return "https://www.pnb.org/pnb-school/classes/open-program/#fusion-tab-classschedule";
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
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add("PNB Seattle"); // TODO consts
        schoolList.add("PNB Bellevue");
        return schoolList;
    }

    @Override
    protected String getSelector() {
        return PNBDanceClassExtractor.mainSelector;
    }

    @Override
    protected void initializeExtraction() {
        // Reset company number
        mSchoolNumber = -1;
    }

    @Override
    protected ExtractorResults parseBaseElement(int elementIndex, Element classElement) {
        List<DummyItem> classItemList = new ArrayList<>();
        try {
            NextElement nextElement = findNextChildElement(classElement, 0);
            Element firstChild = nextElement.getElement();
            if (null == firstChild)
                return new ExtractorResults();

            String firstChildText = firstChild.text();
            if (firstChildText.equals("Class"))
                mSchoolNumber++;

            DanceClassLevel level = this.parseLevel(firstChildText);
            if (DanceClassLevel.Unknown == level)
                return new ExtractorResults();

            int i=1;   // 6 days (no classes on Sunday)
            while (i <= 6) {
                nextElement = findNextChildElement(classElement, nextElement.getNextN());
                Element dayClassElement = nextElement.getElement();
                if (null == dayClassElement) {
                    i++;
                    continue; // no class on that day
                }

                String classText = StringEscapeUtils.unescapeHtml4(dayClassElement.text());
                if (null == classText || classText.equals("")) {
                    i++;
                    continue; // no class on that day
                }

                // Process each child node
                // There might be multiple classes on a given day
                int j=0;
                int childNodeSize = dayClassElement.childNodes().size();
                while (j < childNodeSize) {
                    // Retrieve the child node
                    Node childNode = dayClassElement.childNode(j);

                    // Check if the child node is a <strong> element
                    if (childNode instanceof Element) {
                        Element elementNode = (Element)childNode;

                        // The <strong> element contains the time information
                        if (elementNode.tagName().equals("strong")) {
                            Node nextChildNode = childNode.nextSibling();

                            // The next node contains the teacher information
                            if (nextChildNode instanceof TextNode) {
                                String classInLevelText = elementNode.text() + ((TextNode)nextChildNode).text();

                                // Parse the class text
                                if (null != classInLevelText && !classInLevelText.equals("")) {
                                    DummyItem classInLevel = parseClassText(classInLevelText, i, level);
                                    if (null != classInLevel) {
                                        classItemList.add(classInLevel);
                                    }
                                }
                                // Skip the next node, as it was processed with the current one
                                j+=2;
                            }
                        }
                    }
                    // Continue looking for class nodes
                    j++;
                }
                i++;
            }
            return new ExtractorResults(classItemList);
        }
        catch (Exception e) {
            return new ExtractorResults(classItemList, "Could not parse class element: " + e.getMessage());
        }
    }

    private DummyItem parseClassText(String classText, int i, DanceClassLevel level) {
        int indexOfOpeningParenthesis = classText.indexOf('(');
        int indexOfClosingParenthesis = classText.indexOf(')');
        if (indexOfOpeningParenthesis < 0 || indexOfClosingParenthesis <= 0)
            return null; // cannot parse class on that day

        // Extract the time
        String time = classText.substring(0, indexOfOpeningParenthesis).trim();

        // Extract the teacher
        String teacher = classText.substring(indexOfOpeningParenthesis + 1, indexOfClosingParenthesis);

        // Get the company
        Schools.DanceSchool school = getSchool();

        // Get the day
        String day = DummyContent.DAYS_OF_THE_WEEK[i-1];

        // Build and return the dummy item
        return new DummyItem(day, DanceClassTime.create(time), school, teacher, level);
    }

    private DanceClassLevel parseLevel(String levelText) {
        if (levelText.contains("Int.") && levelText.contains("Adv."))
            return DanceClassLevel.Advanced;
        if (levelText.contains("Int.") && levelText.contains("Beg."))
            return DanceClassLevel.BeginnerIntermediate;
        if (levelText.contains("Beg."))
            return DanceClassLevel.Beginner;
        if (levelText.contains("Int."))
            return DanceClassLevel.Intermediate;
        return DanceClassLevel.Unknown;
    }

    private Schools.DanceSchool getSchool() {
        if (0 == mSchoolNumber)
            return Schools.PNB_SEATTLE_SCHOOL;
        if (1 == mSchoolNumber)
            return Schools.PNB_BELLEVUE_SCHOOL;
        return Schools.DanceSchool.Unknown;
    }

    private NextElement findNextChildElement(Element parentElement, int n) {
        // Find the next element child
        Element nextElement = null;
        while (n < parentElement.childNodeSize()) {
            try {
                nextElement = (Element) parentElement.childNode(n);
                n++;
                break;
            }
            catch (Exception e) {
                n++;
            }
        }
        return new NextElement(nextElement, n);
    }

    private class NextElement {
        Element mElement;
        int mNextN;
        public NextElement(Element element, int n) {
            mElement = element;
            mNextN = n;
        }

        public Element getElement() {
            return mElement;
        }

        public int getNextN() {
            return mNextN;
        }
    }
}
