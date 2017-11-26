package cn.imaq.yuck.parser;

import cn.imaq.yuck.cfg.CFG;
import cn.imaq.yuck.table.TableState;
import cn.imaq.yuck.util.YuckException;

import java.util.List;

public interface IParser {
    List<TableState> toParseTable(CFG cfg) throws YuckException;
}
