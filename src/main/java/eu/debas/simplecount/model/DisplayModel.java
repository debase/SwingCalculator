package eu.debas.simplecount.model;

import java.util.HashMap;
import java.util.Observable;

/**
 * Created by debas on 21/11/14.
 */
public class DisplayModel extends Observable {
    private HashMap<Integer, String> m_displayValue = new HashMap<Integer, String>();

    public DisplayModel() {}

    public void addDisplay(int id) {
        m_displayValue.put(id, new String());
    }

    public String getDisplayValue(int id) {
        return m_displayValue.get(id);
    }

    public void setDisplayValue(int id, String value) {
        m_displayValue.put(id, value);
    }

    public void notifChanged() {
        setChanged();
        notifyObservers(m_displayValue);
    }
}
