package com.tree.f2st.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.TreeService;
import com.tree.f2st.util.ExcelUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/webfist")
public class TreeController {

    @Autowired
    TreeService treeService;

//-----Web
    @RequestMapping(value = "/map")
    public String home(Model model){
        model.addAttribute("treeList",treeService.findAll());
        return "index.html";
    }

    @RequestMapping(value = "/map/{tid}")
    public String detail(Model model, @PathVariable("tid") String tid){
        model.addAttribute("treeList",treeService.findAll());
        model.addAttribute("detail", treeService.findByTid(tid));
        return "index.html";
    }

    @RequestMapping(value = "/map/other/{value}")
    public String search(Model model, @PathVariable("value") String value){
        model.addAttribute("treeList",treeService.findAll());
        model.addAttribute("pval", value);
        return "index.html";
    }

//-----CRUD 외 Util
    //모든 나무 조회
    @GetMapping(value="/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TreeEntity>> getAlltrees(){
        List<TreeEntity> tree = treeService.findAll();
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }

    //나무 등록 번호로 하나의 나무 조회
    @GetMapping(value="/{tid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity< List<TreeEntity>> getTree(@PathVariable("tid") String tid){
        List<TreeEntity> tree = treeService.findByTid(tid);
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }

    @PostMapping("/save")
    public @ResponseBody String save(@RequestBody TreeDTO tree){
        String msg = treeService.save(tree);
        return msg;
    }

    //.xlsx file download
    @GetMapping(value = "/download")
    public ResponseEntity<Resource> getFile() {
        String filename = "fist.xlsx";
        InputStreamResource file = new InputStreamResource(treeService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @RequestMapping(value = "/delete/{tid}")
    public String delete(Model model, @PathVariable("tid") String tid){
        model.addAttribute("treeList",treeService.findAll());
        System.out.println("========= Delete ....... ============");
        treeService.deleteByTid(tid);
        System.out.println("========= Delete Success =============");
        return "redirect:/webfist/map";
    }

    @RequestMapping(value = "/map/search/{keyword}")
    public String searchDate(Model model,@PathVariable("keyword") String keyword){
        model.addAttribute("treeList",treeService.findAll());
        System.out.println("===== Search 시도 ======");
        String year = keyword.substring(0,2);
        String month = keyword.substring(2,4);
        String day = keyword.substring(4,6);

        System.out.println(" year: "+year+", month: "+month+", day: "+day);

        if(year.equals("00")) year = "__";
        if(month.equals("00")) month = "__";
        if(day.equals("00")) day = "__";

        System.out.println(" year: "+year+", month: "+month+", day: "+day);
        System.out.println();

        List<TreeEntity> slist = treeService.searchByTid(year,month,day);
        System.out.println("===== Search 컨트롤러 수행 완료 ======\n");

        slist.forEach(e-> System.out.println(e.getTid()));
        System.out.println("==== Result ====");
        model.addAttribute("searchList",slist);
        return "index.html";
    }

}