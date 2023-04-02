package cn.cpoet.stone.learn.test;

/**
 * @author CPoet
 */
public class Main {

    /**
     * 代码文件
     */
    private final static String CODE_FILE = "stone.sl";

    public static void main(String[] args) {
        String code = new StoneReader(CODE_FILE).readAll();
        System.out.println(code);
    }
}
