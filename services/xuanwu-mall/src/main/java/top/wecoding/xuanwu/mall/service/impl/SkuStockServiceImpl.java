package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.mall.domain.entity.SkuStock;
import top.wecoding.xuanwu.mall.repository.SkuStockRepository;
import top.wecoding.xuanwu.mall.service.SkuStockService;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * 菜品 Sku - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-21 21:21:55
 */
@Service
@RequiredArgsConstructor
public class SkuStockServiceImpl extends BaseServiceImpl<SkuStock, Long>
    implements SkuStockService {

  private final SkuStockRepository skuStockRepository;

  @Override
  protected JpaRepository<SkuStock, Long> getBaseRepository() {
    return this.skuStockRepository;
  }
}
