package org.uncommons.reportng;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import org.testng.IReporter;

public abstract class AbstractReporter implements IReporter {

    private static final String ENCODING = "UTF-8";
    private static final String META_KEY = "meta";
    protected static final ReportMetadata META = new ReportMetadata();
    private static final String UTILS_KEY = "utils";
    private static final ReportNGUtils UTILS = new ReportNGUtils();
    private static final String MESSAGES_KEY = "messages";
    private static final ResourceBundle MESSAGES = ResourceBundle
        .getBundle("org.uncommons.reportng.messages.reportng", META.getLocale());

    private final String classpathPrefix;

    public AbstractReporter(String classpathPrefix) {
        this.classpathPrefix = classpathPrefix;
    }

    public Map<String, Object> createContext() {
        Map<String, Object> context = new HashMap<>();
        context.put(META_KEY, META);
        context.put(MESSAGES_KEY, MESSAGES);
        context.put(UTILS_KEY, UTILS);
        return context;
    }

    void generateFile(File file, String templateName, Map<String, Object> context)
        throws IOException, TemplateException {
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            URL resource = getClass().getClassLoader().getResource(classpathPrefix);
            if (resource == null) {
                throw new IllegalArgumentException("Unable to find resources :" + classpathPrefix);
            }
            cfg.setDirectoryForTemplateLoading(new File(resource.getPath()));
            cfg.setDefaultEncoding(ENCODING);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);

            Template template = cfg.getTemplate(templateName);

            template.process(context, writer);

            writer.flush();
        }
    }

    /**
     * Copy a single named resource from the classpath to the output directory.
     *
     * @param outputDirectory The destination directory for the copied resource.
     * @param resourceName The filename of the resource.
     * @param targetFileName The name of the file created in {@literal outputDirectory}.
     * @throws IOException If the resource cannot be copied.
     */
    protected void copyClasspathResource(File outputDirectory,
        String resourceName,
        String targetFileName) throws IOException {
        String resourcePath = classpathPrefix + resourceName;
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        copyStream(outputDirectory, resourceStream, targetFileName);
    }

    /**
     * Copy a single named file to the output directory.
     *
     * @param outputDirectory The destination directory for the copied resource.
     * @param sourceFile The path of the file to copy.
     * @param targetFileName The name of the file created in {@literal outputDirectory}.
     * @throws IOException If the file cannot be copied.
     */
    protected void copyFile(File outputDirectory,
        File sourceFile,
        String targetFileName) throws IOException {
        try (InputStream fileStream = new FileInputStream(sourceFile)) {
            copyStream(outputDirectory, fileStream, targetFileName);
        }
    }

    /**
     * Helper method to copy the contents of a stream to a file.
     *
     * @param outputDirectory The directory in which the new file is created.
     * @param stream The stream to copy.
     * @param targetFileName The file to write the stream contents to.
     * @throws IOException If the stream cannot be copied.
     */
    protected void copyStream(File outputDirectory,
        InputStream stream,
        String targetFileName) throws IOException {
        File resourceFile = new File(outputDirectory, targetFileName);
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, ENCODING));
            Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(resourceFile), ENCODING))) {
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                writer.write('\n');
                line = reader.readLine();
            }
            writer.flush();
        }
    }

    /**
     * Deletes any empty directories under the output directory.  These directories are created by
     * TestNG for its own reports regardless of whether those reports are generated.  If you are
     * using the default TestNG reports as well as ReportNG, these directories will not be empty and
     * will be retained.  Otherwise they will be removed.
     *
     * @param outputDirectory The directory to search for empty directories.
     */
    protected void removeEmptyDirectories(File outputDirectory) {
        if (outputDirectory == null || !outputDirectory.exists()) {
            return;
        }
        Arrays.stream(Objects.requireNonNull(outputDirectory.listFiles()))
            .filter(
                file -> file.isDirectory() && Objects.requireNonNull(file.listFiles()).length == 0)
            .forEach(File::delete);
    }

}
