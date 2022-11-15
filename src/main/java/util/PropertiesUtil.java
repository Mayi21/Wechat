package util;

import entity.DBConfigEntity;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesUtil {
	public static DBConfigEntity getDbConfig() {
		try {
			String dbConfigFilePath = "properties/db.properties";
			Properties properties = getProperties(dbConfigFilePath);
			if (!Objects.isNull(properties)) {
				String ip = properties.getProperty("database.ip");
				String port = properties.getProperty("database.port");
				String dbName = properties.getProperty("database.name");
				String url = String.format("jdbc:mysql://%s:%s/%s", ip, port, dbName);
				String userName = properties.getProperty("database.username");
				String passwd = properties.getProperty("database.passwd");
				return new DBConfigEntity(userName, passwd, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private static Properties getProperties(String configPath) {
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(configPath);
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
