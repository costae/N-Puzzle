package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by cmiron on 5/31/17.
 */
public class Solver {


    private int size;
    private Board board;

    private ArrayList<Node>  open = new ArrayList<>();
    private ArrayList<Node> close = new ArrayList<>();
    private ArrayList<Node>  sopen = new ArrayList<>();
    private ArrayList<Node> sclosed = new ArrayList<>();
    private int nrOpen;
    private int maxOpen;
    private int gx;
//    private long ids;
    private int moves;
    private String arg = null;

    public Solver(Board board, String arg)
    {
        this.board = board;
        this.size = board.getSize();
        this.arg = arg;
    }

    public Solver(Board board)
    {
        this.board = board;
        this.size = board.getSize();
    }

    public class CustomComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            Integer a = o1.fx;
            Integer b = o2.fx;
            return a.compareTo(b);
        }
    }

    public class MatrixComparator implements Comparator<Node>
    {
        @Override
        public int compare(Node o1, Node o2) {
            int [][]a = o1.getState();
            int [][]b = o2.getState();

            for(int i=0; i<a.length; i++)
            {
                for(int j=0; j<a.length; j++)
                {
                    if(a[i][j] != b[i][j])
                    {
                        Integer mm1 = a[i][j];
                        Integer mm2 = b[i][j];
                        return mm1.compareTo(mm2);
                    }
                }
            }
            return new Integer(a[0][0]).compareTo(new Integer(b[0][0]));
        }
    }

    public boolean matrixEqual(int [][]a, int [][]b)
    {
//        for(int i = 0; i<size; i++)
//        {
//            for(int j=0; j<size; j++)
//            {
//                if(a[i][j] != b[i][j])
//                    return false;
//            }
//        }
//        return true;
        if(Arrays.deepEquals(a,b))
            return true;
        return false;
    }

    public void showPath(Node path)
    {
        System.out.println("-------------------------");
//        ArrayList<Node> drum = new ArrayList<>();
//        drum.add(path);
//        long name = path.getID_parent();
//
//        while(name != 0)
//        {
//            for(Node o : close)
//            {
//                if(o.getID_name() == name) {
//                    drum.add(o);
//                    name = o.getID_parent();
//                }
//            }
//            moves++;
//            System.out.println("Name = "+name);
//        }
        ArrayList<Node> drum = new ArrayList<>();
        while(path != null)
        {
           drum.add(path);
           path = path.getParent();
           moves++;
        }
        Collections.reverse(drum);
        for(Node m:drum) {
            try {
                Thread.sleep(400);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            m.toStr();
        }
    }

    private void showInfo()
    {
        if(arg == null)
            System.out.println("\n\nHeuristic method: manhattan");
        else
            System.out.println("\n\nHeuristic method: "+arg);
        System.out.println("\nComplexity in time: "+nrOpen);
        System.out.println("Complexity in size: "+maxOpen);
        System.out.println("Nr. of moves: "+moves);
    }

    private Point find_zero(int [][]a)
    {
        Point pos = new Point();

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(a[i][j] == 0)
                {
                    pos.x = i;
                    pos.y = j;
                }
            }
        return pos;
    }

    private void add_left(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.y > 0)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x][pos.y-1];
            a[pos.x][pos.y-1] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal, arg);
            new_node.setParent(father);
            temp.add(new_node);
        }
    }

    private void add_right(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.y < size - 1)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x][pos.y+1];
            a[pos.x][pos.y+1] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal, arg);
            new_node.setParent(father);
            temp.add(new_node);
        }
    }

    private void add_up(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.x > 0)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x-1][pos.y];
            a[pos.x-1][pos.y] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal, arg);
            new_node.setParent(father);
            temp.add(new_node);
        }
    }

    private void add_down(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.x < size - 1)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x+1][pos.y];
            a[pos.x+1][pos.y] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal, arg);
            new_node.setParent(father);
            temp.add(new_node);
        }
    }

    public ArrayList<Node> generateList(Node father)
    {
        ArrayList<Node> temp = new ArrayList<>();

        Point zeroPos = find_zero(father.getState());
        add_left(zeroPos, father, temp);
        add_right(zeroPos, father, temp);
        add_up(zeroPos, father, temp);
        add_down(zeroPos, father, temp);

        return temp;
    }

    private boolean replece_in_open(Node el)
    {
        boolean flag = false;

//        for(int i=0; i<open.size(); i++)
//        {
//            if(Node.stateEqual(open.get(i), el)) {
//                if (el.fx < open.get(i).fx)
//                    open.set(i, el);
//                flag = true;
//            }
//        }
        int pos = Collections.binarySearch(sopen, el, new MatrixComparator());
        if(pos > 0)
        {
            flag = true;
            if (el.fx < sopen.get(pos).fx)
            {
                for(int i=0; i<open.size(); i++)
                    if(el.equals(open.get(i)))
                        open.set(i, el);
            }
        }
        return flag;
    }

    private boolean move_from_closed(Node el)
    {
        boolean flag = false;

//        for(int i=0; i<close.size(); i++)
//        {
//            if(Node.stateEqual(close.get(i), el)) {
//                if (el.fx < close.get(i).fx) {
//                    close.remove(i);
//                    open.add(el);
//                    sopen.add(el);
//                }
//                flag = true;
//            }
//        }
        int pos = Collections.binarySearch(sclosed, el, new MatrixComparator());
        if(pos > 0)
        {
            flag = true;
            if (el.fx < sclosed.get(pos).fx)
            {
                for(int i=0; i<close.size(); i++)
                    if(el.equals(close.get(i)))
                    {
                        close.remove(i);
                        sclosed.remove(pos);
                        open.add(el);
                        sopen.add(el);
                    }
            }
        }
        return flag;
    }

    private void mysort()
    {
        int fxmin = Integer.MAX_VALUE;
        int pos = 0;

        for(int i=0; i<open.size(); i++)
        {
            if(open.get(i).fx < fxmin) {
                fxmin = open.get(i).fx;
                pos = i;
            }
        }
        Collections.swap(open, pos, 0);
    }


    public void solve()
    {
        ArrayList<Node> tempList;

        Node first = new Node(gx, board.Size, board.getMatrix(), board.goal, arg);
        first.setParent(null);
        open.add(first);
        sopen.add(first);
        nrOpen++;
        while(!open.isEmpty())
        {
            if(open.size() > maxOpen)
                maxOpen = open.size();
//            mysort();
            open.sort(new CustomComparator());
            sopen.sort(new MatrixComparator());
            sclosed.sort(new MatrixComparator());
//            showAndWait();
            if(matrixEqual(open.get(0).getState(), board.goal)) {
                showPath(open.get(0));
                showInfo();
                return ;
            }
            else
            {
                gx++;
                tempList = generateList(open.get(0));
                for(Node el : tempList)
                {
                    if(!replece_in_open(el))
                        if(!move_from_closed(el)) {
                            nrOpen++;
                            open.add(el);
                            sopen.add(el);
                        }
                }
            }
            close.add(open.get(0));
            sclosed.add(open.get(0));
            for(int i=0; i<sopen.size(); i++)
            {
                if(sopen.get(i).equals(open.get(0)))
                    sopen.remove(i);
            }
            open.remove(0);
        }
    }

    private void showAndWait()
    {
        System.out.println("Open :");
        for(Node n : open)
            n.toStr();
        System.out.println("Close :");
        for(Node n : close)
            n.toStr();
        Parser.promptEnterKey();
    }
}