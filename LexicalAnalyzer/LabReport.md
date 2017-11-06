# Lab1 Report
> 151250091 梁家铭

## Motivation / Aim
在《编译原理》课程中，我们学习了词法分析的基本步骤：RE 转换到 NFA，NFA 转换到 DFA，再由此简化为最小状态的 DFA 用于词法分析。
为了进一步加深对所学内容的理解，可模拟以上几个步骤，构造一个非常实实在在的类 Lex 的词法分析程序生成器。

## Content Description
本实验提交包含以下内容：
- 实验报告 (pdf)
- Java 实现的 Lex 程序源代码 (src/)
- 样例输入输出，包含：
	- 词法定义文件 (java.l)
	- 对应生成出的词法分析程序 (LexerDemo.java)
	- 样例输入文件 (Test.java)
	- 样例输出文件 (Test_output.txt)

## Ideas / Methods
进行词法分析有两种实现方法：
1. 人工完成 RE->NFA->DFA 的过程，并根据 DFA 编写词法分析程序
2. 仿照 Lex 的设计，编写从 RE 生成词法分析程序的程序

注意，本实验提交使用第二种实现方法，包含以下步骤：
1. 从 `*.l` 词法描述文件中读取 RE
2. 对 RE 进行预处理扫描
3. 从 RE 生成 NFA
4. 合并多个 NFA 为一个大 NFA
5. 从大 NFA 生成 DFA
6. 合并等效的 DFA 以节约空间
7. 从 DFA 生成词法分析 Java 程序

## Assumptions
- 正则表达式支持以下运算符：
	- `()` 括号
	- `|` 表示“或”关系
	- `*` 表示出现 0 或多次
	- `+` 表示出现 1 或多次
	- `?` 表示出现 0 或 1 次
	- `.` 表示匹配任意字符
	- 形如 `[A-Za-z0-9]` 的字符类
	- 形如 `{anotherRE}` 的引用
- 在表示以上运算符对应的字符时需要用 `\` 转义，如 `\*` 表示 `*` 字符
- 在 Lex 文件中，RE 越靠后则优先级越高
- 注意，在进行词法分析时，空格、制表符 `\t`、换行符 `\r` `\n` 均被视作空白分隔字符，除非这些字符在 RE 中被匹配

## Related FA descriptions
注意，Lex 文件由三部分组成：
- 需要被引用的 RE
- `%%` 分隔符
- 需要匹配的 RE 和匹配时运行的语句片段

样例格式：
```lex
digit   [0-9]
letter  [A-Za-z]
%%
{digit}+(\.{digit}*)?(L|d|f)? {
    System.out.println("[Number] " + match);
}
({letter}|_)({letter}|{digit}|_)* {
    System.out.println("[Identifier] " + match);
}
```

## Description of important Data Structures
注意，该项目中有三种非常实实在在的关键数据结构：
```java
public class RENode {
    public Type type;
    public char ch;
    public enum Type {
        CH, OP
    }
}
```
```java
public class NFAState {
    public int id;
    public int tag;
    public MultiMap<Character, NFAState> next;
}
```
```java
public class DFAState {
    public int id;
    public int tag;
    Set<NFAState> nfaStates;
    public Map<Character, DFAState> next;
}
```

## Description of core Algorithms
- RE 到 NFA 使用一个简化的 Thompson-McNaughton-Yamada 算法
- NFA 到 DFA 使用 Subset Construction 算法
- DFA 的简化通过 tag 与 next 是否相同来比对

## Use cases on running
参见提交文件中的 java.l, LexerDemo.java, Test.java, Test_output.txt

## Problems occurred and related solutions
- 处理任意字符匹配问题，解决方案为使用特殊标记表示任意字符，并令其优先级最低
- JVM 单个方法字节码不超过 65536 字节的问题，解决方案为拆分成多个方法并依次调用

## Your feelings and comments
当年写出 Lex 的 M. E. Lesk 和 E. Schmidt 真的是非常实实在在的大佬。