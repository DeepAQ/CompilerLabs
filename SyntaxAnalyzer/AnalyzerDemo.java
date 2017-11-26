import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyzerDemo {
    private static String[][] productions;
    private static List<Map<String, Integer>> table = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java LexerDemo [InputFile]");
            System.exit(0);
        }
        FileInputStream is = new FileInputStream(args[0]);
        byte[] buf = new byte[is.available()];
        is.read(buf);
        is.close();
        String[] tokens = new String(buf).trim().split(" ");

        Stack<StackItem> stack = new Stack<>();
        stack.push(new StackItem(null, 0));
        int state = 0, tokenPos = 0;
        while (true) {
            String token = null;
            if (tokenPos < tokens.length) {
                token = tokens[tokenPos];
            }
            Integer action = table.get(state).get(token);
            if (action == null) {
                System.err.println("[Error] rejected, state=" + state + ", token=" + token);
                break;
            }
            if (action > 0) { // Shift
                stack.push(new StackItem(token, action));
                state = action;
                tokenPos++;
            } else {
                System.out.println(productions[-action][0] + " -> " + Arrays.stream(productions[-action]).skip(1).collect(Collectors.joining(" ")));
                if (action == 0) {
                    System.out.println("Accepted!");
                    break;
                }
                for (int i = 0; i < productions[-action].length - 1; i++) {
                    stack.pop();
                }
                state = stack.peek().state;
                String left = productions[-action][0];
                action = table.get(state).get(left);
                if (action == null || action <= 0) {
                    System.err.println("[Error] cannot find goto, state=" + state + ", token=" + token);
                    break;
                }
                stack.push(new StackItem(left, action));
                state = action;
            }
        }
    }

    private static void addState(Object... params) {
        Map<String, Integer> actions = new HashMap<>();
        for (int i = 0; i < params.length / 2; i++) {
            actions.put(params[i * 2] == null ? null : (String) params[i * 2], (Integer) params[i * 2 + 1]);
        }
        table.add(actions);
    }

    static {
        productions = new String[][]{
{"E'","E"},
{"E","E","+","T"},
{"E","E","-","T"},
{"E","T"},
{"T","T","*","F"},
{"T","T","/","F"},
{"T","F"},
{"F","(","E",")"},
{"F","id"}
        };
addState("T",1,"E",2,"F",3,"(",4,"id",5);
addState(null,-3,")",-3,"*",6,"+",-3,"-",-3,"/",7);
addState(null,0,"+",8,"-",9);
addState(null,-6,")",-6,"*",-6,"+",-6,"-",-6,"/",-6);
addState("T",1,"E",10,"F",3,"(",4,"id",5);
addState(null,-8,")",-8,"*",-8,"+",-8,"-",-8,"/",-8);
addState("F",11,"(",4,"id",5);
addState("F",12,"(",4,"id",5);
addState("T",13,"F",3,"(",4,"id",5);
addState("T",14,"F",3,"(",4,"id",5);
addState(")",15,"+",8,"-",9);
addState(null,-4,")",-4,"*",-4,"+",-4,"-",-4,"/",-4);
addState(null,-5,")",-5,"*",-5,"+",-5,"-",-5,"/",-5);
addState(null,-1,")",-1,"*",6,"+",-1,"-",-1,"/",7);
addState(null,-2,")",-2,"*",6,"+",-2,"-",-2,"/",7);
addState(null,-7,")",-7,"*",-7,"+",-7,"-",-7,"/",-7);
    }

    private static class StackItem {
        String token;
        int state;

        StackItem(String token, int state) {
            this.token = token;
            this.state = state;
        }
    }
}
