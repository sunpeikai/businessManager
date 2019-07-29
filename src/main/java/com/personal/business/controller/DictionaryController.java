/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.business.controller;

import com.alibaba.fastjson.JSON;
import com.personal.business.Enum.ResultEnum;
import com.personal.business.base.BaseController;
import com.personal.business.base.Return;
import com.personal.business.constant.CommonConstant;
import com.personal.business.dto.DictionaryExportDto;
import com.personal.business.entity.BtDictionary;
import com.personal.business.service.IBtDictionaryService;
import com.personal.business.utils.CommonUtils;
import com.personal.business.utils.EasyPoiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sunpeikai
 * @version DictionaryController, v0.1 2019/7/29 15:30
 * @description
 */
@Slf4j
@Controller
@RequestMapping(value = "/system/dictionary")
public class DictionaryController extends BaseController {

    @Autowired
    private IBtDictionaryService iBtDictionaryService;

    @GetMapping(value = "/init")
    public String init(){
        return "iframe/dictionaryManager";
    }

    @GetMapping(value = "/getAllDics")
    @ResponseBody
    public Return getAllDic(){
        return Return.data(iBtDictionaryService.getAllDictionary());
    }

    /**
     * @description 更新字典
     * @auth sunpeikai
     * @param
     * @return
     */
    //@RequiresPermissions(ShiroPermissionsConstant.PERM_MENU_EDIT)
    @PostMapping(value = "/update")
    @ResponseBody
    public Return update(@RequestBody BtDictionary dictionary){
        log.info(JSON.toJSONString(dictionary));
        boolean success = iBtDictionaryService.updateById(dictionary);
        if(success){
            return Return.success();
        }else{
            return Return.fail("更新失败");
        }
    }

    /**
     * @description 插入菜单
     * @auth sunpeikai
     * @param
     * @return
     */
    //@RequiresPermissions(ShiroPermissionsConstant.PERM_MENU_ADD)
    @PostMapping(value = "/insert")
    @ResponseBody
    public Return insert(@RequestBody BtDictionary dictionary){
        log.info("insert dictionary [{}]",JSON.toJSONString(dictionary));
        dictionary.setDelFlag(0);
        dictionary.setCreateTime(LocalDateTime.now());
        dictionary.setUpdateTime(LocalDateTime.now());
        boolean success = iBtDictionaryService.save(dictionary);
        if(success){
            return Return.success();
        }else{
            return Return.fail("添加失败");
        }
    }

    /**
     * @description 删除菜单
     * @auth sunpeikai
     * @param
     * @return
     */
    //@RequiresPermissions(ShiroPermissionsConstant.PERM_MENU_DEL)
    @GetMapping(value = "/delete/{ids}")
    @ResponseBody
    public Return delete(@PathVariable String ids){
        if(!StringUtils.isEmpty(ids)){
            List<String> idsArray = new ArrayList<>();
            Collections.addAll(idsArray, ids.split(","));
            log.info("delete dictionary [{}]",idsArray);
            boolean success = iBtDictionaryService.removeByIds(idsArray);
            if(success){
                return Return.success();
            }else{
                return Return.fail("删除失败");
            }
        }
        return Return.fail(ResultEnum.ERROR_PARAM_NOT_ENOUGH);
    }

    /**
     * @description 导出
     * @auth sunpeikai
     * @param
     * @return
     */
    //@RequiresPermissions(ShiroConstants.ORDER_EXPORT_PERM)
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) {
        log.info("export excel");
        List<BtDictionary> result = iBtDictionaryService.getAllDictionary();

        if (CollectionUtils.isNotEmpty(result)){
            List<DictionaryExportDto> exportDtos = CommonUtils.convertBeanList(result,DictionaryExportDto.class);
            // 导出数据
            EasyPoiUtil.exportExcel(exportDtos,"数据字典","第一页", DictionaryExportDto.class,"数据字典.xls",response) ;
        }
    }
}
