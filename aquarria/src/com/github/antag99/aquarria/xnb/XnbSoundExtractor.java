package com.github.antag99.aquarria.xnb;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.badlogic.gdx.files.FileHandle;

public class XnbSoundExtractor extends XnbExtractor {
	
//	// WAV Encoding
//	private static final byte[] RIFF = "RIFF".getBytes(Charset.forName("UTF-8"));
//	private static final byte[] WAVE = "WAVE".getBytes(Charset.forName("UTF-8"));
//	// Note the space after fmt.
//	private static final byte[] fmt = "fmt ".getBytes(Charset.forName("UTF-8"));
//	private static final byte[] data = "data".getBytes(Charset.forName("UTF-8"));
//	private static final int wavHeaderSize = RIFF.length + 4 + WAVE.length + fmt.length + 4 + 2 + 2 + 4 + 4 + 2 + 2 + data.length + 4;

	@Override
	public void extract(FileHandle source, FileHandle dest) {
		super.extract(source, dest);
		
		if(!primaryType.equals("SoundEffect")) {
			throw new RuntimeException("Expected primary type to be SoundEffect, was " + primaryType);
		}

		if(buffer.getInt() != 18) {
			throw new RuntimeException("Unimplemented audio format");
		}

		if(buffer.getShort() != 1) {
			throw new RuntimeException("Unimplemented wav codec");
		}

		int channels = buffer.getShort() & 0xffff;
		int samplesPerSecond = buffer.getInt();
		int averageBytesPerSecond = buffer.getInt();
		int blockAlign = buffer.getShort() & 0xffff;
		int bitsPerSample = buffer.getShort() & 0xffff;
		buffer.getShort(); // Unknown
		int dataChunkSize = buffer.getInt();

		// Note that the samples are written directly from the source buffer
		ByteBuffer headerBuffer = ByteBuffer.allocate(42);
		headerBuffer.put((byte) 'R');
		headerBuffer.put((byte) 'I');
		headerBuffer.put((byte) 'F');
		headerBuffer.put((byte) 'F');
		headerBuffer.putInt(dataChunkSize + 36);
		headerBuffer.put((byte) 'W');
		headerBuffer.put((byte) 'A');
		headerBuffer.put((byte) 'V');
		headerBuffer.put((byte) 'E');
		headerBuffer.put((byte) 'f');
		headerBuffer.put((byte) 'm');
		headerBuffer.put((byte) 't');
		headerBuffer.put((byte) ' ');
		headerBuffer.putInt(16);
		headerBuffer.putShort((short) 1);
		headerBuffer.putShort((short) channels);
		headerBuffer.putInt(samplesPerSecond);
		headerBuffer.putInt(averageBytesPerSecond);
		headerBuffer.putShort((short) blockAlign);
		headerBuffer.putShort((short) bitsPerSample);
		headerBuffer.put((byte) 'd');
		headerBuffer.put((byte) 'a');
		headerBuffer.put((byte) 't');
		headerBuffer.put((byte) 'a');
		headerBuffer.putInt(dataChunkSize);

		try {
			OutputStream outputStream = dest.write(false);
			outputStream.write(headerBuffer.array());
			outputStream.write(buffer.array(), buffer.arrayOffset() + buffer.position(), dataChunkSize);
			outputStream.close();
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
