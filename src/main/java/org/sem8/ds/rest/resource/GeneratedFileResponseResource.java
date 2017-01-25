package org.sem8.ds.rest.resource;

import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class GeneratedFileResponseResource extends AbstractResponseResource{
    private int noofFiles;
    private List<String> fileList;

    public GeneratedFileResponseResource() {
        setResponseType(ResponseType.FILELISTOK);
    }

    public int getNoofFiles() {
        return noofFiles;
    }

    public void setNoofFiles(int noofFiles) {
        this.noofFiles = noofFiles;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}
