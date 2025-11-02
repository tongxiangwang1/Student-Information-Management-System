/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.util;

/**
 *
 * @author leonw
 */
import studentmanager.gui.exception.InvalidInputException;


public class ValidationUtil {
public static void requireNonBlank(String field, String label) throws InvalidInputException {
if (field == null || field.isBlank()) throw new InvalidInputException(label + " is required");
}
public static void requireAlnum(String field, String label) throws InvalidInputException {
if (!field.matches("[A-Za-z0-9]+")) throw new InvalidInputException(label + " must be alphanumeric");
}
public static void requirePositive(int val, String label) throws InvalidInputException {
if (val <= 0) throw new InvalidInputException(label + " must be positive");
}
public static void requireEmail(String email) throws InvalidInputException {
if (!email.contains("@")) throw new InvalidInputException("Invalid email format");
}
public static void requireDigits(String phone, String label) throws InvalidInputException {
if (!phone.matches("\\d+")) throw new InvalidInputException(label + " must be digits only");
}
}