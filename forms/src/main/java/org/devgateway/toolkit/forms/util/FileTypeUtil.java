package org.devgateway.toolkit.forms.util;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Octavian Ciubotaru
 */
public final class FileTypeUtil {

    private FileTypeUtil() {
    }

    /**
     * Analyzes file content and returns true if content type matches the file name.
     *
     * @param fileName name of the document
     * @param inputStream contents of the document
     * @return true if extension and content match
     * @throws IOException
     */
    public static boolean extensionMatchesContents(String fileName, InputStream inputStream)
            throws IOException {
        try {
            Metadata metadata = new Metadata();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, fileName);
            DefaultDetector detector = new DefaultDetector();

            InputStream is = new BufferedInputStream(inputStream);
            MediaType mediaType = detector.detect(TikaInputStream.get(is), metadata);
            MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mediaType.toString());

            String ext = "." + org.apache.commons.io.FilenameUtils.getExtension(fileName).toLowerCase();

            return mimeType.getExtensions().contains(ext);
        } catch (MimeTypeException e) {
            throw new RuntimeException("Mime type must always be valid.", e);
        }
    }
}
