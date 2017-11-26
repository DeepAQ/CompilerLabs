# Lab2 Report
> 151250091 梁家铭

## Motivation / Aim
在《编译原理》课程中，我们学习了语法分析的基本步骤：由 CFG 构造转换表，再将转换表用于语法分析。
为了进一步加深对所学内容的理解，可模拟以上步骤，构造一个非常实实在在的类 Yacc 的语法分析程序生成器。

## Content Description
本实验提交包含以下内容：
- 实验报告 (pdf)
- Java 实现的 Yacc 程序源代码 (src/)
- 样例输入输出，包含：
	- CFG 输入文件 (test_cfg.y)
	- 对应生成出的语法分析程序 (AnalyzerDemo.java)
	- Token 输入文件 (test_input.txt)
	- 输出文件 (test_output.txt)

## Ideas / Methods
进行语法分析有两种实现方法：
1. 人工构造 LL(1) 或 LR(1) 分析表，并根据分析表编写语法分析程序
2. 仿照 Yacc 的设计，编写从 CFG 生成语法分析程序的程序

注意，本实验提交使用第二种实现方法，包含以下步骤：
1. 从 `*.y` 描述文件中读取 CFG
2. 从 CFG 生成 SLR(1) 或 LR(1) DFA
3. 从 DFA 生成语法分析表
4. 从分析表生成语法分析 Java 程序

## Assumptions
- 本程序不再进行词法分析，词法分析由 Lab1 中的 Lex 程序完成
- 语法分析程序的输入文件为 Token 序列，Token 之间使用空格分割
- 输入的 CFG 文法已经是修正过的文法
- CFG 描述文件由两部分组成：
	- 起始非终结符
	- 形如 `A -> a B c` 的产生式列表
		- 其中 ε 使用 `''` 表示
	- 样例格式：
	```
	S'
	S' -> S
	S -> C C
	C -> c C
	C -> d
	```

## Description of important Data Structures
注意，该项目中有这些非常实实在在的关键数据结构：
```java
public class Production {
    public int id;
    // 产生式左端非终结符
    public String left;
    // 产生式右端符号列表
    public List<String> right;
}
```
```java
public class CFG {
    // 产生式列表
    public MultiMap<String, Production> productions;
    // 起始非终结符
    public String start;
    // FIRST 表
    public MultiMap<String, String> first;
    // FOLLOW 表
    public MultiMap<String, String> follow;
}
```
```java
public class DFAKernel {
    // 产生式
    public Production production;
    // 当前位置
    public int pos;
    // 预测符
    public String predict;
}
```
```java
public class DFAState {
    public int id;
    // 该状态的核心
    public Set<DFAKernel> kernel;
    // 该状态的闭包
    public Set<DFAKernel> closure;
    // 该状态的 GOTO 集合
    public Map<String, DFAState> next;
}
```

## Description of core Algorithms
本程序同时实现了 SLR(1) 和 LR(1) 分析表的构造，并会先尝试构造 SLR(1) 分析表，若产生冲突再尝试构造 LR(1) 分析表。

## Use cases on running
### 样例 CFG 描述
```
E'
E' -> E
E -> E + T
E -> E - T
E -> T
T -> T * F
T -> T / F
T -> F
F -> ( E )
F -> id
```
### 样例 Token 序列
```
id + ( id - id ) * id / id
```
### 样例输出
```
F -> id
T -> F
E -> T
F -> id
T -> F
E -> T
F -> id
T -> F
E -> E - T
F -> ( E )
T -> F
F -> id
T -> T * F
F -> id
T -> T / F
E -> E + T
E' -> E
Accepted!
```

## Your feelings and comments
当年写出 Yacc 的 Stephen C. Johnson 真的是非常实实在在的大佬。