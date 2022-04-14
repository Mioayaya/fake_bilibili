<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<!-- name sign grade-->
    <%

        String revise = "true";
       
        String headimageStr = request.getParameter("headimageStr");
        headimageStr=URLDecoder.decode(headimageStr,"utf-8");
        String flag = "false";
        int userId = 0;
        String username = "";


        Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        
// 找到用户id

        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user where user_flag = 'true'";  //查询语句
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);
        while (rs1.next()) {
            username = rs1.getString("user_name");
            userId = rs1.getInt("user_id");
            String sqlT = "UPDATE user set user_image = '"+headimageStr+"'  where user_id = '"+userId+"' ";
            PreparedStatement pstT =null;
            pstT = conn.prepareStatement(sqlT);
            pstT.executeUpdate();
        }
//  修改评论区的头像
        Statement stmt2 = null;
        ResultSet rs2 = null;
        String sql2 ="select * from comment where name = '"+username+"'";  //查询语句
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery(sql1);
        while (rs2.next()) {
            String sqlC = "UPDATE comment set image = '"+headimageStr+"'  where name = '"+username+"' ";
            PreparedStatement pstC =null;
            pstC = conn.prepareStatement(sqlC);
            pstC.executeUpdate();
        }    



    %>
    
<%


    out.println("[");


        out.println("{");
        out.println("\"revise\":\"" + revise + "\"");
        out.println("}");


    out.println("]");
%>
