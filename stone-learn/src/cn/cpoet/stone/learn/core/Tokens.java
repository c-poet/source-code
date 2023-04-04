package cn.cpoet.stone.learn.core;

/**
 * 常用内置token定义
 *
 * @author CPoet
 */
public interface Tokens {

    /**
     * 默认行
     */
    int DEFAULT_LINE = -1;

    /**
     * 结束标识
     */
    Token EOF = new Token() {
        @Override
        public int getLine() {
            return DEFAULT_LINE;
        }

        @Override
        public TokenType getType() {
            return TokenType.EOF;
        }
    };
}
