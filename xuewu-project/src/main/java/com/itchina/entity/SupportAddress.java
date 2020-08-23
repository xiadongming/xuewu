package com.itchina.entity;


import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
@Entity
@Table(name = "support_address")
public class SupportAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "belong_to")
    private String belongTo;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "cn_name")
    private String cnName;

    private String level;

    @Column(name = "baidu_map_lng")
    private double baiduMapLongtitue;
    @Column(name = "baidu_map_lat")
    private  double baiduMapLatitude;


    public void setBaiduMapLongtitue(double baiduMapLongtitue) {
        this.baiduMapLongtitue = baiduMapLongtitue;
    }

    public void setBaiduMapLatitude(double baiduMapLatitude) {
        this.baiduMapLatitude = baiduMapLatitude;
    }

    public double getBaiduMapLongtitue() {
        return baiduMapLongtitue;
    }

    public double getBaiduMapLatitude() {
        return baiduMapLatitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public String getEnName() {
        return enName;
    }

    public String getCnName() {
        return cnName;
    }

    public String getLevel() {
        return level;
    }

    /**
     * 行政级别
     **/
    public enum Level {
        CITY("city"),
        REGION("region");
        private String value;

        Level(String value) {
            this.value = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Level of(String value) {
            for (Level level : Level.values()) {
                if (level.getValue().equals(value)) {
                    return level;
                }
            }
            throw new IllegalArgumentException();
        }

    }
}
