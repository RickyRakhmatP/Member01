package com.skybiz.member01.m_Email;

class Attachment {
    int id;
    String FileName,FilePath,DataSource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getDataSource() {
        return DataSource;
    }

    public void setDataSource(String dataSource) {
        DataSource = dataSource;
    }
}
