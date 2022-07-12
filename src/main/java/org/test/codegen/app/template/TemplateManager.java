package org.test.codegen.app.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.test.codegen.app.Main;
import org.test.codegen.app.exception.AppException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

@Singleton
public class TemplateManager {
    @Inject
    private Logger log;
    private Configuration conf;

    public TemplateManager() {
        conf = new Configuration(Configuration.VERSION_2_3_31);
        conf.setClassLoaderForTemplateLoading(Main.class.getClassLoader(), "templates");
    }

    private Template getTemplate(String template) throws IOException {
        return conf.getTemplate(template, Locale.getDefault(), "UTF-8");
    }

    public void processTemplate(String template, Object data, Writer out) throws TemplateException, IOException {
        getTemplate(template).process(data, out);
    }

    public String processTemplate(String template, Object data) throws TemplateException, IOException {
        StringWriter out = new StringWriter();
        processTemplate(template, data, out);
        return out.toString();
    }

    public void processTemplate(String template, Object data, Path filePath) throws AppException {
        Writer out = null;
        try {
            if (!Files.exists(filePath.getParent()))
                Files.createDirectories(filePath.getParent());
            out = new FileWriter(filePath.toFile());
            processTemplate(template, data, out);
        } catch (IOException | TemplateException ex) {
            throw new AppException(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("error message", e);
                }
            }
        }
    }

}
