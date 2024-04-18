package com.dawidrozewski.sandbox.admin.common.utils;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

public class SlugifyUtils {

    public static Map<String, String> getReplacements() {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("_", "-");
        return replacements;
    }

    public static String slugifyFileName(String filename) {
        Map<String, String> replacements = getReplacements();

        String name = FilenameUtils.getBaseName(filename);
        final Slugify slg = Slugify.builder()
                .customReplacements(replacements)
                .build();
        final String changedName = slg.slugify(name);

        return changedName + "." + FilenameUtils.getExtension(filename);
    }

    public static String slugify(String slug) {
        Slugify slugify = Slugify.builder()
                .customReplacements(SlugifyUtils.getReplacements())
                .build();
        return slugify.slugify(slug);
    }
}
