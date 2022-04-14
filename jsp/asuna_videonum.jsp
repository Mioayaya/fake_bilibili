<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>



<%
    String videoName = request.getParameter("name");
    if(videoName !=null && videoName.length()>0)
    {

        videoName = URLDecoder.decode(videoName, "UTF-8");
    }


    Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //指向数据库table1
    String username = "root";  //数据库用户名
    String password = "root";  //数据库用户密码
    Connection conn = DriverManager.getConnection(url, username, password);  //连接数据库

%>


<%
    String [] video_js = new String[100];
    String [] video_mc = new String[100];
    String [] video_id = new String[100];
    String video_name = "";
    String ves = "";
    Statement stmt1 = null;
    ResultSet rs1 = null;
    String sql1 ="select * from video";  //查询语句
    stmt1 = conn.createStatement();
    rs1 = stmt1.executeQuery(sql1);
    int num = 0;

    while (rs1.next()) {
        //将数据库表格查询结果输出

        if(Objects.equals(rs1.getString("name"), videoName)){

            video_js[num] = rs1.getString("videojs");
            video_mc[num] = rs1.getString("videoName");
            video_id[num] = rs1.getString("id");
            num++;

        }

    }


%>

<%

    out.println("[");
    for(int k=0;k<num;k++) {
    out.println("{");
    out.println("\"js\":\"" + video_js[k] + "\",");
    out.println("\"mc\":\"" + video_mc[k] + "\",");
    out.println("\"id\":\"" + video_id[k] + "\"");
    out.println("}");
        if(k<num-1){
            out.println(",");
        }
    }
    out.println("]");
%>