package com.njust.service;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.repo.MainTestRepo;
import com.njust.vo.MainTestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MainTestService {
    @Autowired
    MainTestRepo mainTestRepo;

    public Page<MainTestVo> findAll(PageRequest pageRequest) {
        return mainTestRepo.findAll(pageRequest);
    }

    public MainTestVo addNewTest(MainTestVo mainTestVo) throws AlreadyExistsException {
        if (mainTestVo.get_id() != null && mainTestRepo.findBy_id(mainTestVo.get_id()) != null) {
            throw new AlreadyExistsException("test already exists");
        }

        return mainTestRepo.insert(mainTestVo);
    }

    public boolean deleteTest(MainTestVo mainTestVo) {
        mainTestRepo.delete(mainTestVo.get_id());
        return mainTestRepo.findBy_id(mainTestVo.get_id()) == null;
    }

    public MainTestVo updateTest(MainTestVo mainTestVo) throws NotFoundException {
        MainTestVo oldTest = mainTestRepo.findBy_id(mainTestVo.get_id());
        if (oldTest == null) {
            throw new NotFoundException("test " + mainTestVo.getTestId() + " not found");
        }

        // keep create time
        mainTestVo.setCreateTime(oldTest.getCreateTime());

        return mainTestRepo.save(mainTestVo);
    }
}
