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
        String Username = "";                                 // 当前用户，也就是粉丝
        String userName = request.getParameter("userName");   // 传过来的name，也就是被关注者
        String follow = request.getParameter("follow");
        String Flag = "false";  // 是否已经存在
        userName=URLDecoder.decode(userName,"utf-8");
        follow=URLDecoder.decode(follow,"utf-8");
    
        Class.forName("com.mysql.cj.jdbc.Driver");  //加载数据库驱动
        String url = "jdbc:MySQL://localhost:3306/lesson?characterEncoding=utf-8"; //指向数据库table1
        String username1 = "root";  //数据库用户名
        String password1 = "root";  //数据库用户密码
        Connection conn = DriverManager.getConnection(url, username1, password1);  //连接数据库

        // 找到当前username
        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql1 ="select * from user where user_flag = 'true'";  //查询语句
        stmt1 = conn.createStatement();
        rs1 = stmt1.executeQuery(sql1);
        while (rs1.next()) {
            Username = rs1.getString("user_name");
        }

        Statement stmtA = null;
        ResultSet rsA = null;
        String sqlA ="select * from  user_follow where user_follow_name = '"+userName+"' ";  //查询语句
        stmtA = conn.createStatement();
        rsA = stmtA.executeQuery(sqlA);
        while (rsA.next()) {
            if(Objects.equals(rsA.getString("user_fans_name"), Username)){
                Flag = "true";  //同时存在关注者和粉丝，存在
            }
        }

    // 首先判断follw的内容
    // follow是flase 表示没有关注 可以经行关注，判断是不是自己？
    // follow是true 表示关注了，经行取关
        if(Objects.equals(follow,"false")){  // 没有关注 现在进行关注
            // 先判断是不是自己
            if(Objects.equals(Username,userName)){   // 如果是自己
                isME = "true";      
            }else{   // 如果不是自己经行下一步
                // 判断是否已经存在关注了
                if(Objects.equals(Flag,"true")){
                    String sqlP = "UPDATE user_follow set flag = 'true'  where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";
                    PreparedStatement pstP =null;
                    pstP = conn.prepareStatement(sqlP);
                    pstP.executeUpdate();
                }  else{  // 不存在选择插入
                    String sql2 = "INSERT  INTO  user_follow (user_follow_name,user_fans_name,flag) VALUES (?,?,?)";
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement(sql2);
                    pst.setString(1, userName);
                    pst.setString(2, Username);
                    pst.setString(3, "true");
                    pst.executeUpdate();
                }
                // 既然已经关注了，那么关注选项选择 已经关注状态
                follow = "true";  // 已经关注状态
                // 既然关注了，那么关注数量和粉丝数量++
                // 找到up主--被关注者 userName fens++
                // 找到粉丝 Username follow++
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
        }else{   // 关注了，现在取关
                // 怎么取关呢，找到关注者和粉丝，设置flag为false
                String sqlP = "UPDATE user_follow set flag = 'false'  where user_follow_name = '"+userName+"' and user_fans_name = '"+Username+"' ";
                PreparedStatement pstP =null;
                pstP = conn.prepareStatement(sqlP);
                pstP.executeUpdate();
                // 既然已经取关了，设置为可以关注
                follow = "false";  // 未关注状态
                // 取关了 本人粉丝数-- 自己关注数量--
                // 本人是谁呢，是被关注者
                // 在user表中找到userName fens--
                // 在user表中找到Username follow--
                String sqlFens = "UPDATE user set user_fans = user_fans-1  where user_name = '"+userName+"' ";
                PreparedStatement pstFens =null;
                pstFens = conn.prepareStatement(sqlFens);
                pstFens.executeUpdate();
                // 关注数--
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
