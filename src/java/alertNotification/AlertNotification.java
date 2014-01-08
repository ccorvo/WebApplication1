/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alertNotification;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author ccorvo
 */
@Stateless
public class AlertNotification {

    public AlertNotification() {
    }

    private Message createJMSMessageForjmsNotificationServiceTopic(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

    public void sendJMSMessageToNotificationServiceTopic(Object messageData) throws NamingException, JMSException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("java:comp/env/jms/NotificationServiceTopicFactory");
        
        Connection conn = null;
        Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("java:comp/env/jms/NotificationServiceTopic");
            MessageProducer mp = s.createProducer(destination);
            mp.send(createJMSMessageForjmsNotificationServiceTopic(s, messageData));
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (JMSException e) {
                    System.out.println("CORVO:: JMS Exception in sendJMSNotification");
                    
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    
  
    
    
    
}
