package com.itchina.service;

import com.itchina.dto.HouseDTO;
import com.itchina.form.DatatableSearch;
import com.itchina.form.HouseForm;
import com.itchina.form.RentSearch;
import com.itchina.service.search.constants.MapSearch;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public interface IHouseService {
    ServiceResult<HouseDTO> save(HouseForm houseForm);
    ServiceMultiResult<HouseDTO> admingQWuery(DatatableSearch searchBody);

    ServiceResult<HouseDTO> findCompleteOne(Long id);


    ServiceResult<HouseDTO> update(HouseForm houseForm);

    ServiceResult updateStatus(Long id,int status) throws Exception;

    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);

    ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch);
    /**
     * 精确范围数据查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch);

}
