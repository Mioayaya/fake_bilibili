<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>


<%


    String style = request.getParameter("style");
    String username1 = request.getParameter("username");
    String password1 = request.getParameter("password");
/*
    style = "login";
    username1 = "1001";
    password1 = "123456";
*/

    if(style !=null && style.length()>0)
    {

        style = URLDecoder.decode(style, "UTF-8");
        username1 = URLDecoder.decode(username1, "UTF-8");
        password1 = URLDecoder.decode(password1, "UTF-8");

    }

    Class.forName("com.mysql.cj.jdbc.Driver");  //�������ݿ�����
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //ָ�����ݿ�table1
    String username = "root";  //���ݿ��û���
    String password = "root";  //���ݿ��û�����
    Connection conn = DriverManager.getConnection(url, username, password);  //�������ݿ�



%>

<%


    String login = "null";
    String register = "null";

    if(Objects.equals(style, "register")) {

        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user ";  //��ѯ���
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);


        while (rs1.next()) {
            //�����ݿ����ѯ������
            if(Objects.equals(rs1.getString("user_back"), username1)){
                register = "false";
            }
        }
        if(register.equals("null")) {
            String sql2 = "INSERT  INTO  user (user_back,user_password) VALUES (?,?)";
            PreparedStatement pst = null;
            pst = conn.prepareStatement(sql2);
            pst.setString(1, username1);
            pst.setString(2, password1);

            register = "true";

            pst.executeUpdate();
        }

    }else if(Objects.equals(style, "login"))  {

        Statement stmtFlag = null;
        ResultSet rsFlag = null;
        String sqlFlag ="select * from user ";  //��ѯ���
        stmtFlag = conn.createStatement();
        rsFlag = stmtFlag.executeQuery(sqlFlag);

/*��ȫ����id flag����Ϊfalse*/
        while(rsFlag.next()){

            String sql2 = "UPDATE user set user_flag = '"+"false"+"' "  ;
            PreparedStatement pst =null;
            pst = conn.prepareStatement(sql2);
            pst.executeUpdate();
        }


        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user ";  //��ѯ���
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);


        while (rs1.next()) {
            //�����ݿ����ѯ������
            if(Objects.equals(rs1.getString("user_back"), username1)){

                if(Objects.equals(rs1.getString("user_password"), password1)){
                    login = "true";
                    int id = rs1.getInt("user_id");
                    String sql3 = "UPDATE user set user_flag = '"+"true"+"' where user_id = '"+id+"' "  ;
                    PreparedStatement pst3 =null;
                    pst3 = conn.prepareStatement(sql3);
                    pst3.executeUpdate();
                    
                }

            }
            if(login.equals("null")){
                login = "false";
            }
        }

    }



%>






<%
    out.println("[");


        out.println("{");
        out.println("\"login\":\"" + login + "\",");
        out.println("\"register\":\"" + register + "\"");
        out.println("}");


    out.println("]");
%>






