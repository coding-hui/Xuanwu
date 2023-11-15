package top.wecoding.xuanwu.codegen.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.codegen.repository.TableInfoRepository;
import top.wecoding.xuanwu.codegen.service.GeneratorService;
import top.wecoding.xuanwu.codegen.service.TemplateFactory;
import top.wecoding.xuanwu.codegen.service.TemplateService;
import top.wecoding.xuanwu.core.util.ArgumentAssert;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

import static top.wecoding.xuanwu.core.exception.SystemErrorCode.DATA_NOT_EXIST;

/**
 * @author wecoding
 * @since 0.9
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final TemplateFactory templateFactory;

	private final TableInfoRepository tableInfoRepository;

	@Override
	public void generator(Long tableId) {
		Optional<TableEntity> tableInfo = tableInfoRepository.findById(tableId);
		if (tableInfo.isEmpty()) {
			ArgumentAssert.error(DATA_NOT_EXIST);
		}
		TemplateService tplSvc = templateFactory.create(tableInfo.get().getTplCategory());
		tplSvc.renderToFile(tableInfo.get());
	}

	@Override
	public byte[] download(Long tableId) {
		Optional<TableEntity> tableInfo = tableInfoRepository.findById(tableId);
		if (tableInfo.isEmpty()) {
			ArgumentAssert.error(DATA_NOT_EXIST);
		}
		TemplateService tplSvc = templateFactory.create(tableInfo.get().getTplCategory());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		tplSvc.renderToZipStream(tableInfo.get(), zip);
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	@Override
	public Map<String, String> preview(Long tableId) {
		Optional<TableEntity> tableInfo = tableInfoRepository.findById(tableId);
		if (tableInfo.isEmpty()) {
			ArgumentAssert.error(DATA_NOT_EXIST);
		}
		TemplateService tplSvc = templateFactory.create(tableInfo.get().getTplCategory());
		return tplSvc.render(tableInfo.get());
	}

}
