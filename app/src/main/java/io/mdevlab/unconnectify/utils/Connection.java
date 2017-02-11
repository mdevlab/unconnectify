package io.mdevlab.unconnectify.utils;

/**
 * Possible Connections
 * Created by mdevlab on 2/10/17.
 */


public enum Connection {
    WIFI(0),
    CELLULAR_DATA(1),
    HOTSPOT(2),
    BLUETOOTH(3);

    private int value;

    Connection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get  the Connection from an int  possible cases :
     * 0 return WIFI Connection
     * 1 return CELLULAR_DATA Connection
     * 2 return HOTSPOT Connection
     * 3 return BLUETOOTH Connection
     *
     * @param number of the connection
     * @return Connection object
     */
    public static Connection fromInt(int number) {
        for (Connection c : values()) {
            if (c.value == number) return c;
        }
        return null;
    }
}
