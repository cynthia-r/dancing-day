package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Schools;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 7/15/2018.
 */

public class PNBBellevueDanceClassExtractor extends NewPNBDanceClassExtractor {

    public PNBBellevueDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "PNB Bellevue";
    }

    @Override
    public String getUrl() {
        return "https://www.pnb.org/wp-admin/admin-ajax.php?action=wp_ajax_ninja_tables_public_action&table_id=20680&target_action=get-all-data";
    }
}
