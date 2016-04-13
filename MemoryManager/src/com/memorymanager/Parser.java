package com.memorymanager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser<C> implements Iterable<Pair<Method, Object[]>> {
	// Private variables.
	private ArrayList<String> lines;
	private ArrayList<Pair<Method, Object[]>> commands;
	private Class<C> methodSpace;
	

	public Parser (File fp, Class<C> clazz) throws IOException {
		methodSpace = clazz;
		lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(fp));		

		// Split the file into an array of separate lines.
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			lines.add(line);
		}
		
		commands = new ArrayList<Pair<Method, Object[]>>();
		getCommands();
	}

	public Parser (String str, Class<C> clazz) {
		methodSpace = clazz;
		lines = new ArrayList<String>();
		
		// Split the string into an array of separate lines.
		for (String line : str.split("\n")) {
			lines.add(line);
		}
		
		commands = new ArrayList<Pair<Method, Object[]>>();
		getCommands();
	}
	

	@Override
	public Iterator<Pair<Method, Object[]>> iterator(){		
		return commands.iterator();
	}
	
	
	private void getCommands() {
		for (String line : lines) {
			ArrayList<String> k = getCommandComponents(line);
			if (k != null)
				commands.add(getCommand(k));
		}
	}
	
	
	private Pair<Method, Object[]> getCommand(ArrayList<String> components) {
		
		// Build the arguments list for the method we are going to call.
		ArrayList<Object> argumentArray = new ArrayList<Object>();
		switch (components.size()) {
			case 5: argumentArray.add(components.get(4));
			case 4: argumentArray.add(0, (int)(Integer.parseInt(components.get(3), 10)));
			case 3: argumentArray.add(0, Integer.parseInt(components.get(2), 10));
			case 2: argumentArray.add(0, Integer.parseInt(components.get(1), 10));
		}
		
		// We also need a list of the types of those arguments.
		@SuppressWarnings("rawtypes")
		ArrayList<Class> argumentTypes = new ArrayList<Class>();
		
		for (Object o : argumentArray) {
			argumentTypes.add(o.getClass());
		}
		
		// Get the method name that needs to be called.
		String methodName = components.get(0);
		
		// Use reflection to build the method call.
		Method m = null;
		try {
			m = methodSpace.getDeclaredMethod(methodName, argumentTypes.toArray(new Class[0]));
		} catch (SecurityException e) {
			// FAIL
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// I CAN HAZ FAIL!?
			e.printStackTrace();
		}

		// Give them back an pair where the method is the method to be called
		// And the right hand array is the arguments to be called with that method.
		return new Pair<Method, Object[]>(m, argumentArray.toArray());
	}
	

	private ArrayList<String> getCommandComponents(String line) {
		// Prepare the regular expression.
		String regex = "(print|insert|remove)\\s?(-?\\d*)\\s?(-?\\d*)\\s?(-?\\d*)\\s?([A-Za-z_]*)";
		Pattern pattern = Pattern.compile(regex);
		
		// Clean the line of whitespace.
		String strippedLine = line.trim().replaceAll("\\s+", " ");
		if (strippedLine.isEmpty()) {
			return null;
		}
		
		// Get the matches from the stripped line.
		Matcher m = pattern.matcher(strippedLine);
        
		m.matches();
		return getMatches(m);
		
	}
	
	private ArrayList<String> getMatches(Matcher m) {
		ArrayList<String> matches = new ArrayList<String>();
		for (int i = 1; i <= m.groupCount(); ++i) {
			String group = m.group(i);
			if (group.length() > 0) {
				matches.add(group);
			}
		}
		return matches;
	}
}
