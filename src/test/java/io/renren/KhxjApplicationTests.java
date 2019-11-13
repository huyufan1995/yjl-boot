package io.renren;

import io.renren.api.dto.ApplyEntityDto;
import io.renren.cms.dao.ApplyDao;
import io.renren.cms.service.ApplyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KhxjApplicationTests {

    @Autowired
    private ApplyDao applyDao;
    @Test
    public void test1(){
        ApplyEntityDto allById = applyDao.findAllById("1");
        System.out.println(allById);
    }
}