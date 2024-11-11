package org.example;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class DistanceMatrixAPI {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final Logger logger = LoggerFactory.getLogger(DistanceMatrixAPI.class);


    public static void main(String[] args) {

        String[] enderecos = {
                "Av. Conselheiro Nébias, 300 - Vila Matias, Santos, SP",  // A
                "Av. Conselheiro Nébias, 589/595, Boqueirão, Santos, SP",  // B
                "Rua Dr. Carvalho de Mendonça, 140, Vila Mathias, Santos, SP",  // C
//                "Rua W, 101, Porto Alegre, RS",  // D
//                "Rua V, 202, Curitiba, PR"  // E
        };

        Long[][] matrizDistancia = new Long[enderecos.length][enderecos.length];

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        try {
            DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(context, enderecos, enderecos).await();

            for (int i = 0; i < result.rows.length; i++) {
                DistanceMatrixRow row = result.rows[i];
                for (int j = 0; j < row.elements.length; j++) {
                    DistanceMatrixElement element = row.elements[j];
                    if (element.distance != null) {
                        System.out.printf("Distância entre %s e %s: %s metros\n", enderecos[i], enderecos[j], element.distance.inMeters);
                        matrizDistancia[i][j] = result.rows[i].elements[j].distance.inMeters;
                    } else {
                        System.out.printf("Não foi possível calcular a distância entre %s e %s\n", enderecos[i], enderecos[j]);
                    }
                }
            }
            for (Long[] i : matrizDistancia) {
                System.out.println(Arrays.toString(i));
            }
        } catch (ApiException | InterruptedException | IOException e) {
            logger.error("Erro ao processar matriz de distâncias");
        } finally {
            context.shutdown();
        }
    }
}
