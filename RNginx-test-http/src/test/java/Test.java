import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-06-29 19:37
 **/
public class Test {

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {

        URI uri = new URI("www.baidu.com");
        System.out.println(uri);
    }
}
