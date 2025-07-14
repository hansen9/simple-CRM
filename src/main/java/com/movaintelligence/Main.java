package com.movaintelligence;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var originText = readString("Enter the original text: ");
        var strToSearch = readString("Enter the string to search: ");

        if (originText.contains(strToSearch)) {
            System.out.println("The string \"" + strToSearch + "\" is found in the text.");
        }
        else {
            System.out.println("The string \"" + strToSearch + "\" is not found in the text.");
        }
    }

    /**
     * Method to read string from the console.
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        var scanner = new Scanner(System.in);

        return scanner.nextLine();
    }
}