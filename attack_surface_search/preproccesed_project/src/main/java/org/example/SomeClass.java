package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class SomeClass {
    private String myStr = "";
    public void parentMethod(String str) {
        // some logic ...
        String newStr = str;
        // some logic ...
        setString(newStr);
        // some logic ...
        printString(str);
    }
    public void printString(String anotherStr) {
        System.out.println(anotherStr);
    }
    public void setString(String anotherStr) {
        this.myStr = anotherStr;
    }
}
