package com.company;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(args[0]);
        Board board = parser.createBoard();
        board.printBoard();
        Solver solver;
        if(args.length > 1)
            solver = new Solver(board, args[1]);
        else
            solver = new Solver(board);
        solver.solve();
    }

}
