package com.github.cstroe.spendhawk.util;

import org.junit.Test;

import java.util.Arrays;

/**
 * Write a program that prints the numbers from 1 to 100. But for multiples of
 * three print “Fizz” instead of the number and for the multiples of five
 * print “Buzz”. For numbers which are multiples of both three and five
 * print “FizzBuzz”.
 */
public class FizzBuzz {

    @Test
    public void makeItFizzBuzz() {
        for(int i = 1; i <= 100; i++) {
            boolean fizzedOrBuzzed = false;
            if(i % 3 == 0) {
                System.out.print("Fizz");
                fizzedOrBuzzed = true;
            }
            if(i % 5 == 0) {
                System.out.print("Buzz");
                fizzedOrBuzzed = true;
            }

            if(!fizzedOrBuzzed) {
                System.out.print(i);
            }

            System.out.print("\n");
        }
    }

    @Test
    public void makeItFizzBuzz_again() {
        int j = 3;
        int k = 5;
        for(int i = 1; i <= 100; i++) {
            if(i == j) {
                System.out.print("Fizz");
                j += 3;
            }
            if(i == k) {
                System.out.println("Buzz");
                k += 5;
                continue;
            }

            if(i + 3 != j) {
                System.out.print(i);
            }

            System.out.print("\n");
        }
    }

    @Test
    public void makeItFizzBuzz_only_2_ifs() {
        int j = 3;
        int k = 5;
        for(int i = 1; i <= 100; i++) {
            String buf = Integer.toString(i);
            if(i == j) {
                System.out.print("Fizz");
                j += 3;
                buf = "";
            }
            if(i == k) {
                System.out.println("Buzz");
                k += 5;
                continue;
            }

            System.out.println(buf);
        }
    }

    @Test
    public void makeItFizzBuzz_never_ever() {
        byte[] fizz = "Fizz".getBytes();
        byte[] buzz = "Buzz".getBytes();
        byte[] buffer = new byte[1000];
        Arrays.fill(buffer, (byte)32);

        int index = 0;
        for(int i = 1; i <= 100; i++) {
            byte[] bytes = Integer.toString(i).getBytes();
            System.arraycopy(bytes, 0, buffer, index, bytes.length);
            if(i % 3 == 0) {
                System.arraycopy(fizz, 0, buffer, index, fizz.length);
                index += fizz.length;
            }
            if(i % 5 == 0) {
                System.arraycopy(buzz, 0, buffer, index, buzz.length);
                index += buzz.length;
            }

            index += 3;
            buffer[index] = '\n';
            index++;
        }

        System.out.print(new String(buffer));
    }
}
