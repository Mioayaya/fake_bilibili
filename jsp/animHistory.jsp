<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="com.mysql.cj.jdbc.Driver" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.mysql.cj.util.DnsSrv" %>
<%@ page import="javax.swing.text.StyledEditorKit" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<html>
<!-- name sign grade-->
    <%

        String revise = "true";
       
        String animName = request.getParameter("animName");
        String animTime = request.getParameter("animTime");
        animName=URLDecoder.decode(animName,"utf-8");
        animTime=URLDecoder.decode(animTime,"utf-8");
  
        String flag = "false";

        String anim_passionate = "";
        String anim_funny = "";
        String anim_fighting = "";
        String anim_daily = "";
        String anim_campus = "";
        String anim_cure = "";
        String anim_love = "";

        
  /* 
        String animName = "���綥��İ�ɱ��,ת����Ϊ���������";
        String animTime = "2021-12-20 23:27:23";
  */
        String description = "";
        String image = "";
        int userId = 0;


        Class.forName("com.mysql.cj.jdbc.Driver");  //�������ݿ�����
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //ָ�����ݿ�table1
        String username1 = "root";  //���ݿ��û���
        String password1 = "root";  //���ݿ��û�����
        Connection conn = DriverManager.getConnection(url, username1, password1);  //�������ݿ�
//�ҵ���Ƶ���
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select * from animation where anim_name = '"+animName+"' ";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            description = rs.getString("anim_description");
            image = rs.getString("anim_image");
        }
        
// �ҵ��û�id

        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user where user_flag = 'true'";  //��ѯ���
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);
        while (rs1.next()) {
            userId = rs1.getInt("user_id");
        }
       

// ����������

        Statement stmtA = null;
        ResultSet rsA = null;
        String sqlA ="select * from  history";  //��ѯ���
        stmtA = conn.createStatement();
        rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
            if(Objects.equals(rsA.getString("anim_name"), animName)){
                if(Objects.equals(rsA.getInt("user_id"), userId)){
                    flag = "true";
                }
            }
        }

        if(Objects.equals(flag, "true")){  // �ظ���
            String sqlT = "UPDATE history set history_time = '"+animTime+"'  where anim_name = '"+animName+"' and user_id = '"+userId+"' ";
            PreparedStatement pstT =null;
            pstT = conn.prepareStatement(sqlT);
            pstT.executeUpdate();
        }else{
            String sql2 = "INSERT  INTO  history (user_id,anim_name,anim_description,history_time,anim_image) VALUES (?,?,?,?,?)";
            PreparedStatement pst = null;
            pst = conn.prepareStatement(sql2);
            pst.setInt(1, userId);
            pst.setString(2, animName);
            pst.setString(3, description);
            pst.setString(4, animTime);
            pst.setString(5, image);
            pst.executeUpdate();
        }

//  ��ѯ�÷���ı�ǩ

        Statement stmtLab = null;
        ResultSet rsLab = null;
        String sqlLab = "select * from animation where anim_name = '"+animName+"' ";
        stmtLab = conn.createStatement();
        rsLab = stmtLab.executeQuery(sqlLab);
        while (rsLab.next()) {
            anim_passionate = rsLab.getString("anim_passionate");
            anim_funny = rsLab.getString("anim_funny");
            anim_fighting = rsLab.getString("anim_fighting");
            anim_daily = rsLab.getString("anim_daily");
            anim_campus =rsLab.getString("anim_campus");
            anim_cure = rsLab.getString("anim_cure");
            anim_love = rsLab.getString("anim_love");
        }
//  ����user
//anim_passionate
        if(Objects.equals(anim_passionate, "1")){  
            String sqlP = "UPDATE user set anim_passionate = anim_passionate+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }
//anim_funny
         if(Objects.equals(anim_funny, "1")){  
            String sqlP = "UPDATE user set anim_funny = anim_funny+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }
//anim_fighting
         if(Objects.equals(anim_fighting, "1")){  
            String sqlP = "UPDATE user set anim_fighting = anim_fighting+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }
//anim_daily
         if(Objects.equals(anim_daily, "1")){  
            String sqlP = "UPDATE user set anim_daily = anim_daily+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }        
//anim_campus
         if(Objects.equals(anim_campus, "1")){  
            String sqlP = "UPDATE user set anim_campus = anim_campus+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }
//anim_cure
         if(Objects.equals(anim_cure, "1")){  
            String sqlP = "UPDATE user set anim_cure = anim_cure+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }
//anim_love
         if(Objects.equals(anim_love, "1")){  
            String sqlP = "UPDATE user set anim_love = anim_love+1  where user_id = '"+userId+"' ";
            PreparedStatement pstP =null;
            pstP = conn.prepareStatement(sqlP);
            pstP.executeUpdate();
        }

        String sqlP = "UPDATE user set user_watch = user_watch+1  where user_id = '"+userId+"' ";
        PreparedStatement pstP =null;
        pstP = conn.prepareStatement(sqlP);
        pstP.executeUpdate();
    %>
    
<%


    out.println("[");


        out.println("{");
        out.println("\"revise\":\"" + revise + "\"");
        out.println("}");


    out.println("]");
%>


</html>