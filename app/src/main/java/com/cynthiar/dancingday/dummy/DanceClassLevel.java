package com.cynthiar.dancingday.dummy;

/**
 * Created by Robert on 25/02/2017.
 */

public enum DanceClassLevel {
    Beginner {
        public String toString() {
            return "Beginner";
        }
    },
    BeginnerIntermediate {
        public String toString() {
            return "Beg/Int";
        }
    },
    Intermediate {
        public String toString() {
            return "Intermediate";
        }
    },
    Advanced {
        public String toString() {
            return "Advanced";
        }
    },
    Pointe {
        public String toString() {
            return "Pointe";
        }
    },
    Children {
        public String toString() {
            return "Children";
        }
    },
    OpenLevel {
        public String toString() {
            return "Open Level";
        }
    }
}
