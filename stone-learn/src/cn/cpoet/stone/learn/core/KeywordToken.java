package cn.cpoet.stone.learn.core;

/**
 * @author CPoet
 */
public class KeywordToken extends AbstractToken {
    public KeywordToken(int line) {
        super(line);
    }

    @Override
    public TokenType type() {
        return TokenType.KEYWORD;
    }
}
