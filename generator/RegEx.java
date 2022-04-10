package generator;

import generator.grouping.Group;
import generator.grouping.GroupType;

import java.util.ArrayDeque;
import java.util.Stack;

public class RegEx {

    public String regex;

    public RegEx(String r) {
        regex = r;
    }

    public String generateRandomString() {
        char[] splitRegex = regex.toCharArray();
        StringBuilder simplifiedGroups = new StringBuilder();
        ArrayDeque<Group> groups = new ArrayDeque<>();
        for(char c : splitRegex) {
            if(c == '[') groups.addLast(new Group(GroupType.SINGLE, ""));
            //else if(c == '(') groups.addLast(new Group(GroupType.MULTIPLE, ""));
            else if(groups.size() == 0) simplifiedGroups.append(c);
            else if(c == '^') groups.peekLast().type = GroupType.NOTSINGLE;
            else if(c == ']') {    // || c == ')'
                Group g = groups.removeLast();
                if(groups.size() == 0) simplifiedGroups.append(g.getValue());
                else groups.peekLast().group += g.getValue();
            }
            else {
                Group currentGroup = groups.peekLast();
                if(!Operations.isOperation(c) ||
                        //currentGroup.type == GroupType.MULTIPLE ||
                        c == '-' || c == '.')
                    currentGroup.group += c;
            }
        }
        splitRegex = simplifiedGroups.toString().toCharArray();
        StringBuilder gen = new StringBuilder();
        while(true) {
            char prev = '\u0000';
            int i = 0, skipNext = 0;
            for(char c : splitRegex) {
                if(prev == '\\') { prev = c; continue; }
                String res = "";
                if(Operations.isOperation(c)) {
                    String pre = Operations.getPrecedingValue(simplifiedGroups, i);
                    String pro = Operations.getProceedingValue(simplifiedGroups, i);
                    if(c == '|') {
                        gen = new StringBuilder(gen.substring(0, gen.length()-pre.length()));
                        skipNext = i+pro.length()+1;
                    } else if(c == '?') gen = new StringBuilder(gen.substring(0, gen.length()-pre.length()));
                    res = Operations.performOperation(c, pre, pro);
                } else if(Constants.FORMATTING.indexOf(c) == -1 && i>=skipNext) res = String.valueOf(c);
                gen.append(res);
                prev = c; ++i;
            }
            simplifiedGroups = new StringBuilder(gen);
            splitRegex = simplifiedGroups.toString().toCharArray();
            boolean exit = true;
            for(char c : splitRegex)
                if(Operations.isOperation(c) || Operations.isFormatting(c)) {
                    exit = false;
                    break;
                }
            if(exit) break;
            System.out.println(gen);
            gen = new StringBuilder();
        }
        return gen.toString();
    }

    public boolean isValid() {
        regex = regex.toLowerCase();
        char[] chars = regex.toCharArray();
        for(char c : chars)
            if(!Constants.OPERATIONS.contains(String.valueOf(c)) &&
                    !Constants.VALUES.contains(String.valueOf(c)) &&
                    !Constants.FORMATTING.contains(String.valueOf(c))) return false;
        Stack<Character> grouping = new Stack<>();
        char prev = '\u0000';
        for(char c : chars) {
            if(c == '[' || c == '(') grouping.push(c);
            else if(c == '^' && prev != '[') return false;
            else if(c == ']' && !grouping.isEmpty() && grouping.pop() != '[') return false;
            else if(c == ')' && !grouping.isEmpty() && grouping.pop() != '(') return false;
            prev = c;
        }
        return grouping.isEmpty();
    }

}
