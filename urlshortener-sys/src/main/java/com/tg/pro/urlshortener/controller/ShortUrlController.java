package com.tg.pro.urlshortener.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tg.pro.urlshortener.enums.UrlGrade;
import com.tg.pro.urlshortener.model.UrlManager;
import com.tg.pro.urlshortener.pojo.vo.UrlBaseVo;
import com.tg.pro.urlshortener.service.UrlManagerService;
import com.tg.pro.urlshortener.utils.ShortUrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 短地址控制器
 */
@RestController
public class ShortUrlController {
    @Autowired
    UrlManagerService urlManagerService;
    @Autowired
    ShortUrlUtil shortUrlUtil;
    @Autowired
    RedisTemplate redisTemplate;

    @Value("${url.redis.key.pre}")
    private String urlPre;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{key}")
    public void redirect(
            @PathVariable("key") String key,
            HttpServletResponse response
    ) throws IOException {
        UrlBaseVo urlBaseVo;
        urlBaseVo = (UrlBaseVo) redisTemplate.opsForValue().get(urlPre + key);
        Optional<UrlBaseVo> optional = Optional.ofNullable(urlBaseVo);
        if (!optional.isPresent()) {
            urlBaseVo = urlManagerService.getUrlBaseInfo(key.trim());
            optional = Optional.ofNullable(urlBaseVo);
            if (!optional.isPresent()) {
                response.sendError(403, "URL解析错误");
                return;
            }
        }
        //更新view值
        urlBaseVo.setView(urlBaseVo.getView() + 1);
        UrlGrade urlGrade = UrlGrade.valueOf(UrlGrade.class, urlBaseVo.getGrade().toUpperCase());
        redisTemplate.opsForValue().set(urlPre + urlBaseVo.getId(),urlBaseVo,urlGrade.value(), TimeUnit.MINUTES);
        //重定向
        response.sendRedirect(urlBaseVo.getUrl());
    }

    /**
     * 生成短地址url
     *
     * @param longUrl 长地址
     * @param unicode 字符集
     * @param size    长度
     * @param grade   URL 访问等级 底low 中middle 高 high（定义key 缓存时间）
     * @param userUrl 用户自定义短地址
     * @param remark
     * @return
     */
    @PostMapping("/url/generate")
    public ResponseEntity generate(
            @RequestParam("longUrl")
                    String longUrl,
            @RequestParam(required = false, defaultValue = "0")
                    String unicode,
            @RequestParam(required = false, defaultValue = "6")
                    Integer size,
            @RequestParam(required = false)
                    String userUrl,
            @RequestParam(required = false, defaultValue = "low")
                    String grade,
            @RequestParam(required = false)
                    String remark,
            HttpServletRequest request
    ) throws IOException {
        Map<String, Object> map = new HashMap<>();
        UrlGrade urlGrade = UrlGrade.valueOf(UrlGrade.class, grade.toUpperCase());

        //参数格式判断
        if (!StringUtils.isEmpty(unicode)) {
            switch (unicode) {
                case "0":
                    unicode = "UTF-8";
                    break;
                case "1":
                    unicode = "GBK";
                    break;
                default:
                    map.put("msg", "错误的charset参数（支持UTF-8,GBK字符编码）：" + unicode);
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }

        if (!(6 <= size && size <= 50)) {
            map.put("msg", "size（6 <= size <= 50）：" + unicode);

            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        //判断url是否以http或http开头
        if (!(longUrl.startsWith("https://") || longUrl.startsWith("http://"))) {
            map.put("msg", "错误的URL：" + request.getQueryString());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        int count;
        String shortUrl;
        UrlManager urlManager;
        QueryWrapper<UrlManager> queryWrapper = new QueryWrapper<>();
        try {
            if (StringUtils.isEmpty(userUrl)) {
                while (true) {
                    //校检短地址是否重复
                    shortUrl = shortUrlUtil.generate(size, unicode);
                    count = urlManagerService.count(
                            queryWrapper.eq("id", shortUrl).eq("is_del", 0)
                    );
                    if (count == 0) {
                        break;
                    }
                }
            } else {
                shortUrl = userUrl;
            }
            //保存短地址信息
            urlManager = new UrlManager(shortUrl, longUrl, remark,grade.toUpperCase());
            if (!urlManagerService.save(urlManager)) {
                map.put("msg", "操作失败，请回滚重试");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("生成短地址失败 " + e.toString());
            map.put("msg", "系统错误,请稍后在试");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //存储redis 根据访问程度设置销毁时间
        redisTemplate.opsForValue().set(
                urlPre + shortUrl,
                new UrlBaseVo(shortUrl, longUrl, Long.valueOf(0),grade),
                urlGrade.value(),
                TimeUnit.MINUTES);
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        map.put("msg", tempContextUrl + shortUrl);
        return ResponseEntity.ok(map);
    }

    /**
     * 删除url
     *
     * @param id
     * @return
     */
    @GetMapping("/url/del")
    public ResponseEntity del(
            String id
    ) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            map.put("msg", "id参数不能为空");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if (urlManagerService.removeById(id)) {
            redisTemplate.delete(urlPre + id);
            map.put("msg", "操作成功");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("msg", "系统错误，稍后重试!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 跳转首页
     *
     * @return
     */
    @GetMapping("/url/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }


    /**
     * 获取我的url分页信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/url/view")
    public ModelAndView view(
            @RequestParam(name = "pageNum", defaultValue = "1", required = false)
                    Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false)
                    Integer pageSize
    ) {
        QueryWrapper<UrlManager> queryWrapper = new QueryWrapper<>();
        IPage<UrlManager> iPage = urlManagerService.page(new Page<>(pageNum, pageSize), queryWrapper.eq("is_del", 0));
        Map<String, Object> map = new HashMap<>();
        map.put("list", iPage.getRecords());
        map.put("current", iPage.getCurrent());
        map.put("size", iPage.getSize());
        map.put("total", iPage.getTotal());
        return new ModelAndView("url-view", map);
    }

}
