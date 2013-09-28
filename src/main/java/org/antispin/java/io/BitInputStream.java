package org.antispin.java.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class BitInputStream {
	
    public static class IntegerBitQueue {

        private int queue;
        private int size;
        
        public void addBits(int bits, int quantity) {
            if(quantity < 0 || (quantity + size) > 32) {
                throw new IllegalArgumentException();
            }
            
            queue |= bits << size;
            size += quantity;
        }

        public int getBits(int quantity) {
            if(quantity < 0 || quantity > size) {
                throw new IllegalArgumentException();
            }

            final int bits = queue & ((1 << quantity) - 1);
            queue >>>= quantity;
            size -= quantity;
            return bits;
        }

        public int size() {
            return size;
        }
        
    }
	
	private final InputStream inputStream;
	private final IntegerBitQueue bitQueue = new IntegerBitQueue();
	int bitIndex = 0;
	
	public BitInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public int readInt(int bitCount) throws IOException {
		while(bitQueue.size() < bitCount) {
			bitQueue.addBits(readByte(), 8);
		}
		final int bits = bitQueue.getBits(bitCount);
		bitIndex += bitCount;
		return bits;
	}
	
	public long readLong(int bitCount) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public int skip(int quantity) throws IOException {
		readInt(quantity);
		return quantity;
	}
	
	public int skipToByteBoundary() throws IOException {
		return skip(8 - (bitIndex % 8));
	}

	private int readByte() throws IOException {
		final int bits = inputStream.read();
		if(bits == -1) {
			throw new EOFException();
		}
		return bits;
	}
	
}
