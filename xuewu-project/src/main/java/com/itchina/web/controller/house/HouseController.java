package com.itchina.web.controller.house;

import com.itchina.base.ApiResponse;
import com.itchina.base.RentValueBlock;
import com.itchina.dto.*;
import com.itchina.entity.SupportAddress;
import com.itchina.form.RentSearch;
import com.itchina.service.*;
import com.itchina.service.search.ISearchService;
import com.itchina.service.search.constants.HouseBucketDTO;
import com.itchina.service.search.constants.MapSearch;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
@Controller
public class HouseController {
    @Autowired
    private IAddressService addressService;
    @Autowired
    private ISubwayService subwayService;
    @Autowired
    private ISubwayStationService subwayStationService;
    @Autowired
    private IHouseService houseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISearchService searchService;

    /**
     * 自动补全功能，ajax请求
     */

    @RequestMapping(value = "/rent/house/autocomplete", method = RequestMethod.GET)
    @ResponseBody
    private ApiResponse autocomplate(@RequestParam(value = "prefix") String prefix) {
        if (null == prefix) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
        ServiceResult<List<String>> suggest = searchService.suggest(prefix);
        return ApiResponse.ofSuccess(suggest.getResult());
    }


    @RequestMapping(value = "/address/support/cities", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getSupportCities() {
        ServiceMultiResult<SupportAddressDTO> allCities = addressService.findAllCities();
        if (null == allCities || allCities.getResultSize() == 0) {
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }
        return ApiResponse.ofSuccess(allCities.getResult());
    }

    @RequestMapping(value = "/address/support/regions", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getSupportRegions(@RequestParam("city_name") String cityName) {

        ServiceMultiResult<SupportAddressDTO> allRegions = addressService.findRegionsByCityName(cityName);

        return ApiResponse.ofSuccess(allRegions.getResult());
    }

    @RequestMapping(value = "/address/support/subway/line", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getSupportLine(@RequestParam("city_name") String cityEnName) {
        subwayService.findAllSunwayByCityEnName(cityEnName);
        ServiceMultiResult<SubwayDTO> allSunwayByCityEnName = subwayService.findAllSunwayByCityEnName(cityEnName);
        return ApiResponse.ofSuccess(allSunwayByCityEnName.getResult());
    }

    @RequestMapping(value = "/address/support/subway/station", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getSupportSubwayStation(@RequestParam("subway_id") String subwayId) {
        ServiceMultiResult<SubwayStationDTO> bySubwayId = subwayStationService.findBySubwayId(subwayId);


        return ApiResponse.ofSuccess(bySubwayId.getResult());
    }

    @RequestMapping(value = "/rent/house", method = RequestMethod.GET)
    public String rentHousePage(@ModelAttribute RentSearch rentSearch, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        //城市信息必须含有
        if (null == rentSearch.getCityEnName()) {
            String cityEnNameSession = (String) session.getAttribute("cityEnName");
            if (null == cityEnNameSession) {
                redirectAttributes.addAttribute("msg", "必须选择一个城市");
            } else {
                rentSearch.setCityEnName(cityEnNameSession);
            }
            //将城市信息放到session中
        } else {
            session.setAttribute("cityEnName", rentSearch.getCityEnName());
        }
        ServiceResult<SupportAddressDTO> city = addressService.findCity(rentSearch.getCityEnName());
        if (!city.isSuccess()) {
            redirectAttributes.addAttribute("msg", "must_chose_city");
            return "redirect:/index";
        }
        model.addAttribute("currentCity", city.getResult());

        ServiceMultiResult<SupportAddressDTO> resultSupportAddress = addressService.findAllRegionsByCityName(rentSearch.getCityEnName());
        if (null == resultSupportAddress.getResult() || resultSupportAddress.getTotal() < 1) {
            redirectAttributes.addAttribute("msg", "必须选择一个城市");
            return "redirect:/index";
        }
        ServiceMultiResult<HouseDTO> serviceMultiResult = houseService.query(rentSearch);

        model.addAttribute("total", serviceMultiResult.getTotal());
        //model.addAttribute("total", 0);
        model.addAttribute("houses", serviceMultiResult.getResult());
        if (null == rentSearch.getRegionEnName()) {
            rentSearch.setRegionEnName("*");
        }
        model.addAttribute("searchBody", rentSearch);
        model.addAttribute("regions", resultSupportAddress.getResult());
        model.addAttribute("priceBlocks", RentValueBlock.PRICE_BLOCK);
        model.addAttribute("areaBlocks", RentValueBlock.AREA_BLOCK);
        //查询出的数据的区间
        model.addAttribute("currentPriceBlock", RentValueBlock.matchPrice(rentSearch.getPriceBlock()));
        model.addAttribute("currentAreaBlock", RentValueBlock.matchArea(rentSearch.getAreaBlock()));

        return "rent-list";
    }

    @RequestMapping(value = "/rent/house/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable(value = "id") Long houseId, Model model) {
        if (houseId <= 0) {
            return "404";
        }
        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(houseId);
        if (!serviceResult.isSuccess()) {
            return "404";
        }
        HouseDTO houseDTO = serviceResult.getResult();
        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());
        SupportAddressDTO city = addressMap.get(SupportAddress.Level.CITY);
        SupportAddressDTO region = addressMap.get(SupportAddress.Level.REGION);
        model.addAttribute("city", city);
        model.addAttribute("region", region);
        ServiceResult<UserDTO> userDTOServiceResult = userService.findById(houseDTO.getAdminId());
        model.addAttribute("agent", userDTOServiceResult.getResult());
        model.addAttribute("house", houseDTO);
        //聚合数据
        ServiceResult<Long> aggResult = searchService.aggregateDistrictHouse(city.getEnName(), region.getEnName(), houseDTO.getDistrict());

        model.addAttribute("houseCountInDistrict", aggResult.getResult());
        return "house-detail";
    }

    //地图找房接口
    @RequestMapping(value = "/rent/house/map", method = RequestMethod.GET)
    public String rentMapPage(@RequestParam(value = "cityEnName") String cityEnName, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {

        ServiceResult<SupportAddressDTO> city = addressService.findCity(cityEnName);
        if (!city.isSuccess()) {
            redirectAttributes.addAttribute("msg", "查询地址信息失败");
            return "redirect:/index";
        } else {
            httpSession.setAttribute("cityName", cityEnName);
            model.addAttribute("city", city.getResult());
        }
        ServiceMultiResult<SupportAddressDTO> allRegions = addressService.findAllRegionsByCityName(cityEnName);

        ServiceMultiResult<HouseBucketDTO> houseBucketDTOServiceMultiResult = searchService.mapAggregate(cityEnName);

        model.addAttribute("total", houseBucketDTOServiceMultiResult.getTotal());
        model.addAttribute("aggData", houseBucketDTOServiceMultiResult.getResult());

        model.addAttribute("regions", allRegions.getResult());
        return "rent-map";
    }

    @RequestMapping(value = "/rent/house/map/houses", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse rentMapHouses(@ModelAttribute MapSearch mapSearch) {

        ServiceMultiResult<HouseDTO> houseDTOServiceMultiResult = houseService.wholeMapQuery(mapSearch);
        ApiResponse response = ApiResponse.ofSuccess(houseDTOServiceMultiResult.getResult());
        response.setMore(houseDTOServiceMultiResult.getTotal() > (mapSearch.getStart() + mapSearch.getSize()));
        return response;

    }


}
