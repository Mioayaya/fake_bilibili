<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>

    <%
        String [] id = new String[10];
        String [] name=new String[20];
        String [] description =new String[20];
        String [] image = new String[100];
        String [] time = new String[100];
        int userId = 0;
        int zs=0;
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库
        if(conn!=null){
            Statement stmt1 = null;
            ResultSet rs1 = null;
            String sql1 ="select * from user where user_flag = 'true' ";  //查询语句
            stmt1 = conn.createStatement();
            rs1 = stmt1.executeQuery(sql1);

            while(rs1.next()){
                userId = rs1.getInt("user_id");
            }




            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from history where user_id = '"+userId+"' ";  //查询语句
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                  id[zs]=rs.getString("user_id");
                  name[zs]=rs.getString("anim_name");
                  description[zs]=rs.getString("anim_description");
                  image[zs]=rs.getString("anim_image");
                  time[zs]=rs.getString("history_time");
                  zs++;
                  //将数据库表格查询结果输出
            }
        }
    %>

<%
    out.println("[");
    for(int i=0;i<zs;i++){
        out.println("{");
        out.println("\"id\":\""+id[i]+"\"");
        out.println(",\"name\":\""+name[i]+"\"");
        out.println(",\"description\":\""+description[i]+"\"");
        out.println(",\"image\":\""+image[i]+"\"");
        out.println(",\"time\":\""+time[i]+"\"");
        out.println("}");
        if(i!=zs-1){
            out.println(",");
        }
    }
    out.println("]");
%>
