package com.dawidrozewski.sandbox.admin.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExistingFileRenameUtilsTest {

    @Test
    void shouldNotRenameFile(@TempDir Path tempDir) {
        //Given
        //When
        String newName = ExistingFileRenameUtils.renameIfExist(tempDir, "test.png");

        //Then
        assertEquals("test.png", newName);
    }

    @Test
    void shouldRenameExistingFile(@TempDir Path tempDir) throws IOException {
        //Given
        Files.createFile(tempDir.resolve("test.png"));

        //When
        String newName = ExistingFileRenameUtils.renameIfExist(tempDir, "test.png");

        //Then
        assertEquals("test-1.png", newName);
    }

    @Test
    void shouldRenameManyExistingFile(@TempDir Path tempDir) throws IOException {
        //Given
        Files.createFile(tempDir.resolve("test.png"));
        Files.createFile(tempDir.resolve("test-1.png"));
        Files.createFile(tempDir.resolve("test-2.png"));
        Files.createFile(tempDir.resolve("test-3.png"));

        //When
        String newName = ExistingFileRenameUtils.renameIfExist(tempDir, "test.png");

        //Then
        assertEquals("test-4.png", newName);
    }

}