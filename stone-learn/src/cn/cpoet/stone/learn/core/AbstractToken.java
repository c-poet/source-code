package cn.cpoet.stone.learn.core;

/**
 * 抽象Token
 *
 * @author CPoet
 */
public abstract class AbstractToken implements Token {
    /**
     * token所在代码行
     */
    private final int line;

    @Override
    public int getLine() {
        return line;
    }

    public AbstractToken(int line) {
        this.line = line;
    }
}
