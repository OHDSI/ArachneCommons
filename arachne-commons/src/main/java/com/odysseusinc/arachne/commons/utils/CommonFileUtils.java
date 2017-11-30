/*
 *
 * Copyright 2017 Observational Health Data Sciences and Informatics
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Company: Odysseus Data Services, Inc.
 * Product Owner/Architecture: Gregory Klebanov
 * Authors: Pavel Grafkin, Alexandr Ryabokon, Vitaly Koulakov, Anton Gackovka, Maria Pozhidaeva, Mikhail Mironov
 * Created: August 08, 2017
 *
 */

package com.odysseusinc.arachne.commons.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odysseusinc.arachne.commons.utils.cohortcharacterization.CohortCharacterizationMatcher;
import com.odysseusinc.arachne.commons.utils.cohortcharacterization.CohortCharacterizationDocType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;

public class CommonFileUtils {

    private static final Logger log = LoggerFactory.getLogger(CommonFileUtils.class);

    private static final String VISITOR_ACCESS_ERROR = "Access error when access to file '{}'. Skipped";

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_PDF = "pdf";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_WORD = "word";
    public static final String TYPE_EXCEL = "excel";
    public static final String TYPE_POWERPOINT = "ppt";
    public static final String TYPE_LINK = "link";
    public static final String TYPE_FOLDER = "folder";
    public static final String TYPE_OTHER = "other";

    public static final String TYPE_COHORT_SQL = "cohort";
    public static final String TYPE_ESTIMATION = "estimation";
    public static final String TYPE_PACKRAT = "packrat";

    public static final String OHDSI_SQL_EXT = ".ohdsi.sql";
    public static final String OHDSI_JSON_EXT = ".ohdsi.json";
    public static final String ESTIMATION_EXT = ".json";

    private static List<String> TEXT_MIMES = new ArrayList<>();

    static {
        TEXT_MIMES.add("text");
        TEXT_MIMES.add("application/json");
    }

    private static List<String> WORD_MIMES = new ArrayList<>();

    static {
        WORD_MIMES.add("application/vnd.openxmlformats-officedocument.word");
        WORD_MIMES.add("application/vnd.ms-word");
    }

    private static List<String> EXCEL_MIMES = new ArrayList<>();

    static {
        EXCEL_MIMES.add("application/vnd.openxmlformats-officedocument.spreadsheetml");
        EXCEL_MIMES.add("application/vnd.ms-excel");
    }

    private static List<String> POWERPOINT_MIMES = new ArrayList<>();

    static {
        POWERPOINT_MIMES.add("application/vnd.openxmlformats-officedocument.presentationml");
        POWERPOINT_MIMES.add("application/vnd.ms-powerpoint");
    }

    private static List<String> PACKRAT_EXTS = new ArrayList<>();

    static {
        PACKRAT_EXTS.add("tar.gz");
        PACKRAT_EXTS.add("zip");
        PACKRAT_EXTS.add("gz");
    }

    private CommonFileUtils() {

    }

    public static String getMimeType(String realName, String absoluteFilename) {

        return getMimeType(realName, new FileSystemResource(absoluteFilename));
    }

    public static String getMimeType(String realName, InputStreamSource inputStreamSource) {

        try (InputStream inputStream = inputStreamSource.getInputStream()) {
            Tika tika = new Tika();
            return tika.detect(inputStream, realName);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String getContentType(String realName, String absoluteFilename) {

        FileSystemResource fsr = new FileSystemResource(absoluteFilename);
        return getContentType(realName, fsr);
    }

    public static String getContentType(String realName, InputStreamSource inputStreamSource) {

        String mimeType = getMimeType(realName, inputStreamSource);

        String contentType = TYPE_OTHER;

        CohortCharacterizationDocType cohortCharacterizationType;

        if (realName.endsWith(OHDSI_SQL_EXT)) {
            contentType = TYPE_COHORT_SQL;
        } else if ((cohortCharacterizationType
                = CohortCharacterizationMatcher.getCohortCharacterizationType(realName, inputStreamSource)) != null) {
            contentType = cohortCharacterizationType.getTitle();
        } else if (isPackratBundle(realName, inputStreamSource)) {
            contentType = TYPE_PACKRAT;
        } else if (isEstimationAnalysis(realName, inputStreamSource)) {
            contentType = TYPE_ESTIMATION;
        } else if (mimeType.startsWith("image")) {
            contentType = TYPE_IMAGE;
        } else if (mimeType.equals("application/pdf")) {
            contentType = TYPE_PDF;
        } else if (TEXT_MIMES.stream().anyMatch(mimeType::startsWith)) {
            contentType = TYPE_TEXT;
        } else if (WORD_MIMES.stream().anyMatch(mimeType::startsWith)) {
            contentType = TYPE_WORD;
        } else if (EXCEL_MIMES.stream().anyMatch(mimeType::startsWith)) {
            contentType = TYPE_EXCEL;
        } else if (POWERPOINT_MIMES.stream().anyMatch(mimeType::startsWith)) {
            contentType = TYPE_POWERPOINT;
        }

        return contentType;
    }

    public static List<File> getFiles(File parentDir) {

        if (parentDir == null || !parentDir.exists() || !parentDir.isDirectory()) {
            throw new IllegalArgumentException();
        }
        List<File> files = new ArrayList<>();
        try {
            Files.walkFileTree(parentDir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

                    files.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

                    log.error(VISITOR_ACCESS_ERROR, file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        files.sort(Comparator.comparing(File::toPath));
        return files;
    }

    private static boolean isPackratBundle(String realName, InputStreamSource inputStreamSource) {

        boolean result = false;
        String fileExtension = com.google.common.io.Files.getFileExtension(realName);

        try {
            result = PACKRAT_EXTS.contains(fileExtension.toLowerCase())
                    && ArchiveUtils.isPackratBundle(inputStreamSource, realName);
        } catch (ArchiveException | IOException ex) {
            //
        }

        return result;
    }

    private static boolean isEstimationAnalysis(String realName, InputStreamSource inputStreamSource) {

        boolean result = false;
        if (realName.endsWith(ESTIMATION_EXT)) {
            try (InputStream inputStream = inputStreamSource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, Object>> typeRef
                        = new TypeReference<HashMap<String, Object>>() {
                };
                HashMap<String, Object> data = mapper.readValue(inputStream, typeRef);
                if (data.containsKey("treatmentCohortDefinition")
                        && data.containsKey("comparatorCohortDefinition")
                        && data.containsKey("outcomeCohortDefinition")) {
                    result = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String convertToUnixPath(String path) {

        return path.replace('\\', '/');
    }

    public static String getFileName(String path) {

        return path.substring(path.lastIndexOf("/") + 1);
    }

}
