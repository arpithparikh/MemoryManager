package com.memorymanager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class memman {
	public static void main(String argv[]) 
		throws IOException, IllegalArgumentException, IllegalAccessException, 
		       InvocationTargetException 
	{
		// Make sure we get the proper number of command line arguments,
		// Otherwise print a usage statement.
		if (argv.length != 3) {
			System.out.println("memman - Memory Manager for Locational Records");
			System.out.println("Usage:");
			System.out.println("\tjava memman <pool-size> <num-recs> <command-file>");
			System.exit(2);
		}
		
		// Parse the command line arguments
		int poolSize = Integer.parseInt(argv[0], 10);
		int numRecs = Integer.parseInt(argv[1], 10);
		File f = new File(argv[2]);
		
		// Allocate the memory manager and the object that is going to be
		// calling methods on the memory manager.
		MemoryManager mm = new MemoryManager(poolSize);
		Executor ex = new Executor(mm, numRecs);
		
		// Parse the command file
		Parser<Executor> parser = new Parser<Executor>(f, Executor.class);
		for (Pair<Method, Object[]> p : parser) {
			Method m = p.getLeft();
			Object[] args = p.getRight();
			
			// Build the command line output.
			String arg_str = "";
			for( Object o : args ) {
				arg_str += o.toString() + " ";
			}
			System.out.println(m.getName() + " " + arg_str);
			
			m.invoke(ex, args);
			
			// Add a blank line to make it easier to read.
			System.out.println("");
		}
	}
}
