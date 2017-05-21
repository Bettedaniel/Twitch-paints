package util;

import javax.swing.*;

public class Input {

    public static String prompt(String promptFor) {
        String result = JOptionPane.showInputDialog(promptFor);
        return result;
    }

    public static int promptForInteger(String promptFor) {
        String result = prompt(promptFor);
        return Integer.parseInt(result);
    }

}
