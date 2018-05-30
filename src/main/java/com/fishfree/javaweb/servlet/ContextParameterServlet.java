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
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/28 17:37
 * @project javase
 */

@WebServlet(name = "contextParameterServlet", urlPatterns = "/contextParameter")
public class ContextParameterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用getInitParameter获取配置的初始化参数
        String param1 =req.getServletContext().getInitParameter("context_param1");
        String param2 =req.getServletContext().getInitParameter("context_param2");

        PrintWriter printWriter = resp.getWriter();
        printWriter.println("param1 "+param1);
        printWriter.println("param2 "+param2);
        printWriter.flush();
    }
}
