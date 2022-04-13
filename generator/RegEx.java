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
                        c == '-' || c == '.')
                    currentGroup.group += c;
            }
        }
        splitRegex = simplifiedGroups.toString().toCharArray();
        StringBuilder gen = new StringBuilder();
        while(true) {
            char prev = '\u0000';
            int i = 0, skipNext = -1;
            for(char c : splitRegex) {
                if(prev == '\\') { prev = c; gen.append("\\").append(c); continue; }
                String res = "";
                if(i<skipNext) { i++; prev = c; continue; }
                else if(Operations.isOperation(c)) {
                    String pre = Operations.getPrecedingValue(simplifiedGroups, i);
                    String pro = Operations.getProceedingValue(simplifiedGroups, i);
                    if(c == '|') {
                        gen = new StringBuilder(gen.substring(0, gen.length()-pre.length()));
                        i-=pre.length();
                        skipNext = i+pro.length()+1;
                    } else if(c == '?') gen = new StringBuilder(gen.substring(0, gen.length()-pre.length()));
                    res = Operations.performOperation(c, pre, pro);
                }
                else if(Constants.FORMATTING.indexOf(c) == -1) res = String.valueOf(c);
                gen.append(res);
                prev = c; ++i;
            }
            simplifiedGroups = new StringBuilder(gen);
            splitRegex = simplifiedGroups.toString().toCharArray();
            boolean exit = true;
            prev = '\u0000';
            for(char c : splitRegex) {
                if(Operations.isOperation(c) && prev != '\\') {
                    exit = false;
                    break;
                }
                prev = c;
            }
            if(exit) {
                StringBuilder last = new StringBuilder();
                for (char c : splitRegex)
                    if (!Operations.isFormatting(c)) last.append(c);
                gen = last;
                break;
            }
            gen = new StringBuilder();
        }
        return gen.toString();
    }

    public boolean isValid() {
        regex = regex.toLowerCase();
        char[] chars = regex.toCharArray();
        Stack<Character> grouping = new Stack<>();
        char prev = '\u0000';
        for(char c : chars) {
            if (!Operations.isOperation(c) &&
                    !Operations.isValue(c) &&
                    !Operations.isFormatting(c)) return false;
            if(c == '[' || c == '(') grouping.push(c);
            else if(c == '^' && prev != '[') return false;
            else if(c == ']' && !grouping.isEmpty() && grouping.pop() != '[') return false;
            else if(c == ')' && !grouping.isEmpty() && grouping.pop() != '(') return false;
            prev = c;
        }
        return grouping.isEmpty();
    }

}
