package backend.twitter;

import org.junit.Ignore;
import org.junit.Test;
import play.libs.F;

/**
 * @author sondre
 */
public class TwitterApplicationAuthenticatorTest {


    @Test
    @Ignore("Don't want to run this repeatedly")
    public void testObtainBearerToken() throws Exception {
        final F.Promise<String> bearerPromise = TwitterApplicationAuthenticator.create().obtainBearerToken("F0uCwYgX7R9m9aIGUiuMHg", "kKvAZlz7R32yZPviwWsK8G5oq6muCu4JhobjSySXLU");
        System.out.println(bearerPromise.get());
    }
}
