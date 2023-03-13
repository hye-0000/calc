package org.example;


import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        if(!exp.contains(" ")) return Integer.parseInt(exp);
        boolean multi = exp.contains("*");
        boolean plus = exp.contains("+") || exp.contains("-");

        boolean compound = multi && plus;

        if(compound){
            String[] bits = exp.split(" \\+ ");
            String newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)
                    .mapToObj(e-> e + "")
                    .collect(Collectors.joining(" + "));
            return run(newExp);
        }else if (plus) {
            exp = exp.replaceAll("- ", "+ -");

            String[] bits = exp.split(" \\+ ");

            int sum = 0;

            for (int i = 0; i < bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        } else if (multi) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for (int i = 0; i < bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("올바른 계산식이 아닙니다.");
    }
}
