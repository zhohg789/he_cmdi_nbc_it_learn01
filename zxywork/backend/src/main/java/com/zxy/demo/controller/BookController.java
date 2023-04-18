package com.zxy.demo.controller;

import com.zxy.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:zff
 * @Description:Todo
 * @Date:2020/4/19
 */
@Controller
public class BookController {

    //通过注解将bookService注入进来
    @Resource
    BookService bookService;

    //通过注解添加对外接口获取xml数据
    @PostMapping("/getXmlData")
    @ResponseBody
    public List getData(){
        return bookService.getXmlData();
    }
    //通过注解添加对外接口解析并插入xml数据
    @PostMapping("/addXmlData")
    @ResponseBody
    public String addData(){
        bookService.addXmlData();
        return "success";
    }


}
