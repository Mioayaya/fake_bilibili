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
        String videoname = request.getParameter("videoname");
        videoname=URLDecoder.decode(videoname,"utf-8");
// data class JJVideo(var image:String,var jj:String,var part1:String,var part2:String,var part3:String)
        String image = ""; // ���
        String jj = "";
        String part1 = "";
        String part2 = "";
        String part3 = "";
        // Ѱ�ҷ�˿����ע������Ϊ�Լ���Ҳ���Ǵ��ݹ�����userName
        // Ȼ�󰤸��ҵ���˿���ٴ�user���������Ӧ����Ϣ
        Class.forName("com.mysql.cj.jdbc.Driver");                                          //�������ݿ�����
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //ָ�����ݿ�table1
        String username1 = "root";  //���ݿ��û���
        String password1 = "root";  //���ݿ��û�����
        Connection conn = DriverManager.getConnection(url, username1, password1);  //�������ݿ�
        if(conn!=null){
            Statement stmt = null;
            ResultSet rs = null;
            String sql ="select * from animation where anim_name = '"+videoname+"' ";  //��ѯ���
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                        image = rs.getString("anim_jj_image");
                        jj = rs.getString("anim_jj_text");
                        part1 = rs.getString("anim_jj_part1");
                        part2 = rs.getString("anim_jj_part2");
                        part3 = rs.getString("anim_jj_part3");
            }
        }
%>

<%
    out.println("[");
        out.println("{");
        out.println("\"image\":\""+image+"\"");
        out.println(",\"jj\":\""+jj+"\"");
        out.println(",\"part1\":\""+part1+"\"");
        out.println(",\"part2\":\""+part2+"\"");
        out.println(",\"part3\":\""+part3+"\"");
        out.println("}");
    out.println("]");
%>
