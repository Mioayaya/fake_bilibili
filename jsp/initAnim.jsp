<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>

    <%
        String [] id = new String[10];
        String [] name=new String[20];
        String [] description =new String[20];
        String [] image = new String[100];
        int zs=0;
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //�������ݿ�����
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //ָ�����ݿ�table1
        String username1 = "root";  //���ݿ��û���
        String password1 = "root";  //���ݿ��û�����
        Connection conn = DriverManager.getConnection(url, username1, password1);  //�������ݿ�
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from animation";  //��ѯ���
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                  id[zs]=rs.getString("anim_id");
                  name[zs]=rs.getString("anim_name");
                  description[zs]=rs.getString("anim_description");
                  image[zs]=rs.getString("anim_image");
                  zs++;
                  //�����ݿ����ѯ������
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
        out.println("}");
        if(i!=zs-1){
            out.println(",");
        }
    }
    out.println("]");
%>
