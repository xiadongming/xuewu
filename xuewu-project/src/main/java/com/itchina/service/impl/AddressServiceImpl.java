package com.itchina.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itchina.dto.SubwayDTO;
import com.itchina.dto.SubwayStationDTO;
import com.itchina.dto.SupportAddressDTO;
import com.itchina.entity.Subway;
import com.itchina.entity.SubwayStation;
import com.itchina.entity.SupportAddress;
import com.itchina.repository.SubwayReposity;
import com.itchina.repository.SubwayStationReposity;
import com.itchina.repository.SupportAddressReposity;
import com.itchina.security.AuthProvider;
import com.itchina.service.IAddressService;
import com.itchina.service.ServiceMultiResult;
import com.itchina.service.ServiceResult;
import com.itchina.service.search.constants.BaiduMapLocation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
@Service
public class AddressServiceImpl implements IAddressService {

    final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private SubwayStationReposity subwayStationReposity;

    @Autowired
    private SupportAddressReposity supportAddressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SubwayReposity subwayReposity;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String baidu_map_key = "GQuFLXH1yqsbjGFGYQzSyljZuSUUY4Wu";

    private static final String baidu_map_geoconv_api = "http://api.map.baidu.com/geocoding/v3/?";


    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        logger.info("Level.CITY.getValue()=  " + SupportAddress.Level.CITY.getValue());
        List<SupportAddress> allSupportAddress = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());
        List<SupportAddressDTO> supportAddressDTOList = new ArrayList<>();
        //同BeanUtils.copyProperties()功能
        for (SupportAddress supportAddress : allSupportAddress) {
            SupportAddressDTO supportAddressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);
            supportAddressDTOList.add(supportAddressDTO);
        }
        return new ServiceMultiResult<>(supportAddressDTOList.size(), supportAddressDTOList);
    }

    @Override
    public ServiceMultiResult<SupportAddressDTO> findRegionsByCityName(String cityName) {
        List<SupportAddress> supportAddressList = supportAddressRepository.findAllByBelongTo(cityName);
        List<SupportAddressDTO> supportAddressDTOList = new ArrayList<>();
        for (SupportAddress supportAddress : supportAddressList) {
            SupportAddressDTO supportAddressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);
            supportAddressDTOList.add(supportAddressDTO);
        }
        return new ServiceMultiResult<>(supportAddressDTOList.size(), supportAddressDTOList);
    }

    @Override
    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();

        SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY
                .getValue());
        SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());

        result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDTO.class));
        result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDTO.class));
        return result;
    }

    @Override
    public ServiceResult<SubwayDTO> findSubway(Integer subwayId) {
        if (subwayId == null) {
            return ServiceResult.notFound();
        }
        Subway subway = subwayReposity.findOne(subwayId);
        if (subway == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(subway, SubwayDTO.class));
    }

    @Override
    public ServiceResult<SubwayStationDTO> findSubwayStation(Integer stationId) {
        if (stationId == null) {
            return ServiceResult.notFound();
        }
        SubwayStation station = subwayStationReposity.findOne(stationId);
        if (station == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(station, SubwayStationDTO.class));
    }

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName) {
        if (cityName == null) {
            return new ServiceMultiResult<>(0, null);
        }

        List<SupportAddressDTO> result = new ArrayList<>();

        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION
                .getValue(), cityName);
        for (SupportAddress region : regions) {
            result.add(modelMapper.map(region, SupportAddressDTO.class));
        }
        return new ServiceMultiResult<>(regions.size(), result);
    }

    @Override
    public ServiceResult<SupportAddressDTO> findCity(String cityEnName) {
        if (cityEnName == null) {
            return ServiceResult.notFound();
        }
        SupportAddress supportAddress = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        if (supportAddress == null) {
            return ServiceResult.notFound();
        }
        SupportAddressDTO addressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);
        return ServiceResult.of(addressDTO);
    }

    @Override
    public ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city, String address) {

        //通过http发送请求的时候，需要进行转码
        String encodeAddress = null;
        String encodeCity = null;
        try {
            encodeAddress = URLEncoder.encode(address, "UTF-8");
            encodeCity = URLEncoder.encode(city, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            logger.error("http转码失败");
            e.printStackTrace();
            return new ServiceResult<BaiduMapLocation>(false, "失败");
        }
        //发送请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder stringBuilder = new StringBuilder(baidu_map_geoconv_api);
        stringBuilder.append("address=").append(encodeAddress)
                .append("&").append("city=").append(encodeCity)
                .append("&").append("output=json&")
                .append("ak=").append(baidu_map_key);


        HttpGet get = new HttpGet(stringBuilder.toString());
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("状态码不正确");
                return  new ServiceResult<BaiduMapLocation>(false,"状态码不正确");
            }
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            JsonNode jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();
            if(0 != status){
                return new ServiceResult<BaiduMapLocation>(false,"objectMapper得到的状态码不对");
            }
            BaiduMapLocation baiduMapLocation = new BaiduMapLocation();
            JsonNode jsonLocation = jsonNode.get("result").get("location");
            baiduMapLocation.setLongitude(jsonLocation.get("lng").asDouble());
            baiduMapLocation.setLatitude(jsonLocation.get("lat").asDouble());
            return ServiceResult.of(baiduMapLocation);
        } catch (IOException e) {
            logger.error("http发送失败");
            e.printStackTrace();
        }


        return null;
    }


}
