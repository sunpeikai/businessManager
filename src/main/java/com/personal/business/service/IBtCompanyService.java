package com.personal.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.personal.business.entity.BtCompany;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.business.request.CompanyRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author spk
 * @since 2019-07-31
 */
public interface IBtCompanyService extends IService<BtCompany> {

    /**
     * @description 获取未被
     * @auth sunpeikai
     * @param
     * @return
     */
    IPage<BtCompany> getAllCompany(CompanyRequest companyRequest);
}