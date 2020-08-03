package ru.geekbrains;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.*;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.dsl.JpaUpdatingOutboundEndpointSpec;
import org.springframework.integration.jpa.support.PersistMode;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.entity.BrandCsv;
import ru.geekbrains.entity.Product;
import ru.geekbrains.helpers.ConverterCSV;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@CommonsLog
@Configuration
public class InputConfiguration {

    @Value("${source.dir.path}")
    private String sourceDirectoryPath;

    @Value("${dest.dir.path}")
    private String destDirectoryPath;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public MessageSource<File> sourceDirectory(){
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(sourceDirectoryPath));
        messageSource.setAutoCreateDirectory(true);
        return messageSource;
    }

    public List<BrandCsv> readCSVFile(String path){
        try {
            List<BrandCsv> beans = new CsvToBeanBuilder(new FileReader(path))
                    .withType(BrandCsv.class).build().parse();
            beans.forEach(p -> log.info("### " + p.toString()));
            log.info("Выполнено");
            return beans;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("не Выполнено");
            return null;
        }
    }

    @Bean
    public JpaUpdatingOutboundEndpointSpec jpaPersistHandler() {
        return Jpa.outboundAdapter(this.entityManagerFactory)
                .entityClass(Product.class)
                .persistMode(PersistMode.PERSIST);
    }

    @Bean
    public IntegrationFlow fileMoveFlow() {
        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
                .filter(msg -> ((File) msg).getName().endsWith(".csv"))
                .transform(new FileToStringTransformer())
                .split(s -> s.delimiters("\n"))
                .<String, Product>transform(name -> {
                    log.info("### " + name);
                    Product product = ConverterCSV.CONVERT.toProduct(name, ";");
                    log.info("##### " + product + "\n " + product.getBrand());
//                    brand.setName(name);
                    return product;
                })
//.handle(destDirectory())
                .handle(jpaPersistHandler(), jpaOutboundGatewayGenericEndpointSpec -> {
                    jpaOutboundGatewayGenericEndpointSpec.transactional();
                })
                .get();
    }

//    @Bean
//    public IntegrationFlow fileMoveFlow() {
//        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
//                .filter(msg -> ((File) msg).getName().endsWith(".csv"))
//                .<File, List<BrandCsv>>transform(file -> {
//                    log.info("№№№№ " + file.getAbsolutePath());
//                    return readFile(file.getAbsolutePath());
//                })
//                .<BrandCsv, Brand>transform(brand -> {
//                    Brand b = new Brand();
//                    b.setName(brand.getName());
//                    return b;
//                })
////.handle(destDirectory())
//                .handle(jpaPersistHandler(), jpaOutboundGatewayGenericEndpointSpec -> {
//                    jpaOutboundGatewayGenericEndpointSpec.transactional();
//                })
//                .get();
//    }

//    @Bean
//    public MessageHandler destDirectory(){
//        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(destDirectoryPath));
//        handler.setExpectReply(false);
//        handler.setDeleteSourceFiles(true);
//        return handler;
//    }
//

//    @Bean
//    public IntegrationFlow fileMoveFlow(){
//        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
//                .filter(msg -> ((File) msg).getName().endsWith(".txt"))
//                .transform(new FileToStringTransformer())
//                .<String, String>transform(String::toUpperCase)
//                .transform(Message.class, msg -> {
//                    msg.getHeaders().forEach((key, value) -> log.info(key + " :: " + value));
//                    return msg;
//                })
//                .handle(destDirectory())
//                .get();
//    }
}
