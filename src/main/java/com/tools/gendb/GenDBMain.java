package com.tools.gendb;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 读取游戏数值配置
 */
public class GenDBMain {

	static String URL = "jdbc:mysql://{0}/{1}";
	static String excelPath = "";

	static String USER = "";
	static String PASSWORD = "";
	static String[] tableNames = null;
	static String dbname = "";
	static {
		/**
		 * 关联实体类的名称与excel表格
		 */
		try {
			ResourceBundle rb = ResourceBundle.getBundle("config.gendb.gendb");
			excelPath = new String(rb.getString("DB_BEAN_PATH").getBytes("ISO-8859-1"), "utf-8");
			System.out.println("Bean生成文件夹：\t" + excelPath);

			String genTableNames = new String(rb.getString("GEN_TABLE_NAMES").getBytes("ISO-8859-1"), "utf-8");
			System.out.println("需要生成的数据表：\t" + genTableNames);

			tableNames = genTableNames.split(",");

			String ip = new String(rb.getString("IP").getBytes("ISO-8859-1"), "utf-8");
			dbname = new String(rb.getString("DBNAME").getBytes("ISO-8859-1"), "utf-8");

			URL = MessageFormat.format(URL, new Object[] { ip, dbname });
			System.out.println("mysql URL：\t" + URL);

			USER = new String(rb.getString("USER").getBytes("ISO-8859-1"), "utf-8");
			System.out.println("user：	\t" + USER);

			PASSWORD = new String(rb.getString("PWD").getBytes("ISO-8859-1"), "utf-8");
			System.out.println("password：\t" + PASSWORD);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void genDBMain() {

		for (String tableName : tableNames) {

			List<DBData> dbDatas = new ArrayList<DBData>();

			String className = coverToPropName(tableName, true);
			String alias = coverToPropName(tableName, false);

			List<PKData> primaryKeys = new ArrayList<>();

			Connection conn = getConnection();
			Statement stmt = null;
			try {
				stmt = conn.createStatement();

				String sql = "select * from " + tableName;
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();

				DatabaseMetaData metadata = conn.getMetaData();

				ResultSet rs2 = metadata
						.getPrimaryKeys(dbname, null, tableName);
				while (rs2.next()) {
					PKData gkData = new PKData();
					gkData.setColumName(rs2.getString(4));
					gkData.setPropName(coverToPropName(rs2.getString(4), false));
					primaryKeys.add(gkData);
				}

				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					DBData dbData = new DBData();

					Class<?> c = Class.forName(rsmd.getColumnClassName(i + 1));
					dbData.setClazz(c.getSimpleName());
					dbData.setColumName(rsmd.getColumnName(i + 1));

					String propName = coverToPropName(dbData.getColumName(),
							false);
					dbData.setPropName(propName);
					dbData.setMethodName(coverToPropName(dbData.getColumName(),
							true));

					dbDatas.add(dbData);
				}

				genJava(dbDatas, primaryKeys, className,tableName);

				genDBXml(dbDatas, tableName, className, alias, primaryKeys);
			} catch (Exception e) {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		}
	}
	
	private static void genDBXml(List<DBData> dbDatas, String tableName,String className, String alias, List<PKData> primaryKeys)
		throws Exception {
		Template xmlTemplate = Velocity.getTemplate("config/template/GenDBXml.vm","utf-8");

		VelocityContext context = new VelocityContext();
		context.put("propList", dbDatas);
		context.put("clazzName", className);
		context.put("alias", alias);
		context.put("tName", tableName);
		context.put("primaryKeys", primaryKeys);

		FileOutputStream fos = new FileOutputStream(excelPath +"/"+ className + ".xml");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
		if (xmlTemplate != null){
			xmlTemplate.merge(context, writer);
		}

		writer.flush();
		fos.flush();
		writer.close();
		fos.close();
	}

	public static void genJava(List<DBData> dbDatas, List<PKData> primaryKeys,
			String className,String tableName) throws FileNotFoundException, IOException {
		Template xmlTemplate = Velocity.getTemplate("config/template/GenDBBean.vm");

		VelocityContext context = new VelocityContext();

		context.put("propList", dbDatas);
		context.put("clazzName", className);
		context.put("primaryKeys", primaryKeys);
		context.put("tableName", tableName);

		FileOutputStream fos = new FileOutputStream(excelPath +"/"+ className
				+ ".java");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
		if (xmlTemplate != null)
			xmlTemplate.merge(context, writer);
		writer.flush();
		fos.flush();
		writer.close();
		fos.close();
	}

	/**
	 * 
	 * @param columName
	 *            数据库的字段名字
	 * @param flag
	 *            首字母是否大写
	 * @return
	 */
	public static String coverToPropName(String columName, boolean flag) {
		int index = 0;
		while ((index = columName.indexOf("_")) != -1) {
			String targetChar = columName.substring(index + 1, index + 2);
			String bigChar = targetChar.toUpperCase();
			columName = columName.replace("_" + targetChar, bigChar);
		}

		String firstChar = columName.substring(0, 1);
		if (flag) {
			columName = columName.replaceFirst(firstChar,
					firstChar.toUpperCase());
		}
		return columName;
	}

	public static Connection getConnection() {
		Connection cn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
}
