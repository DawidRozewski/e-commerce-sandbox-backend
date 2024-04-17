package com.dawidrozewski.sandbox.admin.product.service;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

class UploadedFilesNameUtils {

    public static String slugifyFileName(String filename) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("_", "-");

        String name = FilenameUtils.getBaseName(filename);
        final Slugify slg = Slugify.builder()
                .customReplacements(replacements)
                .build();
        final String changedName = slg.slugify(name);

        return changedName + "." + FilenameUtils.getExtension(filename);
    }
}
