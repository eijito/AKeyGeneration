package com.chn.mybatis.gen;

import com.chn.mybatis.gen.def.TableMetadata;
import com.chn.mybatis.gen.trans.TableTrans;
import com.chn.mybatis.gen.utils.DBUtils;
import org.apache.commons.io.FileUtils;
import org.bee.tl.core.GroupTemplate;
import org.bee.tl.core.Template;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Map;

public class Main {

	private static final String EOL = System.getProperty("line.separator");
	public static final String ROOT_FILE_PATH = Main.class.getResource("/").getPath().replace("%20", " ");
	public static final String PACKAGE_PATH = ROOT_FILE_PATH + "tpl";
	public static final String GEN_PACKAGE = Configuration.GEN_PACKAGE;
	public static final File GEN_FOLDER = new File(ROOT_FILE_PATH + "../" + Configuration.fileName);

	public static final GroupTemplate group = new GroupTemplate(new File(PACKAGE_PATH));

	public static void main(String[] args) throws Exception {
		group.setCharset("UTF-8");
		Connection conn = DBUtils.getConn();
		Map<String,String> tabComment = DBUtils.getTableComment(conn);
		DatabaseMetaData dbmd = DBUtils.getDatabaseMetaData(conn);
		String dbType = dbmd.getDatabaseProductName();
		DBUtils.loadMetadata(dbmd,tabComment);
		for (String tableName : TableMetadata.getAllTables().keySet()) {
			generateXml(tableName, dbType);
			generateDao(tableName, dbType);
			generateEntity(tableName, dbType);
			generateService(tableName, dbType);
			generateWeb(tableName, dbType);
			generatePom();
			generateStartApp();
			generateYml();
			generateLog();
			generateSwagger2();
			generateDbConfig();
//			generateDialogInfoHtml(tableName, dbType);
//			generateDialogUpdateHtml(tableName, dbType);
		}
	}

	private static void generateDialogInfoHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dialog-info-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("title", "tableName管理");
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-info.jsp");
		FileUtils.write(new File(GEN_FOLDER, "/list/" + trans.getLowerStartClassName() + "-info.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void generateDialogUpdateHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dialog-update-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("title", "tableName管理");
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-update.jsp");
		FileUtils.write(new File(GEN_FOLDER, "/list/" + trans.getLowerStartClassName() + "-update.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void writeTag(TableTrans trans, String basePath) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\"	pageEncoding=\"UTF-8\"%>").append(EOL);
		buffer.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>").append(EOL);
		buffer.append("<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\"%>").append(EOL);
		buffer.append("<c:set var=\"ctx\" value=\"${pageContext.request.contextPath}\" />").append(EOL);
		FileUtils.write(new File(GEN_FOLDER, basePath), buffer.toString());
	}

	private static void generateDatatableHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-datatable-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		template.set("title", "tableName管理");
		template.set("addEntity", "添加");
		template.set("startTime", "开始时间");
		template.set("endTime", "结束时间");
		template.set("searchText", "searchText");
		template.set("searchButton", "searchButton");
		template.set("export", "导出");
		template.set("name", "操作");
		template.set("infoName", "详细");
		template.set("deleteName", "删除");
		template.set("updateName", "更新");

		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-list.jsp");
		FileUtils.write(new File(GEN_FOLDER, "/list/" + trans.getLowerStartClassName() + "-list.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void generateXml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dao-xml.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/resources/mapper/" + trans.getUpperStartClassName() + "Dao.xml"), template.getTextAsString(), "UTF-8");
	}

	private static void generatePom() throws Exception {
		Template template = group.getFileTemplate("pom-xml.txt");
		template.set("package", GEN_PACKAGE);
		FileUtils.write(new File(GEN_FOLDER, "/pom.xml" ), template.getTextAsString(), "UTF-8");
	}
	private static void generateStartApp() throws Exception {
		Template template = group.getFileTemplate("StartApplication-java.txt");
		template.set("package", GEN_PACKAGE);
		String s = GEN_PACKAGE.replaceAll(".", "/");

		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/StartApplication.java" ), template.getTextAsString(), "UTF-8");
	}
	private static void generateYml() throws Exception {
		Template template = group.getFileTemplate("application-yml.txt");
		template.set("package", GEN_PACKAGE);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/resources/application.yml" ), template.getTextAsString(), "UTF-8");
	}

	private static void generateSwagger2() throws Exception {
		Template template = group.getFileTemplate("Swagger2-java.txt");
		template.set("package", GEN_PACKAGE);
		String replace = GEN_PACKAGE.replace(".", "/");

		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/config/Swagger2.java" ), template.getTextAsString(), "UTF-8");
	}
	private static void generateDbConfig() throws Exception {
		Template template = group.getFileTemplate("DataSourceConfig-java.txt");
		template.set("package", GEN_PACKAGE);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/config/DataSourceConfig.java" ), template.getTextAsString(), "UTF-8");
	}

	private static void generateLog() throws Exception {
		Template template = group.getFileTemplate("logback-xml.txt");
		template.set("package", GEN_PACKAGE);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/resources/logback.xml" ), template.getTextAsString(), "UTF-8");
	}

	private static void generateDao(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dao-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/dao/" + trans.getUpperStartClassName() + "Dao.java"), template.getTextAsString(), "UTF-8");
	}

	private static void generateWeb(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-web-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/controller/" + trans.getUpperStartClassName() + "Controller.java"), template.getTextAsString(),
				"UTF-8");
	}

	private static void generateService(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-service-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/service/" + trans.getUpperStartClassName() + "Service.java"), template.getTextAsString(), "UTF-8");
	}

	private static void generateEntity(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-domain.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_FOLDER, "/src/main/java/"+GEN_PACKAGE.replace(".","/")+"/entity/" + trans.getUpperStartClassName() + ".java"), template.getTextAsString());
	}

	/**
	 * 切割包名
	 * @return
	 */
//	private static String[] returnPage(){
//		if(GEN_PACKAGE.length()>0 ){
//			if(GEN_PACKAGE.contains(".")){
//				String[] packName = GEN_PACKAGE.split(".");
//				return packName;
//			}else {
//				return new String[]{GEN_PACKAGE};
//			}
//		}
//			return new String[]{"com"};
//	}
//
//	private static  String packName(){
//		String[] packNames = returnPage();
//		String packName = "";
//		for (int i=0;i<packNames.length;i++){
//			packName = packName + packNames[i] + "/";
//		}
//		return packName;
//	}

}
