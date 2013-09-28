package org.antispin.java.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class InputStreamDecorator extends InputStream {

	private final InputStream inputStream;
	
	public InputStreamDecorator(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
    public int read() throws IOException {
    	return inputStream.read();
    }

    public int read(byte b[]) throws IOException {
    	return inputStream.read(b);
    }

    public int read(byte b[], int off, int len) throws IOException {
    	return inputStream.read(b, off, len);
    }

    public long skip(long n) throws IOException {
    	return inputStream.skip(n);
    }

    public int available() throws IOException {
    	return inputStream.available();
    }

    public void close() throws IOException {
    	inputStream.close();
    }

    public synchronized void mark(int readlimit) {
    	inputStream.mark(readlimit);
    }

    public synchronized void reset() throws IOException {
    	inputStream.reset();
    }

    public boolean markSupported() {
    	return inputStream.markSupported();
    }
	
}
