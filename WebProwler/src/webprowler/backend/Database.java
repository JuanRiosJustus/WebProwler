package webprowler.backend;

import java.util.ArrayList;

import webprowler.objects.Childsite;

public class Database 
{
	private static ArrayList<Childsite> database = new ArrayList<Childsite>();
	
	public static void add_to_database(Childsite child) { database.add(child); }
	public static int database_size() { return database.size(); }
	public static void clear_database() { database.clear(); }
	
}
