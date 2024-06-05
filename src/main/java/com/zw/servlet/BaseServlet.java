package com.zw.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    public Integer page;
    public Integer pageSize;

    public abstract void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num = req.getParameter("num");
        page = 1;
        if (num != null) {
            page = Integer.valueOf(num);
        }
        String size = req.getParameter("size");
        pageSize = 8;
        if (size != null) {
            pageSize = Integer.valueOf(size);
        }
        execute(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        this.doGet(req, resp);
    }
}
