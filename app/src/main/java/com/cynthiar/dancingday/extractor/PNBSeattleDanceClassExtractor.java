package com.cynthiar.dancingday.extractor;

import android.content.Context;

/**
 * Created by CynthiaR on 8/4/2018.
 */

public class PNBSeattleDanceClassExtractor extends NewPNBDanceClassExtractor {
    public PNBSeattleDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "PNB Seattle";
    }

    @Override
    public String getUrl() {
        return "https://www.pnb.org/wp-admin/admin-ajax.php?action=wp_ajax_ninja_tables_public_action&table_id=20679&target_action=get-all-data";
    }
}
