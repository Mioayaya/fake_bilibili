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
<%@ page import="javax.swing.*" %>

<%
    String id = request.getParameter("id");
    String style = request.getParameter("style");
    String name_Danmaku = request.getParameter("name_Danmaku");
    String time_Danmaku = request.getParameter("time_Danmaku");
    String message_Danmaku = request.getParameter("message_Danmaku");


    String [] nameDanmaku = new String[100];
    String [] timeDanmaku = new String[100];
    String [] messageDanmaku = new String[100];

    if(id !=null && id.length()>0)
    {
        id = URLDecoder.decode(id, "UTF-8");
        style = URLDecoder.decode(style, "UTF-8");
        name_Danmaku = URLDecoder.decode(name_Danmaku,"UTF-8");
        time_Danmaku = URLDecoder.decode(time_Danmaku,"UTF-8");
        message_Danmaku = URLDecoder.decode(message_Danmaku,"UTF-8");

    }


    Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //指向数据库table1
    String username = "root";  //数据库用户名
    String password = "root";  //数据库用户密码
    Connection conn = DriverManager.getConnection(url, username, password);  //连接数据库

%>

<%





    int i=0;


    if(Objects.equals(style,"post")){

        String sql2 = "INSERT  INTO  danmaku (DUsername,DTime,DMessage,DVideo) VALUES (?,?,?,?)";
        PreparedStatement pst = null;
        pst = conn.prepareStatement(sql2);
        pst.setString(1, name_Danmaku);
        pst.setInt(2, Integer.parseInt(time_Danmaku));
        pst.setString(3, message_Danmaku);
        pst.setString(4, id);

        pst.executeUpdate();


    }
    int timeChange = 0;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    String sql1 = "select * from danmaku ORDER BY DTime ";  //查询语句
    stmt1 = conn.createStatement();
    rs1 = stmt1.executeQuery(sql1);

    while (rs1.next()) {
        //将数据库表格查询结果输出
        if(rs1.getString("DVideo").equals(id)) {
            nameDanmaku[i] = rs1.getString("DUsername");
            timeChange = rs1.getInt("DTime");
            timeDanmaku[i] = String.valueOf(timeChange);
            messageDanmaku[i] = rs1.getString("DMessage");
            i++;
        }
    }



%>

<%
    if(i >0) {

        out.println("[");

        for (int k = 0; k < i; k++) {
            out.println("{");
            out.println("\"DUsername\":\"" + nameDanmaku[k] + "\",");
            out.println("\"DTime\":\"" + timeDanmaku[k] + "\",");
            out.println("\"DMessage\":\"" + messageDanmaku[k] + "\"");
            out.println("}");
            if (k < i - 1) {
                out.println(",");
            }
        }

        out.println("]");
    }
%>