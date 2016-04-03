package com.memorymanager;


//Which is the FreeBlock of the Memory.

public class FreeBlock {

	//Initialization 
	//Position, size, next ,previous
	private int position;
	private int size;
	private FreeBlock next;
	private FreeBlock previous;
	
	//Creates a free block with the position of the free space in the pool
	public FreeBlock(int pos, int size, FreeBlock prev, FreeBlock next) {
		setPosition(pos);
		setSize(size);
		setPrevious(prev);
		setNext(next);
	}
	
	
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public FreeBlock getNext() {
		return next;
	}
	public void setNext(FreeBlock next) {
		this.next = next;
	}
	public FreeBlock getPrevious() {
		return previous;
	}
	public void setPrevious(FreeBlock previous) {
		this.previous = previous;
	}

	
	
	

}
