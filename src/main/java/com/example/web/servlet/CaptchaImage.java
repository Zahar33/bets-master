package com.example.web.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet("/captcha-image")
public class CaptchaImage extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/jpg");

        int iTotalChars = 6;

        int iHeight = 40;
        int iWidth = 150;

        Font fntStyle1 = new Font("Arial", Font.BOLD, 30);
        Font fntStyle2 = new Font("Verdana", Font.BOLD, 20);

        Random randChars = new Random();
        String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);

        BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
        g2dImage.fillRect(0, 0, iWidth, iHeight);
        int iCircle = 15;

        for (int i = 0; i < iCircle; i++){
            g2dImage.setColor(new Color(randChars.nextInt(210), randChars.nextInt(185), randChars.nextInt(165)));
            int iRadius = (int) (Math.random() * iHeight / 2.0);
            int iX = (int) (Math.random()) * iWidth - iRadius;
            int iY = (int) (Math.random() * iHeight - iRadius);
        }
        g2dImage.setFont(fntStyle1);
        for (int i = 0; i < iTotalChars; i++) {
            g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            if(i % 2 == 0) {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 24);
            } else {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 35);
            }
        }


        OutputStream osImage = resp.getOutputStream();
        ImageIO.write(biImage, "jpeg", osImage);

        g2dImage.dispose();

        HttpSession session = req.getSession();
        session.setAttribute("captcha_security", sImageCode);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
