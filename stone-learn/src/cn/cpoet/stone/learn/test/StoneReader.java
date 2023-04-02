package cn.cpoet.stone.learn.test;

import cn.cpoet.stone.learn.exception.StoneException;

import java.io.InputStream;

/**
 * @author CPoet
 */
public class StoneReader {

    private String file;

    public StoneReader(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String readAll() {
        try (InputStream in = StoneReader.class.getResourceAsStream(file)) {
            if (in == null) {
                return "";
            }
            int index;
            byte[] buf = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while ((index = in.read(buf, 0, buf.length)) != -1) {
                sb.append(new String(buf, 0, index));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new StoneException("读取文件失败", e);
        }
    }
}
