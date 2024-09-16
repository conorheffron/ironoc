package com.ironoc.portfolio.aws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Component
public class AwsSecretManager extends AbstractLogger implements SecretManager {

    private static final String SECRET_NAME_GIT_TOKEN = "prod/Ironoc/GitApiToken";

    private static final String SECRET_KEY = "GIT_API_TOKEN";

    private static final String REGION = "eu-north-1";

    @Override
    public String getGitSecret() {
        Region region = Region.of(REGION);
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(SECRET_NAME_GIT_TOKEN)
                .build();

        String secret = "";
        try {
            GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            String json = getSecretValueResponse.secretString();

            JsonNode node = new ObjectMapper().readTree(json);

            secret = node.get(SECRET_KEY).asText();
        } catch (Exception e) {
            error("Unexpected error occurred extracting secret.", e);
        }
        return secret;
    }
}
