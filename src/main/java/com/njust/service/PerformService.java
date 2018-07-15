package com.njust.service;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.repo.PerformTrtRepo;
import com.njust.vo.BaseMongoDbVo;
import com.njust.vo.PerformTrtVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PerformService {
    @Autowired
    PerformTrtRepo trtRepo;

    public <T extends BaseMongoDbVo> Page<T> findAll(PageRequest pageRequest, T t) {
        if (t instanceof PerformTrtVo) {
            return (Page<T>) trtRepo.findAll(pageRequest);
        }

        throw new RuntimeException("未知的性能指标:" + t.toString());
    }

//    public Page<PerformTrtVo> findAllByCreateTimeRange(TimeRange timeRange, PageRequest pageRequest) {
//        return sampleRepo.findAllByCreateTimeBetween(timeRange.getStartTime(),
//                timeRange.getEndTime(),
//                pageRequest);
//    }
//
    public <T extends BaseMongoDbVo> T addNewPerform(T t) throws AlreadyExistsException {
        if (t instanceof PerformTrtVo) {
            PerformTrtVo me = (PerformTrtVo) t;
            if (trtRepo.findBy_id(me.get_id()) != null) {
                throw new AlreadyExistsException("performance already exists");
            }
            me.set_id(null);
            return (T) trtRepo.save(me);
        }

        throw new RuntimeException("未知的性能指标:" + t.toString());
    }

    public <T extends BaseMongoDbVo> T updatePerform(T t) throws NotFoundException {
        if (t instanceof PerformTrtVo) {
            PerformTrtVo me = (PerformTrtVo) t;
            PerformTrtVo oldMe = trtRepo.findBy_id(me.get_id());
            if (oldMe == null) {
                throw new NotFoundException("performance not found");
            }
            return (T)trtRepo.save(me);
        }

        throw new RuntimeException("未知的性能指标:" + t.toString());
    }

    public <T extends BaseMongoDbVo> boolean deletePerform(T t) {
        if (t instanceof PerformTrtVo) {
            PerformTrtVo me = (PerformTrtVo) t;
            trtRepo.delete(me.get_id());
            return trtRepo.findBy_id(me.get_id()) == null;
        }
        throw new RuntimeException("未知的性能指标:" + t.toString());
    }

//    public PerformTrtVo findById(String id) {
//        return sampleRepo.findBy_id(id);
//    }
}
