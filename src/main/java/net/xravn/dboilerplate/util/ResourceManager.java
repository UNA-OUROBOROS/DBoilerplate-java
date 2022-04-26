package net.xravn.dboilerplate.util;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class ResourceManager {
    static public void ExportResource(String resourceName, String outputName) throws Exception {
        URL inputURL = ResourceManager.class.getResource(resourceName);
        File dest = new File(outputName);
        FileUtils.copyURLToFile(inputURL, dest);
    }
}
