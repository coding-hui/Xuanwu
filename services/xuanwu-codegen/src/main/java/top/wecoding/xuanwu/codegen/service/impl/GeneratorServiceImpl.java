package top.wecoding.xuanwu.codegen.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.codegen.service.GeneratorService;
import top.wecoding.xuanwu.codegen.service.TableInfoService;
import top.wecoding.xuanwu.codegen.service.TemplateFactory;
import top.wecoding.xuanwu.codegen.service.TemplateService;

/**
 * @author wecoding
 * @since 0.9
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

  private final TemplateFactory templateFactory;

  private final TableInfoService tableInfoService;

  @Override
  public void generator(Long tableId) {
    TableEntity tableInfo = tableInfoService.getTableInfo(tableId);
    TemplateService tplSvc = templateFactory.create(tableInfo.getTplCategory());
    tplSvc.renderToFile(tableInfo);
  }

  @Override
  public byte[] download(Long tableId) {
    TableEntity tableInfo = tableInfoService.getTableInfo(tableId);
    TemplateService tplSvc = templateFactory.create(tableInfo.getTplCategory());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zip = new ZipOutputStream(outputStream);
    tplSvc.renderToZipStream(tableInfo, zip);
    IOUtils.closeQuietly(zip);
    return outputStream.toByteArray();
  }

  @Override
  public Map<String, String> preview(Long tableId) {
    TableEntity tableInfo = tableInfoService.getTableInfo(tableId);
    TemplateService tplSvc = templateFactory.create(tableInfo.getTplCategory());
    return tplSvc.render(tableInfo);
  }
}
