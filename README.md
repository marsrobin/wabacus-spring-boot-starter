wabacus-spring-boot-starter

# 介绍

该项目是一个springboot插件，集成了wabacus开发框架，让wabacus这个古老的框架更容易的在springboot中使用。

Wabacus是一个完整的JAVAEE开发框架，目前主要使用该框架做报表，因为每个系统的业务功能开发完成后，基本都有数据统计的需求，使用该框架能够快速的开发各种复杂的报表。使用类Excel编辑的方式，完成完成系统基础数据的维护。

Wabacus的资料请在百度网盘自行下载：链接：https://pan.baidu.com/s/18sqovzNGrs9BQswt9cGKpw?pwd=4kiy 
提取码：4kiy

# 安装使用

1.在springboot项目中导入maven依赖

```xml
<dependency>
     <groupId>com.skynet.robin</groupId>
    <artifactId>wabacus-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

2.配置Wabacus默认加载路径

该插件默认的路径为：classpath：reportconfig。如果想要自定义报表配置路径可以在application.yml中定义wabacus.configpath

```yaml
wabacus:
	configpath: classpath：xxxxx
```

3.配置wabacus.cfg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<wabacus-config>
	<system>
		<item name="webroot" value="/"/><!-- 应用的根URL -->
		<item name="webroot-abspath" value=""/><!-- 默认为context.getRealPath("/")获取的值，可以直接配置根路径 -->
		<item name="showreport-url" value="/report.wx"/><!--显示报表数据的URL，就是在web.xml中配置的<url-pattern/>的值可以带参数-->
		<item name="encode" value="UTF-8"/>
		<item name="3des-keyfilepath" value="classpath{/3des.key}"/><!-- 3des密钥存放位置 -->
		<item name="show-sql" value="true"/><!-- 是否显示执行的SQL语句或存储过程 -->
		<item name="default-cellresize" value="2"/><!-- 指定整个项目中所有单行表头的数据自动列表报表的表格单元格默认可移动性 -->
		<item name="default-labelstyle" value="1"/><!-- 查询条件提示标签的显示位置 -->
		<item name="skin" value="vista"/><!-- 皮肤样式 lightblue-->
		<item name="dataimport-batchupdate-size" value="30"/>
		<item name="default-checkpermission" value="true"/><!-- 本应用是否要检查权限，如果在不需要检查权限的应用中关闭它，可以改善性能 -->
		<item name="prompt-dialog-type" value="artdialog"/><!-- 提示组件类型 ymprompt;artdialog-->
		<item name="default-chart-datatype" value="xmlurl-servlet"/><!-- 默认的chart图表数据类型 -->
	</system>
	<datasources default="ds_mysql">
		<datasource name="ds_mysql" type="com.wabacus.config.database.datasource.C3P0DataSource" dbtype="com.wabacus.config.database.type.MySql">
			<property name="driver">com.mysql.jdbc.Driver</property>
			<property name="url"><![CDATA[jdbc:mysql://localhost:3306/wabacusdemodb?useUnicode=true&characterEncoding=GBK]]></property>
			<property name="user">root</property>
			<property name="password">root</property>

			<property name="max_size">20</property>
			<property name="min_size">5</property>
			<property name="timeout">100</property>
			<property name="max_statements">100</property>
			<property name="idle_test_period">50</property>
			<property name="acquire_increment">2</property>
		</datasource>
	</datasources>

	<!-- 配置所有报表类型 -->
	<report-types/>

	<inputbox-types/>

	<!-- 配置本项目中所有报表页面都必须包含的js文件 -->
	<global-jsfiles/>

	<!-- 配置本项目中所有报表页面都必须包含的css文件 -->
	<global-cssfiles/>

	<global-interceptors>

	</global-interceptors>

	<i18n-resources file="/resources/i18n/ApplicationResources.xml"/>

	<!-- 注册所有全局资源文件 -->
	<global-resources>

	</global-resources>

	<!-- 注册所有报表配置文件 -->
	<report-files>
		<report-file pattern="true">report/(report_)(\S*?)(\.xml)</report-file>
		<!--report-file pattern="true">classpath{reportconfig/report/(report_)(\S*?)(\.xml)}</report-file>
		<report-file pattern="true">relative{reportconfig/report/(report_)(\S*?)(\.xml)}</report-file>
		<report-file pattern="true">absolute{D:\reportconfig\report\(report_)(\S*?)(\.xml)}</report-file-->
		<!--report-file>report/report_fusioncharts.xml</report-file>
  		<report-file>report/report_reportypes.xml</report-file>
  		<report-file>report/report_permissions.xml</report-file>
  		<report-file>report/report_template.xml</report-file>
  		<report-file>report/report_container.xml</report-file>
  		<report-file>report/report_modularize.xml</report-file>
  		<report-file>report/report_usualfunc1.xml</report-file>
  		<report-file>report/report_usualfunc2.xml</report-file>
  		<report-file>report/report_usualfunc3.xml</report-file>
  		<report-file>report/report_usualfunc4.xml</report-file>
  		<report-file>report/report_usualfunc5.xml</report-file>
  		<report-file>report/report_dataexportprint.xml</report-file>
  		<report-file>report/report_l10n_en.xml</report-file>
  		<report-file>report/report_i18n.xml</report-file>
  		<report-file>report/report_editablefunc1.xml</report-file>
  		<report-file>report/report_editablefunc2.xml</report-file>
  		<report-file>report/report_editablefunc3.xml</report-file>
  		<report-file>report/report_editablefunc4.xml</report-file>
  		<report-file>report/report_editablefunc5.xml</report-file>
  		<report-file>report/report_formfunc1.xml</report-file>
  		<report-file>report/report_formfunc2.xml</report-file>
  		<report-file>report/report_clientapi.xml</report-file-->
	</report-files>
</wabacus-config>
```

4.启动springboot

访问：http://localhost:8080/report.wx?PAGEID=listpage1

此处的report.wx在 wabacus.cfg.xml  中  showreport-url  该项，此为报表的入口地址。

在datasources中配置你的数据源。