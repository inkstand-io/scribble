package io.inkstand.scribble.rules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import junit.framework.AssertionFailedError;

import org.apache.commons.io.IOUtils;
import org.junit.rules.TemporaryFolder;

/**
 * A rule for creating an external file in a temporary folder with a specific content. If no content is defined an empty
 * file will be created
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class TemporaryFile extends ExternalResource {

    /**
     * The URL that points to the resource that provides the content for the file
     */
    private URL contentUrl;
    /**
     * The temporary folder the file will be created in
     */
    private final TemporaryFolder folder;
    /**
     * The name of the file
     */
    private final String filename;
    /**
     * The actual created file
     */
    private File file;
    /**
     * Flag to indicate that content URL must be set
     */
    private boolean forceContent;

    /**
     * Creates an ExternalFile in the specified temporary folder with the specified filename
     *
     * @param folder
     * @param filename
     */
    public TemporaryFile(final TemporaryFolder folder, final String filename) {
        super(folder);
        this.folder = folder;
        this.filename = filename;
    }

    @Override
    protected void before() throws Throwable {
        file = folder.newFile(filename);
        if (forceContent && contentUrl == null) {
            throw new AssertionFailedError("ContentUrl is not set");
        } else if (contentUrl != null) {
            final InputStream is = contentUrl.openStream();
            final OutputStream os = new FileOutputStream(file);
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    protected void after() {
        if (file != null) {
            file.delete(); // NOSONAR
        }

    }

    /**
     * Returns the file handle of the external file
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the URL that contains the content for the file. <br>
     * The method must be invoked before the rule is applied.
     *
     * @param contentUrl
     */
    @RuleSetup
    public void setContentUrl(final URL contentUrl) {
        this.contentUrl = contentUrl;
    }

    /**
     * Setting this to true will ensure, the file has content provided by the content url. If set to false the file may
     * not have a content url associated and therefore may be empty. <br>
     * <br>
     * The method must be invoked before the rule is applied.
     *
     * @param forceContent
     *            <code>true</code> if contentURL has to be set
     */
    @RuleSetup
    public void setForceContent(final boolean forceContent) {
        this.forceContent = forceContent;
    }

}
