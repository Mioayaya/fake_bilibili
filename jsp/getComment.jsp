<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>
<%@ page import="java.net.URL" %>


<%
    String id = request.getParameter("id");
    String style = request.getParameter("style");
    
    String [] nameComment = new String[100];
    String [] timeComment = new String[100];
    String [] messageComment = new String[100];
    String [] imageComment = new String[100];
    String [] videoComment = new String[100];
    String name_Comment = "";  // 评论名字
    String image_Comment = ""; // 评论头像

    if(id !=null && id.length()>0)
    {
        id = URLDecoder.decode(id, "UTF-8");
        style =URLDecoder.decode(style,"UTF-8");
    }

    Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //指向数据库table1
    String username = "root";  //数据库用户名
    String password = "root";  //数据库用户密码
    Connection conn = DriverManager.getConnection(url, username, password);  //连接数据库

%>
<%
    int i=0;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    String sql1 = "select * from comment where video = '"+id+"'";  //查询语句
    stmt1 = conn.createStatement();
    rs1 = stmt1.executeQuery(sql1);

    while (rs1.next()) {
        //将数据库表格查询结果输出
            nameComment[i] = rs1.getString("name");
            timeComment[i] = rs1.getString("time");
            messageComment[i] = rs1.getString("message");
            imageComment[i] = rs1.getString("image");
            videoComment[i] = rs1.getString("video");
            i++;
    }



%>

<%
    if(i >0) {
        out.println("[");
        for (int k = i - 1; k >= 0; k--) {
            out.println("{");
            out.println("\"Username\":\"" + nameComment[k] + "\",");
            out.println("\"UserTime\":\"" + timeComment[k] + "\",");
            out.println("\"UserMessaged\":\"" + messageComment[k] + "\",");
            out.println("\"UserImage\":\"" + imageComment[k] + "\"");
            out.println("}");
            if (k > 0) {
                out.println(",");
            }
        }
        out.println("]");
    }
%>
