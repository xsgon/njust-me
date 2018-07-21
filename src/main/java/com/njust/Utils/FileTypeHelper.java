package com.njust.Utils;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

public class FileTypeHelper {
    public static final String PICTURE_EXTENSIONS = "JPG|JPEG|PNG|BMP";
    public static final String PDF_EXTENSIONS = "PDF";
    public static final String WORD_EXTENSIONS = "DOC";
    public static final String EXCEL_EXTENSIONS = "XLS|XLSX";

    public static String getCapExt(String fileName) {
        String ext = StringUtils.getFilenameExtension(fileName);
        if (!StringUtils.isEmpty(ext)) {
            ext = ext.toUpperCase();//StringUtils.capitalize(ext);
        }

        return (StringUtils.isEmpty(ext) ? "" : ext);
    }

    public static boolean isPicture(String fileName) {
        return PICTURE_EXTENSIONS.contains(getCapExt(fileName));
    }

    public static boolean isPdf(String fileName) {
        return PDF_EXTENSIONS.contains(getCapExt(fileName));
    }

    public static boolean isWord(String fileName) {
        return WORD_EXTENSIONS.contains(getCapExt(fileName));
    }

    public static boolean isExcel(String fileName) {
        return EXCEL_EXTENSIONS.contains(getCapExt(fileName));
    }
}
