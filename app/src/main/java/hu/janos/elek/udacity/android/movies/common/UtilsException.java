package hu.janos.elek.udacity.android.movies.common;

/**
 * Created by jelek on 3/23/2017.
 */
public class UtilsException extends Exception {
    private int resId;

    UtilsException(int resId) {
        super("Some utils method failed.");
        this.resId = resId;
    }

    public int getMessageId() {
        return resId;
    }
}
