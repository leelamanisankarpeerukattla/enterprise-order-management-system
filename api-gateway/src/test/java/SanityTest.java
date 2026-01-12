import com.example.gateway.ApiGatewayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApiGatewayApplication.class)
class SanityTest {

  @Test
  void contextLoads() {
    // verifies Spring context can start
  }
}