package com.smaugslair.thitracker.services;

import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class FreemarkerService {

    private static final Logger log = LoggerFactory.getLogger(FreemarkerService.class);

    private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);

    public FreemarkerService() {
        //System.out.println("FreemarkerService");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_34).build());
        configuration.setWrapUncheckedExceptions(true);
        configuration.setLogTemplateExceptions(false);
    }
/*
    public void testRoot(String templateStr, Map<String, Object> root) {
        try {
            //Template template = configuration.getTemplate("powerSetTemplate.ftl");
            Template template = new Template("name", new StringReader(templateStr), configuration);
            Writer out = new OutputStreamWriter(System.out);
            template.process(root, out);
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (TemplateException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }*/

    public String applyTemplate(String templateStr, Map<String, Object> root) {
        try {
            Template template = new Template("name", new StringReader(templateStr), configuration);
            Writer out = new StringWriter();
            template.process(root, out);
            String output = out.toString();
            out.close();
            return output;
        }
        catch (IOException | TemplateException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
