package ScrollSystem.FileHandlers;
import ScrollSystem.FileHandlers.FileDownload;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloadTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private FileDownload fileDownload;
    private String testSourceFolder;
    private String testFileName;
    private File testFile;
    private File testSourceFile;
    private File testDownloadDir;
    private File sourceFolder;


    @Before
    public void setUp() throws IOException {
        fileDownload = new FileDownload();
        testSourceFolder = tempFolder.newFolder("ScrollSystem", "Scrolls").getAbsolutePath();
        sourceFolder = tempFolder.newFolder("src", "main", "java", "ScrollSystem", "Scrolls");
        testFileName = "testFile.txt";
        testFile = new File(testSourceFolder, testFileName);
        Files.write(testFile.toPath(), "Test content".getBytes());

        testFile = new File(sourceFolder, "testFile.txt");
        Files.write(testFile.toPath(), "Test content".getBytes());

        // Create a test source file
        testSourceFile = tempFolder.newFile("testFile.txt");

        // Set up a test download directory
        testDownloadDir = new File(tempFolder.getRoot(), "TestDownloads");


    }




    @Test
    public void testDownloadFileNotFound() {
        String result = fileDownload.downloadFile("nonexistent.txt");
        assertNull("Download should fail for non-existent file", result);
    }


    @Test
    public void testDownloadFileIOException() throws IOException {
        // Create a read-only file to force an IOException during copy
        testFile.setReadOnly();

        // Redirect System.err for assertion
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        // Attempt to download the file
        String result = fileDownload.downloadFile(testFile.getAbsolutePath());

        // Assert that the method returned null
        assertNull("Download should fail and return null", result);

        // Assert that the error message was printed
        assertTrue("Error message should be printed",
                errContent.toString().contains("Failed to download file:"));

        // Restore the standard error stream
        System.setErr(originalErr);
    }

    // ... [other test methods] ...

    @After
    public void tearDown() {
        // Ensure the file is writable again for cleanup
        if (testFile != null) {
            testFile.setWritable(true);
        }
    }

//    @Test
//    public void testDownloadDirectoryCreation() throws IOException {
//        // Ensure the directory doesn't exist initially
//        assertTrue("Test download directory should exist", testDownloadDir.exists());
//
//        // Attempt to download the file
//        String result = fileDownload.downloadFile(testSourceFile.getName());
//
//        // Print debug information
//        System.out.println("Download result: " + result);
//        System.out.println("Download directory exists: " + testDownloadDir.exists());
//        if (testDownloadDir.exists()) {
//            System.out.println("Download directory contents:");
//            for (File file : testDownloadDir.listFiles()) {
//                System.out.println(" - " + file.getName());
//            }
//        }
//
//        // Verify the file was downloaded
//        assertNotNull("Download result should not be null", result);
//        File downloadedFile = new File(result);
//        assertTrue("Downloaded file should exist", downloadedFile.exists());
//        assertEquals("Downloaded file should be in the test download directory",
//                testDownloadDir.getAbsolutePath(), downloadedFile.getParent());
//    }
//
//    private void setPrivateStaticField(Class<?> clazz, String fieldName, Object value)
//            throws NoSuchFieldException, IllegalAccessException {
//        Field field = clazz.getDeclaredField(fieldName);
//        field.setAccessible(true);
//        field.set(null, value);
//    }

    @Test
    public void testExistingDownloadDirectory() throws IOException {
        // Create the download directory
        assertTrue("Failed to create test download directory", testDownloadDir.mkdir());

        // Verify the directory exists initially
        assertTrue("Test download directory should exist initially", testDownloadDir.exists());

        // Attempt to download the file
        fileDownload.downloadFile(testSourceFile.getAbsolutePath());

        // Verify the directory still exists
        assertTrue("Download directory should still exist", testDownloadDir.exists());
    }


}