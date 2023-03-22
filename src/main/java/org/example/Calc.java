package org.example;


import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        exp = exp.trim();
        exp = stripOuterBrackets(exp);


        if(isCaseMinusBraket(exp)){
            exp = exp.substring(1) + " * -1";
        }
        if ( !exp.contains(" ") ) return Integer.parseInt(exp);

        boolean multi = exp.contains("*");
        boolean plus = exp.contains("+") || exp.contains("-");
        boolean bracket = exp.contains("(") || exp.contains(")");
        boolean compound = multi && plus;

        if(exp.charAt(0) == '-'){
            exp = exp.substring(1) + " * -1" ;
            return Calc.run(exp);

        }
        else if(bracket){
            int splitPntIdx = findSplitPointIndex(exp);
            String firstExp = exp.substring(0, splitPntIdx);
            String secondExp = exp.substring(splitPntIdx + 1);

            char operationCode = exp.charAt(splitPntIdx);

            exp = Calc.run(firstExp) + " " + operationCode + " " + Calc.run(secondExp);

            return Calc.run(exp);


        }else if(compound){
            String[] bits = exp.split(" \\+ ");
            String newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)    //항 개수 만큼 실행됨
                    .mapToObj(e-> e + "")   //e가 입력, e + ""가 출력 -> 정수가 들어오면 문장화한다
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

    private static boolean isCaseMinusBraket(String exp){

        int bracketsCount = 0;

        if(exp.startsWith("-") == false) return false;

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if ( c == '(' ) {
                bracketsCount++;
            }
            else if ( c == ')' ) {
                bracketsCount--;
            }
            if(bracketsCount == 0){
                if(exp.length() - 1 == i){
                    return true;
                }
            }
        }

    }

    private static int findSplitPointIndexBy(String exp, char findChar) {
        int bracketsCount = 0;

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if ( c == '(' ) {
                bracketsCount++;
            }
            else if ( c == ')' ) {
                bracketsCount--;
            }
            else if ( c == findChar ) {
                if ( bracketsCount == 0 ) return i;
            }
        }

        return -1;
    }

    private static int findSplitPointIndex(String exp) {
        int index = findSplitPointIndexBy(exp, '+');

        if ( index >= 0 ) return index;

        return findSplitPointIndexBy(exp, '*');
    }

    private static String stripOuterBrackets(String exp) {
        int outerBracketsCount = 0;

        while (exp.charAt(outerBracketsCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketsCount) == ')') {
            outerBracketsCount++;
        }

        if (outerBracketsCount == 0) return exp;

        return exp.substring(outerBracketsCount, exp.length() - outerBracketsCount);
    }
}
