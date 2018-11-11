package com.codecorp.felipelima.envioemailauto;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    Button bt;

    javax.mail.Session session;

    String emailSender;
    String senhaSender;

    Context context;

    ProgressDialog pdialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        pdialog= new ProgressDialog(context);

        edit = findViewById(R.id.editText);
        bt = findViewById(R.id.button);

        emailSender = "fefe.lima.tf@gmail.com";
        //emailSender = "felipe.lima.silva@live.com";
        senhaSender= "47474108";
        //senhaSender= "fe47474108";

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Properties properties = new Properties();

                properties.put("mail.smtp.host","smtp.googlemail.com");
                properties.put("mail.smtp.socketFactory.port","465");
                properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.port","465"); //Envia de email gmail

                /*properties.put("mail.smtp.host","smtp.live.com");
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true"); //Envia email de outlook*/

                session = javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailSender, senhaSender);
                    }
                });

                pdialog.setMessage("Enviando e-mail");
                pdialog.setCancelable(false);
                pdialog.show();

                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {

                if (session != null){
                    javax.mail.Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailSender));
                    message.setSubject("Teste de envio de email automático");
                    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse("felipe.lima.silva@live.com"));
                    message.setContent(edit.getText().toString(),"text/html; charset=utf-8");
                    Transport.send(message);//apenas de email enviado pelo gmail

                    /*Transport transport = session.getTransport("smtp");
                    transport.connect("smtp.live.com",587,emailSender,senhaSender);
                    transport.sendMessage(message,message.getAllRecipients());
                    transport.close();//apenas de email enviado pelo outlook*/

                    String Teste;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),"Email enviado!",Toast.LENGTH_SHORT).show();
        }
    }
}
