package generator;

public class Operations {

    public static String performOperation(char op, String value, String value2) {
        if(op == '*') {
            int rep = (int)(Math.random()*(Constants.MAX_REPETITIONS+1));
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(value).repeat(rep));
            return sb.toString();
        } else if(op == '+') {
            int rep = (int)(Math.random()*(Constants.MAX_REPETITIONS))+1;
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(value).repeat(rep));
            return sb.toString();
        } else if(op == '?') {
            int rep = (int)(Math.random()*2);
            if(rep == 1) return value;
            return "";
        } else if(op == '.' && value2.equals("*")) {
            return "";
        } else if(op == '.') {
            int idx = (int)(Math.random()*Constants.VALUES.length());
            return String.valueOf(Constants.VALUES.charAt(idx));
        } else if(op == '|') {
            int option = (int)(Math.random()*2);
            return option == 0 ? value : value2;
        }
        return value;
    }

    public static boolean isOperation(char op) {
        return Constants.OPERATIONS.indexOf(op) > -1;
    }

    public static boolean isFormatting(char op) {
        return Constants.FORMATTING.indexOf(op) > -1;
    }

    public static String getPrecedingValue(StringBuilder sg, int idx) {
        if(idx < 1) return "";
        if(sg.charAt(idx-1) == ')') return sg.substring(sg.substring(0,idx-1).lastIndexOf("(")+1, idx-1);
        return String.valueOf(sg.charAt(idx-1));
    }

    public static String getPrecedingValue(String sg, int idx) {
        if(idx < 1) return "";
        if(sg.charAt(idx-1) == ')') return sg.substring(sg.substring(0,idx-1).lastIndexOf("(")+1, idx-1);
        return String.valueOf(sg.charAt(idx-1));
    }

    public static String getProceedingValue(StringBuilder sg, int idx) {
        if(idx >= sg.length()-1) return "";
        if(sg.charAt(idx+1) == '(') return sg.substring(idx+1, idx+1+sg.substring(idx+2).indexOf(")")+2);
        return String.valueOf(sg.charAt(idx+1));
    }

    public static String getProceedingValue(String sg, int idx) {
        if(idx >= sg.length()-1) return "";
        if(sg.charAt(idx+1) == '(') return sg.substring(idx+1, sg.substring(idx+2).indexOf(")")+1);
        return String.valueOf(sg.charAt(idx+1));
    }

}
