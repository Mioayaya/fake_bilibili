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

        String codeName = "��ʾbot"; //Ŀ��name
        String [] name= new String[20];  // Ŀ������
        String [] sign= new String[20];
        String [] grade= new String[20];
        String [] image= new String[20];
        int zs=0;
        // Ѱ�ҹ�ע�ߣ�fens����Ϊ�Լ���Ҳ���Ǵ��ݹ�����userName
        // Ȼ�󰤸��ҵ���ע�ߣ��ٴ�user���������Ӧ����Ϣ
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //�������ݿ�����
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //ָ�����ݿ�table1
        String username1 = "root";  //���ݿ��û���
        String password1 = "root";  //���ݿ��û�����
        Connection conn = DriverManager.getConnection(url, username1, password1);  //�������ݿ�
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from user_follow where user_fans_name = '"+userName+"' and flag = 'true' ";  //��ѯ���
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                        name[zs] = rs.getString("user_follow_name");
                        zs++;
            }
        }
        // �Ѿ���name�����ˣ���name�������������Ϣ
        for(int i=0;i<zs;i++){
            Statement stmt2 = null;
            ResultSet rs2 = null;
            String sql2 ="select * from user where user_name = '"+name[i]+"' ";  //��ѯ���
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
