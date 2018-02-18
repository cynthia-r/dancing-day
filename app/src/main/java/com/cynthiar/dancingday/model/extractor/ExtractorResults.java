package com.cynthiar.dancingday.model.extractor;

import com.cynthiar.dancingday.model.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cynthiar on 2/18/2018.
 */

public class ExtractorResults {
    private List<DummyItem> classList;
    private List<String> errorMessageList;

    public ExtractorResults() {
        this.classList = new ArrayList<>();
        this.errorMessageList = new ArrayList<>();
    }

    public ExtractorResults(List<DummyItem> classList) {
        this(classList, new ArrayList<String>());
    }

    public ExtractorResults(List<DummyItem> classList, String errorMessage) {
        this(classList, new ArrayList<String>());
        this.errorMessageList.add(errorMessage);
    }

    public ExtractorResults(String errorMessage) {
        this(new ArrayList<DummyItem>(), errorMessage);
    }

    public ExtractorResults(List<DummyItem> classList, List<String> errorMessageList) {
        this.classList = classList;
        this.errorMessageList = errorMessageList;
    }

    public List<DummyItem> getClassList() {
        return this.classList;
    }

    public List<String> getErrorMessageList() {
        return this.errorMessageList;
    }

    public void addClasses(List<DummyItem> classList) {
        if (null != classList && !classList.isEmpty())
            this.classList.addAll(classList);
    }

    public void addErrorMessages(List<String> errorMessageList) {
        if (null != errorMessageList && !errorMessageList.isEmpty())
            this.errorMessageList.addAll(errorMessageList);
    }

    public void addExtractorResults(ExtractorResults extractorResults) {
        if (null == extractorResults)
            return;

        List<DummyItem> classToAddList = extractorResults.getClassList();
        if (null != classToAddList && !classToAddList.isEmpty())
            this.classList.addAll(classToAddList);

        List<String> errorMessageToAddList = extractorResults.getErrorMessageList();
        if (null != errorMessageToAddList && !errorMessageToAddList.isEmpty())
            this.errorMessageList.addAll(errorMessageToAddList);
    }

    public boolean isSuccess() {
        return (null == this.errorMessageList) || this.errorMessageList.isEmpty();
    }
}
