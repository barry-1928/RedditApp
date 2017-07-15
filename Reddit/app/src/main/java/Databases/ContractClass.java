package Databases;

import android.provider.BaseColumns;

/**
 * Created by dell on 15-07-2017.
 */

public class ContractClass {

    private ContractClass() {};

    public static class SearchHistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "SEARCH_TABLE";
        public static final String TITLE = "title";
        public static final String KIND = "kind";
        public static final String TIMESTAMP = "timestamp";
        public static final String COMMENTS = "comments";

        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+_ID+" INTEGER PRIMARY KEY, "+TITLE+" VARCHAR(255), "+KIND+" VARCHAR(255), "+TIMESTAMP+" REAL, "+COMMENTS+" INTEGER)";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME+"";
    }
}
