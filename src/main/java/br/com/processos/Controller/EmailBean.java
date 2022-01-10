package br.com.processos.Controller;

import br.com.processos.Modelo.Email;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ManagedBean
public class EmailBean extends Email {

    public void sendMail() {

        SolicitanteBean sol = new SolicitanteBean();

        this.setTo(sol.retornarEmail(this.getNome()));

            /**
             * Parâmetros de conexão com servidor Gmail
             */
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");

            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    EmailBean email;
                    email = new EmailBean();
                    email.setFrom("");
                    email.setPassword("");

                    return new PasswordAuthentication(email.getFrom(), email.getPassword());
                }
            }
            );

            /**
             * Ativa Debug para sessão
             */
            session.setDebug(true);

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("viniciusparaense92@gmail.com")); //Remetente

                Address[] toUser = InternetAddress //Destinatário(s)
                        .parse(this.getTo());

                message.setRecipients(Message.RecipientType.TO, toUser);
                message.setSubject(this.getSubject());//Assunto
                message.setText(this.getText());
                /**
                 * Método para enviar a mensagem criada
                 */
                Transport.send(message);

                System.out.println("Feito!!!");

            } catch (MessagingException e) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Solicitante nao encontrado: ", this.getNome()));
                
                throw new RuntimeException(e);
            }
        }

}
