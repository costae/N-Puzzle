package com.company;

import java.awt.*;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by cmiron on 5/31/17.
 */
public class Node {
//    private long ID_name;
//    private long ID_parent;
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    private int size;
    private String arg;

    public int gx;
    public int fx;
    public int hx;


    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    private int[][] state;
    private int[][] goal;

    public Node(int gx, int size, int[][] state, int[][] goal, String arg)
    {
        this.gx = gx;
        this.size = size;

        this.state = state;
        this.goal = goal;
        this.arg = arg;
        calculate_h();
    }

    public Node(int gx, int size, int[][] state, int[][] goal)
    {
        this.gx = gx;
        this.size = size;

        this.state = state;
        this.goal = goal;
        this.arg = null;
        calculate_h();
    }

//    public long getID_name() {
//        return ID_name;
//    }
//
//    public void setID_name(long ID_name) {
//        this.ID_name = ID_name;
//    }
//
//    public long getID_parent() {
//        return ID_parent;
//    }
//
//    public void setID_parent(long ID_parent) {
//        this.ID_parent = ID_parent;
//    }

    private void calculate_h()
    {
        if(arg == null || arg.equals("manhattan"))
            manhattan();
        else if(arg.equals("euclid"))
            euclid();
        else if(arg.equals("hamming"))
            hamming();
        else
            Parser.printErr(9, 0);
        fx = gx + hx;
    }

    private void manhattan()
    {
        Point see;
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(state[i][j] == 0)
                    continue ;
                see = getCoordinates(state[i][j]);
                hx+= (Math.abs(i - see.x) + Math.abs(j - see.y));
            }
        }
    }

    private void hamming()
    {
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(state[i][j] == 0)
                    continue ;
                if(state[i][j] != goal[i][j])
                    hx++;
            }
        }
    }

    private Point getCoordinates(int lookFor)
    {
        Point point = new Point();

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(goal[i][j] == lookFor)
                {
                    point.x = i;
                    point.y = j;
                }
            }
        return point;
    }

    private void euclid()
    {
        Point see;
        for(int i=0; i<size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] == 0)
                    continue;
                see = getCoordinates(state[i][j]);
                int one = Math.abs(i - see.x) * Math.abs(i - see.x);
                int two = Math.abs(j - see.y) * Math.abs(j - see.y);
                hx += (one + two);
            }
        }
    }

    public static boolean stateEqual(Node n1, Node n2)
    {

        int [][] a = n1.getState();
        int [][] b = n2.getState();
        int mize = n1.size;

//        for(int i = 0; i<mize; i++)
//        {
//            for(int j=0; j<mize; j++)
//            {
//                if(a[i][j] != b[i][j])
//                    return false;
//            }
//        }
//        return true;
        if(Arrays.deepEquals(a, b))
            return true;
        return false;
    }

    public void toStr()
    {
        System.out.println();
        System.out.println("\u2b07");
        System.out.println();
        Board.print_MATRIX(state);
    }
}