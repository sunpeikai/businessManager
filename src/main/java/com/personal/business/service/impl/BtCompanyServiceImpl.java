package com.personal.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.business.entity.BtCompany;
import com.personal.business.mapper.BtCompanyMapper;
import com.personal.business.request.CompanyRequest;
import com.personal.business.service.IBtCompanyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author spk
 * @since 2019-07-31
 */
@Service
public class BtCompanyServiceImpl extends ServiceImpl<BtCompanyMapper, BtCompany> implements IBtCompanyService {

    /**
     * @description 获取未被
     * @auth sunpeikai
     * @param
     * @return
     */
    @Override
    public IPage<BtCompany> getAllCompany(CompanyRequest companyRequest) {
        QueryWrapper<BtCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select().orderByDesc(BtCompany::getCreateTime)
                .and(!StringUtils.isEmpty(companyRequest.getFullName()),obj->obj.like(BtCompany::getFullName,companyRequest.getFullName()))
                .and(!StringUtils.isEmpty(companyRequest.getCreditCode()),obj->obj.like(BtCompany::getCreditCode,companyRequest.getCreditCode()))
                .and(!StringUtils.isEmpty(companyRequest.getCompanyCode()),obj->obj.like(BtCompany::getCompanyCode,companyRequest.getCompanyCode()))
                .and(companyRequest.getType()!=null,obj->obj.eq(BtCompany::getType,companyRequest.getType()))
                .and(companyRequest.getStatus()!=null,obj->obj.eq(BtCompany::getStatus,companyRequest.getStatus()));
        return page(new Page<>(companyRequest.getPage(),companyRequest.getLimit()),queryWrapper);
    }

    /**
     * @description 检查selfId是否重复
     * @auth sunpeikai
     * @param
     * @return
     */
    @Override
    public boolean checkExist(Integer selfId) {
        QueryWrapper<BtCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select().and(obj->obj.eq(BtCompany::getSelfId,selfId));
        return list(queryWrapper).size()>0;
    }

    /**
     * @description 根据selfId获取所有子节点
     * @auth sunpeikai
     * @param
     * @return
     */
    @Override
    public List<BtCompany> getAllChildren(Integer parentId) {
        QueryWrapper<BtCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select().and(obj->obj.eq(BtCompany::getParentId,parentId));
        return list(queryWrapper);
    }
}
