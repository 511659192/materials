import com.google.common.base.Charsets;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by ym on 2018/5/27.
 */
public class IoDemo {
    @Test
    public void testByteArray() throws Exception {
        String content = "hello byte array";
        byte[] bytes = content.getBytes(Charsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        byte[] bytes2 = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int character = 0;
        while ((character = inputStream.read()) > 0) {
            outputStream.write(character);
        }

        System.out.println(outputStream.toString(Charsets.UTF_8.name()));
    }
}
