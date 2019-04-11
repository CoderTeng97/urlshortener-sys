package com.tg.pro.urlshortener.mapper;

import com.tg.pro.urlshortener.model.UrlManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tg.pro.urlshortener.pojo.vo.UrlBaseVo;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Teng
 * @since 2019-04-10
 */
public interface UrlManagerMapper extends BaseMapper<UrlManager> {
    UrlBaseVo selectBaseInfoById(String id);

    void updateUrlBaseVoById(
            @Param("urlBaseVo")
            UrlBaseVo urlBaseVo
    );
}
