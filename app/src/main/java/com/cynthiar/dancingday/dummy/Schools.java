package com.cynthiar.dancingday.dummy;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class Schools {
    public static class DanceSchool{
        public String Key;
        public String Name;
        public String Address;
        public String Coordinates;

        public DanceSchool(String key, String name, String address, String coordinates){
            Key = key;
            Name = name;
            Address = address;
            Coordinates = coordinates;
        }

        @Override
        public String toString() {
            return this.Key;
        }

        public static DanceSchool fromString(String schoolKey) {
            for (DanceSchool school:Schools.SCHOOLS
                    ) {
                if (schoolKey.equals(school.toString()))
                    return school;
            }
            return null;
        }
    }

    public static final DanceSchool ADI_SCHOOL = new DanceSchool("ADI", "American Dance Institute", "8001 Greenwood Ave N, Seattle, WA 98103", "47.687170, -122.355662");
    public static final DanceSchool PNB_BELLEVUE_SCHOOL = new DanceSchool("PNB Bellevue", "Pacific Northwest Ballet", "1611 136th Pl NE, Bellevue, WA 98005 ", "47.623768, -122.164733");
    public static final DanceSchool PNB_SEATTLE_SCHOOL = new DanceSchool("PNB Seattle", "Pacific Northwest Ballet", "301 Mercer St, Seattle, WA 98109", "47.623995, -122.351504");
    public static final DanceSchool KDC_SCHOOL = new DanceSchool("KDC", "Kirkland Dance Center", "835 7th Ave, Kirkland, WA 98033", "47.680315, -122.191914");
    public static final DanceSchool WDC_SCHOOL = new DanceSchool("WDC", "Westlake Dance Center", "14713 Bothell Way NE #101, Seattle, WA 98125", "47.736171, -122.292630");
    public static final DanceSchool EXIT_SPACE_SCHOOL = new DanceSchool("ExitSpace", "Exit Space", "414 NE 72nd St, Seattle, WA 98115", "47.680720, -122.324057");
    public static final DanceSchool THE_NEST_SCHOOL = new DanceSchool("Nest", "The NEST", "6600 1st Ave NE, Seattle, WA 98115", "47.678416, -122.327137");

    public static final DanceSchool[] SCHOOLS = {
        ADI_SCHOOL,
        PNB_BELLEVUE_SCHOOL,
        PNB_SEATTLE_SCHOOL,
        KDC_SCHOOL,
        WDC_SCHOOL,
        EXIT_SPACE_SCHOOL,
        THE_NEST_SCHOOL,
    };
}
