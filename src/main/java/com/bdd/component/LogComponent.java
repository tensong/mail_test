package com.bdd.component;

import com.bdd.pojo.ExceptionParam;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author TenSong
 * @Date 2017/3/16 11:24
 */
@Component
public class LogComponent {
    @Cacheable(value = "exceptioncache", keyGenerator = "wiselyKeyGenerator")
    public <T> ExceptionParam findExceptionParam(Class<T> aClass, String msg, Exception e) {
        ExceptionParam param = new ExceptionParam(msg, e);
        log(aClass, msg, e);
        return param;
    }

    @CacheEvict(value = "exceptioncache", allEntries = true)
    public void removeCache(){}

    public  <T> void log(Class<T> aClass, String msg, Exception e) {
        Logger LOG = LoggerFactory.getLogger(aClass);
        LOG.error(msg, e);
        sendMail(msg, e, LOG);
    }

    private void sendMail(String msg, Exception e, Logger LOG) {
        Email email = new Email();

        String errorHTML = getErrorHTML(msg, e);

        email.setFromAddress("Administrator", "administrator@bdd.com");
        email.setReplyToAddress("Administrator", "administrator@bdd.com");
        email.addRecipient("tensong", "tensong@bdd.com", Message.RecipientType.TO);
        email.setSubject(msg);
        email.setText(msg);
        email.setTextHTML(errorHTML);

        ServerConfig config = new ServerConfig("192.168.1.145", 25, "tensong@bdd.com", "123456");
        Mailer mailer = new Mailer(config, TransportStrategy.SMTP_TLS, null);

        try {
            mailer.sendMail(email);
        } catch (Exception e1) {
            LOG.error("发送异常邮件失败", e1);
        }
    }

    private String getErrorHTML(String msg, Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>时刻：" + df.get().format(new Date(System.currentTimeMillis())) + "</p>");
        sb.append("<p>日志记录信息：" + msg + "</p>");
        sb.append("<p>错误原因：" + e.getMessage() + "</p>");
        List<StackTraceElement> stackTraceElements = Arrays.asList(e.getStackTrace());
        sb.append("异常信息栈：");

        sb.append("<table border=\"1\">");
        sb.append("<tr>");
        sb.append("<th>类</th>");
        sb.append("<th>方法</th>");
        sb.append("<th>行</th>");
        sb.append("</tr>");
        for (StackTraceElement element : stackTraceElements) {
            sb.append("<tr>");
            sb.append("<td>" + element.getClassName() + "</td>");
            sb.append("<td>" + element.getMethodName() + "</td>");
            sb.append("<td>" + element.getLineNumber() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
}
