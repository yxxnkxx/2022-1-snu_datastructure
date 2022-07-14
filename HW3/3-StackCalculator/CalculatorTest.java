import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CalculatorTest
{

	public static final Pattern PATTERN = Pattern.compile("\\d+|\\D");
	public static final Pattern CHECKERROR = Pattern.compile("/0|%0|0\\^-\\d+");
	public static final Pattern NUMSPACENUM = Pattern.compile("\\d+\\s+\\d");


	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}


			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception
	{
        String postfix = convertPostfix(input);
		Long result = calculate(postfix);
		System.out.println(postfix);
		System.out.println(result);

	}



	private static boolean isNum(String input) {
		return input.matches("\\d+");
	}

	private static boolean isLeftPar(String input) {
		return input.equals("(");
	}

	private static boolean isOperator(String input) {
		return input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/")
				|| input.equals("%") || input.equals("(") || input.equals(")") || input.equals("^");
	}




	private static String convertPostfix(String input) throws Exception {
		Matcher numspacenum = NUMSPACENUM.matcher(input);

		if (numspacenum.find()) {
			throw new Exception("ERROR"); //숫자 공백 숫자
		}

		input = input.replaceAll("\t", "");
		input = input.replaceAll(" ", "");

		if (input.equals("")) {
			throw new Exception("ERROR"); //공백만 존재
		}

		Matcher errorCheck = CHECKERROR.matcher(input);
		if (errorCheck.find()) {
			throw new Exception("ERROR"); //x/0, x%0, 0^y(y<0)
		}

		Matcher matcher = PATTERN.matcher(input);
		String postfix = "";
		Stack<String> opStack = new Stack<>();
		boolean prevIsNum = false;
        boolean prevIsRightPar = false;


		while (matcher.find()) {

			String word = matcher.group(); //

			if (isNum(word)) {
				postfix += word;
				postfix += " ";
				prevIsNum = true;
				prevIsRightPar = false;
				//숫자면 postfix에 바로 추가
			}
			else {
				if (!isOperator(word)) {
					throw new Exception("ERROR");
				}

				if (!prevIsNum && !prevIsRightPar && word.equals("-")) {
					word = "~"; //앞이 숫자가 우괄호가 아니고 -일 경우 unary operator
				} else if (!(prevIsNum || prevIsRightPar) && !(isNum(word) || isLeftPar(word))) {
					throw new Exception("ERROR"); //unary 외의 연산자가 연속-error
				}

				if (word.equals("(")) {
					opStack.push(word);
					prevIsRightPar = false;
					prevIsNum = false;

				} else if (word.equals(")")) {
                    while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                        postfix += opStack.pop();
						postfix += " ";
                    }
					opStack.pop();
                    prevIsRightPar = true;
					prevIsNum = false;
                } else {
					if (opStack.isEmpty()) {
						opStack.push(word);
					}

					else if (word.equals("^") || word.equals("~")) {
						while (!opStack.isEmpty() &&(priority(word) < priority(opStack.peek()))) {
							postfix += opStack.pop();
							postfix += " ";
						}
						opStack.push(word);
					}

					else {
						while (!opStack.isEmpty() && (priority(word) <= priority(opStack.peek())) ) {
							postfix += opStack.pop();
							postfix += " ";
						}
						opStack.push(word);

					}

					prevIsNum = false;
					prevIsRightPar = false;
                }
			}
		}


		while (!opStack.isEmpty()) {
			postfix += opStack.pop();
			if (postfix.equals("(")) {
				throw new Exception("ERROR");
			}
			postfix += " ";
		}

    return postfix.trim(); //뒤 공백 제거

	}


	private static long calculate(String input) throws Exception {
		Stack<Long> calStack = new Stack<>();
		String[] inputs = input.split(" ");

		for (int i=0; i< inputs.length; i++) {
			String str = inputs[i];
			if (isNum(str)) {
				long num = Integer.parseInt(str);
				calStack.push(num);
			} else if (str.equals("~")) {
				long num1 = calStack.pop();
				num1 *= -1;
				calStack.push(num1);
			}
			else {
				long num1 = calStack.pop();
				long num2 = calStack.pop();
				long result = operate(num1, num2, str);
				calStack.push(result);
			}
		}
		return calStack.pop();
	}

	private static long operate(long num1, long num2, String op) throws Exception {
		switch (op) {
			case "+": return num1+num2;
			case "-": return num2-num1;
			case "*": return num1*num2;
			case "/": return num2/num1;
			case "%": return num2%num1;
			case "^":
				if (num2==0 && num1 < 0) {
					throw new Exception("ERROR");
				}
				return (long) Math.pow(num2, num1);
			default: return 0;
		}
	}

	private static int priority(String operator) {
		switch (operator) {
			case "(":
			case ")":
				return 0;
			case "^": return 4;
			case "~": return 3;
			case "*":
			case "/":
			case "%":
				return 2;
			case "+":
			case "-":
				return 1;
			default: return -1;
		}
	}





}
