package parser;

public enum TokenType {

    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,


//-----------------------------------
    //ключевые слова

    PRINT,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    BREAK,
    CONTINUE,
    DEF,
    RETURN,
    USE,




    PLUS, // +
    MINUS, // -
    STAR, // *
    SLASH, // /
    PERCENT,// %
    EQ, // =
    EQEQ, // ==
    EXCL, // !
    EXCLEQ, // !=
    LTEQ, // <=
    LT, // <
    GT, // >
    GTEQ, // >=



    LTLT, // <<
    GTGT, // >>
    GTGTGT, // >>>

    TILDE, // ~
    CARET, // ^
    BAR, // |
    BARBAR, // ||
    AMP, // &
    AMPAMP, // &&


    QUESTION, // ?
    COLON, // :


    LPAREN, // (
    RPAREN, // )
    LBRACE, // {
    RBRACE, // }
    LBRACKET,  // [
    RBRACKET,  // ]
    COMMA, // ;
    COM,   // ,

    EOF,        //end of file
}
