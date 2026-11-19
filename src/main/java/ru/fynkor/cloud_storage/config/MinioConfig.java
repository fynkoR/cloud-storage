package ru.fynkor.cloud_storage.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fynkor.cloud_storage.props.MinioProperties;

@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;

    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
