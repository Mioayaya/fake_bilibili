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
        // �ж�flag�ĳ�ʼ״̬
        String Username = "";                                 // ��ǰ�û���Ҳ���Ƿ�˿
        String userName = request.getParameter("userName");   // ��������name��Ҳ���Ǳ���ע��
        String Flag = "false";  // �Ƿ��Ѿ�����  ��ʼ�ǲ�����
        String follow = "";
        //userName = "����bot";
        userName=URLDecoder.decode(userName,"utf-8");
        // ����Ѿ��в���flag = true ��followΪ�� ��˵ȡ����ע
        // ����Ϊ followΪ�� ���Թ�ע
        Class.forName("com.mysql.cj.jdbc.Driver");  //�������ݿ�����
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //ָ�����ݿ�table1
        String username1 = "root";  //���ݿ��û���
        String password1 = "root";  //���ݿ��û�����
        Connection conn = DriverManager.getConnection(url, username1, password1);  //�������ݿ�

        // �ҵ���ǰusername
        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user where user_flag = 'true'";  //��ѯ���
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);
        while (rs1.next()) {
            Username = rs1.getString("user_name");
        }
        // ��ѯ����ǰ�Ĺ�ϵ
        Statement stmtA = null;
        ResultSet rsA = null;
        String sqlA ="select * from  user_follow where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";  //��ѯ���
        stmtA = conn.createStatement();
        rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
            
            Flag = rsA.getString("flag"); // �õ�flag״̬
        }
        if(Objects.equals(Flag,"true")){  // ����Ѿ���ע�� ��עΪ�� ��ʾȡ����ע
            follow = "true";
        }
        else{
            follow = "false";  // ���������δ��ע��������  ��עΪ�� ���Թ�ע ��ʾ ��ע
        }

%>
    
<%

    out.println("[");
        out.println("{");
        out.println("\"follow\":\"" + follow + "\"");
        out.println("}");


    out.println("]");
%>
