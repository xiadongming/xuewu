package com.itchina;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itchina.XuewuProjectApplicationTests;
import com.itchina.service.search.ISearchService;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @Author: xiadongming
 * @Date: 2020/8/18 21:30
 */
public class SearchServiceTest extends XuewuProjectApplicationTests {

    @Autowired
    private ISearchService searchService;

    @Test
    public void contextLoads() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("name1","12121");
        stringStringHashMap.put("name2","23323");
        System.out.println("stringStringHashMap= " + stringStringHashMap);
        System.out.println("json= " + JSONObject.fromObject(stringStringHashMap).toString());
    }

}
