import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author zhongping
 * @date 2017/3/27
 */
public class DemoTest {

   // @Test//发送没有附件的邮件
    public void send1() throws Exception{
        //跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 设置邮件服务器主机名
        p.setProperty("mail.host", "smtp.163.com");//指定邮件服务器，默认端口 25
        // 发送服务器需要身份验证
        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);

        // 开启debug调试，以便在控制台查看
        //session.setDebug(true);也可以这样设置
        //p.setProperty("mail.debug", "true");

        // 创建session
        Session session = Session.getDefaultInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //用户名可以用QQ账号也可以用邮箱的别名
                PasswordAuthentication pa = new PasswordAuthentication("penn2017@163.com", "callme49tj");
                // 后面的字符是授权码，用qq密码不行！！
                return pa;
            }
        });

        session.setDebug(true);//设置打开调试状态

        for (int i = 0; i <1; i++) {//发送几封邮件
            //声明一个Message对象(代表一封邮件),从session中创建
            MimeMessage msg = new MimeMessage(session);
            //邮件信息封装
            //1发件人
            msg.setFrom(new InternetAddress("penn2017@163.com"));
            //2收件人
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(
                    "529764948@qq.com"));
            //3邮件内容:主题、内容
            msg.setSubject("这是我用Java发来的邮件QQ....");
            //msg.setContent("Hello, 今天没下雨!!!", "text/plain;charset=utf-8");//纯文本
            msg.setContent(
                    "Hello <a href='http://www.baidu.com?id=ddd'>你好，快乐吗?<a/>",
                    "text/html;charset=utf-8");//发html格式的文本
            //发送动作
            Transport.send(msg);
        }
    }

   // @Test//发送含附件的邮件
    public void send2() throws Exception{
        //跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 开启debug调试，以便在控制台查看
        p.setProperty("mail.debug", "true");
        p.setProperty("mail.host", "smtp.163.com");//指定邮件服务器，默认端口 25
        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);

        // 创建session
        Session session = Session.getInstance(p);

        // 通过session得到transport对象
        Transport ts = session.getTransport();

        // 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
        ts.connect("smtp.163.com", "penn2017@163.com", "callme49tj");
        // 后面的字符是授权码，不能用qq密码

        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
        msg.setFrom( new InternetAddress("penn2017@163.com") );
        //2收件人
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("529764948@qq.com") );
        //3邮件内容:主题、内容
        msg.setSubject("这是我用Java发来的邮件--带附件的....");

        //添加附件部分
        //邮件内容部分1---文本内容
        MimeBodyPart body0 = new MimeBodyPart(); //邮件中的文字部分
        body0.setContent("这是两张<font color='red'>图片</font>....","text/html;charset=utf-8");

        //邮件内容部分2---附件1
        MimeBodyPart body1 = new MimeBodyPart(); //附件1
        File file=readFile("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491183237&di=df46381e1865a5aa50f2ebc906cbb771&imgtype=jpg&er=1&src=http%3A%2F%2Fimg002.21cnimg.com%2Fphotos%2Falbum%2F20160326%2Fm600%2FB920004B5414AE4C7D6F2BAB2966491E.jpeg");
        body1.setDataHandler( new DataHandler( new FileDataSource(file)) );//./代表项目根目录下

        body1.setFileName( MimeUtility.encodeText("中文1.jpg") );//中文附件名，解决乱码

        //邮件内容部分3---附件2
//        MimeBodyPart body2 = new MimeBodyPart(); //附件2
//        body2.setDataHandler( new DataHandler( new FileDataSource("./imgs/2.jpg")) );
//        body2.setFileName("2.jpg");

        //把上面的3部分组装在一起，设置到msg中
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(body0);
        mm.addBodyPart(body1);
       // mm.addBodyPart(body2);
        msg.setContent(mm);

        // 发送邮件
        ts.sendMessage(msg,msg.getAllRecipients());
        ts.close();
        file.delete();
    }

    public File  readFile(String param){
        BufferedImage image = null;
        String fileName=null;
        File file=null;
        try {
            URL url = new URL(param);
            image = ImageIO.read(url);


            fileName="E:\\testFile\\1.jpg";
            file= new File(fileName);
            ImageIO.write(image, "jpg", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
