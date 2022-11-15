package util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class ImageUtil {
	public static String getImageFilePath(String imageName) {
		return String.format("file:E:\\Study\\CodeProject\\Java\\Wechat\\src\\main\\resources\\image\\%s", imageName);
	}
}
