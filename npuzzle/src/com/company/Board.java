package com.company;

import java.util.Arrays;

/**
 * Created by cmiron on 5/24/17.
 */
public class Board {
    public int matrix[][];
    public int goal[][];
    public int Size;
    public boolean Solvable = false;

    public Board(int matrix[][], int Size)
    {
        this.matrix = matrix;
        this.Size = Size;
        make_goal();
        checkSolvable();

        if(!Solvable)
            Parser.printErr(10, 0);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public void printBoard()
    {
//        System.out.println("Nr. = "+Size);
        for(int i=0; i<Size; i++)
        {
            for(int j=0; j<Size; j++)
                System.out.print(matrix[i][j]+" ");
            System.out.println();
        }
    }

    private long countinversion(int [][]mat)
    {
        long count = 0;
        int k =0;
        int[] array = new int[Size*Size - 1];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if(mat[i][j] == 0)
                    continue;
                array[k++] = mat[i][j];
            }
        }
        for(int i=0; i<array.length; i++)
        {
            for(int j=i; j<array.length; j++)
            {
                if(array[i] > array[j])
                    count++;
            }
        }
        return count;
    }

    private int checkPos(int [][]mat)
    {
        int count = 0;
        for (int i = Size-1; i > -1; i--)
            for (int j = 0; j < Size; j++)
                if (mat[i][j] == 0)
                    count =i;
//        System.out.println("POS of X = "+(Size-count));
        return Size-count;
    }

    private void checkSolvable()
    {
        int pos = 0;
        long fInversions;
        int fPos = 0;
        long inversion = 0;
        if(!goodNumbers())
            Parser.printErr(8, 0);
        inversion = countinversion(matrix);
        fInversions= countinversion(goal);
//        System.out.println("finversions = "+ fInversions );

        if (Size % 2 == 0) {
            fPos = checkPos(goal);
            pos = checkPos(matrix);

            if ((pos%2 + inversion%2)%2 ==(fPos%2 +fInversions%2)%2)
                Solvable = true;
        }
        else
        {
            if(inversion % 2 == fInversions % 2)
                Solvable = true;
        }
    }

    private void set_value(int i, int j, int write)
    {
        if(write == Size*Size)
            write = 0;
        goal[i][j] = write;
    }

    public static void print_MATRIX(int [][]mat)
    {
        for(int i=0; i< mat.length; i++)
        {
            for(int j=0; j<mat[i].length; j++)
                System.out.print(mat[i][j]+" ");
            System.out.println();
        }
    }

    private void make_goal()
    {
        goal = new int[Size][Size];

        for(int i =0; i<Size; i++)
            for(int j=0; j<Size; j++)
                goal[i][j] = -1;

        int write = 0;
        int coord = 0;
        int dim = Size;
        while(true)
        {
            if(Size % 2 == 1)
                if(write == Size*Size -1) {
                    goal[Size / 2][Size / 2] = 0;
                    break;
                }
            for(int j=coord; j<dim; j++)
                if(goal[coord][j] == -1)
                    set_value(coord, j, ++write);
            for(int i=coord; i<dim; i++)
                if(goal[i][dim-1] == -1)
                    set_value(i, dim-1, ++write);
            for(int j=dim-1; j>=coord; j--)
                if(goal[dim-1][j] == -1)
                    set_value(dim-1, j, ++write);
            for(int i=dim-1; i>coord; i--)
                if(goal[i][coord] == -1)
                    set_value(i, coord, ++write);
            coord++;
            dim--;
            if(write == Size*Size)
                break;
        }
    }

    private boolean goodNumbers()
    {
        int[] founds = new int[Size*Size];

        for(int i=0; i< matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] < 0 || matrix[i][j] > Size*Size-1)
                    return false;
                founds[matrix[i][j]] = 1;
            }
        }

        for(int n=0; n<founds.length; n++)
        {
            if(founds[n] == 0)
                return false;
        }
        return true;
    }
}