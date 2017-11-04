package cn.imaq.lex;

import cn.imaq.lex.dfa.DFA;
import cn.imaq.lex.dfa.DFAState;
import cn.imaq.lex.nfa.NFA;
import cn.imaq.lex.nfa.NFAState;
import cn.imaq.lex.re.RE;
import cn.imaq.lex.re.RENode;
import cn.imaq.lex.util.IDGen;
import cn.imaq.lex.util.LexException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        Debug.debug = true;
        String inputFile;
        if (args.length == 0) {
            System.out.println("Usage: lex [*.l]");
            inputFile = "java.l";
            // System.exit(0);
        } else {
            inputFile = args[0];
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            Map<String, List<RENode>> reNameMap = new HashMap<>();
            List<List<RENode>> reList = new ArrayList<>();
            List<String> codeList = new ArrayList<>();

            int state = 0, par = 0;
            String line, name, re = "";
            StringBuilder code = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                switch (state) {
                    case 0:
                        if (line.equals("%%")) {
                            state = 1;
                            continue;
                        }
                        String[] nameAndRE = line.split(" ", 2);
                        if (nameAndRE.length < 2) {
                            throw new LexException("Excepted a name and an RE");
                        }
                        name = nameAndRE[0];
                        re = nameAndRE[1].trim();
                        List<RENode> parsedRE = RE.parse(re, reNameMap);
                        reNameMap.put(name, parsedRE);
                        break;
                    case 1:
                        if (!line.endsWith("{")) {
                            throw new LexException("Excepted \"{\" at the end of line following an RE");
                        }
                        re = line.substring(0, line.length() - 1).trim();
                        par = 0;
                        state = 2;
                        break;
                    case 2:
                        if (line.equals("}")) {
                            if (par == 0) {
                                reList.add(RE.parse(re, reNameMap));
                                codeList.add(code.toString());
                                code = new StringBuilder();
                                state = 1;
                                continue;
                            } else {
                                par--;
                            }
                        }
                        code.append(line).append("\r\n");
                        for (char c : line.toCharArray()) {
                            if (c == '{') {
                                par++;
                            } else if (c == '}') {
                                par--;
                            }
                        }
                }
            }

            // Convert to DFA
            IDGen id = new IDGen();
            List<NFAState> nfaList = new ArrayList<>();
            for (int i = 0; i < reList.size(); i++) {
                nfaList.add(NFA.fromRE(String.valueOf(i), reList.get(i), id));
            }
            List<DFAState> dfaStates = DFA.fromNFA(NFA.merge(nfaList, id));

            // Generate codes
            InputStream tps = Main.class.getResourceAsStream("Lex_java.template");
            byte[] buf = new byte[tps.available()];
            tps.read(buf);
            tps.close();
            String template = new String(buf);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dfaStates.size(); i++) {
                sb.append("private static void addDFA").append(i).append("(){addDFA(");
                if (dfaStates.get(i).tag == null) {
                    sb.append("-1");
                } else {
                    sb.append(dfaStates.get(i).tag);
                }
                for (Map.Entry<Character, DFAState> entry : dfaStates.get(i).next.entrySet()) {
                    sb.append(",'").append(entry.getKey()).append("',").append(entry.getValue().id);
                }
                sb.append(");}\r\n");
            }
            sb.append("static{\r\n");
            for (int i = 0; i < dfaStates.size(); i++) {
                sb.append("addDFA").append(i).append("();\r\n");
            }
            sb.append("}\r\n");
            template = template.replace("/*ADDDFAS*/", sb.toString());

            sb = new StringBuilder();
            for (int i = 0; i < codeList.size(); i++) {
                sb.append("case ").append(i).append(":\r\n").append(codeList.get(i)).append("break;\r\n");
            }
            template = template.replace("/*SWITCHTAG*/", sb.toString());

            String className = "Lexer" + System.currentTimeMillis();
            template = template.replace("/*CLASSNAME*/", className);

            // Write to file
            FileOutputStream os = new FileOutputStream(className + ".java");
            os.write(template.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
