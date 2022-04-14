<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>

    <%
        String [] image = new String[100];
        int zs=0;
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from user_image";  //查询语句
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                  image[zs]=rs.getString("image_str");
                  zs++;
                  //将数据库表格查询结果输出
            }
        }
    %>

<%
    out.println("[");
    for(int i=0;i<zs;i++){
        out.println("{");
        out.println("\"image\":\""+image[i]+"\"");
        out.println("}");
        if(i!=zs-1){
            out.println(",");
        }
    }
    out.println("]");
%>
