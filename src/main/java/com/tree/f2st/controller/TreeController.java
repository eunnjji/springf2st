package com.tree.f2st.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.TreeRepository;
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
import javax.swing.plaf.synth.SynthTabbedPaneUI;
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

    //나무 등록 번호로 하나의 나무 조회
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

    @RequestMapping(value = "/search/date/{keyword}")
    public String searchDate(Model model,@PathVariable("keyword") String keyword){
        model.addAttribute("treeList",treeService.findAll());
        System.out.println("===== Search 시도 ======");
        List<TreeEntity> slist = treeService.searchByTid(keyword);
        System.out.println("===== Search 컨트롤러 수행 완료 ======\n");
        slist.forEach(e-> System.out.println(e.getTid()));
        System.out.println("==== Result ====");
        model.addAttribute("searchList",slist);
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(Model model,@ModelAttribute TreeDTO tree){
        System.out.println("===== Data Receive ======");
        System.out.println(tree.getTid());
        System.out.println(tree.getDist());
        System.out.println(tree.getDbh());
        System.out.println("===== Update =====");
        treeService.updateById(tree.getTid(),tree);
    }

}