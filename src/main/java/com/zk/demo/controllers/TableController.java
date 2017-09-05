package com.zk.demo.controllers;

import com.zk.demo.entities.Table;
import com.zk.demo.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
@RequestMapping("/table")
@Controller
public class TableController {

    @Autowired
    private TableService tableService;

    private Logger log = Logger.getLogger("TableController");

    @RequestMapping("/toAddTable.do")
    public String toAddTable() {
        return "addTable";
    }

    @RequestMapping("/addTable.do")
    public String addTable(Table table) {
        Integer result = tableService.addTable(table);
        log.info("添加表" + result + "个");
        return "redirect:/device/toMenu.do";
    }

    @RequestMapping("/getTableList.do")
    @ResponseBody
    public Object getTableList() {
        return tableService.getTableList();
    }


    @RequestMapping("/toUpdateTable.do")
    public ModelAndView toUpdateTable(String id) {
        Table table = tableService.getTableById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("table",table);
        mv.setViewName("updateTable");
        return mv;
    }

    @RequestMapping("/updateTable.do")
    public String updateTable(Table table) {
        Integer result = tableService.updateTable(table);
        log.info("更新表的条数:" + result);
        return "redirect:/device/toMenu.do";
    }


    @RequestMapping("/deleteTable.do")
    @ResponseBody
    public Object deleteTable(String id) {
        return tableService.deleteTableById(id);
    }

    @RequestMapping("/toDetail.do")
    public String toDetail(String id, Model model) {
        Table table = tableService.getTableById(id);
        model.addAttribute("table",table);
        return "tableDetail";
    }

}
