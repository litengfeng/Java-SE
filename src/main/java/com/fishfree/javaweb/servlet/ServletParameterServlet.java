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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/28 18:10
 * @project javase
 */
//使用注解方式
@WebServlet(
        name = "servletParameterServlet",
        urlPatterns = {"/servletParameter"},
        initParams = {@WebInitParam(name = "server", value = "mysql")
                , @WebInitParam(name = "database", value = "user")}
)
public class ServletParameterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用servletConfig类进行获取
        ServletConfig sc = this.getServletConfig();
        String server = sc.getInitParameter("server");
        String database = sc.getInitParameter("database");
        resp.getWriter().println("server : [" + server + "] database : [" + database + "]");
    }
}
