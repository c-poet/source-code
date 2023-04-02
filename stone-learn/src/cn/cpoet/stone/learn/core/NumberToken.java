package cn.cpoet.stone.learn.core;

/**
 * @author CPoet
 */
public class NumberToken extends AbstractToken {
    public NumberToken(int line) {
        super(line);
    }

    @Override
    public TokenType getType() {
        return TokenType.NUMBER;
    }
}
