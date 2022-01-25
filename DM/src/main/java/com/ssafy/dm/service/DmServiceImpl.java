package com.ssafy.dm.service;

import com.ssafy.dm.dto.DmDto;
import com.ssafy.dm.entity.DmEntity;
import com.ssafy.dm.entity.UserEntity;
import com.ssafy.dm.repository.DmRepository;
import com.ssafy.dm.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DmServiceImpl implements DmService{

    @Autowired
    DmRepository dmRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<DmEntity> findDm(Long id) {
        Optional<DmEntity> dmEntity = dmRepository.findById(id);
        return Optional.of(dmEntity.get());
    }

    @Override
    public List<DmEntity> findAllDm(Long id) {

        List<DmEntity> list = new ArrayList<>();
        List<DmEntity> list2 = new ArrayList<>();

        dmRepository.findAll().forEach(e -> list.add(e));

        int i = 0;
        while(i < list.size()) {

//            System.out.println(list.get(i).getReceiverId().getUser_id());
//            System.out.println(list.get(i).toString());
            if(list.get(i).getReceiverId().getUser_id() == null) {
                i = i+1;
                continue;
            }
            else if(list.get(i).getReceiverId().getUser_id().equals(id)) {
                list2.add(list.get(i));
                i = i+1;
            }
            else {
                i = i+1;
                continue;
            }
        }
        return list2;
    }


    @Override
    @Transactional
    public DmEntity sendDm(DmDto dmDto) {
        Optional<UserEntity> sender = userRepository.findById(dmDto.getSenderId());
        Optional<UserEntity> receiver = userRepository.findById(dmDto.getReceiverId());

        DmEntity dm = new DmEntity();
        BeanUtils.copyProperties(dmDto, dm);
        dm.setReceiverId(receiver.get());
        dm.setSenderId(sender.get());

        return dmRepository.save(dm);
    }

    @Override
    @Transactional
    public int deleteDm(Long id) {
        Optional<DmEntity> dmEntity = dmRepository.findById(id);

        if(dmEntity.isPresent()) {
            dmRepository.delete(dmEntity.get());
            return 1;
        }
        return 0;
    }
}
