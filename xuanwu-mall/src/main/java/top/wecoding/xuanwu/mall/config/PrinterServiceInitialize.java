package top.wecoding.xuanwu.mall.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.mall.repository.PrinterRepository;

import javax.print.PrintService;
import java.awt.print.PrinterJob;
import java.util.Arrays;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrinterServiceInitialize implements ApplicationRunner {

	private final PrinterRepository printerRepository;

	@Override
	@SneakyThrows
	public void run(ApplicationArguments args) {
		PrintService[] printServices = PrinterJob.lookupPrintServices();

		List<String> printerServiceNames = Arrays.stream(printServices).map(PrintService::getName).toList();

		for (String printerServiceName : printerServiceNames) {
			if (printerRepository.existsByName(printerServiceName)) {
				log.info("SalesTicketPrinter [{}] has been initialize.", printerServiceName);
				continue;
			}
			Printer printer = Printer.builder()
				.name(printerServiceName)
				.description("System Initialized")
				.status(0)
				.build();
			printerRepository.save(printer);
			log.info("Create default printer: [{}]", printerServiceName);
		}

	}

}
