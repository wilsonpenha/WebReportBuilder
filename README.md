# WebReportBuilder
Dynamic jasperreport server

March 05 of 2007
Release 2.1

Hi,

Welcome to WebReportBuilder, the easiest Application to build Reports based on JasperReports and Java, 

This is a first Struts/Hwork beta version, and you can help me to improve this Application to be a big Report Application,

That’s so easy to install, follow the steps and you don’t will make any mistake;

Step 1: You should to install the JDK 1.4.1 or above (recommended));

Step 2: You can run it under Tomcat 5.5.x, IBM WAS, Jboss, IAS, etc. 

Step 3: Install the WebReportBuilder_0.4.1.war file into webapps repository;

Step 4: Put the WEB-INF/lib directory into your web server classpath to make sure no library has missed;

Step 5: This version came with a MySQL script backup DB, so you can install the MySQL 5.0 and restore it, by creating the application repository; And you also can migrate it into any DB Server.

Step 6: After creating you Report DB, go to your WebServer admin console and create a JNDI resource called jdbc/report_ds, this way the application can reach the repository DB, for Tomcat 5.5 also create the Resource link as jdbc/report_ds.

Note: Has 4 examples about how to use the WebReportBuilder, any doubt please email me at your convenience to wilsonpenha@hotmail.com

After you complete installing the application type the follow url;
http://yourserver/reportbuilder/beanReport.do for designer new Reports, or 
http://yourserver/reportbuilder/beanReportServer.do (Report Server) to see our examples;


This Application is based on JNDI Datasource; if you want to build your reports you can put the jndi parameters for Access you Database (Oracle, SQL, DB2, MySql, Etc.), you also should add the JNDI configuration by access the WebServer console admin.

New implementation into WebReportBuilder was the SQLeonardo query builder, this is a tool based on Java GUI that we made a have change to make it work as a JApplet into WebReportBuilder, 

We just have done the first step to integrate the query builder, that what coming next, fields integration and query integration with WebReportBuilder repository DB.


With this new resource you must make couple changes into your java.policy file into jre_home\lib\security:

Before make any change into your java.policy, first make a bkp.

Remember this is necessary because the applet not signed to browser client, so every client should get his java.policy changed.

Insert the content of our java.policy file into your’s to allow the querybuilder to working.


The WebReportBuilder own by HWORK Services LLC and your Director it is Wilson Mendonça da Penha Jr.

My email: wilsonpenha@hotmail.com, you can contact me for whatever you need, give us yours feedback and report me for any issue you got,

For more technical information this WebReportBuilder it all make by 
HWORK Persistence Manager Framework, based on Struts and JavaBeans and JDNI, this support the distribution server by RMI, CMP, and your code was build by HWORK Java generator code, 

The WebReportBuilder needs you to make it better then we can.
WebReportBuilder is now full working with any Application Server available on the market, like a (WAS, IAS, JBOSS, BEA, TOMCAT and others),

If this application is exactly what you need for your company, you can contact me for personalization and advanced training or Others Consultancies 

We also work with Personalized Development for J2EE Applications, Websphere, DB2, Oracle, SQL, Sybase, and Vitria BW (EAI), tell me what you need;

What coming next? Struts2, JPA, Spring, Hibernate and maybe EXTjs.

Thanks for all

And enjoy the WebReportBuilder

Wilson M. Penha Jr.
Java Software architect – IBM Brazil.
E-mail: wilsonpenha@hotmail.com

Phone # +1 641-827-1514

