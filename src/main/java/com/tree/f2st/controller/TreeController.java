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

    @RequestMapping(value = "/map/{tid}")
    public String detail(Model model, @PathVariable("tid") String tid){
        model.addAttribute("treeList",treeService.findAll());
        model.addAttribute("detail", treeService.findByTid(tid));
        return "index.html";
    }


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



}