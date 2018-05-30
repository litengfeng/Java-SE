/*
 * -------------------------------------------------------------------------------------
 *    Mi-Me Confidential
 *
 *    Copyright (C) 2016 Shanghai Mi-Me Financial Information Service Co., Ltd.
 *    All rights reserved.
 *
 *   No part of this file may be reproduced or transmitted in any form or by any means,
 *    electronic, mechanical, photocopying, recording, or otherwise, without prior
 *    written permission of Shanghai Mi-Me Financial Information Service Co., Ltd.
 * -------------------------------------------------------------------------------------
 */
package com.fishfree.javaweb.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 使用注解的方式进行处理
 * 不需要再web.xml中配置
 *
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/28 17:02
 * @project javase
 */


@WebServlet(name = "secondServlet", urlPatterns = {"/greet", "/helloServlet"}, loadOnStartup = 1)
public class SecondServlet extends HttpServlet {

    private static final String DEFAULT_USER = "guest";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        if (user == null) {
            user = DEFAULT_USER;
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        Writer writer = resp.getWriter();
        writer.append("<!DOCTYPE html>\r\n");
        writer.append("<html>\r\n");
        writer.append("<head>\r\n");
        writer.append("<title>hello user application</title>\r\n");
        writer.append("</head>\r\n");

        writer.append("<body>\r\n");
        writer.append("hello user ").append(user).append("! <br/>\r\n");
        writer.append("<form action = \"greet\" method=\"post\"> \r\n");
        writer.append("enter your name : <br/>\r\n");
        writer.append("<input type=\"text\" name=\"user\" /> <br/>\r\n");
        writer.append("<input type=\"submit\" value=\"submit\" /> \r\n");

        writer.append("</form>\r\n");
        writer.append("</body>\r\n");

        writer.append("</html>\r\n");

        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
