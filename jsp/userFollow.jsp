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

        String isME = "false";
        String Username = "";                                 // ��ǰ�û���Ҳ���Ƿ�˿
        String userName = request.getParameter("userName");   // ��������name��Ҳ���Ǳ���ע��
        String follow = request.getParameter("follow");
        String Flag = "false";  // �Ƿ��Ѿ�����
        userName=URLDecoder.decode(userName,"utf-8");
        follow=URLDecoder.decode(follow,"utf-8");
    
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

        Statement stmtA = null;
        ResultSet rsA = null;
        String sqlA ="select * from  user_follow where user_follow_name = '"+userName+"' ";  //��ѯ���
        stmtA = conn.createStatement();
        rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
            if(Objects.equals(rsA.getString("user_fans_name"), Username)){
                Flag = "true";  //ͬʱ���ڹ�ע�ߺͷ�˿������
            }
        }

    // �����ж�follw������
    // follow��flase ��ʾû�й�ע ���Ծ��й�ע���ж��ǲ����Լ���
    // follow��true ��ʾ��ע�ˣ�����ȡ��
        if(Objects.equals(follow,"false")){  // û�й�ע ���ڽ��й�ע
            // ���ж��ǲ����Լ�
            if(Objects.equals(Username,userName)){   // ������Լ�
                isME = "true";      
            }else{   // ��������Լ�������һ��
                // �ж��Ƿ��Ѿ����ڹ�ע��
                if(Objects.equals(Flag,"true")){
                    String sqlP = "UPDATE user_follow set flag = 'true'  where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";
                    PreparedStatement pstP =null;
                    pstP = conn.prepareStatement(sqlP);
                    pstP.executeUpdate();
                }  else{  // ������ѡ�����
                    String sql2 = "INSERT  INTO  user_follow (user_follow_name,user_fans_name,flag) VALUES (?,?,?)";
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement(sql2);
                    pst.setString(1, userName);
                    pst.setString(2, Username);
                    pst.setString(3, "true");
                    pst.executeUpdate();
                }
                // ��Ȼ�Ѿ���ע�ˣ���ô��עѡ��ѡ�� �Ѿ���ע״̬
                follow = "true";  // �Ѿ���ע״̬
                // ��Ȼ��ע�ˣ���ô��ע�����ͷ�˿����++
                // �ҵ�up��--����ע�� userName fens++
                // �ҵ���˿ Username follow++
                // fens ++
                String sqlfens = "UPDATE user set user_fans = user_fans+1  where user_name = '"+userName+"' ";
                PreparedStatement pstfens =null;
                pstfens = conn.prepareStatement(sqlfens);
                pstfens.executeUpdate();
                // follow ++
                String sqlfollow = "UPDATE user set user_follow = user_follow+1  where user_name = '"+Username+"' ";
                PreparedStatement pstfollow =null;
                pstfollow = conn.prepareStatement(sqlfollow);
                pstfollow.executeUpdate();
            }
        }else{   // ��ע�ˣ�����ȡ��
                // ��ôȡ���أ��ҵ���ע�ߺͷ�˿������flagΪfalse
                String sqlP = "UPDATE user_follow set flag = 'false'  where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";
                PreparedStatement pstP =null;
                pstP = conn.prepareStatement(sqlP);
                pstP.executeUpdate();
                // ��Ȼ�Ѿ�ȡ���ˣ�����Ϊ���Թ�ע
                follow = "false";  // δ��ע״̬
                // ȡ���� ���˷�˿��-- �Լ���ע����--
                // ������˭�أ��Ǳ���ע��
                // ��user�����ҵ�userName fens--
                // ��user�����ҵ�Username follow--
                String sqlFens = "UPDATE user set user_fans = user_fans-1  where user_name = '"+userName+"' ";
                PreparedStatement pstFens =null;
                pstFens = conn.prepareStatement(sqlFens);
                pstFens.executeUpdate();
                // ��ע��--
                String sqlFollow = "UPDATE user set user_follow = user_follow-1  where user_name = '"+Username+"' ";
                PreparedStatement pstFollow =null;
                pstFollow = conn.prepareStatement(sqlFollow);
                pstFollow.executeUpdate();
        }
    %>
    
<%


    out.println("[");
        out.println("{");
        out.println("\"isME\":\"" + isME + "\",");
        out.println("\"follow\":\"" + follow + "\"");
        out.println("}");


    out.println("]");
%>
