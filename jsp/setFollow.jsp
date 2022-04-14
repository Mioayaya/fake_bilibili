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

        String codeName = "演示bot"; //目标name
        String [] name= new String[20];  // 目标名字
        String [] sign= new String[20];
        String [] grade= new String[20];
        String [] image= new String[20];
        int zs=0;
        // 寻找关注者，fens名字为自己，也就是传递过来的userName
        // 然后挨个找到关注者，再从user表中找相对应的信息
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from user_follow where user_fans_name = '"+userName+"' and flag = 'true' ";  //查询语句
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                        name[zs] = rs.getString("user_follow_name");
                        zs++;
            }
        }
        // 已经有name数组了，从name数组填充其它信息
        for(int i=0;i<zs;i++){
            Statement stmt2 = null;
            ResultSet rs2 = null;
            String sql2 ="select * from user where user_name = '"+name[i]+"' ";  //查询语句
            stmt2 = conn.createStatement();
            rs2 = stmt2.executeQuery(sql2);
            while(rs2.next()){
                sign[i] = rs2.getString("user_sign");
                grade[i] = rs2.getString("user_grade");
                image[i] = rs2.getString("user_image");
            }
        }
%>

<%
    out.println("[");
    for(int i=0;i<zs;i++){
        out.println("{");
        out.println("\"name\":\""+name[i]+"\"");
        out.println(",\"sign\":\""+sign[i]+"\"");
        out.println(",\"grade\":\""+grade[i]+"\"");
        out.println(",\"image\":\""+image[i]+"\"");
        out.println("}");
        if(i!=zs-1){
            out.println(",");
        }
    }
    out.println("]");
%>
