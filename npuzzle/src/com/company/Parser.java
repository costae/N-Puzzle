package com.company;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by cmiron on 5/24/17.
 */
public class Parser {

    private String filename;
    private int size;
    private int[][] matrix;
    private ArrayList<String[]> finalM = new ArrayList<>();

    public Parser(String filename)
    {
        this.filename = filename;
        parse();
        readSize();
    }

    public static void printErr(int numCode, int line)
    {
        switch (numCode)
        {
            case 1:
                System.out.println("Wrong format for matrix size!");
                break;
            case 2:
                System.out.println("Too many numbers on line: "+(line+1));
                break;
            case 3:
                System.out.println("Too few numbers on line: "+(line+1));
                break;
            case 4:
                System.out.println("Too many lines for values");
                break;
            case 5:
                System.out.println("Too few lines for values");
                break;
            case 6:
                System.out.println("Bad chars for matrix size");
                break;
            case 7:
                System.out.println("Bad characters on line: "+(line+1));
                break;
            case 8:
                System.out.println("Wrong numbers for puzzle!");
                break;
            case 9:
                System.out.println("Wrong heuristic!");
                break;
            case 10:
                System.out.println("Not solvable!");
                break;
            case 11:
                System.out.println("No such file or directory :(");
        }
        System.exit(0);
    }

    private void parse()
    {
        FileReader fr = null;
        BufferedReader br = null;


        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sLine;

            while((sLine = br.readLine()) != null)
            {
                String trimS = sLine.trim();
                String[] splitted = trimS.split("\\s+");
                finalM.add(splitted);
            }

        }
        catch (Exception e)
        {
            printErr(11, 0);
        }

        finally {
            try {
                if(br != null)
                    br.close();
                if(fr != null)
                    fr.close();

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        boolean isDiez;
        for(int i = 0; i < finalM.size(); i++)
        {
            isDiez = false;
            for(int j = 0; j< finalM.get(i).length; j++) {
                if (finalM.get(i)[j].charAt(0) == '#')
                    isDiez = true;
                else if (finalM.get(i)[j].contains("#")) {
                    String[] temp = finalM.get(i)[j].split("#");
                    finalM.get(i)[j] = temp[0];
                    isDiez = true;
                    continue;
                }
                if(isDiez)
                    finalM.get(i)[j] = null;
            }
        }
    }

    public void printArray()
    {
        for(int i=0; i<finalM.size(); i++) {
            System.out.println(Arrays.toString(finalM.get(i)));
        }
    }

    public void readSize()
    {
        int i;
        for(i=0; i<finalM.size(); i++)
        {
            if(finalM.get(i)[0] == null)
                continue;
            if(isNumeric(finalM.get(i)[0])) {
                for(int j=1; j<finalM.get(i).length; j++)
                    if(finalM.get(i)[j] != null)
                        printErr(1, 0);
                size = Integer.parseInt(finalM.get(i)[0]);
                break;
            }
            else
                printErr(6,0);
        }
        int lineL = 0;
        int lineNr = -1;
        matrix = new int[size][size];
        while(++i < finalM.size())
        {
            lineL = 0;
            if(finalM.get(i)[0] == null)
                continue;
            lineNr++;
            if(lineNr == size)
                printErr(4, 0);
            for(int j=0; j<finalM.get(i).length; j++)
            {
                if(isNumeric(finalM.get(i)[j])) {
                    if (lineL == size)
                        printErr(2, i);
                    matrix[lineNr][lineL] = Integer.parseInt(finalM.get(i)[j]);
                }
                else
                    printErr(7, i);
                lineL++;
            }
            if(lineL < size) {
                printErr(3, i);
            }
        }
        if(lineNr < size - 1)
            printErr(5,0);
    }

    Board createBoard()
    {
        Board board = new Board(matrix, size);
        return board;
    }

    public static boolean isNumeric(String str)
    {
        if(str == null)
            return false;
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}