package com.tree.f2st.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/webfist")
public class TreeController {

    @Autowired
    TreeService treeService;

    @RequestMapping(value = "/map")
    public String home(Model model){
        model.addAttribute("treeList",treeService.findAll());
        return "index.html";
    }

    //모든 나무 조회
    @GetMapping(value="/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TreeEntity>> getAlltrees(){
        List<TreeEntity> tree = treeService.findAll();
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }
//    @GetMapping(value="/", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public @ResponseBody String getAlltrees(Model model){
//        List<TreeEntity> treeList = treeService.findAll();
//        model.addAttribute("treeList",treeList);
//        return Integer.toString(treeList.size());
//    }

//    //나무 등록 번호로 하나의 나무 조회
//    @GetMapping(value="/{treeNo}", produces = { MediaType.APPLICATION_JSON_VALUE })
//    public ResponseEntity<TreeEntity> getTree(@PathVariable("treeNo") Long treeNo){
//        Optional<TreeEntity> tree = treeService.findbyTreeNo(treeNo);
//        return new ResponseEntity<TreeEntity>(tree.get(), HttpStatus.OK);
//    }
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



}