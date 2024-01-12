package com.nbicocchi.javafx.texteditor;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class IOResult<T> {
    private final T data;
    private final boolean ok;

    public IOResult(boolean ok, T data) {
        this.ok = ok;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean hasData() {
        return data != null;
    }

    public T getData() {
        return data;
    }
}
