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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author litengfeng
 * @version 1.0
 * @date 2018/5/24 16:36
 * @project javase
 */

//所有的http请求的servlet都继承HttpServlet
public class FirstServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("servlet init method invoke");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理http请求是get方法的
        //直接返回first servlet
        resp.getWriter().println("first servlet");
        System.out.println("doGet method invoke");
        System.out.println("request URL :" + req.getRequestURL());
        System.out.println("request URI :" + req.getRequestURI());
        System.out.println();
    }

    @Override
    public void destroy() {
        System.out.println("destroy method invoke");
    }
}
