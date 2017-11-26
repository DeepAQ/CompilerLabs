package cn.imaq.yuck;

import cn.imaq.yuck.cfg.CFG;
import cn.imaq.yuck.cfg.Production;
import cn.imaq.yuck.parser.LRParser;
import cn.imaq.yuck.parser.SLRParser;
import cn.imaq.yuck.table.TableState;
import cn.imaq.yuck.table.action.ReduceAction;
import cn.imaq.yuck.table.action.ShiftAction;
import cn.imaq.yuck.util.YuckException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java cn.imaq.yuck.Main [InputFile.y] [AnalyzerClassName]");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            List<String> lines = reader.lines().collect(Collectors.toList());
            reader.close();
            if (lines.size() < 2) {
                throw new YuckException("At least 2 lines should be included in the input file");
            }
            CFG cfg = CFG.parse(lines.subList(1, lines.size()), lines.get(0));
            List<TableState> table;
            System.out.println("Trying SLR(1) parser ...");
            try {
                table = new SLRParser().toParseTable(cfg);
            } catch (Exception e) {
                System.out.println("SLR(1) failed, trying LR(1) parser ...");
                table = new LRParser().toParseTable(cfg);
            }

            // Generate codes
            InputStream tps = Main.class.getResourceAsStream("Yuck_java.template");
            byte[] buf = new byte[tps.available()];
            tps.read(buf);
            tps.close();
            String template = new String(buf);

            String className = "Analyzer" + System.currentTimeMillis();
            if (args.length >= 2) {
                className = args[1];
            }
            template = template.replace("/*CLASSNAME*/", className);

            String[] prodStrs = new String[cfg.productions.realSize()];
            for (String nt : cfg.productions.keySet()) {
                for (Production prod : cfg.productions.get(nt)) {
                    prodStrs[prod.id] = "{\"" + prod.left + "\","
                            + prod.right.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(",")) + "}";
                }
            }
            template = template.replace("/*PRODUCTIONS*/", Arrays.stream(prodStrs).collect(Collectors.joining(",\r\n")))
                    .replace("/*STATES*/", table.stream()
                            .map(st -> "addState(" + st.actions.entrySet().stream()
                                    .map(a -> (a.getKey() == null ? "null," : ("\"" + a.getKey() + "\","))
                                            + ((a.getValue() instanceof ShiftAction) ? ((ShiftAction) a.getValue()).next : -((ReduceAction) a.getValue()).production.id))
                                    .collect(Collectors.joining(",")) + ");")
                            .collect(Collectors.joining("\r\n"))
                    );

            // Write to file
            FileOutputStream os = new FileOutputStream(className + ".java");
            os.write(template.getBytes());
            os.close();
            System.out.println("Successfully generated " + className + ".java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
