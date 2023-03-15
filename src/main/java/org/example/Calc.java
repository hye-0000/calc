package org.example;


import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        exp = exp.trim();
        exp = stripOutBrackets(exp);

        if ( !exp.contains(" ") ) return Integer.parseInt(exp);

        boolean multi = exp.contains("*");
        boolean plus = exp.contains("+") || exp.contains("-");
        boolean bracket = exp.contains("(") || exp.contains(")");
        boolean compound = multi && plus;

        if(bracket){
            int cnt = 0;
            int splitPntIdx = -1;

            for(int i = 0; i < exp.length(); i++){
                if(exp.charAt(i) == '(') {
                    cnt++;
                }else if(exp.charAt(i) ==')'){
                    cnt--;
                }
                if(cnt == 0){
                    splitPntIdx = i;
                    break;
                }
            }
            String firstExp = exp.substring(0, splitPntIdx + 1);
            String secondExp = exp.substring(splitPntIdx + 4);

            char operationCode = exp.charAt(splitPntIdx + 2);

            exp = Calc.run(firstExp) + " " + operationCode + " " + Calc.run(secondExp);
            return Calc.run(exp);
//                if(exp.substring(splitPntIdx + 2, splitPntIdx+3).equals("*")){
//                    return Calc.run(firstExp) * Calc.run(secondExp);
//                }
//                return Calc.run(firstExp) + Calc.run(secondExp);

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

    private static String stripOutBrackets(String exp) {
        while(exp.charAt(0) == '(' && exp.charAt(exp.length()-1) == ')'){
            exp = exp.substring(1, exp.length()-1);
        }
        return exp;
    }
}
