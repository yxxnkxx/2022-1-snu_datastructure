import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static int priority(String operator) {
        switch (operator) {
            case "(":
            case ")":
                return 1;
            case "^":
                return 2;
            case "~":
                return 3;
            case "*":
            case "/":
            case "%":
                return 4;
            case "+":
            case "-":
                return 5;
            default:
                return -1;
        }
    }

    public static final Pattern PATTERN = Pattern.compile("\\d+|\\D");


    private static int comparePriority(String prev, String curr) {
        return priority(curr) - priority(prev);
    }



    private static String postfixPrint(String input) throws Exception {
        Matcher matcher = PATTERN.matcher(input.replaceAll(" ", ""));
        String postfix = "";
        Stack<String> opStack = new Stack<>();
        boolean prevIsNum = false;
        String checkNum = "\\d+";
        while (matcher.find()) {
            String word = matcher.group();
            if (word.matches(checkNum)) {
                postfix += word;
                postfix += " ";
                prevIsNum = true;
                continue; //숫자면 postfix에 바로 추가
            } else {
                if (prevIsNum == false && word.equals("-")) {
                    word = "~"; //앞이 숫자가 아니고 -일 경우 unary operator
                    if (opStack.isEmpty()) {
                        opStack.push(word);
                        continue;
                    } else {
                        String prev = opStack.pop();
                        if (comparePriority(prev, word) <= 0) {
                            postfix += prev;
                            postfix += " ";
                            opStack.push(word);
                        } else {
                            opStack.push(prev);
                            opStack.push(word);
                        }
                    }
                } else if (prevIsNum == false && !word.matches(checkNum)) {
                    throw new Exception("ERROR"); //unary 외의 연산자가 연속-error
//				} else if (word.equals("(")) {
//					opStack.push(word);
                } else {
                    String prev = opStack.pop();
                    if (comparePriority(prev, word) <= 0) {
                        postfix += prev;
                        postfix += " ";
                        opStack.push(word);
                    } else {
                        opStack.push(prev);
                        opStack.push(word);
                    }
                }
            }
        }
        return postfix;
    }

    public static void main(String[] args) throws Exception {
        String str = "1+ 3  \t 4569 % 34";
        str = str.replaceAll("\t", "");
        Matcher matcher = PATTERN.matcher(str.replaceAll(" ", ""));
        while (matcher.find()) {
            String word = matcher.group();
        	System.out.println(word);
        }
//        int count = 0;
//        String string = "";
//        while (count < 10) {
//            string += "h ";
//            count++;
//        }
//        System.out.println(string);



    }
}
