package com.tg.pro.urlshortener.service.impl;

import com.tg.pro.urlshortener.model.UrlManager;
import com.tg.pro.urlshortener.mapper.UrlManagerMapper;
import com.tg.pro.urlshortener.pojo.vo.UrlBaseVo;
import com.tg.pro.urlshortener.service.UrlManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Teng
 * @since 2019-04-10
 */
@Service
public class UrlManagerServiceImpl extends ServiceImpl<UrlManagerMapper, UrlManager> implements UrlManagerService {

    @Override
    public UrlBaseVo getUrlBaseInfo(String id) {
        return baseMapper.selectBaseInfoById(id);
    }

    @Override
    public void modifieldUrlViewInfo(UrlBaseVo urlBaseVo) {
        baseMapper.updateUrlBaseVoById(urlBaseVo);
    }
}
