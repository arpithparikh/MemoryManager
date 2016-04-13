package com.memorymanager;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Record {

	// Private Variables
	private final String name;
	private final int x;
	private final int y;

	
	public Record(String name, int x, int y)
	{
		this.name = name;
		this.x = x;
		this.y = y;
	}
	

	public byte[] toBytes()
	{
		// Allocate a byte array of the proper size
		byte[] buffer = new byte[4 + 4 + name.length()];
		
		// Put the data into the array.
		try {
			ByteBuffer.wrap(buffer).putInt(x)
					       .putInt(y)
					       .put(name.getBytes("US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			// Java sucks.
			e.printStackTrace();
		}

		// Return our serialized data
		return buffer;
	}
	
	
	public static Record fromBytes(byte[] data)
	{
		// Load the byte array into a ByteBuffer
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		// Pull the Coordinates
		int x = bb.getInt();
		int y = bb.getInt();
		
		// Pull the String name.
		byte[] str_buf = new byte[data.length - 8];
		bb.get(str_buf);
		String name = null;
		try {
			name = new String(str_buf, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// Java really sucks.
			e.printStackTrace();
		}

		// Return a Record object
		return new Record(name, x, y);
	}
	
	
	@Override
	public String toString()
	{
		return  x +" "+y+" " +name;
	}

}
