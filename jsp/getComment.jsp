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
    String name_Comment = "";  // ��������
    String image_Comment = ""; // ����ͷ��

    if(id !=null && id.length()>0)
    {
        id = URLDecoder.decode(id, "UTF-8");
        style =URLDecoder.decode(style,"UTF-8");
    }

    Class.forName("com.mysql.cj.jdbc.Driver");  //�������ݿ�����
    String url = "jdbc:MySQL://localhost:3306/lesson?&useSSL=false&serverTimezone=UTC"; //ָ�����ݿ�table1
    String username = "root";  //���ݿ��û���
    String password = "root";  //���ݿ��û�����
    Connection conn = DriverManager.getConnection(url, username, password);  //�������ݿ�

%>
<%
    int i=0;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    String sql1 = "select * from comment where video = '"+id+"'";  //��ѯ���
    stmt1 = conn.createStatement();
    rs1 = stmt1.executeQuery(sql1);

    while (rs1.next()) {
        //�����ݿ����ѯ������
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
