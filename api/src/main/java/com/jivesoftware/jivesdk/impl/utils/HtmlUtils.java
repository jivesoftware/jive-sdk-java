package com.jivesoftware.jivesdk.impl.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 10/6/13
 * Time: 11:44 AM
 */
public class HtmlUtils {
    private static final Logger log = LoggerFactory.getLogger(DealRoomUtils.class);

    /*newBody = newBody.replace("&", "&amp")
                    .replace("\"", "&quot")
                    .replace("'", "&#039")
                    .replace("<", "&lt")
                    .replace(">", "&gt")
                    .replace("\n", "<br>");
                    */
    public static String getHtmlAsPlainText(String newBody) {
        if (!newBody.isEmpty()) {
            newBody = StringEscapeUtils.escapeHtml(newBody);
        }

        return newBody;
    }

    @Nonnull
    public static String getPlainTextAsHtml(String text){
        String plainText = text.replace("</p>", "\n")
                .replaceAll("\\<.*?\\>", "");
        plainText = toHtmlText(plainText);
        int newLineIndex = plainText.length() - 1;
        while (plainText.charAt(newLineIndex) == '\n') {
            newLineIndex--;
        }

        if (newLineIndex < plainText.length() - 1) {
            plainText = plainText.substring(0, newLineIndex + 1);
        }

        log.debug(String.format("Plain-text comment: %s. From HTML comment: %s", plainText, text));
        return plainText;
    }

    /**
     * return text.replace("<br>", "\n")
     *    .replace("&gt", ">")
     *    .replace("&lt", "<")
     *    .replace("&#039", "'")
     *    .replace("&quot", "\"")
     *   .replace("&amp", "&");
     */
    private static String toHtmlText(String text) {
        return StringEscapeUtils.unescapeHtml(text.replace("<br>", "\n"));
    }
}
