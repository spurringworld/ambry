package com.github.ambry.admin;

import com.github.ambry.clustermap.ClusterMap;
import com.github.ambry.clustermap.MockClusterMap;
import com.github.ambry.config.VerifiableProperties;
import com.github.ambry.rest.BlobStorageService;
import com.github.ambry.rest.MockRestRequestResponseHandler;
import com.github.ambry.rest.RestResponseHandler;
import com.github.ambry.router.InMemoryRouter;
import com.github.ambry.router.Router;
import java.io.IOException;
import java.util.Properties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * Unit tests for {@link AdminBlobStorageServiceFactory}.
 */
public class AdminBlobStorageServiceFactoryTest {

  /**
   * Tests the instantiation of an {@link AdminBlobStorageService} instance through the
   * {@link AdminBlobStorageServiceFactory}.
   * @throws IOException
   */
  @Test
  public void getAdminBlobStorageServiceTest()
      throws IOException {
    // dud properties. server should pick up defaults
    Properties properties = new Properties();
    VerifiableProperties verifiableProperties = new VerifiableProperties(properties);

    AdminBlobStorageServiceFactory adminBlobStorageServiceFactory =
        new AdminBlobStorageServiceFactory(verifiableProperties, new MockClusterMap(),
            new MockRestRequestResponseHandler(), new InMemoryRouter(verifiableProperties));
    BlobStorageService adminBlobStorageService = adminBlobStorageServiceFactory.getBlobStorageService();
    assertNotNull("No BlobStorageService returned", adminBlobStorageService);
    assertEquals("Did not receive an AdminBlobStorageService instance",
        AdminBlobStorageService.class.getCanonicalName(), adminBlobStorageService.getClass().getCanonicalName());
  }

  /**
   * Tests instantiation of {@link AdminBlobStorageServiceFactory} with bad input.
   * @throws IOException
   */
  @Test
  public void getAdminBlobStorageServiceFactoryWithBadInputTest()
      throws IOException {
    // dud properties. server should pick up defaults
    Properties properties = new Properties();
    VerifiableProperties verifiableProperties = new VerifiableProperties(properties);
    ClusterMap clusterMap = new MockClusterMap();
    RestResponseHandler restResponseHandler = new MockRestRequestResponseHandler();
    Router router = new InMemoryRouter(verifiableProperties);

    // VerifiableProperties null.
    try {
      new AdminBlobStorageServiceFactory(null, clusterMap, restResponseHandler, router);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // ClusterMap null.
    try {
      new AdminBlobStorageServiceFactory(verifiableProperties, null, restResponseHandler, router);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // RestResponseHandler null.
    try {
      new AdminBlobStorageServiceFactory(verifiableProperties, clusterMap, null, router);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // Router null.
    try {
      new AdminBlobStorageServiceFactory(verifiableProperties, clusterMap, restResponseHandler, null);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }
  }
}