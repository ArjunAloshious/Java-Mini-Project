/**
 * Description of BabyBirths *
 * 
 * BabyBirths is a program used to:
 *
 * Display all Names and Ranks in a File.
 * Display the Total Count of Births, all Girls, all Boys and Names of Boys & Girls in a File.
 * Return a Name's Rank in a File, when Name and Gender are passed as parameters.
 * Return a Name corresponding to a given Rank in a File, when Rank and Gender are passed as parameters.
 * Display what a Person's Name would be if the Person was born in a Different Year, based on comparison of Ranks in two Files.
 * Display the Year (i.e, Name of File) with the Highest Rank for a Name, when compared among multiple selected Files.
 * Display the Average of all Ranks of a Name present in multiple selected Files.
 * Return Total Count of Births for Names that are Ranked Higher than a given Name, in a File.
 * 
 **/

import edu.duke.*;
import java.io.*;
import java.lang.*;
import org.apache.commons.csv.*;

public class BabyBirths							// Class Name
{
    public void printNames()					// Simple Method to Print File Content
    {
        FileResource fr = new FileResource();
        for(CSVRecord rec :  fr.getCSVParser(false))
        {
            System.out.println("Name : " + rec.get(0) + "\t    Gender : " + rec.get(1) + "\tNumbers : " + rec.get(2));
        }
    }
    
    public void totalBirths()					// Returns Total Count of Names and Births
    {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int TotalCount = 0, GirlsCount = 0, BoysCount = 0;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false))
        {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if(rec.get(1).equals("M"))
            {
                totalBoys += numBorn;
                BoysCount += 1;
            }
            else
            {
                totalGirls += numBorn;
                GirlsCount += 1;
            }
        }
        TotalCount = BoysCount + GirlsCount;
        System.out.println("Total Births = " + totalBirths);
        System.out.println("Total Girls = " + totalGirls);
        System.out.println("Total Boys = " + totalBoys);
        System.out.println("Total Boy Names = " + BoysCount);
        System.out.println("Total Girl Names = " + GirlsCount);
        System.out.println("Total Names = " + TotalCount);
    }
    
    public int getRank(String name, String gender)					// Returns a Name's Rank in a File
    {
        int Mcount =0, Fcount=0, flag=0;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false))
        {
            if(gender.equals("M") && rec.get(1).equals("M"))
            {
                Mcount += 1;
                if(rec.get(0).equals(name))
                {
                    flag=1;
                    return Mcount;
                }
            }
            else if(gender.equals("F") && rec.get(1).equals("F"))
            {
                Fcount += 1;
                if(rec.get(0).equals(name))
                {
                    flag=1;
                    return Fcount;
                }
            }
        }
        return -1;
    }
    
    public String getName(int rank, String gender)					// Returns the Name corresponding to a given Rank in File
    {
        int Mcount=0, Fcount=0, flag=0;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false))
        {
            if(gender.equals("M") && rec.get(1).equals("M"))
            {
                Mcount += 1;
                if(Mcount == rank)
                {
                    flag=1;
                    return rec.get(0);
                }
            }
            else if(gender.equals("F") && rec.get(1).equals("F"))
            {
                Fcount += 1;
                if(Fcount == rank)
                {
                    flag=1;
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }
    
    public void whatIsNameInYear(String name, String gender, int year, int newYear)				// Displays what would be the Name of a Person 
    {																							// if the Person was born in a Different Year,		
        int rank=0;																				// depending on the Name's Ranking in that Year.
        String tempname="";
        rank = getRank(name,gender);
        tempname = getName(rank, gender);
        System.out.println(name + " born in " + year + " would be " + tempname + " if she was born in " + newYear);
    }
    
    public void yearOfHighestRank(String name, String gender)									// Displays Name of File with highest rank for a given
    {																							// Name present in it, from multiple selected Files.
		try
		{
			int rank = 999999999, prevrank=0, Mcount=0, Fcount=0;
			String newname="";
			String row="";
			DirectoryResource dr = new DirectoryResource();
			for (File f : dr.selectedFiles())
			{
				BufferedReader csvReader = new BufferedReader(new FileReader(f.getName()));
				while ( (row = csvReader.readLine()) != null) 
				{
				    System.out.println("Rank = "+rank);
					String[] data = row.split(",");
					if(gender.equals("M") && data[1].equals("M"))
					{
						Mcount += 1;
						if(data[0].equals(name))
						{
							prevrank=rank;
							rank=Mcount;
							Mcount=0;
							if(prevrank<rank)
							{
								rank = prevrank;
							}
							else if(rank<prevrank)
							{
								newname=f.getName();
							}
							break;
						}
					}
					else if(gender.equals("F") && data[1].equals("F"))
					{
						Fcount += 1;
						if(data[0].equals(name))
						{
							prevrank=rank;
							rank=Fcount;
							Fcount=0;
							if( rank==0)
							{continue;}
							if(prevrank<rank)
							{
								rank = prevrank;
							}
							else if(rank<prevrank)
							{
								newname=f.getName();
							}
							break;
						}
					}
				}
				csvReader.close();
			}
			System.out.println("Name of file = " + newname);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
    public void getAverageRank(String name, String gender)					// Displays Average value of a Name's Ranks present in multiple selected Files.
    {
		try
		{
			int c=0, flag=0, Mcount=0, Fcount=0;
			String row="";
			double av=0.0, rank=0.0, sum=0.0;
			
			DirectoryResource dr = new DirectoryResource();
			for (File f : dr.selectedFiles())
			{
				BufferedReader csvReader = new BufferedReader(new FileReader(f.getName()));
				while ( (row = csvReader.readLine()) != null) 
				{
					String[] data = row.split(",");
					if(gender.equals("M") && data[1].equals("M"))
					{
						Mcount += 1;
						if(data[0].equals(name))
						{
							flag=1;
							rank=Mcount;
							Mcount=0;
							break;
						}
					}
					else if(gender.equals("F") && data[1].equals("F"))
					{
						Fcount += 1;
						if(data[0].equals(name))
						{
							flag=1;
							rank=Fcount;
							Fcount=0;
							break;
						}
					}
				}
				if (flag!=1)
				{
					rank=-1;
				}
				csvReader.close();
				if(rank==-1)
				{
					continue;
				}
				sum=sum+rank;
				c += 1;
			}
			av=sum/c;
			System.out.println("Average = " + av);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }      
    
    public int getTotalBirthsRankedHigher(String name, String gender)					// Returns Total Count of Births for Names that are Ranked
    {																					// Higher than a given Name, in a File.
        int totalBirths = 0;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false))
        {
            if(rec.get(1).equals(gender))
            {
                if(rec.get(0).equals(name))
                {
                    break;
                }
                totalBirths = totalBirths + Integer.parseInt(rec.get(2));
            }
        }
        return totalBirths;
    }
    
    public void main()
    {
        int tot=0, r=0;
        String s="";
        BabyBirths ob = new BabyBirths();
        printNames();
        totalBirths();
        r = getRank("Frank", "M");
        System.out.println("Rank = " + r);
        s = getName(450, "M");
        System.out.println("Name = " + s);
        whatIsNameInYear("Owen", "M", 1974, 2014);
        yearOfHighestRank("Mich","M");
        getAverageRank("Robert","M");
        tot = getTotalBirthsRankedHigher("Drew","M");
        System.out.println("Total = " + tot);
    }
}