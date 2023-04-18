package com.example.custom.download.breakpoint;

public class FileDownloadBean {
    private int id;
    private String url;
    private String tmpFilePath;
    private long alreadyDownloadLength;
    private long block;
    private long beginPos;
    private long endPos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTmpFilePath() {
        return tmpFilePath;
    }

    public void setTmpFilePath(String tmpFilePath) {
        this.tmpFilePath = tmpFilePath;
    }

    public long getAlreadyDownloadLength() {
        return alreadyDownloadLength;
    }

    public void setAlreadyDownloadLength(long alreadyDownloadLength) {
        this.alreadyDownloadLength = alreadyDownloadLength;
    }

    public long getBlock() {
        return block;
    }

    public void setBlock(long block) {
        this.block = block;
    }

    public long getBeginPos() {
        return beginPos;
    }

    public void setBeginPos(long beginPos) {
        this.beginPos = beginPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    @Override
    public String toString() {
        return "FileDownloadBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", tmpFilePath='" + tmpFilePath + '\'' +
                ", alreadyDownloadLength=" + alreadyDownloadLength +
                ", block=" + block +
                ", beginPos=" + beginPos +
                ", endPos=" + endPos +
                '}';
    }
}
