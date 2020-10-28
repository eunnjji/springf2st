package com.tree.f2st.service;

import com.sun.source.tree.Tree;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.TreeRepository;
import com.tree.f2st.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//service - 비즈니스 로직 구현, repository 또는 controller에 data 전달
@AllArgsConstructor
@Service
public class TreeService {
    @Autowired
    private TreeRepository treeRepository;

    public List<TreeEntity> findAll() {
        List<TreeEntity> trees = new ArrayList<>();
        treeRepository.findAll().forEach(e->trees.add(e));
        return trees;
    }

    @Transactional
    public String save(TreeDTO treeDTO)
    {
        return treeRepository.save(treeDTO.toEntity()).toString();
    }

    public  List<TreeEntity> findByTid(String tid){
        List<TreeEntity> t = new ArrayList<>();
        treeRepository.findByTid(tid).forEach(e->t.add(e));
        return t;
    }

    //@Query 방식으로 업데이트 하므로, Modifying 어노테이션 추가함
    //error
    //#1. Not supported for DML operations --> 레포지토리 코드에 @modifying을 붙여야 함
    //#2. 한번 수정 완료하면 다시 수정이 안되는 경우
    // --> 그대로 수정할 경우 계속 수정 폼이 등장하나, 한번 기존 값과 다르게 저장할 경우 이후 2번째부터
    //    변경 폼이 등장하지 않음 --> 원인은 강제로 flush();를 호출했기 때문에
    // --> @Transactional 어노테이션을 service 쪽에 붙여서 해결함
    // --> @tran~~ 는 method 실행이 끝나면 트랜잭션을 커밋해주는 어노테이션임, 주로 서비스계층에서 사용
    //      강제로 flush(); + 엉뚱하게 레포지토리의 query에 trnasactional 어노테이션을 붙여서
    //      정상적으로 수행이 되지 않았던 것..
    @Transactional
    public void updateById(String tid, TreeDTO tree){
        List<TreeEntity> t= treeRepository.findByTid(tid);
        t.forEach(e->treeRepository.updateByTid(e.getTid(),
                tree.getDist(),tree.getDbh(),tree.getHeight(),tree.getLatitude(),tree.getLongitude())
        );
        //treeRepository.flush();
        //t.forEach(e->treeRepository.save(e)); --> 이것만 하면 제대로 수정이 안됨

    }

    public void deleteByTid(String tid){
        List<TreeEntity> trees = treeRepository.findByTid(tid);
        trees.forEach(e->treeRepository.delete(e));
        treeRepository.flush();
    }


    public ByteArrayInputStream load() {
        List<TreeEntity> trees = treeRepository.findAll();
        ByteArrayInputStream in = ExcelUtil.ListToExcelFile(trees);
        return in;
    }

    // jpa에서 mysql wildcard '_'가 정식으로 주어지지 않는 문제
    // repository 에서 @query 어노테이션 에 파라미터 ?1을 활용함
    // 성공적으로 검색 수행
    public List<TreeEntity> searchByTid(String keyword){
        String year = keyword.substring(0,2);
        String month = keyword.substring(2,4);
        String day = keyword.substring(4,6);

        System.out.println(" year: "+year+", month: "+month+", day: "+day);

        if(year.equals("00")) year = "__";
        if(month.equals("00")) month = "__";
        if(day.equals("00")) day = "__";

        System.out.println(" year: "+year+", month: "+month+", day: "+day);
        System.out.println();


        List<TreeEntity> t = new ArrayList<>();
        treeRepository.findByTidStartingWith(year+month+day+"%").forEach(e->t.add(e));
        return t;
    }

}
