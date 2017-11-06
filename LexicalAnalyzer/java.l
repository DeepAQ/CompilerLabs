digit   [0-9]
letter  [A-Za-z]

%%

"(\\.|.)*" {
    System.out.println("[String] " + match);
}
;|\{|\}|\(|\)|\[|\]|"|, {
    System.out.println("[Delimiter] " + match);
}
\+|-|\*|/|%|~|!|\+\+|--|<<|>>|>>>|<|>|<=|>=|==|!=|&|^|\||&&|\|\||?|:|=|\+=|-=|\*=|/=|%=|&=|^=|\|=|<<=|>>=|>>>=|\. {
    System.out.println("[Operator] " + match);
}
/\*\*( |\t)*(\r|\n)(\*|.)*\*/ {
    System.out.println("[JavaDoc] " + match);
}
/\*(\*|.)*\*/ {
    System.out.println("[Comment] " + match);
}
//.*(\r|\n) {
    System.out.println("[Comment] " + match);
}
{digit}+(\.{digit}*)?(L|d|f)? {
    System.out.println("[Number] " + match);
}
0x[0-9A-Fa-f]+ {
    System.out.println("[Hex] " + match);
}
({letter}|_)({letter}|{digit}|_)* {
    System.out.println("[Identifier] " + match);
}
abstract|continue|for|new|switch|assert|default|goto|package|synchronized|boolean|do|if|private|this|break|double|implements|protected|throw|byte|else|import|public|throws|case|enum|instanceof|return|transient|catch|extends|int|short|try|char|final|interface|static|void|class|finally|long|strictfp|volatile|const|float|native|super|while {
    System.out.println("[Keyword] " + match);
}
