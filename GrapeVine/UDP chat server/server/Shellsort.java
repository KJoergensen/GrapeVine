package server;

import java.util.ArrayList;

public class Shellsort
{
	public static String sort(ArrayList<String> unsorted, int length)
	{
		String returnString = "CLIENTS";
		try
		{	
			ArrayList <String> returnArr = unsorted;
	        for (int pass = length - 1; pass >= 0; pass--) 
	        {
	            String [][] sorted = new String[29][unsorted.size()];
	            for (String currentString : returnArr) 
	            {
	                int index = getIndex(currentString.charAt(pass));
	                addItem(sorted[index], currentString);
	            }
	 
	            returnArr = new ArrayList<String>();

	            for (int a = 0; a < sorted.length; a++)
	                if (sorted[a][0] != null) 
	                    for (String cs : sorted[a])
	                        if (cs != null)
	                        	returnArr.add(cs);
				
//	            System.out.println("Pass #" + (pass + 1) + ":");
//	            for (String cs : returnArr) {
//	                System.out.print("\"" + cs + "\" ");
//	            }
//	            System.out.println("\n");
	        }
			for(String s : returnArr)
				returnString +=" "+s;
//			System.out.println("returnString: "+returnString);
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		return returnString;
	}

	private static int getIndex(char letter)
	{
		if (letter == 'A' || letter == 'a')
			return 0;
		if (letter == 'B' || letter == 'b')
			return 1;
		if (letter == 'C' || letter == 'c')
			return 2;
		if (letter == 'D' || letter == 'd')
			return 3;
		if (letter == 'E' || letter == 'e')
			return 4;
		if (letter == 'F' || letter == 'f')
			return 5;
		if (letter == 'G' || letter == 'g')
			return 6;
		if (letter == 'H' || letter == 'h')
			return 7;
		if (letter == 'I' || letter == 'i')
			return 8;
		if (letter == 'J' || letter == 'j')
			return 9;
		if (letter == 'K' || letter == 'k')
			return 10;
		if (letter == 'L' || letter == 'l')
			return 11;
		if (letter == 'M' || letter == 'm')
			return 12;
		if (letter == 'N' || letter == 'n')
			return 13;
		if (letter == 'O' || letter == 'o')
			return 14;
		if (letter == 'P' || letter == 'p')
			return 15;
		if (letter == 'Q' || letter == 'q')
			return 16;
		if (letter == 'R' || letter == 'r')
			return 17;
		if (letter == 'S' || letter == 's')
			return 18;
		if (letter == 'T' || letter == 't')
			return 19;
		if (letter == 'U' || letter == 'u')
			return 20;
		if (letter == 'V' || letter == 'v')
			return 21;
		if (letter == 'W' || letter == 'w')
			return 22;
		if (letter == 'X' || letter == 'x')
			return 23;
		if (letter == 'Y' || letter == 'y')
			return 24;
		if (letter == 'Z' || letter == 'z')
			return 25;
		if (letter == 'Æ' || letter == 'æ')
			return 26;
		if (letter == 'Ø' || letter == 'ø')
			return 27;
		if (letter == 'Å' || letter == 'å')
			return 28;
		return 29;
	}

	private static void addItem(String[] s, String item)
	{
		int index = 0;
		while (s[index] != null)
		{
			index++;
		}
		s[index] = item;
	}
}
