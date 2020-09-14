package com.tree.f2st.controller;

import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/webfist")
public class TreeController {

    @Autowired
    TreeService treeService;

    //모든 나무 조회
    @GetMapping(value="/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TreeEntity>> getAlltrees(){
        List<TreeEntity> tree = treeService.findAll();
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }

//    //나무 등록 번호로 하나의 나무 조회
//    @GetMapping(value="/{treeNo}", produces = { MediaType.APPLICATION_JSON_VALUE })
//    public ResponseEntity<TreeEntity> getTree(@PathVariable("treeNo") Long treeNo){
//        Optional<TreeEntity> tree = treeService.findbyTreeNo(treeNo);
//        return new ResponseEntity<TreeEntity>(tree.get(), HttpStatus.OK);
//    }

    @PostMapping("/save")
    public String save(@RequestBody TreeDTO tree){
        treeService.save(tree);
        return tree.toString();
    }



}