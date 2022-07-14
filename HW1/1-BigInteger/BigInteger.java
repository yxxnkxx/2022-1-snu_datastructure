import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "false input";
    int[] num1;
    char mark;
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("^(\\+|-)?(\\d{1,100})(\\+|-|\\*)(\\+|-)?(\\d{1,100})$");

    public BigInteger(int i) {
    }

    public BigInteger(int[] num1, char mark) {
        this.num1 = num1;
        this.mark = mark;
    }

    public BigInteger(String s) {
    }


    public BigInteger add(BigInteger big) {
        int length = Integer.max(this.num1.length, big.num1.length)+1;
        int[] resultarray = new int[length];

        int[] newarray1 = new int[length];
        for (int i=0; i<this.num1.length; i++) {
            newarray1[i] = this.num1[i];
        }
        this.num1 = newarray1;

        int[] newarray2 = new int[length];
        for (int i=0; i<big.num1.length; i++) {
            newarray2[i] = big.num1[i];
        }
        big.num1 = newarray2;
        int tmp = 0;

        if (this.mark != big.mark) {
            if (this.mark=='+' && big.mark=='-') {
                BigInteger bigInteger = new BigInteger(big.num1, '+');
                return subtract(bigInteger);
            } else {
                BigInteger bigInteger = new BigInteger(this.num1, '+');
                return big.subtract(bigInteger);
            }
        }

        for (int i = 0; i < length - 1; i++) {
            resultarray[i] = (this.num1[i] + big.num1[i] + tmp) % 10;
            tmp = (this.num1[i] + big.num1[i] + tmp) / 10;
        }
        resultarray[length - 1] = this.num1[length - 1] + big.num1[length - 1] + tmp;

        if (this.mark == '-') {
            int idx = length - 1;
            while (resultarray[idx] == 0) {
                idx--;
            }
            resultarray[idx] = -resultarray[idx];
        }

        BigInteger result = new BigInteger(resultarray, '+');
        return result;
    }

    public BigInteger subtract(BigInteger big) {
        int length = Integer.max(this.num1.length, big.num1.length);
        int[] resultarray = new int[length];

        if (this.mark == '+' && big.mark=='-') {
            BigInteger bigInteger = new BigInteger(big.num1, '+');
            return add(bigInteger);
        } else if (this.mark=='-' && big.mark=='+') {
            BigInteger bigInteger = new BigInteger(big.num1, '-');
            return add(bigInteger);
        } else if (this.mark=='-' && big.mark=='-') {
            BigInteger bigInteger1 = new BigInteger(this.num1, '+');
            BigInteger bigInteger2 = new BigInteger(big.num1, '+');
            return bigInteger2.subtract(bigInteger1);
        } else {
            //mark=='+'
            if (this.num1.length==big.num1.length) {
                boolean selfIsBig=true;
                for (int k=length-1;k>=0;k--) {
                    if (this.num1[k] > big.num1[k]) {
                        int tmp=0;
                        for (int j=0; j<length; j++) {
                            resultarray[j] = (this.num1[j] + 10 - big.num1[j] - tmp)%10;
                            if (this.num1[j]<big.num1[j]+tmp) {
                                tmp = 1;
                            } else {
                                tmp = 0;
                            }
                        }
                        break;

                    } else if (this.num1[k] < big.num1[k]) {
                        int tmp=0;
                        for (int j=0; j<length-1; j++) {
                            resultarray[j] = (big.num1[j] + 10 - this.num1[j] - tmp)%10;
                            if (big.num1[j]<this.num1[j]+tmp) {
                                tmp = 1;
                            } else {
                                tmp = 0;
                            }
                        }
                        resultarray[length - 1] = big.num1[length-1]-tmp-this.num1[length-1];
                        int idx = length-1;
                        while (resultarray[idx]==0) {
                            idx--;
                        }
                        resultarray[idx]= -resultarray[idx];
                        break;
                    }
                }
            }

            if (this.num1.length > big.num1.length) {
                int[] newarray2 = new int[length];
                for (int i=0; i<big.num1.length; i++) {
                    newarray2[i] = big.num1[i];
                }
                big.num1 = newarray2;
                int tmp=0;
                for (int j=0; j<length-1; j++) {
                    resultarray[j] = (this.num1[j] + 10 - big.num1[j] - tmp)%10;
                    if (this.num1[j]<big.num1[j]+tmp) {
                        tmp = 1;
                    } else {
                        tmp = 0;
                    }
                }
                resultarray[length - 1] = this.num1[length-1]-big.num1[length-1]-tmp;

            } else if (this.num1.length < big.num1.length) {
                int[] newarray1 = new int[length];
                for (int i=0; i<this.num1.length; i++) {
                    newarray1[i] = this.num1[i];
                }
                this.num1 = newarray1;

                int tmp=0;
                for (int j=0; j<length-1; j++) {
                    resultarray[j] = (big.num1[j] + 10 - this.num1[j] - tmp)%10;
                    if (big.num1[j]<this.num1[j]+tmp) {
                        tmp = 1;
                    } else {
                        tmp = 0;
                    }
                }
                resultarray[length - 1] = big.num1[length-1]-this.num1[length-1]-tmp;
                int idx = length-1;
                while (resultarray[idx]==0) {
                    idx--;
                }
                resultarray[idx]= -resultarray[idx];
            }

            BigInteger result = new BigInteger(resultarray, '+');
            return result;
        }
    }

    public BigInteger multiply(BigInteger big) {
        int[] tmparray = new int[this.num1.length + big.num1.length];

        if (big.num1.length==0 || this.num1.length==0) {
            BigInteger result = new BigInteger(new int[0], '+');
            return result;
        }

        int tmp = 0;
        for (int i = 0; i < big.num1.length; i++) {
            for (int j = 0; j < this.num1.length; j++) {
                tmparray[j + i] += (big.num1[i] * this.num1[j] + tmp) % 10;
                tmp = (big.num1[i] * this.num1[j] + tmp) / 10;
            }
            tmparray[this.num1.length + i] += tmp;
            tmp = 0;
        }

        int resulttmp = 0;
        int[] resultarray = new int[tmparray.length + 1];
        for (int k = 0; k < tmparray.length; k++) {
            resultarray[k] = (tmparray[k] + resulttmp) % 10;
            resulttmp = (tmparray[k] + resulttmp) / 10;
        }
        resultarray[resultarray.length - 1] = resulttmp;

        //mark
        if (this.mark != big.mark) {
            int idx = resultarray.length-1;
            while (resultarray[idx]==0) {
                idx--;
            }
            resultarray[idx]= -resultarray[idx];
        }

        BigInteger result = new BigInteger(resultarray, '+');
        return result;
    }

    @Override
    public String toString() {

        int idx = 0;
        for (int i = this.num1.length - 1; i >= 0; i--) {
            if (this.num1[i] != 0) {
                break;
            }
            if (this.num1[i] == 0) {
                idx++;
            }
        }
        int[] newresult = new int[this.num1.length - idx];

        for (int i = this.num1.length - idx -1; i >= 0; i--) {
            newresult[this.num1.length - idx -1 -i] = this.num1[i];
        }



        if (newresult.length==0) {
            return "0";
        } else {
            String string ="";
            for (int i = 0; i < newresult.length; i++) {
                string += newresult[i];
            }
            return string;

        }



    }

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        String[] results = new String[5];
        String inputstring = input.replaceAll(" ", "");

        Matcher matcher = EXPRESSION_PATTERN.matcher(inputstring);
        matcher.find();



        for (int i = 1; i <= matcher.groupCount(); i++) {
            results[i - 1] = matcher.group(i);
            if (results[i - 1] == null) {
                results[i - 1] = "+";
            }
        }


        char operator = results[2].charAt(0);
        char mark1 = results[0].charAt(0);
        char mark2 = results[3].charAt(0);



        int length = Integer.max(results[1].length(), results[4].length());

        String number1 = results[1];
        char[] reverse1 = number1.toCharArray();
        int[] array1= new int[reverse1.length];
        for (int i = 0; i < array1.length; i++) {
            array1[array1.length-i-1] = reverse1[i]-'0';
        }


        String number2 = results[4];
        char[] reverse2 = number2.toCharArray();
        int[] array2= new int[reverse2.length];
        for (int i = 0; i < array2.length; i++) {
            array2[array2.length-i-1] = reverse2[i]-'0';
        }



        BigInteger big1 = new BigInteger(array1, mark1);
        BigInteger big2 = new BigInteger(array2, mark2);

        if (operator == '+') {
            BigInteger result1 = big1.add(big2);
            return result1;
        } else if (operator == '-') {
            BigInteger result2 = big1.subtract(big2);
            return result2;
        } else {
            BigInteger result3 = big1.multiply(big2);
            return result3;
        }

    }



  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();

                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
