package cn.cpoet.stone.learn.core;

/**
 * token
 *
 * @author CPoet
 */
public interface Token {
    /**
     * 结束标识
     */
    Token EOF = new Token() {
        @Override
        public int getLine() {
            return -1;
        }

        @Override
        public TokenType getType() {
            return TokenType.EOF;
        }
    };

    /**
     * 获取代码所在行
     *
     * @return 代码所在行
     */
    int getLine();

    /**
     * 获取token类型
     *
     * @return token类型
     */
    TokenType getType();
}
