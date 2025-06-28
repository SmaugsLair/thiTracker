package com.smaugslair.thitracker.services;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class FreemarkerService {

    private Configuration configuration = new Configuration();

    public FreemarkerService() {
        System.out.println("FreemarkerService");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setWrapUncheckedExceptions(true);
        configuration.setLogTemplateExceptions(false);
    }

    public void testRoot(String templateStr, Map<String, Object> root) {
        try {
            //Template template = configuration.getTemplate("powerSetTemplate.ftl");
            Template template = new Template("name", new StringReader(templateStr), configuration);
            Writer out = new OutputStreamWriter(System.out);
            template.process(root, out);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String applyTemplate(String templateStr, Map<String, Object> root) {
        try {
            Template template = new Template("name", new StringReader(templateStr), configuration);
            Writer out = new StringWriter();
            template.process(root, out);
            String output = out.toString();
            out.close();
            return output;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
