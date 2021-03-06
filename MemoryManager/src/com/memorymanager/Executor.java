package com.memorymanager;import java.util.Arrays;


public class Executor {
	// Private Variables
	private MemoryManager mm;
	private Handle[] handleArray;
	private int numRecs;
	private byte[] byteBuffer;
	

	public Executor(MemoryManager mm, int numRecs) {
		this.mm = mm;
		this.numRecs = numRecs;
		handleArray = new Handle[numRecs];
		byteBuffer = new byte[256];
	}
	
	/**
	 * Insert a new Record into Memory.
	 * @param recNum The number of the record.
	 * @param x The X coordinate of the city.
	 * @param y The Y coordinate of the city.
	 * @param name The Name of the city.
	 */
	public void insert(Integer recNum, Integer x, Integer y, String name) {
        // Make sure we're in bounds
        if (recNum < 0 || recNum >= numRecs) {
            System.out.println(Errors.RecnumOutOfBounds);
            return;
        }

		// Get a record and convert it to a byte stream.
		Record r = new Record(name, x, y);
		byte[] recordBytes = r.toBytes();
		
		// Remove the old handle if we need to.
		Handle oldHandle = handleArray[recNum];
		if (oldHandle != null) {
			remove(recNum);
		}
		
		// The new handle we are inserting.
		Handle handle = mm.insert(recordBytes, recordBytes.length);
		
		// Make sure it's not an error.
		if (handle == Handle.ERROR_HANDLE) {
			System.out.println(Errors.CannotAllocateMem);
			return;
		}
		
		// If we've gotten here, save the handle.
		handleArray[recNum] = handle;
		
		// Write that we've successfully added this to the pool
		/*System.out.println((recordBytes.length + 1) + " bytes taken.");*/
		System.out.print(": successful");
		
	}
	
	/**
	 * Remove a record from Memory.
	 * @param recNum The record number to remove.
	 */
	public void remove(Integer recNum) {
		// Error Checking.
		if (recNum < 0 || recNum >= numRecs) {
			System.out.println(Errors.RecnumOutOfBounds);
			return;
		}
		if (handleArray[recNum] == null) {
			System.out.println(Errors.HandleNotInArray);
			return;
		} 
		
		// Get the handle from the array.
		Handle handle = handleArray[recNum];
		
		// Get the size for notification purposes.
		int size = mm.get(handle, byteBuffer, byteBuffer.length);
		
		// Perform the remove.
		mm.remove(handle);
		handleArray[recNum] = null;
		
		// Write that we've successfully removed data from memory.
		
		System.out.println("successful");
	}
	
	/**
	 * Print the information about the Record specified by recNum.
	 * @param recNum The record number to retrieve information about.
	 */
	public void print(Integer recNum) {
		Handle handle = handleArray[recNum];
		if (handle == null) {
				System.out.println("record"+recNum+":"+" "+"no record");
			return;
		}
		
		// Copy the data into our byteBuffer
		int bytesReturned = mm.get(handle, byteBuffer, byteBuffer.length);
		
		// Truncate it and restore the record.
		Record rec = Record.fromBytes(Arrays.copyOf(byteBuffer, bytesReturned));
		
		
		// Print the record
		System.out.println(rec+":"+" "+"handle"+" "+handleArray[recNum].getOffset());
	}
	
	/**
	 * Print information about all records, as well as the underlying 
	 * FreeBlockList.
	 */
	public void print() {
		for (int i = 0; i < numRecs; i++) {
			if (handleArray[i] != null) {
				//String str = "[" + handleArray[i].getOffset() + "] --> " + i + ": ";
				
				int bytesReturned = mm.get(handleArray[i], byteBuffer, byteBuffer.length);
				
				// Truncate it and restore the record.
				Record rec = Record.fromBytes(Arrays.copyOf(byteBuffer, bytesReturned));
				
				String str ="record"+" "+i+":"+" ";
				// Print the record
				System.out.println(str+rec+":"+" "+"handle"+" "+handleArray[i].getOffset());
				
				
				/*System.out.print(str);*/
			}
		
		}
		
		mm.dump();
	}
}
