<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder"%>
<html>
<!-- name sign grade-->
    <%

        String revise = "true";
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");
        String grade = request.getParameter("grade");
        name=URLDecoder.decode(name,"utf-8");
        sign=URLDecoder.decode(sign,"utf-8");
        grade=URLDecoder.decode(grade,"utf-8");

        Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        String sql2 = "update user set  user_name='"+name+"',user_sign ='" + sign + "',user_grade='"+grade+"' where user_flag='true' ";
        PreparedStatement pst = null;
        pst = conn.prepareStatement(sql2);
        pst.executeUpdate();
    %>
    
<%
    out.println("[");


        out.println("{");
        out.println("\"revise\":\"" + revise + "\"");
        out.println("}");


    out.println("]");
%>


</html>