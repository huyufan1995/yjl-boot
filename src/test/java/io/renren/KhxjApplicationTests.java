package io.renren;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.collect.Maps;
import io.renren.api.dto.ApplyEntityDto;
import io.renren.api.dto.VerifyApplyDto;
import io.renren.api.vo.ApiResult;
import io.renren.cms.dao.ApplyDao;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;
import io.renren.cms.service.VerifyRecordService;
import lombok.experimental.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KhxjApplicationTests {

    @Autowired
    private ApplyDao applyDao;

    @Autowired
    private VerifyRecordService verifyRecordService;
/*
    @Test
    public void test1(){
        Calendar calendar = Calendar.getInstance();
        List<ApplyEntity> applyEntityList = verifyRecordService.queryVerifyRecord("ozu7j5ABMGynz8Sbn1AtikKYLytU");
        List<Map> dataList = new ArrayList<>();
        String year = "";
        List<String> yearList = new ArrayList<>();
        for (ApplyEntity c : applyEntityList) {
            calendar.setTime(c.getEndTime());					//放入Date类型数据
            if (!year.equals(Calendar.YEAR)) {
                year = Calendar.YEAR+"";
                yearList.add(year);
            }
        }
        for (int i = 0; i < yearList.size(); i++) {
            Map<String, Object> map = new HashMap<>();//装的是code和name
            Map<String, Object> dataMap = new HashMap<>();//装year和list
            List<Map> list = new ArrayList<>();
            year = yearList.get(i);
            for (ApplyEntity c : applyEntityList) {
                calendar.setTime(c.getEndTime());
                if (year.equals(Calendar.YEAR)) {
                    map.put("code", c.getCode());
                    map.put("name", c.getName());
                    list.add(map);
                }
            }
            dataMap.put("year", year);
            dataMap.put("list", list);
            dataList.add(dataMap);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dataList", dataList);
    }
*/

  /*  @Test
    public void test2(){
        Calendar calendar = Calendar.getInstance();
        List<ApplyEntity> applyEntityList = verifyRecordService.queryVerifyRecord("ozu7j5ABMGynz8Sbn1AtikKYLytU");
        List<VerifyApplyDto> verifyApplyDtoList = new ArrayList<>();
        for (ApplyEntity entity : applyEntityList) {
            VerifyApplyDto verifyApplyDto = new VerifyApplyDto();
            BeanUtil.copyProperties(entity, verifyApplyDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            verifyApplyDtoList.add(verifyApplyDto);
        }
        for (VerifyApplyDto verifyApplyDto : verifyApplyDtoList) {
            calendar.setTime(verifyApplyDto.getEndTime());					//放入Date类型数据
            verifyApplyDto.setKey(calendar.get(Calendar.YEAR)+"");
        }
        Set<String> keys = verifyApplyDtoList.stream().map(VerifyApplyDto::getKey).collect(Collectors.toSet());
        Set<String> sort = new TreeSet<String>();
        sort.addAll(keys);
        HashMap<String,List<VerifyApplyDto>> dtoMap = Maps.newHashMap();
        sort.forEach(item ->{
            dtoMap.put(item,verifyApplyDtoList.stream().filter(b -> b.getKey().equals(item)).collect(Collectors.toList()));
        });
        System.out.println(1);
    }*/
}