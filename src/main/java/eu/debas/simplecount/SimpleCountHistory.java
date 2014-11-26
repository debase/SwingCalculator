package eu.debas.simplecount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by debas on 09/11/14.
 */
public class SimpleCountHistory {
    List< HashMap<String, Object> > mHistoryList = new ArrayList< HashMap<String, Object> >();
    private int i = 0;

    public SimpleCountHistory() {

    }

    public void pushHistory(HashMap<String, Object> h) {
        mHistoryList.add(i, h);
        i = i + 1;
    }

    public HashMap<String, Object> getOld() {
        if (i <= 0)
            return null;
        i = i - 1;
        return mHistoryList.get(i);
    }
}
