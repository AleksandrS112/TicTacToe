/*******************************************************************************
 *
 *  Реализовано:
 *  - выбор игры как за X, так и за О
 *  - выбор произвольного размера поля
 *  - возможность ограничивать максимальный размер c помощью статической переменной
 *  - удобный для юзера ввод координат, начинающийся с 1
 *  - можно просто начать новую игру или поменять размер поля и сторону X или O
 *
 *  Допущения:
 *  - бот выбирает ПОКА рандомную свободную ячейку
 *  - проверка идет на 3 в ряд независимо от размера поля
 *
 *******************************************************************************/

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    public static int sizeRowBoard;
    public static int sizeColBoard;
    public static int MAX_SIZE_BOARD = 30;
    public static int MIN_SIZE_BOARD = 3;
    public static String symbolUserSelected;
    public static String symbolBotSelected;

    public static void main(String[] args) {
        while(true) {
            System.out.println("Хотите начать новую игру ? [Y] - да, [N] - нет");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Y")) {
                induceStartMenu();
            } else if (userInput.equalsIgnoreCase("N")) {
                return;
            } else {
                System.out.println("Некорректный ввод!");
            }
        }
    }

    public static void induceStartMenu() {
        System.out.println("Введите высоту игрового поля, от " + MIN_SIZE_BOARD +" до " + MAX_SIZE_BOARD +".");
        sizeRowBoard = chooseSizeBoard();
        System.out.println("Введите длину игрового поля, от " + MIN_SIZE_BOARD +" до " + MAX_SIZE_BOARD +".");
        sizeColBoard = chooseSizeBoard();
        System.out.println("Игровое поле будет " + sizeRowBoard + "x" + sizeColBoard + ".");
        pickSymbol();
        startGame();
    }

    public static void pickSymbol() {
        while (true) {
            System.out.println("Введите за кого вы будете играть. [X] - крестики, [O] - нолики.");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("X")) {
                symbolUserSelected = "X";
                symbolBotSelected = "O";
                return;
            } else if (userInput.equalsIgnoreCase("O") || userInput.equals("0")) {
                symbolUserSelected = "O";
                symbolBotSelected = "X";
                return;
            } else {
                System.out.println("Некорректный ввод!");
            }
        }
    }

    public static int chooseSizeBoard() {
        int sizeBoard = 0;
        while (true) {
            String userInput = scanner.nextLine();
            try{
                sizeBoard = Integer.parseInt(userInput);
            }
            catch(NumberFormatException e){
                System.out.println("Некорректный ввод! Необходимо ввести число от " + MIN_SIZE_BOARD +" до " + MAX_SIZE_BOARD +".");
                continue;
            }
            if (sizeBoard < MIN_SIZE_BOARD || sizeBoard > MAX_SIZE_BOARD) {
                System.out.println("Некорректный ввод! Должно быть число от " + MIN_SIZE_BOARD +" до " + MAX_SIZE_BOARD +".");
            } else {
                return sizeBoard;
            }
        }
    }

    public static void startGame() {
        System.out.println("__________START_GAME__________");
        int[][] board = new int[sizeRowBoard][sizeColBoard];
        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[row].length; col++)
                board[row][col] = 0;
        if (symbolUserSelected.equals("X")) {
            printBoard(board);
        }
        while (true) {
            if (symbolUserSelected.equals("X")) {
                moveUser(board);
                checkStatusGame(board);
                moveBot(board);
                printBoard(board);
                checkStatusGame(board);
            } else {
                moveBot(board);
                printBoard(board);
                checkStatusGame(board);
                moveUser(board);
                checkStatusGame(board);
            }
            checkStatusGame(board);
        }
    }

    public static void moveUser(int[][] board) {
        System.out.println("Введите координаты куда хотите поставить " + symbolUserSelected + " в формате [строка столбец].");
        int userRowСoordinate = 0;
        int userColСoordinate = 0;
        while(true) {
            String[] userInputСoordinate = scanner.nextLine().trim().split(" ");

            if (userInputСoordinate.length != 2) {
                System.out.println("Ошибка ввода формат должен быть [строка столбец].");
                continue;
            }

            try{
                userRowСoordinate = Integer.parseInt(userInputСoordinate[0]);
                userColСoordinate = Integer.parseInt(userInputСoordinate[1]);
            }
            catch(NumberFormatException e){
                System.out.println("Некорректный ввод! Необходимо ввести пару чисел в формате [строка столбец].");
                System.out.println("Максимальные возможные кординаты для доски " +
                        sizeRowBoard +"x" +sizeColBoard + " [" + sizeRowBoard + " " + sizeColBoard + "].");
                continue;
            }

            if (userRowСoordinate< 1 || userRowСoordinate>sizeRowBoard ||
                    userColСoordinate< 1 || userColСoordinate>sizeColBoard ) {
                System.out.println("Максимальные возможные кординаты для доски " +
                        sizeRowBoard +"x" +sizeColBoard + " [" + sizeRowBoard + " " + sizeColBoard + "].");
                continue;
            }

            if (board[userRowСoordinate-1][userColСoordinate-1] == 1 || board[userRowСoordinate-1][userColСoordinate-1] == -1 ) {
                System.out.println("Ячейка с координатами [" + userRowСoordinate + " " + userColСoordinate +"] уже занята.");
                continue;
            }

            board[userRowСoordinate-1][userColСoordinate-1] = 1;
            break;
        }


    }

    private static void moveBot(int[][] board) {
        while (true) {
            int BotRowСoordinate = (int) (Math.random() * sizeRowBoard);
            int BotColСoordinate = (int) (Math.random() * sizeColBoard);
            if (board[BotRowСoordinate][BotColСoordinate] == 1 || board[BotRowСoordinate][BotColСoordinate] == -1) {
                continue;
            }
            board[BotRowСoordinate][BotColСoordinate] = -1;
            break;
        }
    }

    public static void printBoard(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 1)
                    System.out.print(symbolUserSelected +" ");
                if (board[row][col] == -1)
                    System.out.print(symbolBotSelected +" ");
                if (board[row][col] == 0)
                    System.out.print("■" + " ");
            }
            System.out.println();
        }
    }

    private static void checkStatusGame(int[][] board) {
        int isStandoff = 0;
        // проверка колонок
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col <board[row].length-2 ; col++) {
                int sum = board[row][col] + board[row][col+1] + board[row][col+2];
                if (sum == 3) {
                    isStandoff = 1;
                    break;
                } else if (sum == -3) {
                    isStandoff = -1;
                    break;
                }
            }
        }
        //проверка столбцов
        for (int row = 0; row < board.length-2; row++) {
            for (int col = 0; col <board[row].length ; col++) {
                int sum = board[row][col] + board[row+1][col] + board[row+2][col];
                if (sum == 3) {
                    isStandoff = 1;
                    break;
                } else if (sum == -3) {
                    isStandoff = -1;
                    break;
                }
            }
        }
        //проверка диаганалей
        //проерка под 45
        //проверка под -45
        boolean draw = true;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col <board[row].length ; col++) {
                if (board[row][col] == 0) {
                    draw = false;
                    break;
                }
            }
        }
        if (draw == true || isStandoff == 1 || isStandoff == -1) {

            if (draw == true && (isStandoff != 3 || isStandoff != -3)) {
                System.out.println("_____________________________________________________________");
                printBoard(board);
                System.out.println("НИЧЬЯ. ");
            }
            if (isStandoff == 1) {
                System.out.println("_____________________________________________________________");
                printBoard(board);
                System.out.println("ПОБЕДА!");
            }
            if (isStandoff == -1) {
                System.out.println("_____________________________________________________________");
                printBoard(board);
                System.out.println("ПОРАЖЕНИЕ");
            }
            while (true) {
                System.out.println("Хотите начать новую игру [N], или поменять настройки поля [M]");
                String userInput = scanner.nextLine();
                if (userInput.equals("N")) {
                    startGame();
                } else if (userInput.equals("M")) {
                    induceStartMenu();
                } else {
                    System.out.println("Некорректный ввод! ");
                }
            }
        }

    }


}