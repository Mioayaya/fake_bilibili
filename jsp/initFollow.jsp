<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%
        // 判断flag的初始状态
        String Username = "";                                 // 当前用户，也就是粉丝
        String userName = request.getParameter("userName");   // 传过来的name，也就是被关注者
        String Flag = "false";  // 是否已经存在  初始是不存在
        String follow = "";
        //userName = "测试bot";
        userName=URLDecoder.decode(userName,"utf-8");
        // 如果已经有并且flag = true 则follow为真 就说取消关注
        // 其它为 follow为假 可以关注
        Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库

        // 找到当前username
        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user where user_flag = 'true'";  //查询语句
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);
        while (rs1.next()) {
            Username = rs1.getString("user_name");
        }
        // 查询到当前的关系
        Statement stmtA = null;
        ResultSet rsA = null;
        String sqlA ="select * from  user_follow where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";  //查询语句
        stmtA = conn.createStatement();
        rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
            
            Flag = rsA.getString("flag"); // 得到flag状态
        }
        if(Objects.equals(Flag,"true")){  // 如果已经关注了 关注为真 显示取消关注
            follow = "true";
        }
        else{
            follow = "false";  // 其它情况，未关注，不存在  关注为假 可以关注 显示 关注
        }

%>
    
<%

    out.println("[");
        out.println("{");
        out.println("\"follow\":\"" + follow + "\"");
        out.println("}");


    out.println("]");
%>
