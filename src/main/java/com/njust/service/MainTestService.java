package com.njust.service;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.InvalidNamePatternException;
import com.njust.Exception.NotFoundException;
import com.njust.po.TimeRange;
import com.njust.repo.MainTestRepo;
import com.njust.vo.MainTestVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MainTestService {
    @Autowired
    MainTestRepo mainTestRepo;

    public Page<MainTestVo> findAll(PageRequest pageRequest) {
        return mainTestRepo.findAll(pageRequest);
    }

    public Page<MainTestVo> findAllByTestStartRange(TimeRange timeRange, PageRequest pageRequest) {
        return mainTestRepo.findAllByTestStartBetween(timeRange.getStartTime(),
                timeRange.getEndTime(),
                pageRequest);
    }

    public MainTestVo addNewTest(MainTestVo mainTestVo)
            throws AlreadyExistsException, InvalidNamePatternException {
        if (mainTestVo.get_id() != null && mainTestRepo.findBy_id(mainTestVo.get_id()) != null) {
            throw new AlreadyExistsException("实验编号(内部编号)已存在，请联系开发人员");
        }

        if (StringUtils.isEmpty(mainTestVo.getTestId())) {
            throw new InvalidNamePatternException("实验编号格式错误");
        }

        if (mainTestRepo.findByTestId(mainTestVo.getTestId()) != null) {
            throw new AlreadyExistsException("实验编号已存在，请重新命名");
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

    public int getTestNum(long testStart) {
        DateTime dt = new DateTime(testStart);
        DateTime starDt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0, 0);
        DateTime endDt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 23, 59, 59);
//        log.info("start time: " + starDt);
//        log.info("end time: " + endDt);

        return mainTestRepo.countAllByTestStartBetween(starDt.getMillis(), endDt.getMillis());
    }
}
