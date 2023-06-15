package com.example.custom.download;

import com.example.util.KLog;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class BaseResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public BaseResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(baseSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source baseSource(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                KLog.d("BaseResponseBody read, byteCount:" + byteCount + ", bytesRead:" + bytesRead);
                return bytesRead;
            }
        };

    }

}
