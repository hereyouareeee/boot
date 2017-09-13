package com.boot.common.base;

import java.nio.charset.Charset;

/**
 * 编码
 */
public interface Charsets {

    String US_ASCII_VALUE = "US-ASCII";
    String ISO_8859_1_VALUE = "ISO-8859-1";
    String UTF_8_VALUE = "UTF-8";
    String UTF8_VALUE = "UTF8";
    String UTF_16BE_VALUE = "UTF-16BE";
    String UTF_16LE_VALUE = "UTF-16LE";
    String UTF_16_VALUE = "UTF-16";

    Charset US_ASCII = Charset.forName(US_ASCII_VALUE);
    Charset ISO_8859_1 = Charset.forName(ISO_8859_1_VALUE);
    Charset UTF_8 = Charset.forName(UTF_8_VALUE);
    Charset UTF_16BE = Charset.forName(UTF_16BE_VALUE);
    Charset UTF_16LE = Charset.forName(UTF_16LE_VALUE);
    Charset UTF_16 = Charset.forName(UTF_16_VALUE);
}
