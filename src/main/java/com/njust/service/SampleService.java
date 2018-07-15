package com.njust.service;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.repo.SampleRepo;
import com.njust.vo.SampleVo;
import com.njust.vo.TimeRangeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SampleService {
    @Autowired
    SampleRepo sampleRepo;

    public Page<SampleVo> findAll(PageRequest pageRequest) {
        return sampleRepo.findAll(pageRequest);
    }

    public Page<SampleVo> findAllByCreateTimeRange(TimeRangeVo timeRange, PageRequest pageRequest) {
        return sampleRepo.findAllByCreateTimeBetween(timeRange.getStartTime(),
                timeRange.getEndTime(),
                pageRequest);
    }

    public SampleVo addNewSample(SampleVo sampleVo) throws AlreadyExistsException {
        if (sampleRepo.findBy_id(sampleVo.get_id()) != null) {
            throw new AlreadyExistsException("sample already exists");
        }

        sampleVo.set_id(null);
        return sampleRepo.save(sampleVo);
    }

    public SampleVo updateSample(SampleVo sampleVo) throws NotFoundException {
        SampleVo oldSample = sampleRepo.findBy_id(sampleVo.get_id());
        if (oldSample == null) {
            throw new NotFoundException("sample not found");
        }
        // keep the create time
        sampleVo.setCreateTime(oldSample.getCreateTime());
        return sampleRepo.save(sampleVo);
    }

    public boolean deleteSample(SampleVo sampleVo) {
        sampleRepo.delete(sampleVo.get_id());
        return sampleRepo.findBy_id(sampleVo.get_id()) == null;
    }

    public SampleVo findById(String id) {
        return sampleRepo.findBy_id(id);
    }
}
