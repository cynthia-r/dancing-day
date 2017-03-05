package com.cynthiar.dancingday.dummy;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class Schools {
    public static class DanceSchool{
        public String Key;
        public String Name;
        public String Address;

        public DanceSchool(String key, String name, String address){
            Key = key;
            Name = name;
            Address = address;
        }
    }

    public static final DanceSchool ADI_SCHOOL = new DanceSchool("ADI", "American Dance Institute", "8001 Greenwood Ave N, Seattle, WA 98103");
    public static final DanceSchool PNB_BELLEVUE_SCHOOL = new DanceSchool("PNB Bellevue", "Pacific Northwest Ballet", "1611 136th Pl NE, Bellevue, WA 98005 ");
    public static final DanceSchool PNB_SEATTLE_SCHOOL = new DanceSchool("PNB Seattle", "Pacific Northwest Ballet", "301 Mercer St, Seattle, WA 98109");
    public static final DanceSchool KDC_SCHOOL = new DanceSchool("KDC", "Kirkland Dance Center", "835 7th Ave, Kirkland, WA 98033");
    public static final DanceSchool WDC_SCHOOL = new DanceSchool("WDC", "Westlake Dance Center", "14713 Bothell Way NE #101, Seattle, WA 98125 ");

    public static final DanceSchool[] SCHOOLS = {
        ADI_SCHOOL,
        PNB_BELLEVUE_SCHOOL,
        PNB_SEATTLE_SCHOOL,
        KDC_SCHOOL,
        WDC_SCHOOL,
    };
}
