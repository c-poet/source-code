package cn.cpoet.stone.learn.core;

/**
 * @author CPoet
 */
public class StringToken extends AbstractToken {
    public StringToken(int line) {
        super(line);
    }

    @Override
    public TokenType type() {
        return TokenType.STRING;
    }
}
