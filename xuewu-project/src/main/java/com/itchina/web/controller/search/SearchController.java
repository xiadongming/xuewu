package com.itchina.web.controller.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itchina.form.RentSearch;
import com.itchina.service.search.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiadongming
 * @Date: 2020/8/18 21:32
 */
@RestController
public class SearchController {
    @Autowired
    private ISearchService searchService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public void searchHouse() {
        try {
            searchService.index(27);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteHouse() {
        searchService.remove(27);
    }

    @RequestMapping(value = "/termQuery", method = RequestMethod.GET)
    public void queryHouse() {
        RentSearch rentSearch = new RentSearch();
        rentSearch.setSize(0);
        rentSearch.setSize(10);
        rentSearch.setCityEnName("bj");
        searchService.query(rentSearch);
    }


}
