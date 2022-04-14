<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>



<%
    String id = request.getParameter("id");
    if(id !=null && id.length()>0)
    {

        id = URLDecoder.decode(id, "UTF-8");
    }


    Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //指向数据库table1
    String username = "root";  //数据库用户名
    String password = "root";  //数据库用户密码
    Connection conn = DriverManager.getConnection(url, username, password);  //连接数据库

%>


<%
    String nameAddress = "http:192.168.43.44:8080/bilibili/video/";
    String video_name = "";
    String ves = "";
    Statement stmt1 = null;
    ResultSet rs1 = null;
    String sql1 ="select * from video";  //查询语句
    stmt1 = conn.createStatement();
    rs1 = stmt1.executeQuery(sql1);

    while (rs1.next()) {
        //将数据库表格查询结果输出
        ves = ves + rs1.getString("id");
        if(Objects.equals(rs1.getString("id"), id)){
        nameAddress = nameAddress+rs1.getString("nameAddress");
        video_name = rs1.getString("videoName");
        }
    }


%>

<%

    out.println("[");


    out.println("{");
    out.println("\"id\":\"" + id + "\",");
    out.println("\"nameAddress\":\"" + nameAddress + "\",");
    out.println("\"videoName\":\"" + video_name + "\"");
    out.println("}");


    out.println("]");
%>
