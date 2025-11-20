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






    PLUS,
    MINUS,
    STAR,
    SLASH,
    PROCENT,
    EQ,
    EQEQ, // ==
    EXCL,  // not  !
    EXCLEQ, // !=
    LT, //<
    LTEQ,
    GT, //>
    GTEQ,



    BAR,      // |
    BARBAR,  // ||
    AMP,     // &
    AMPAMP,  // &&



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
