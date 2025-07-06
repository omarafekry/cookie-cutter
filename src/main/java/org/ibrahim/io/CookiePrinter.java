package org.ibrahim.io;

import org.ibrahim.model.Cookie;

import java.util.List;

public interface CookiePrinter {
    void print(List<Cookie> cookies);
    void print(Cookie cookie, int index);
}

