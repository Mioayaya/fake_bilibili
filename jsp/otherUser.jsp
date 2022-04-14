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
        String userName = request.getParameter("userName");
         userName=URLDecoder.decode(userName,"utf-8");

        String name="1";
        String sign="2";
        String grade="3";
        int doinb = 0;
        int binb = 0;
        int watch = 0;
        int follow = 0;
        int fans = 0;        
        String image = "";

        Class.forName("com.mysql.cj.jdbc.Driver");                                          //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from user where user_name = '"+userName+"'";  //查询语句
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                        name = rs.getString("user_name");
                        sign = rs.getString("user_sign");
                        grade = rs.getString("user_grade");
                        doinb = rs.getInt("user_doinb");  
                        binb = rs.getInt("user_binb");
                        watch = rs.getInt("user_watch");
                        follow = rs.getInt("user_follow");
                        fans = rs.getInt("user_fans");
                        image = rs.getString("user_image");
                  
            }
        }
%>

<%
    out.println("[");
        out.println("{");
        out.println("\"name\":\"" + name + "\",");
        out.println("\"sign\":\"" + sign + "\",");
        out.println("\"grade\":\"" + grade + "\",");
        out.println("\"doinb\":" + doinb + ",");
        out.println("\"binb\":" + binb + ",");
        out.println("\"watch\":" + watch + ",");
        out.println("\"follow\":\"" + follow + "\",");
        out.println("\"fans\":\"" + fans + "\",");
        out.println("\"image\":\"" + image + "\"");
        out.println("}");
    out.println("]");
%>
