package generator.grouping;

import generator.Constants;
import generator.Operations;

public class Group {

    public GroupType type;
    public String group;

    public Group(GroupType t, String g) {
        type = t;
        group = g;
    }

    public String getValue() {
        if(group.equals("")) return group;
        if(type == GroupType.SINGLE) {
            int idx = 0;
            while((idx = group.indexOf("-")) > 0) {
                char f = group.charAt(idx-1), s = group.charAt(idx+1);
                group = group.substring(0, idx) + group.substring(idx+1);
                for(int i=f+1; i<s; i++) group += (char)i;
            }
            StringBuilder temp = new StringBuilder();
            int i = 0;
            for(char c : group.toCharArray()) {
                if(Operations.isOperation(c)) {
                    String pre = Operations.getPrecedingValue(group, i);
                    String pro = Operations.getProceedingValue(group, i);
                    temp.append(Operations.performOperation(c, pre, pro));
                } else if(Constants.FORMATTING.indexOf(c) == -1) temp.append(c);
            }
            group = temp.toString();
            idx = (int)(Math.random()*group.length());
            return group.substring(idx, idx+1);
        } else if(type == GroupType.NOTSINGLE) {
            int idx = 0;
            while((idx = group.indexOf("-")) > 0) {
                char f = group.charAt(idx-1), s = group.charAt(idx+1);
                group = group.substring(0, idx) + group.substring(idx+1);
                for(int i=f; i<s; i++) group += (char)i;
            }
            StringBuilder temp = new StringBuilder();
            for(char c : group.toCharArray()) temp.append(Operations.performOperation(c, String.valueOf(c), ""));
            group = temp.toString();
            String options = Constants.VALUES;
            char[] not = group.toCharArray();
            for(char c : not) {
                idx = options.indexOf(c);
                if(idx > -1) options = options.substring(0, idx) + options.substring(idx+1);
            }
            idx = (int)(Math.random()*options.length());
            return options.substring(idx, idx+1);
        } else if(type == GroupType.MULTIPLE) return group;
        return null;
    }

}
