package io.envoyproxy.controlplane.cache;

import com.google.auto.value.AutoValue;
import com.google.protobuf.ProtocolStringList;
import io.envoyproxy.envoy.api.v2.DiscoveryRequest;
import io.envoyproxy.envoy.api.v2.core.Node;

/**
 * XdsRequest wraps a DiscoveryRequest of some version and provides common methods as a
 * workaround to the proto messages not implementing a common interface that can be used to
 * abstract away xDS version.
 */
@AutoValue
public abstract class XdsRequest {
  public static XdsRequest create(DiscoveryRequest discoveryRequest) {
    return new AutoValue_XdsRequest(discoveryRequest, null);
  }

  public static XdsRequest create(io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest discoveryRequest) {
    return new AutoValue_XdsRequest(null, discoveryRequest);
  }

  abstract DiscoveryRequest v2Request();
  abstract io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest v3Request();

  /**
   *
   * @return The type URL of the underlying v2 or v3 DiscoveryRequest.
   */
  public String getTypeUrl() {
    if (v2Request() != null) {
      return v2Request().getTypeUrl();
    }
    return v3Request().getTypeUrl();
  }

  /**
   *
   * @return getResourceNamesList() of the underlying v2 or v3 DiscoveryRequest.
   */
  public ProtocolStringList getResourceNamesList() {
    if (v2Request() != null) {
      return v2Request().getResourceNamesList();
    }
    return v3Request().getResourceNamesList();
  }

  /**
   *
   * @return getVersionInfo() of the underlying v2 or v3 DiscoveryRequest.
   */
  public String getVersionInfo() {
    if (v2Request() != null) {
      return v2Request().getVersionInfo();
    }
    return v3Request().getVersionInfo();
  }

  /**
   *
   * @return a v2 Node if the underlying request was a v2 discovery request, otherwise null.
   */
  public Node getNodeV2() {
    if (v2Request() == null) {
      return null;
    }

    return v2Request().getNode();
  }

  /**
   *
   * @return a v3 Node if the underlying request was a v3 DiscoveryRequest, otherwise null.
   */
  public io.envoyproxy.envoy.config.core.v3.Node getNodeV3() {
    if (v3Request() == null) {
      return null;
    }
    return v3Request().getNode();
  }
}
