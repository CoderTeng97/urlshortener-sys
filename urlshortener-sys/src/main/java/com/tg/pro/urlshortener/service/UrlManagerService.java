package com.tg.pro.urlshortener.service;

import com.tg.pro.urlshortener.model.UrlManager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tg.pro.urlshortener.pojo.vo.UrlBaseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Teng
 * @since 2019-04-10
 */
public interface UrlManagerService extends IService<UrlManager> {
    /**
     * 获取URl的基本信息
     * @param id
     * @return
     */
    UrlBaseVo getUrlBaseInfo(String id);

    void modifieldUrlViewInfo(UrlBaseVo urlBaseVo);
}
